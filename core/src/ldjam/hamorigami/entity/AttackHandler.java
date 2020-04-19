package ldjam.hamorigami.entity;

import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;

public class AttackHandler implements Updateable {

   private static final float ATTACK_INTERVAL = 0.4f;

   private final DeltaTimer attackTimer = new DeltaTimer(ATTACK_INTERVAL);

   private final GameObject attacker;
   private final EntityFactory entityFactory;

   private boolean attacking = false;

   public AttackHandler(GameObject attacker, EntityFactory entityFactory) {
      this.attacker = attacker;
      this.entityFactory = entityFactory;
   }

   public void attack() {
      attacking = true;
   }

   @Override
   public void update(float delta) {
      attackTimer.update(delta);
      if (attacking && attackTimer.reached(ATTACK_INTERVAL)) {
         attackTimer.reset();
         entityFactory.spawnDamageTelegraph(attacker, attacker.getLeft(), attacker.getTop(), attacker.getWidth() * 1.5f, attacker.getHeight() * 1.5f, 0f);
      }
      attacking = false;
   }
}
