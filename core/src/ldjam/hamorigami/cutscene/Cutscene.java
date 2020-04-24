package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cutscene {

   private final Map<Float, List<CutsceneStep>> steps;
   private final List<Tween> tweens = new ArrayList<>();
   private final List<BaseTween<?>> executedTweens = new ArrayList<>();

   public Cutscene(Map<Float, List<CutsceneStep>> steps) {
      this.steps = steps;
   }

   public void play() {
      if (tweens.size() > 0f) {
         return;
      }
      for (Map.Entry<Float, List<CutsceneStep>> entry : steps.entrySet()) {
         float delay = entry.getKey();
         for (final CutsceneStep step : entry.getValue()) {
            tweens.add(Tween.call(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> tween) {
                  step.execute();
                  executedTweens.add(tween);
               }
            }).setCallbackTriggers(TweenCallback.COMPLETE)
                  .delay(delay)
                  .start(SharedTweenManager.getInstance()));
         }
      }
   }

   public void stop() {
      for (Tween tween : tweens) {
         SharedTweenManager.getInstance().killTarget(tween.getTarget());
         tween.free();
      }
      tweens.clear();
   }

   public boolean isOver() {
      return executedTweens.size() == tweens.size();
   }
}
