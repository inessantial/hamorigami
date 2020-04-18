package ldjam.hamorigami.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.mappings.Xbox;

public class IngameControllerAdapter extends ControllerAdapter {

   public IngameControllerAdapter() {
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getEscapeButton(controller)) {
         Gdx.app.exit();
         return true;
      }
      return false;
   }

   private int getEscapeButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.START;
      }
      return -1;
   }
}
