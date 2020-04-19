package ldjam.hamorigami.behavior;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;

public class TreeHealthBindingBehavior extends BehaviorAdapter {

   private final GameObject treeObject;

   public TreeHealthBindingBehavior(GameObject treeObject) {
      this.treeObject = treeObject;
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      HealthData treeHealth = treeObject.getAttribute(HealthData.class);
      source.getColor().a = treeHealth.getHealthPercentage();
      if (treeHealth.isDead()) {
         // game over! let's restart the day
      }
   }
}
