package ldjam.hamorigami.effects;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.TreeStatus;

public class AddWaterEffect implements SpiritAbsorbEffect {

   @Override
   public boolean applyEffect(GameObject treeObject) {
      treeObject.getAttribute(TreeStatus.class).increaseSoilWater(0.05f);
      if (treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel() == 1f) {
         // tree dead
         return false;
      }
      return true;
   }
}
