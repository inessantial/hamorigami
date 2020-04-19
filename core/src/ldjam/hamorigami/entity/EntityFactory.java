package ldjam.hamorigami.entity;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.behavior.TreeBehavior;
import ldjam.hamorigami.model.*;

import static ldjam.hamorigami.model.ObjectType.GAUGE;

public class EntityFactory {

   private final GameContext2D context;

   public EntityFactory(GameContext2D context) {
      this.context = context;
   }

   public GameObject spawnGauge(float x, float y) {
      GameObject object = context.getGameWorld().addObject();
      object.setPosition(context.getGameCamera().getLeft() + x, context.getGameCamera().getTop() + y);
      object.setDimensions(32f, 128f);
      object.setType(GAUGE);
      object.setActive(false);
      return object;
   }

   public GameObject spawnSpirit(SpiritType spiritType, float x, float y) {
      GameObject object = context.getGameWorld().addObject();
      object.setZIndex(3);
      object.setType(spiritType);
      object.setPosition(context.getGameCamera().getLeft() + x, context.getGameCamera().getTop() + y);
      object.setDimensions(32f, 64f);
      object.setAttribute(HealthData.class, new HealthData(spiritType.getHealth()));
      object.setAttribute(Movement.class, new Movement(spiritType.getMaxSpeed(), context.getGameCamera()));
      context.getBehaviorManager().apply(object.getAttribute(Movement.class), object);
      return object;
   }

   public GameObject spawnTree() {
      GameObject object = context.getGameWorld().addObject();
      object.setType(ObjectType.TREE);
      object.setZIndex(2);
      object.setPosition(
            context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() / 2f - 100,
            context.getGameCamera().getTop() + 30);
      object.setDimensions(200, 200);
      object.setAttribute(TreeStatus.class, new TreeStatus());
      object.setAttribute(HealthData.class, new HealthData(1000));
      context.getBehaviorManager().apply(new TreeBehavior(), object);
      return object;
   }

   public GameObject spawnFloor() {
      GameObject object = context.getGameWorld().addObject();
      object.setType(ObjectType.FLOOR);
      object.setZIndex(1);
      object.setPosition(
            context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() / 2f - 400,
            context.getGameCamera().getTop());
      object.setDimensions(800, 100);
      return object;
   }

   public GameObject spawnDamageTelegraph(final GameObject owner, final float centerX, final float centerY, final float width, final float height, final float rotation) {
      final GameObject damage = context.getGameWorld().addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setType("DAMAGE");
            target.setPosition(centerX, centerY);
            target.setRotation(rotation);
            target.setDimensions(width, height);
            target.setAttribute("owner", owner);
         }
      }, false);
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int type, BaseTween<?> source) {
            context.getGameWorld().remove(damage);
         }
      }).delay(0.1f).start(SharedTweenManager.getInstance());
      context.getBehaviorManager().apply(new BehaviorAdapter() {

         private boolean applicableForRemoval;

         @Override
         public void update(GameObject source, float delta) {
            super.update(source, delta);
            if (applicableForRemoval) {
               context.getGameWorld().remove(source);
            }
         }

         @Override
         public void update(GameObject source, GameObject target, float delta) {
            super.update(source, target, delta);
            if ("DAMAGE".equals(source.getType()) && source.collidesWith(target)) {
               if (target.equals(source.getAttribute("owner"))) {
                  // let's not hit ourselves ;)
                  return;
               }
               if (target.hasAttribute(HealthData.class) && target.getType() != ObjectType.TREE) {
                  if (!target.getAttribute(HealthData.class).isDead()) {
                     target.getAttribute(HealthData.class).reduceHealth(20);
                     target.setColor(1f, 1f, 1f, 0.5f);
                     TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 1f);
                     applicableForRemoval = true;
                  }
               }
            }
         }
      }, damage);
      return damage;
   }
}
