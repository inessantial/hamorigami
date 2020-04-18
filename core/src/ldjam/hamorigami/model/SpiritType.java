package ldjam.hamorigami.model;

public enum SpiritType {

   SPIRIT_EARTH(80, 30, 20),
   SPIRIT_WATER(40, 2, 10),
   SPIRIT_FIRE(25, 3, 50);

   private final int health;
   private final float maxSpeed;
   private final int damage;


   SpiritType(int health, float maxSpeed, int damage) {
      this.health = health;
      this.maxSpeed = maxSpeed;
      this.damage = damage;
   }

   public int getHealth() {
      return health;
   }

   public float getMaxSpeed() {
      return maxSpeed;
   }

   public int getDamage() {
      return damage;
   }
}
