package ldjam.hamorigami.tree;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.TreeStatus;

public class AddWaterEffect implements TreeEffect {

   @Override
   public boolean applyEffect(GameObject treeObject) {
      treeObject.getAttribute(TreeStatus.class).increaseWater(0.05f);
      if (treeObject.getAttribute(TreeStatus.class).getWaterLevel() == 1f) {
         // tree dead
         return false;
      }
      return true;
   }
}
