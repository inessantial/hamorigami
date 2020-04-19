package ldjam.hamorigami.behavior;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.TreeStatus;

public class TreeBehavior extends BehaviorAdapter {

   public static final float DECAY_RATE = 2f;

   private final DeltaTimer decayTimer = new DeltaTimer();

   @Override
   public void update(GameObject source, float delta) {
      decayTimer.update(delta);
      if (decayTimer.reached(DECAY_RATE)) {
         decayTimer.reset();
         updateStatus(
               source.getAttribute(TreeStatus.class),
               source.getAttribute(HealthData.class)
         );
      }
   }

   private void updateStatus(TreeStatus status, HealthData treeHealth) {
      // consume water
      if (status.getSoilWaterLevel() > 0f) {
         float value = 1f - Math.min(status.getTreeWateredLevel(), 0);
         status.decreaseSoilWater(0.04f * value);
         status.increaseTreeWateredLevel(0.08f * value);
      }
      // dryness
      status.decreaseTreeWateredLevel(0.01f);
      status.decreaseSoilWater(0.01f);

      // tree takes damage when watered level is below -0.5 or above 0.5
      // tree heals otherwise
      float absoluteLevel = Math.abs(status.getTreeWateredLevel());
      if (absoluteLevel >= 0.5f) {
         treeHealth.reduceHealth((int) (120 * absoluteLevel));
      } else {
         treeHealth.addHealth((int) (60 * (1f - absoluteLevel)));
      }
   }
}
