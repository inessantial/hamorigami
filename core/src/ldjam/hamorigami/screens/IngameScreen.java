package ldjam.hamorigami.screens;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;

public class IngameScreen extends BrainGdxScreen2D<HamorigamiGame> {

   public IngameScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      context.setDebug(getGame().isDebug());
      setupInput(context);
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter());
      context.getInputManager().register(new IngameControllerAdapter());
   }


}
