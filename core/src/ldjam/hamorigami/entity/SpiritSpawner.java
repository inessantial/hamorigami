package ldjam.hamorigami.entity;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.screens.CreditsScreen;
import ldjam.hamorigami.screens.StoryScreen;

public class SpiritSpawner implements Updateable {

   private final EntityFactory entityFactory;
   private final GameContext2D context;
   private final GameObject treeObject;
   private final SpiritSpawnPool spawnPool;

   private SpiritSpawnPool.SpiritSpawn currentSpawn;
   private final DeltaTimer deltaTimer = new DeltaTimer();
   private boolean gameOver = false;

   public SpiritSpawner(SpiritSpawnPool spawnPool, EntityFactory entityFactory, GameContext2D context, GameObject treeObject) {
      this.spawnPool = spawnPool;
      this.entityFactory = entityFactory;
      this.context = context;
      this.treeObject = treeObject;
      currentSpawn = spawnPool.getNext();
   }

   @Override
   public void update(float delta) {
      if (gameOver) {
         return;
      }
      deltaTimer.update(delta);
      if (deltaTimer.reached(currentSpawn.durationUntil)) {
         deltaTimer.reset();
         for (SpiritType type : currentSpawn.spawns) {
            spawnSpirit(type);
         }
         currentSpawn = spawnPool.getNext();
         if (currentSpawn == null) {
            gameOver = true;
            // game successful!
            context.getBehaviorManager().clear();
            CreditsScreen creditsScreen = new CreditsScreen((HamorigamiGame) context.getGame());
            context.getScreenTransitions().out(new StoryScreen((HamorigamiGame) context.getGame(), creditsScreen,
                  Messages.STORY_OUTRO_1, Messages.STORY_OUTRO_2, Messages.STORY_OUTRO_3, Messages.STORY_OUTRO_4), 1f);
         }
      }
   }

   private void spawnSpirit(SpiritType type) {
      GameObject spirit = entityFactory.spawnSpirit(type, -32, -64);
      type.getSpawnEffect().onSpawnSpirit(spirit, treeObject, context);
   }
}
