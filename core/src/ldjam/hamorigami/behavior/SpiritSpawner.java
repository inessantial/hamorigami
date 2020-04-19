package ldjam.hamorigami.behavior;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.model.SpiritType;

public class SpiritSpawner implements Updateable {

   private final float intervalInSeconds;
   private final EntityFactory entityFactory;
   private final GameContext2D context;
   private final GameObject treeObject;

   private int spawns = 0;
   private final DeltaTimer deltaTimer = new DeltaTimer();

   public SpiritSpawner(float intervalInSeconds, EntityFactory entityFactory, GameContext2D context, GameObject treeObject) {
      this.intervalInSeconds = intervalInSeconds;
      this.entityFactory = entityFactory;
      this.context = context;
      this.treeObject = treeObject;
   }

   @Override
   public void update(float delta) {
      deltaTimer.update(delta);
      if (deltaTimer.reached(intervalInSeconds)) {
         deltaTimer.reset();
         spawns++;
         for (int i = 0; i < getSpiritSpawnCount(); ++i) {
            spawnRandomSpirit();
         }
      }
   }

   private void spawnRandomSpirit() {
      SpiritType randomType = SpiritType.values()[1 + (int) ((SpiritType.values().length - 1) * Math.random())];
      GameObject spirit = entityFactory.spawnSpirit(randomType, -32, -64);
      randomType.getSpawnEffect().onSpawnSpirit(spirit, treeObject, context);
   }

   private int getSpiritSpawnCount() {
      if (Math.random() < 0.3f) {
         return (int) (1 + (Math.random() * (spawns / 1000f)));
      } else {
         return 1;
      }
   }
}
