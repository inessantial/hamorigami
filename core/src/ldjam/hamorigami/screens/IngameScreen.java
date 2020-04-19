package ldjam.hamorigami.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.debug.DebugMetric;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets.Textures;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.behavior.SpiritSpawner;
import ldjam.hamorigami.behavior.TreeHealthBindingBehavior;
import ldjam.hamorigami.entity.AttackHandler;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.entity.SpiritedAway;
import ldjam.hamorigami.graphics.EntityOrderComparator;
import ldjam.hamorigami.graphics.GaugeRenderer;
import ldjam.hamorigami.graphics.SpiritRenderer;
import ldjam.hamorigami.graphics.TreeRenderer;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;
import ldjam.hamorigami.model.*;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static ldjam.hamorigami.Assets.Musics.BACKGROUND_01;
import static ldjam.hamorigami.Assets.Textures.*;

public class IngameScreen extends BrainGdxScreen2D<HamorigamiGame> {

   private SpiritSpawner spawner;
   private GameObject playerObject;
   private GameObject treeObject;
   private AttackHandler attackHandler;

   public IngameScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      Music music = SharedAssetManager.getInstance().get(BACKGROUND_01, Music.class);
      music.setLooping(true);
      music.setVolume(0.1f);
      music.play();
      context.setBackgroundColor(Color.valueOf("7766ff"));
      context.setDebug(getGame().isDebug());
      setupLevel(context);
      setupGraphics(context);
      setupInput(context);
      setupDebugUi(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      spawner.update(delta);
      attackHandler.update(delta);
   }

   private void setupLevel(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);
      context.getGameCamera().setZoom(300, GameCamera.ZoomMode.TO_HEIGHT);

      // add tree
      this.treeObject = entityFactory.spawnTree();

      // add player
      this.playerObject = entityFactory.spawnSpirit(
            SpiritType.SPIRIT_EARTH,
            0f, 0f
      );
      playerObject.setDimensions(64f, 64f);
      context.getBehaviorManager().apply(new TreeHealthBindingBehavior(treeObject), playerObject);

      // add floor
      GameObject floorObject = entityFactory.spawnFloor();

      // add gauge
      GameObject gaugeObject = entityFactory.spawnGauge(210, 70);

      // Spirit spawning
      spawner = new SpiritSpawner(3f, entityFactory, context, treeObject);
      attackHandler = new AttackHandler(playerObject, entityFactory);
      context.getBehaviorManager().apply(new SpiritedAway(context));

   }

   private void setupGraphics(GameContext2D context) {
      context.getRenderManager().setRenderOrderComparator(new EntityOrderComparator());
      AnimationSpriteSheet kodamaSpritesheet = new AnimationSpriteSheet(
            SPIRIT_EARTH_KODAMA_SRITESHEET, 64, 64
      );
      AnimationSpriteSheet hiSpritesheet = new AnimationSpriteSheet(
            SPIRIT_FIRE_HI_SPRITESHEET, 32, 64
      );
      AnimationSpriteSheet ameSpritesheet = new AnimationSpriteSheet(
            SPIRIT_WATER_AME_SPRITESHEET, 32, 64
      );

      context.getRenderManager().register(SpiritType.SPIRIT_EARTH, new SpiritRenderer(context.getGameCamera(), kodamaSpritesheet, AnimationConfig.builder()
            .registerFrames(SpiritAnimationType.ATTACKING_EAST, AnimationFrames.builder()
                  .origin(0, 5)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.ATTACKING_WEST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 1)
                  .frames(5)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(5)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_EAST, AnimationFrames.builder()
                  .origin(0, 3)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 2)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .build()));
      context.getRenderManager().register(SpiritType.SPIRIT_WATER, new SpiritRenderer(context.getGameCamera(), ameSpritesheet, AnimationConfig.builder()
            .registerFrames(SpiritAnimationType.LANDING_WEST, AnimationFrames.builder()
                  .origin(0, 2)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.LANDING_EAST, AnimationFrames.builder()
                  .origin(0, 3)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 5)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_WEST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_EAST, AnimationFrames.builder()
                  .origin(0, 5)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_WEST, AnimationFrames.builder()
                  .origin(0, 1)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .build()));
      context.getRenderManager().register(SpiritType.SPIRIT_FIRE, new SpiritRenderer(context.getGameCamera(), hiSpritesheet, AnimationConfig.builder()
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .build()));

      context.getRenderManager().register(ObjectType.TREE, new TreeRenderer());
      context.getRenderManager().register(ObjectType.FLOOR, new SpriteRenderer(Textures.BACKGROUND_FLOOR));
      context.getRenderManager().register(ObjectType.GAUGE, new GaugeRenderer(treeObject));
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
