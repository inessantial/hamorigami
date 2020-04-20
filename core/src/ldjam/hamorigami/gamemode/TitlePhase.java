package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.ui.Styles;

public class TitlePhase implements GamePhase {

   private Table layout;
   private boolean exiting;

   static {
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
   }

   private final GamePhaseHandler phaseHandler;
   private Image icon;
   private Label logoA;
   private Label logoB;

   public TitlePhase(GamePhaseHandler phaseHandler) {
      this.phaseHandler = phaseHandler;
   }

   @Override
   public void disable(final GameContext2D context, GameObject treeObject) {
      Tween.to(layout, ActorTween.ALPHA, 0.7f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(icon, ActorTween.ALPHA, 0.7f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(logoA, ActorTween.ALPHA, 0.7f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(logoB, ActorTween.ALPHA, 0.7f)
            .target(0f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  context.getWorldStage().getActors().removeValue(layout, true);
                  context.getWorldStage().getActors().removeValue(icon, true);
                  context.getWorldStage().getActors().removeValue(logoA, true);
                  context.getWorldStage().getActors().removeValue(logoB, true);
               }
            })
            .start(SharedTweenManager.getInstance());

   }

   @Override
   public void enable(GameContext2D context, GameObject treeObject) {
      this.exiting = false;
      this.layout = new Table();
      layout.setFillParent(true);
      layout.getColor().a = 0f;

      Sprite sprite = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.LOGO, Texture.class));
      sprite.setSize(75, 75);
      this.icon = new Image(new SpriteDrawable(sprite));
      icon.setPosition(context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() / 2f - sprite.getWidth() / 2f - 40f, context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() - 150f);
      context.getWorldStage().addActor(icon);
      icon.getColor().a = 0f;

      logoA = new Label("HAM", Styles.LABEL_LOGO);
      logoB = new Label("RIGAMI", Styles.LABEL_LOGO);
      logoA.setPosition(icon.getX() - logoA.getPrefWidth() - 10f, icon.getY() - 16f);
      logoB.setPosition(icon.getX() + icon.getWidth() + 10f, icon.getY() - 16f);
      context.getWorldStage().addActor(logoA);
      context.getWorldStage().addActor(logoB);
      logoA.getColor().a = 0f;
      logoB.getColor().a = 0f;

      Tween.to(layout, ActorTween.ALPHA, 2.7f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(icon, ActorTween.ALPHA, 2.7f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(logoA, ActorTween.ALPHA, 2.7f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(logoB, ActorTween.ALPHA, 2.7f)
            .target(1f)
            .start(SharedTweenManager.getInstance());


      Label pressAnyButton = new Label(Bundle.get(Messages.PLAY_GAME), Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(120f).row();

      context.getWorldStage().addActor(layout);

      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);

      pressAnyButton.getColor().a = 0f;
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f)
            .target(1f)
            .ease(TweenEquations.easeInCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(context.getTweenManager());
   }

   @Override
   public boolean isFinished() {
      return exiting;
   }

   @Override
   public void update(float delta) {
      if (!exiting && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         exiting = true;
         phaseHandler.changePhase(Phases.INTRO);
      }
   }
}
