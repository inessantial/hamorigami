package ldjam.hamorigami.entity;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.model.SpiritType;

public class EntityFactory {

   private final GameContext2D context;

   public EntityFactory(GameContext2D context) {
      this.context = context;
   }

   public GameObject spawnSpirit(SpiritType spiritType, float x, float y) {
      GameObject object = context.getGameWorld().addObject();
      object.setZIndex(3);
      object.setType(spiritType);
      object.setPosition(context.getGameCamera().getLeft() + x, context.getGameCamera().getTop() + y);
      object.setDimensions(32f, 32f);
      object.setAttribute(HealthData.class, new HealthData(spiritType.getHealth()));
      object.setAttribute(Movement.class, new Movement(spiritType.getMaxSpeed(), context.getGameCamera()));
      context.getBehaviorManager().apply(object.getAttribute(Movement.class), object);
      Tween.to(object, GameObjectTween.OFFSET_Y, 0.6f)
            .target(4f)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(SharedTweenManager.getInstance());
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
}
