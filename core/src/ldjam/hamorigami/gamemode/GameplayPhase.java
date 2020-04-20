package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.behavior.TreeBehavior;
import ldjam.hamorigami.behavior.TreeHealthBindingBehavior;
import ldjam.hamorigami.entity.*;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.SpiritType;

import java.util.ArrayList;
import java.util.List;

import static ldjam.hamorigami.Assets.Musics.BACKGROUND_01;
import static ldjam.hamorigami.model.SpiritType.*;

public class GameplayPhase implements GamePhase, Proceedable {

   private static final List<SpiritType> CANDIDATES = new ArrayList<SpiritType>();

   static {
      for (SpiritType type : SpiritType.values()) {
         if (type != SPIRIT_EARTH) {
            CANDIDATES.add(type);
         }
      }
   }

   private Music music;
   private Music cityscape;

   private GameObject playerObject;
   private GameObject treeObject;
   private AttackHandler attackHandler;
   private SpiritSpawner spawner;

   private boolean gameOver;

   private GameContext2D context;

   private final GamePhaseHandler gamePhaseHandler;

   public GameplayPhase(GamePhaseHandler gamePhaseHandler) {
      this.gamePhaseHandler = gamePhaseHandler;
   }

   @Override
   public void disable(GameContext2D context, GameObject treeObject) {
      music.stop();
      cityscape.setVolume(0.4f);
   }

   @Override
   public void enable(final GameContext2D context, final GameObject treeObject) {
      SharedAssetManager.getInstance().get(Assets.Musics.MENU, Music.class).stop();
      gameOver = false;
      this.context = context;
      this.treeObject = treeObject;
      this.music = SharedAssetManager.getInstance().get(BACKGROUND_01, Music.class);
      cityscape = SharedAssetManager.getInstance().get(Assets.Musics.CITYSCAPE, Music.class);
      cityscape.setVolume(0.2f);
      music.setLooping(true);
      music.setVolume(0.1f);
      music.play();


      EntityFactory entityFactory = new EntityFactory(context);

      // add player
      this.playerObject = entityFactory.spawnSpirit(
            SpiritType.SPIRIT_EARTH,
            context.getGameCamera().getScaledCameraWidth() / 2f, 0f
      );
      this.playerObject.getColor().a = 0f;
      final String playerId = playerObject.getId();
      Tween.to(this.playerObject.getColor(), ColorTween.A, 3f)
            .target(1f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  if (context.getGameWorld().getObjectById(playerId) != null) {
                     context.getBehaviorManager().apply(new TreeHealthBindingBehavior(treeObject, context.getAudioManager(), gamePhaseHandler, context), playerObject);
                  }
               }
            })
            .start(SharedTweenManager.getInstance());
      playerObject.setDimensions(64f, 64f);

      // Spirit spawning
      SpiritSpawnPool spiritSpawnPool = new SpiritSpawnPool();

      spiritSpawnPool.addSpawnWave(5f, SPIRIT_WATER);

      spiritSpawnPool.addSpawnWave(5f, SPIRIT_WATER);
      spiritSpawnPool.addSpawnWave(1f, SPIRIT_WATER);

      spiritSpawnPool.addSpawnWave(8f, SPIRIT_WATER, SPIRIT_WATER, SPIRIT_WATER);
      spiritSpawnPool.addSpawnWave(2f, SPIRIT_WATER, SPIRIT_WATER);
      spiritSpawnPool.addSpawnWave(2f, SPIRIT_WATER, SPIRIT_WATER);

      spiritSpawnPool.addSpawnWave(5f, SPIRIT_FIRE);
      spiritSpawnPool.addSpawnWave(3f, SPIRIT_FIRE);

      spiritSpawnPool.addSpawnWave(8f, SPIRIT_WATER, SPIRIT_WATER, SPIRIT_FIRE);
      spiritSpawnPool.addSpawnWave(2f, SPIRIT_WATER, SPIRIT_FIRE);
      spiritSpawnPool.addSpawnWave(2f, SPIRIT_FIRE, SPIRIT_FIRE);

      spiritSpawnPool.addSpawnWave(8f, SPIRIT_WATER, SPIRIT_WATER, SPIRIT_FIRE);
      spiritSpawnPool.addSpawnWave(1f, SPIRIT_WATER, SPIRIT_WATER, SPIRIT_WATER, SPIRIT_WATER);
      spiritSpawnPool.addSpawnWave(1f, SPIRIT_FIRE, SPIRIT_FIRE, SPIRIT_FIRE);

      spawner = new SpiritSpawner(spiritSpawnPool, entityFactory, context, treeObject);
      attackHandler = new AttackHandler(playerObject, entityFactory, context.getAudioManager());
      context.getBehaviorManager().apply(new SpiritedAway(context));

      setupInput(context);

      context.getBehaviorManager().apply(new TreeBehavior(), treeObject);
   }

   @Override
   public boolean isFinished() {
      return gameOver || treeObject.getAttribute(HealthData.class).isDead();
   }

   @Override
   public void update(float delta) {
      spawner.update(delta);
      attackHandler.update(delta);
      if (!spawner.canSpawn() && !gameOver) {
         // check that the only spirit left is the player
         Array<GameObject> objects = context.getGameWorld().getObjects();
         boolean spiritStillAlive = false;
         for (GameObject object : objects) {
            if (CANDIDATES.contains(object.getType())) {
               spiritStillAlive = true;
               break;
            }
         }

         // game successful!
         if (!spiritStillAlive) {
            kilAllSpirits();
            gameOver = true;
            gamePhaseHandler.changePhase(Phases.OUTRO);
         }
      }
   }

   private void kilAllSpirits() {
      // let all spirits fade away
      for (GameObject spirit : context.getGameWorld().getGroup("spirits")) {
         final String spiritId = spirit.getId();
         SharedTweenManager.getInstance().killTarget(spirit);
         SharedTweenManager.getInstance().killTarget(spirit.getColor());
         Tween.to(spirit, GameObjectTween.OFFSET_Y, 1.5f)
               .target(180)
               .start(SharedTweenManager.getInstance());
         Tween.to(spirit.getColor(), ColorTween.A, 1.5f)
               .target(0f)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> tween) {
                     context.getGameWorld().remove(spiritId);
                  }
               })
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .ease(TweenEquations.easeOutCubic)
               .start(SharedTweenManager.getInstance());
      }
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerObject, attackHandler));
      context.getInputManager().register(new IngameControllerAdapter(playerObject, attackHandler));
      context.getInputManager().register(new ProceedableControllerAdapter(this));
   }

   @Override
   public void proceed() {

   }

   @Override
   public void skip() {
      kilAllSpirits();
      gameOver = true;
      gamePhaseHandler.changePhase(Phases.CREDITS);
   }
}
