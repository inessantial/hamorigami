package ldjam.hamorigami.entity;

import ldjam.hamorigami.model.SpiritType;

import java.util.ArrayList;
import java.util.List;

public class SpiritSpawnPool {

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
      spawnList.add(new SpiritSpawn(durationUntilNext, spawns));
   }

   public SpiritSpawn getNext() {
      SpiritSpawn current = spawnList.get(currentIndex);
      currentIndex++;
      if (currentIndex == spawnList.size()) {
         currentIndex = 0;
      }
      return current;
   }
}
