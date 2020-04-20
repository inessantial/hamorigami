package ldjam.hamorigami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.debug.DebugMetric;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.entity.*;
import ldjam.hamorigami.graphics.EntityOrderComparator;
import ldjam.hamorigami.graphics.GaugeRenderer;
import ldjam.hamorigami.graphics.SpiritRenderer;
import ldjam.hamorigami.graphics.TreeRenderer;
import ldjam.hamorigami.model.*;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static ldjam.hamorigami.Assets.Textures.*;
import static ldjam.hamorigami.Assets.Textures.SPIRIT_WATER_AME_SPRITESHEET;
import static ldjam.hamorigami.model.SpiritType.SPIRIT_FIRE;
import static ldjam.hamorigami.model.SpiritType.SPIRIT_WATER;

public abstract class BaseScreen extends BrainGdxScreen2D<HamorigamiGame> {

   protected GameObject treeObject;

   public BaseScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      ColorTransition colorTransition = new ColorTransition();
      colorTransition.setColor(Color.WHITE.cpy());
      context.getRenderPipeline().putAfter(RenderPipeIds.BACKGROUND, "cityscape", new RenderLayer2D() {


         @Override
         public void render(Batch batch, float delta) {
            Texture background = SharedAssetManager.getInstance().get(SUNSET_BACKGROUND, Texture.class);
            batch.begin();
            batch.draw(background, Gdx.graphics.getWidth() / 2f - 400, Gdx.graphics.getHeight() / 2f - 300);
            batch.end();
         }
      });
      context.setBackgroundColor(Color.valueOf("7766ff"));
      context.setDebug(getGame().isDebug());
      setupLevel(context);
      setupGraphics(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
   }

   private void setupLevel(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);
      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);

      // add tree
      this.treeObject = entityFactory.spawnTree();

      // add floor
      GameObject floorObject = entityFactory.spawnFloor();

      // add gauge
      GameObject gaugeObject = entityFactory.spawnGauge(360, 65);

      // Spirit spawning
      SpiritSpawnPool spiritSpawnPool = new SpiritSpawnPool();
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
                  .frames(4)
                  .duration(0.1f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.ATTACKING_WEST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.1f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 1)
                  .frames(5)
                  .duration(0.1f)
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
            .build()) {
         @Override
         public void render(GameObject object, Batch batch, float delta) {
            TreeStatus treeStatus = treeObject.getAttribute(TreeStatus.class);
            float alpha = object.getColor().a;
            if (treeStatus.getTreeWateredLevel() < 0f) {
               Color health = Color.RED.cpy().lerp(Color.WHITE, 1f - treeStatus.getTreeWateredLevel() / -1f);
               health.a = alpha;
               object.setColor(health);
            } else {
               Color health = Color.BLUE.cpy().lerp(Color.WHITE, 1f - treeStatus.getTreeWateredLevel() / 1f);
               health.a = alpha;
               object.setColor(health);
            }
            super.render(object, batch, delta);
         }
      });
      context.getRenderManager().register(SPIRIT_WATER, new SpiritRenderer(context.getGameCamera(), ameSpritesheet, AnimationConfig.builder()
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
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 5)
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
      context.getRenderManager().register(SPIRIT_FIRE, new SpiritRenderer(context.getGameCamera(), hiSpritesheet, AnimationConfig.builder()
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
      context.getRenderManager().register(ObjectType.FLOOR, new SpriteRenderer(Assets.Textures.BACKGROUND_FLOOR));
      context.getRenderManager().register(ObjectType.GAUGE, new GaugeRenderer(treeObject));
   }
}
