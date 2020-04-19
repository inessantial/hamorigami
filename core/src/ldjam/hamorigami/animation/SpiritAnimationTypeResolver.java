package ldjam.hamorigami.animation;

import de.bitbrain.braingdx.graphics.animation.AnimationTypeResolver;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.SpiritAnimationType;

public class SpiritAnimationTypeResolver implements AnimationTypeResolver<GameObject> {

   @Override
   public Object getAnimationType(GameObject object) {
      Movement movement = object.getAttribute(Movement.class);
      float directionAngle = movement.getMovement().angle();
      if (movement.getMovement().len() < 20f && object.hasAttribute(SpiritAnimationType.class)) {
         return object.getAttribute(SpiritAnimationType.class);
      }
      String direction = "EAST";
      if (directionAngle < 90f || directionAngle >= 270) {
         direction = "WEST";
      }
      String type = "HOVERING";
      if (object.hasAttribute("falling")) {
         type = "FALLING";
      } else if (movement.getMovement().len() < 25f) {
         type = "IDLE";
      }
      SpiritAnimationType animationType = SpiritAnimationType.valueOf(type + "_" + direction);
      object.setAttribute(SpiritAnimationType.class, animationType);
      return animationType;
   }
}
