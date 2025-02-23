package ldjam.hamorigami.model;

import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.effects.*;

public enum SpiritType {

   SPIRIT_EARTH(
         80,
         30,
         new SpiritAbsorbEffect() {
            @Override
            public boolean applyEffect(GameObject treeObject) {
               // NoOp
               return true;
            }
         }
         , new SpiritSpawnEffect() {
      @Override
      public void onSpawnSpirit(GameObject spirit, GameObject tree, GameContext2D context) {
         // noOp
      }
   },
         Color.valueOf("a3ffdb44"),
         Assets.Particles.EARTH),
   SPIRIT_WATER(80, 5, new AddWaterEffect(), new SpawnWaterSpiritEffect(), Color.valueOf("306eff33"), Assets.Particles.WATER),
   SPIRIT_FIRE(45, 10, new AddSunlightEffect(), new SpawnFireEffect(), Color.valueOf("ffc2b488"), Assets.Particles.FIRE);

   private final int health;
   private final float maxSpeed;
   private final SpiritAbsorbEffect absorbEffect;
   private final SpiritSpawnEffect spawnEffect;
   private final Color lightingColor;
   private final String particleId;


   SpiritType(int health, float maxSpeed, SpiritAbsorbEffect absorbEffect, SpiritSpawnEffect spawnEffect, Color lightingColor, String particleId) {
      this.health = health;
      this.maxSpeed = maxSpeed;
      this.absorbEffect = absorbEffect;
      this.spawnEffect = spawnEffect;
      this.lightingColor = lightingColor;
      this.particleId = particleId;
   }

   public String getParticleId() {
      return particleId;
   }

   public Color getLightingColor() {
      return lightingColor;
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
