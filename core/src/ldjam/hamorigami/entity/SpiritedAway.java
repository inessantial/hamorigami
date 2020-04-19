package ldjam.hamorigami.entity;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.SpiritType;

public class SpiritedAway extends BehaviorAdapter {

   private final GameContext2D context;

   public SpiritedAway(GameContext2D context) {
      this.context = context;
   }

   @Override
   public void update(final GameObject source, float delta) {
      if (source.hasAttribute("spiritedAway")) {
         return;
      }
      if (source.hasAttribute(HealthData.class) && source.getType() instanceof SpiritType) {
         if (source.getAttribute(HealthData.class).isDead()) {
            source.setActive(false);
            source.setAttribute("spiritedAway", true);
            context.getBehaviorManager().remove(source);
            SharedTweenManager.getInstance().killTarget(source);
            SharedTweenManager.getInstance().killTarget(source.getColor());
            Tween.to(source, GameObjectTween.OFFSET_Y, 1.5f)
                  .target(180)
                  .start(SharedTweenManager.getInstance());
            Tween.to(source.getColor(), ColorTween.A, 1.5f)
                  .target(0f)
                  .setCallback(new TweenCallback() {
                     @Override
                     public void onEvent(int type, BaseTween<?> tween) {
                       context.getGameWorld().remove(source);
                     }
                  })
                  .setCallbackTriggers(TweenCallback.COMPLETE)
                  .ease(TweenEquations.easeOutCubic)
                  .start(SharedTweenManager.getInstance());
         }
      }
   }
}
