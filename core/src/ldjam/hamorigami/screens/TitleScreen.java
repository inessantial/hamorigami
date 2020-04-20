package ldjam.hamorigami.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.tweens.ActorTween;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.ui.Styles;

public class TitleScreen extends BaseScreen {

   private boolean exiting;

   private GameContext2D context;

   public TitleScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      super.onCreate(context);
      this.context = context;

      Table layout = new Table();
      layout.setFillParent(true);

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
   protected void onUpdate(float delta) {
      if (!exiting && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         exiting = true;
         context.getInputManager().clear();
         final IngameScreen initialScreen = new IngameScreen(getGame());
         //
         ColorTransition colorTransition = new ColorTransition();
         colorTransition.setColor(Color.WHITE.cpy());
         context.getScreenTransitions().out(colorTransition, new StoryScreen(getGame(), initialScreen,
               Messages.STORY_INTRO_1,
               Messages.STORY_INTRO_2,
               Messages.STORY_INTRO_3,
               Messages.STORY_INTRO_4
         ), 1f);
      }
   }
}
