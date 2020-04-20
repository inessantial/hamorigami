package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.ui.Styles;

public class TitlePhase implements GamePhase {

   private Table layout;
   private boolean exiting;

   private final GamePhaseHandler phaseHandler;

   public TitlePhase(GamePhaseHandler phaseHandler) {
      this.phaseHandler = phaseHandler;
   }

   @Override
   public void disable(GameContext2D context, GameObject treeObject) {
      context.getWorldStage().getActors().removeValue(layout, true);
   }

   @Override
   public void enable(GameContext2D context, GameObject treeObject) {
      this.exiting = false;
      this.layout = new Table();
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
