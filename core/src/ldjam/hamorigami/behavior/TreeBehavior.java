package ldjam.hamorigami.behavior;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.TreeStatus;

public class TreeBehavior extends BehaviorAdapter {

   public static final float DECAY_RATE = 2f;
   public static final float HEALING_RATE = 2f;

   private final DeltaTimer decayTimer = new DeltaTimer();
   private final DeltaTimer healingTimer = new DeltaTimer();

   @Override
   public void update(GameObject source, float delta) {
      decayTimer.update(delta);
      healingTimer.update(delta);
      if (decayTimer.reached(DECAY_RATE)) {
         decayTimer.reset();
         TreeStatus status = source.getAttribute(TreeStatus.class);
         status.decreaseWater(0.01f);
         source.getAttribute(HealthData.class).reduceHealth(status.calculateDamage());
      }
      if (healingTimer.reached(HEALING_RATE)) {
         healingTimer.reset();
         TreeStatus status = source.getAttribute(TreeStatus.class);
         source.getAttribute(HealthData.class).addHealth(status.calculateHealing());
      }

   }
}
