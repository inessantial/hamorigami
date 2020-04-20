package ldjam.hamorigami.gamemode;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;

public interface GamePhase extends Updateable {
   void disable(GameContext2D context, GameObject treeObject);
   void enable(GameContext2D context, GameObject treeObject);
   boolean isFinished();
}
