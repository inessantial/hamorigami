package ldjam.hamorigami.model;

public class TreeStatus {

   // 0.5f is perfect
   private float waterLevel = 0.5f;
   // 0.5f is perfect
   private float sunlightLevel = 0.5f;

   public void decreaseSunlight(float sunlight) {
      this.sunlightLevel = Math.max(this.sunlightLevel - sunlight, 0);
   }

   public void increaseSunlight(float sunlight) {
      this.sunlightLevel = Math.min(this.sunlightLevel + sunlight, 1f);
   }

   public void decreaseWater(float water) {
      this.waterLevel = Math.max(this.waterLevel - water, 0);
   }

   public void increaseWater(float water) {
      this.waterLevel = Math.min(this.waterLevel + water, 1f);
   }

   public float getWaterLevel() {
      return waterLevel;
   }

   public float getSunlightLevel() {
      return sunlightLevel;
   }

   public int calculateDamage() {
      return (int) (300 * Math.abs(0.5f - sunlightLevel) + Math.abs(0.5f - waterLevel));
   }

   public int calculateHealing() {
      return Math.max(0, 13 - (int) (13 * Math.abs(0.5f - sunlightLevel) + Math.abs(0.5f - waterLevel)));
   }
}
