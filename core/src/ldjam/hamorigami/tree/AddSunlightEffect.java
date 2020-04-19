package ldjam.hamorigami.tree;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.TreeStatus;

public class AddSunlightEffect implements TreeEffect {

   @Override
   public boolean applyEffect(GameObject treeObject) {
      treeObject.getAttribute(TreeStatus.class).decreaseWater(0.05f);
      if (treeObject.getAttribute(TreeStatus.class).getWaterLevel() == 0f) {
         // tree dead
         return false;
      }
      return true;
   }
}
