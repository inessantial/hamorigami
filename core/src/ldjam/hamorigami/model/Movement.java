package ldjam.hamorigami.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;

public class Movement extends BehaviorAdapter {

   private final Vector2 lookDirection = new Vector2(0f, 1f);
   private final Vector2 moveDirection = new Vector2(0f, 1f);
   private final Vector3 tmp = new Vector3();
   private final GameCamera gameCamera;
   private final float maxSpeed;
   private Vector2 movement = new Vector2();
   private GameObject gameObject;

   public Movement(float maxSpeed, GameCamera gameCamera) {
      this.maxSpeed = maxSpeed;
      this.gameCamera = gameCamera;

   }

   public void move(Vector2 direction, float amount) {
      moveDirection.x = direction.x;
      moveDirection.y = direction.y;
      moveDirection.nor();
      movement.x += moveDirection.x * amount;
      movement.y += moveDirection.y * amount;
   }

   public void move(Vector2 direction) {
      move(direction, maxSpeed);
   }

   public Vector2 getLookDirection() {
      return lookDirection;
   }

   public void lookAtScreen(float screenX, float screenY) {
      tmp.set(screenX, screenY, 0f);
      gameCamera.getInternalCamera().unproject(tmp);
      lookAtWorld(tmp.x, tmp.y);
   }

   public void lookAtWorld(float worldX, float worldY) {
      if (gameObject != null) {
         lookDirection.x = gameObject.getLeft() + gameObject.getWidth() / 2f - worldX;
         lookDirection.y = gameObject.getTop() + gameObject.getHeight() / 2f - worldY;
         lookDirection.nor();
      }
   }

   @Override
   public void onAttach(GameObject source) {
      this.gameObject = source;
   }

   @Override
   public void update(GameObject source, float delta) {
      float moveX = movement.x * delta;
      float moveY = movement.y * delta;
      gameObject.move(moveX, moveY);
      movement.scl(0.9f);
   }
}
