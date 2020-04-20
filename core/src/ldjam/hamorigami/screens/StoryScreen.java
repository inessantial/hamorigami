package ldjam.hamorigami.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.story.StoryTeller;
import ldjam.hamorigami.ui.Styles;

public class StoryScreen extends BrainGdxScreen2D<HamorigamiGame> {

   private Label label, action;

   private StoryTeller teller;

   private boolean aborted = false;
   private GameContext2D context;
   private final AbstractScreen<?, ?> nextScreen;
   private final Messages[] messageKeys;

   public StoryScreen(HamorigamiGame game, AbstractScreen<?, ?> nextScreen, Messages... messageKeys) {
      super(game);
      this.nextScreen = nextScreen;
      this.messageKeys = messageKeys;
   }

   @Override
   protected void onCreate(GameContext2D context) {
      context.getScreenTransitions().in(1.5f);
      this.context = context;
      teller = new StoryTeller(messageKeys);


      Table layout = new Table();
      layout.setFillParent(true);

      label = new Label(teller.getNextStoryPoint(), Styles.STORY);
      label.setWrap(true);
      label.setAlignment(Align.center);
      layout.center().add(label).width(600f).padBottom(200f).row();
      action = new Label(Bundle.get(Messages.ANY_KEY), Styles.DIALOG_TEXT);
      action.getColor().a = 1f;
      Tween.to(action, ActorTween.ALPHA, 1f).target(0.2f)
            .ease(TweenEquations.easeInOutCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(SharedTweenManager.getInstance());
      layout.center().add(action).row();

      context.getWorldStage().addActor(layout);
      context.getGameCamera().setZoom(1000, GameCamera.ZoomMode.TO_HEIGHT);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !aborted) {
         aborted = true;
         // TODO play start game sound
         context.getScreenTransitions().out(nextScreen, 1f);
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && !aborted) {
         if (teller.hasNextStoryPoint()) {
            Tween.to(label, ActorTween.ALPHA, 0.5f)
                  .target(0f)
                  .ease(TweenEquations.easeOutQuad)
                  .setCallback(new TweenCallback() {
                     @Override
                     public void onEvent(int type, BaseTween<?> source) {
                        label.setText(teller.getNextStoryPoint());
                        Tween.to(label, ActorTween.ALPHA, 1f)
                              .target(1f)
                              .ease(TweenEquations.easeInOutQuad)
                              .start(SharedTweenManager.getInstance());
                     }
                  })
                  .setCallbackTriggers(TweenCallback.COMPLETE)
                  .start(SharedTweenManager.getInstance());
         } else if (!aborted) {
            aborted = true;
            // TODO play start game sound
            context.getScreenTransitions().out(nextScreen, 1f);
         }
      }
   }
}
