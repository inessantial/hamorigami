package ldjam.hamorigami.entity;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
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
         attacker.setAttribute("attacking", true);
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               attacker.removeAttribute("attacking");
            }
         }).delay(0.2f).start(SharedTweenManager.getInstance());
         entityFactory.spawnDamageTelegraph(attacker, attacker.getLeft(), attacker.getTop(), attacker.getWidth() * 1.5f, attacker.getHeight() * 1.5f, 0f);
      }
      attacking = false;
   }
}
