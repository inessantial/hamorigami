package ldjam.hamorigami.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.Movement;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.*;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   private final GameObject playerObject;
   private final Vector2 moveDirection = new Vector2();

   public IngameKeyboardAdapter(GameObject playerObject) {
      this.playerObject = playerObject;
   }

   @Override
   public void update(float delta) {
      if (input.isKeyPressed(ESCAPE)) {
         Gdx.app.exit();
      }
      if (input.isKeyPressed(W)) {
         moveDirection.y = 1f;
      }
      if (input.isKeyPressed(A)) {
         moveDirection.x = -1;
      }
      if (input.isKeyPressed(S)) {
         moveDirection.y = -1;
      }
      if (input.isKeyPressed(D)) {
         moveDirection.x = 1;
      }
      playerObject.getAttribute(Movement.class).move(moveDirection);
      moveDirection.x = 0;
      moveDirection.y = 0;
   }
}
