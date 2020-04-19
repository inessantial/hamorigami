package ldjam.hamorigami.animation;

import de.bitbrain.braingdx.graphics.animation.AnimationTypeResolver;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.SpiritAnimationType;

public class SpiritAnimationTypeResolver implements AnimationTypeResolver<GameObject> {

   @Override
   public Object getAnimationType(GameObject object) {
      Movement movement = object.getAttribute(Movement.class);
      if (movement.getMoveDirection().len() < 0.1f && object.hasAttribute(SpiritAnimationType.class)) {
         return object.getAttribute(SpiritAnimationType.class);
      }
      float directionAngle = movement.getMoveDirection().angle();
      String direction = "SOUTH";
      if (directionAngle < 45f || directionAngle >= 315f) {
         direction = "EAST";
      } else if (directionAngle < 135f && directionAngle >= 45f) {
         direction = "NORTH";
      } else if (directionAngle < 225f && directionAngle >= 135f) {
         direction = "WEST";
      }
      String type = "HOVERING";
      SpiritAnimationType animationType = SpiritAnimationType.valueOf(type + "_" + direction);
      object.setAttribute(SpiritAnimationType.class, animationType);
      return animationType;
   }
}
