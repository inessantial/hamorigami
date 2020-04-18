package ldjam.hamorigami.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.model.SpiritType;

public class IngameScreen extends BrainGdxScreen2D<HamorigamiGame> {

   private Movement playerMovement;

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
   }

   private void setupLevel(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);

      // add player
      context.getGameCamera().setZoom(300, GameCamera.ZoomMode.TO_HEIGHT);
      GameObject playerObject = entityFactory.spawnSpirit(
            SpiritType.SPIRIT_EARTH,
            0f, 0f
      );
      this.playerMovement = new Movement(20, context.getGameCamera());
      context.getBehaviorManager().apply(playerMovement, playerObject);
      Tween.to(playerObject, GameObjectTween.OFFSET_Y, 0.6f)
            .target(4f)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(SharedTweenManager.getInstance());

      // add tree
      GameObject treeObject = entityFactory.spawnTree();

      // add floor
      GameObject floorObject = entityFactory.spawnFloor();

   }

   private void setupGraphics(GameContext2D context) {
      context.getRenderManager().register(SpiritType.SPIRIT_EARTH, new SpriteRenderer(Assets.Textures.SPIRIT_EARTH));
      context.getRenderManager().register(SpiritType.SPIRIT_WATER, new SpriteRenderer(Assets.Textures.SPIRIT_WATER));
      context.getRenderManager().register(SpiritType.SPIRIT_FIRE, new SpriteRenderer(Assets.Textures.SPIRIT_FIRE));

      context.getRenderManager().register(ObjectType.TREE, new SpriteRenderer(Assets.Textures.TREE));
      context.getRenderManager().register(ObjectType.FLOOR, new SpriteRenderer(Assets.Textures.BACKGROUND_FLOOR));
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerMovement));
      context.getInputManager().register(new IngameControllerAdapter(playerMovement));
   }


}
