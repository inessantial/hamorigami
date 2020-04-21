package ldjam.hamorigami.cutscene;

import de.bitbrain.braingdx.context.GameContext2D;

public interface CutsceneSetup {

   void cleanup(GameContext2D context);

   void setup(GameContext2D context);
}
