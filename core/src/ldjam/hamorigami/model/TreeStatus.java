package ldjam.hamorigami.model;

public class TreeStatus {

   // 0.5f is perfect
   private float waterLevel = 0.5f;

   public void decreaseWater(float water) {
      this.waterLevel = Math.max(this.waterLevel - water, 0);
   }

   public void increaseWater(float water) {
      this.waterLevel = Math.min(this.waterLevel + water, 1f);
   }

   public float getWaterLevel() {
      return waterLevel;
   }

   public int calculateDamage() {
      return (int) (300 * Math.abs(0.5f - waterLevel));
   }

   public int calculateHealing() {
      return (int) (50 * (0.5f - Math.abs(0.5f - waterLevel)));
   }
}
