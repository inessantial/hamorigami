package ldjam.hamorigami.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.ui.Styles;

import static ldjam.hamorigami.Assets.Musics.OUTRO;

public class CreditsScreen extends BrainGdxScreen2D<HamorigamiGame> {

   private boolean exiting;

   private GameContext2D context;
   private Music music;

   public CreditsScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {

      this.music = SharedAssetManager.getInstance().get(OUTRO, Music.class);
      music.setLooping(true);
      music.setVolume(0.1f);
      music.play();

      this.context = context;
      context.getScreenTransitions().in(1.5f);

      Table layout = new Table();
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
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f).delay(2f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f).delay(3f)
            .target(1f)
            .ease(TweenEquations.easeInCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(context.getTweenManager());
   }

   @Override
   public void dispose() {
      super.dispose();
      music.stop();
   }

   @Override
   protected void onUpdate(float delta) {
      if (!exiting && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         exiting = true;
         context.getInputManager().clear();
         context.getScreenTransitions().out(new TitleScreen(getGame()), 1f);
      }
   }
}
