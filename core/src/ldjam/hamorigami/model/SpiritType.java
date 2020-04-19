package ldjam.hamorigami.model;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.tree.AddSunlightEffect;
import ldjam.hamorigami.tree.AddWaterEffect;
import ldjam.hamorigami.tree.TreeEffect;

public enum SpiritType {

   SPIRIT_EARTH(80, 30, new TreeEffect() {
      @Override
      public boolean applyEffect(GameObject treeObject) {
         // NoOp
         return true;
      }
   }),
   SPIRIT_WATER(40, 2, new AddWaterEffect()),
   SPIRIT_FIRE(25, 3, new AddSunlightEffect());

   private final int health;
   private final float maxSpeed;
   private final TreeEffect effect;


   SpiritType(int health, float maxSpeed, TreeEffect effect) {
      this.health = health;
      this.maxSpeed = maxSpeed;
      this.effect = effect;
   }

   public TreeEffect getEffect() {
      return effect;
   }

   public int getHealth() {
      return health;
   }

   public float getMaxSpeed() {
      return maxSpeed;
   }
}
