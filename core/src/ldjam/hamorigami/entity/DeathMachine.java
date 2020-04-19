package ldjam.hamorigami.entity;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.SpiritType;

public class DeathMachine extends BehaviorAdapter {

   private final GameContext2D context;

   public DeathMachine(GameContext2D context) {
      this.context = context;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (source.hasAttribute(HealthData.class) && source.getType() instanceof SpiritType) {
         if (source.getAttribute(HealthData.class).isDead()) {
            context.getGameWorld().remove(source);
         }
      }
   }
}
