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
      boolean spawnLeftSide = Math.random() < 0.5f;
      float x;
      if (spawnLeftSide) {
         x = (float) (10f + 20f * Math.random());
      } else {
         x = (float) (context.getGameCamera().getScaledCameraWidth() - 64f - 20f * Math.random());
      }
      float y = (float) (Math.random() * (context.getGameCamera().getScaledCameraHeight() - 128));
      GameObject spirit = entityFactory.spawnSpirit(randomType, x, y);
      float padding = 35f;
      float targetX = padding + (float) Math.random() * (treeObject.getWidth() - padding * 2f);
      float targetY = padding + (float) Math.random() * (treeObject.getHeight() - padding * 2f);
      context.getBehaviorManager().apply(new ChasingBehavior(treeObject, targetX, targetY), spirit);
   }

   private int getSpiritSpawnCount() {
      if (Math.random() < 0.3f) {
         return (int) (1 + (Math.random() * (spawns / 1000f)));
      } else {
         return 1;
      }
   }
}
