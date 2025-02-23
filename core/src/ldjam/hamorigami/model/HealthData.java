package ldjam.hamorigami.model;

public class HealthData {

   private int maximumHealth;
   private int currentHealth;

   public HealthData(int maximumHealth) {
      this.maximumHealth = maximumHealth;
      currentHealth = maximumHealth;
   }

   public void reduceHealth(int damage) {
      currentHealth = Math.max(currentHealth - damage, 0);
   }

   public void addHealth(int points) {
      currentHealth = Math.min(currentHealth + points, maximumHealth);
   }


   public boolean isDead() {
      return currentHealth == 0;
   }

   public float getHealthPercentage() {
      return (float) currentHealth / maximumHealth;
   }

   public int getTotalHealth() {
      return maximumHealth;
   }

   public void reset() {
      currentHealth = maximumHealth;
   }

   public boolean isFullHealth() {
      return currentHealth == maximumHealth;
   }

   public void kill() {
      currentHealth = 0;
   }
}
