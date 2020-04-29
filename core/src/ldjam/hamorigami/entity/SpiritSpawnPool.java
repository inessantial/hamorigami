package ldjam.hamorigami.entity;

import ldjam.hamorigami.model.SpiritType;

import java.util.ArrayList;
import java.util.List;

public class SpiritSpawnPool {

   private float totalDuration = 0f;

   public class SpiritSpawn {
      public final SpiritType[] spawns;
      public final float durationUntil;

      public SpiritSpawn(float durationUntil, SpiritType[] spawns) {
         this.durationUntil = durationUntil;
         this.spawns = spawns;
      }
   }

   private final List<SpiritSpawn> spawnList = new ArrayList<>();

   private int currentIndex = 0;

   public void addSpawnWave(float durationUntilNext, SpiritType... spawns) {
      totalDuration += durationUntilNext;
      spawnList.add(new SpiritSpawn(durationUntilNext, spawns));
   }

   public float getTotalDuration() {
      return totalDuration;
   }

   public SpiritSpawn getNext() {
      if (spawnList.isEmpty() || currentIndex >= spawnList.size()) {
         return null;
      }
      SpiritSpawn current = spawnList.get(currentIndex);
      currentIndex++;
      if (currentIndex == spawnList.size()) {
         return null;
      }
      return current;
   }
}
