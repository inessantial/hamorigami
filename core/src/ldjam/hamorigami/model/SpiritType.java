package ldjam.hamorigami.model;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.effects.*;

public enum SpiritType {

   SPIRIT_EARTH(80, 30, new SpiritAbsorbEffect() {
      @Override
      public boolean applyEffect(GameObject treeObject) {
         // NoOp
         return true;
      }
   }, new SpiritSpawnEffect() {
      @Override
      public void onSpawnSpirit(GameObject spirit, GameObject tree, GameContext2D context) {
         // noOp
      }
   }),
   SPIRIT_WATER(80, 2, new AddWaterEffect(), new SpawnWaterSpiritEffect()),
   SPIRIT_FIRE(45, 3, new AddSunlightEffect(), new SpawnFireEffect());

   private final int health;
   private final float maxSpeed;
   private final SpiritAbsorbEffect absorbEffect;
   private final SpiritSpawnEffect spawnEffect;


   SpiritType(int health, float maxSpeed, SpiritAbsorbEffect absorbEffect, SpiritSpawnEffect spawnEffect) {
      this.health = health;
      this.maxSpeed = maxSpeed;
      this.absorbEffect = absorbEffect;
      this.spawnEffect = spawnEffect;
   }

   public SpiritAbsorbEffect getAbsorbEffect() {
      return absorbEffect;
   }

   public SpiritSpawnEffect getSpawnEffect() {
      return spawnEffect;
   }

   public int getHealth() {
      return health;
   }

   public float getMaxSpeed() {
      return maxSpeed;
   }
}
