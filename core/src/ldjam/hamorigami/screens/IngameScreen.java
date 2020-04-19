package ldjam.hamorigami.screens;

import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.debug.DebugMetric;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.behavior.SpiritSpawner;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.model.TreeStatus;

public class IngameScreen extends BrainGdxScreen2D<HamorigamiGame> {

   private SpiritSpawner spawner;
   private GameObject playerObject;
   private GameObject treeObject;

   public IngameScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
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
   }

   private void setupLevel(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);

      // add player
      context.getGameCamera().setZoom(300, GameCamera.ZoomMode.TO_HEIGHT);
      this.playerObject = entityFactory.spawnSpirit(
            SpiritType.SPIRIT_EARTH,
            0f, 0f
      );

      // add tree
      this.treeObject = entityFactory.spawnTree();

      // add floor
      GameObject floorObject = entityFactory.spawnFloor();

      // Spirit spawning
      spawner = new SpiritSpawner(5f, entityFactory, context, treeObject);

   }

   private void setupGraphics(GameContext2D context) {
      context.getRenderManager().register(SpiritType.SPIRIT_EARTH, new SpriteRenderer(Assets.Textures.SPIRIT_EARTH));
      context.getRenderManager().register(SpiritType.SPIRIT_WATER, new SpriteRenderer(Assets.Textures.SPIRIT_WATER));
      context.getRenderManager().register(SpiritType.SPIRIT_FIRE, new SpriteRenderer(Assets.Textures.SPIRIT_FIRE));

      context.getRenderManager().register(ObjectType.TREE, new SpriteRenderer(Assets.Textures.TREE));
      context.getRenderManager().register(ObjectType.FLOOR, new SpriteRenderer(Assets.Textures.BACKGROUND_FLOOR));
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerObject));
      context.getInputManager().register(new IngameControllerAdapter(playerObject));
   }

   private void setupDebugUi(GameContext2D context) {
      context.getDebugPanel().addMetric("tree water level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getWaterLevel());
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
