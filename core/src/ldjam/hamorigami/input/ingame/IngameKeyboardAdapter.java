package ldjam.hamorigami.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.braingdx.util.Updateable;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.ESCAPE;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   @Override
   public void update(float delta) {
      if (input.isKeyPressed(ESCAPE)) {
         Gdx.app.exit();
      }
   }
}
