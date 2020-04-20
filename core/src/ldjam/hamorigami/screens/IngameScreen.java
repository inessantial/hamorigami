package ldjam.hamorigami.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.debug.DebugMetric;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.behavior.TreeHealthBindingBehavior;
import ldjam.hamorigami.entity.*;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.model.TreeStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ldjam.hamorigami.Assets.Musics.BACKGROUND_01;
import static ldjam.hamorigami.model.SpiritType.*;

public class IngameScreen extends BaseScreen {

   private static final List<SpiritType> CANDIDATES = new ArrayList<SpiritType>();

   static {
      for (SpiritType type : SpiritType.values()) {
         if (type != SPIRIT_EARTH) {
            CANDIDATES.add(type);
         }
      }
   }

   private GameContext2D context;
   private SpiritSpawner spawner;
   private GameObject playerObject;
   private AttackHandler attackHandler;
   private Music music;

   private boolean gameOver;

   public IngameScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      super.onCreate(context);
      this.context = context;
      this.music = SharedAssetManager.getInstance().get(BACKGROUND_01, Music.class);
      music.setLooping(true);
      music.setVolume(0.1f);
      music.play();
      setupLevel(context);
      setupInput(context);
      setupDebugUi(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
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
            gameOver = true;
            context.getBehaviorManager().clear();
            CreditsScreen creditsScreen = new CreditsScreen((HamorigamiGame) context.getGame());
            ColorTransition colorTransition = new ColorTransition();
            colorTransition.setColor(Color.WHITE.cpy());
            context.getScreenTransitions().out(colorTransition, new StoryScreen((HamorigamiGame) context.getGame(), creditsScreen,
                  Messages.STORY_OUTRO_1, Messages.STORY_OUTRO_2, Messages.STORY_OUTRO_3, Messages.STORY_OUTRO_4), 1f);
         }
      }
   }

   @Override
   public void dispose() {
      super.dispose();
      music.stop();
   }

   private void setupLevel(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);

      // add player
      this.playerObject = entityFactory.spawnSpirit(
            SpiritType.SPIRIT_EARTH,
            0f, 0f
      );
      playerObject.setDimensions(64f, 64f);
      context.getBehaviorManager().apply(new TreeHealthBindingBehavior(treeObject, context.getAudioManager(), context), playerObject);

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

   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerObject, attackHandler));
      context.getInputManager().register(new IngameControllerAdapter(playerObject, attackHandler));
   }

   private void setupDebugUi(GameContext2D context) {
      context.getDebugPanel().addMetric("tree watered level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getTreeWateredLevel());
         }
      });
      context.getDebugPanel().addMetric("soil water level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel());
         }
      });
      context.getDebugPanel().addMetric("tree health", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return (treeObject.getAttribute(HealthData.class).getHealthPercentage() * 100f) + "%";
         }
      });
   }
}
