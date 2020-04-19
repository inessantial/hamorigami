package ldjam.hamorigami.tree;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.TreeStatus;

public class AddSunlightEffect implements TreeEffect {

   @Override
   public boolean applyEffect(GameObject treeObject) {
      treeObject.getAttribute(TreeStatus.class).increaseSunlight(0.05f);
      if (treeObject.getAttribute(TreeStatus.class).getSunlightLevel() == 1f) {
         // tree dead
         return false;
      }
      return true;
   }
}
