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
      if (directionAngle < 65f || directionAngle >= 335f) {
         direction = "EAST";
      } else if (directionAngle < 145f && directionAngle >= 75f) {
         direction = "NORTH";
      } else if (directionAngle < 195f && directionAngle >= 165f) {
         direction = "WEST";
      }
      String type = "HOVERING";
      SpiritAnimationType animationType = SpiritAnimationType.valueOf(type + "_" + direction);
      if (animationType.toString().contains("WEST") || (animationType.toString().contains("EAST"))) {
         object.setAttribute(SpiritAnimationType.class, animationType);
      }
      return animationType;
   }
}
