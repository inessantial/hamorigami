package ldjam.hamorigami.gamemode;

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
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.story.StoryTeller;
import ldjam.hamorigami.ui.Styles;

public class CutscenePhase implements GamePhase {

   private final GamePhaseHandler phaseHandler;
   private final Messages[] messages;
   private final String nextPhase;
   private StoryTeller teller;
   private Label label;
   private boolean aborted;
   private Table layout;

   public CutscenePhase(GamePhaseHandler phaseHandler, String nextPhase, Messages... messages) {
      this.phaseHandler = phaseHandler;
      this.messages = messages;
      this.nextPhase = nextPhase;
   }

   @Override
   public void disable(final GameContext2D context, GameObject treeObject) {
      Tween.to(context.getGameCamera(), GameCameraTween.ZOOM_WIDTH, 1f)
            .target(800f)
            .start(SharedTweenManager.getInstance());
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

      Tween.to(context.getGameCamera(), GameCameraTween.ZOOM_WIDTH, 4f)
            .target(680f)
            .start(SharedTweenManager.getInstance());
      aborted = false;
      teller = new StoryTeller(messages);

      this.layout = new Table();
      layout.setFillParent(true);
      layout.getColor().a = 0f;
      Tween.to(layout, ActorTween.ALPHA, 2f)
            .target(1f)
            .start(SharedTweenManager.getInstance());

      label = new Label(teller.getNextStoryPoint(), Styles.STORY);
      label.setWrap(true);
      label.setAlignment(Align.center);
      layout.center().add(label).width(600f).padBottom(200f).row();
      Label action = new Label(Bundle.get(Messages.ANY_KEY), Styles.DIALOG_TEXT);
      action.getColor().a = 1f;
      Tween.to(action, ActorTween.ALPHA, 1f).target(0.2f)
            .ease(TweenEquations.easeInOutCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(SharedTweenManager.getInstance());
      layout.center().add(action).row();

      context.getWorldStage().addActor(layout);
   }

   @Override
   public boolean isFinished() {
      return aborted;
   }

   @Override
   public void update(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !aborted) {
         aborted = true;
         // TODO play start game sound
         phaseHandler.changePhase(nextPhase);
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
            phaseHandler.changePhase(nextPhase);
         }
      }
   }
}
