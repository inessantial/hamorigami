package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.ui.Styles;

import static ldjam.hamorigami.Assets.Musics.OUTRO;

public class CreditsPhase implements GamePhase, Proceedable {

   private boolean exiting;

   private final GamePhaseHandler phaseHandler;
   private Music music;
   private Table layout;

   public CreditsPhase(GamePhaseHandler phaseHandler) {
      this.phaseHandler = phaseHandler;
   }

   @Override
   public void disable(final GameContext2D context, GameObject treeObject) {
      music.stop();
      SharedAssetManager.getInstance().get(Assets.Musics.CITYSCAPE, Music.class).setVolume(0.9f);
      Tween.to(layout, ActorTween.ALPHA, 1f)
            .target(0f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  context.getWorldStage().getActors().removeValue(layout, true);
               }
            })
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public void enable(GameContext2D context, GameObject treeObject) {
      context.getInputManager().register(new ProceedableControllerAdapter(this));
      exiting = false;
      SharedAssetManager.getInstance().get(Assets.Musics.CITYSCAPE, Music.class).setVolume(0.05f);
      this.music = SharedAssetManager.getInstance().get(OUTRO, Music.class);
      music.setLooping(false);
      music.setVolume(0.1f);
      music.play();

      this.layout = new Table();
      layout.getColor().a = 0f;
      Tween.to(layout, ActorTween.ALPHA, 3f).delay(1f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      layout.setFillParent(true);

      Label credits1 = new Label(Bundle.get(Messages.CREDITS_1), Styles.STORY);
      layout.add(credits1).row();
      Label credits2 = new Label(Bundle.get(Messages.CREDITS_2), Styles.STORY);
      layout.add(credits2).row();
      Label credits3 = new Label(Bundle.get(Messages.CREDITS_3), Styles.STORY);
      layout.add(credits3).row();
      Label credits4 = new Label(Bundle.get(Messages.CREDITS_4), Styles.STORY);
      layout.add(credits4).row();
      Label credits5 = new Label(Bundle.get(Messages.CREDITS_5), Styles.STORY);
      layout.add(credits5).row();
      Label credits6 = new Label(Bundle.get(Messages.CREDITS_6), Styles.STORY);
      layout.add(credits6).row();

      Label pressAnyButton = new Label(Bundle.get(Messages.THANKS), Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(120f).row();

      context.getWorldStage().addActor(layout);

      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);

      pressAnyButton.getColor().a = 0f;
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f).delay(3f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f).delay(4f)
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
         skip();
      }
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         proceed();
      }
   }

   @Override
   public void proceed() {
      exiting = true;
      phaseHandler.changePhase(Phases.TITLE);
   }

   @Override
   public void skip() {
      Gdx.app.exit();
   }
}
