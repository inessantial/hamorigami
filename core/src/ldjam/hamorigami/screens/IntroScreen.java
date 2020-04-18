package ldjam.hamorigami.screens;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.cutscene.CutsceneListener;

public class IntroScreen extends BrainGdxScreen2D<HamorigamiGame> {


   public IntroScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      CutsceneListener cutsceneListener = new CutsceneListener() {
         @Override
         public void cutsceneCompleted() {
            context.getScreenTransitions().out(new IngameScreen(getGame()), 1f);
         }
      };
      Cutscene cutscene = new CutsceneBuilder(cutsceneListener)
            .positionCamera(200, 200)
            .fadeIn(2f)
            .initiateDialog(null)
            .delay(3f)
            .moveCameraTo(200, -200,2f)
            .initiateDialog(null)
            .moveCameraTo(50, -100, 1.5f)
            .zoomCameraIn(0.2f)
            .initiateDialog(null)
            .build();
      cutscene.play();
   }
}
