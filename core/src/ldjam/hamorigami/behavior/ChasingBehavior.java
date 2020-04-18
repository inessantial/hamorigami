package ldjam.hamorigami.behavior;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.Movement;

public class ChasingBehavior extends BehaviorAdapter {

   private final GameObject target;
   private final Vector2 direction = new Vector2();
   private final Vector2 offset = new Vector2();

   public ChasingBehavior(GameObject target, float offsetX, float offsetY) {
      this.target = target;
      offset.set(offsetX, offsetY);
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      Movement mover = source.getAttribute(Movement.class);
      direction.x = (target.getLeft() + offset.x) - source.getLeft();
      direction.y = (target.getTop() + offset.y) - source.getTop();
      mover.lookAtWorld(source.getLeft() + direction.x, source.getTop() + direction.y);
      mover.move(direction);
   }
}
