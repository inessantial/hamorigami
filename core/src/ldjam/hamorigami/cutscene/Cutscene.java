package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

import java.util.List;
import java.util.Map;

public class Cutscene {

   private final Map<Float, List<CutsceneStep>> steps;

   public Cutscene(Map<Float, List<CutsceneStep>> steps) {
      this.steps = steps;
   }

   public void play() {
      for (Map.Entry<Float, List<CutsceneStep>> entry : steps.entrySet()) {
         float delay = entry.getKey();
         for (final CutsceneStep step : entry.getValue()) {
            Tween.call(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> tween) {
                  step.execute();
               }
            }).delay(delay).start(SharedTweenManager.getInstance());
         }
      }
   }

   public void stop() {

   }
}
