package ldjam.hamorigami.gamemode;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.TreeStatus;

import java.util.HashMap;
import java.util.Map;

public class GamePhaseHandler implements Updateable {

   private String currentPhase;
   private String nextPhase;

   private final Map<String, GamePhase> phases = new HashMap<>();
   private final GameContext2D context;
   private final GameObject treeObject;

   public GamePhaseHandler(GameContext2D context, GameObject treeObject) {
      this.context = context;
      this.treeObject = treeObject;
   }

   public void addPhase(String id, GamePhase phase) {
      this.phases.put(id, phase);
      if (this.phases.size() == 1) {
         changePhase(id);
      }
   }

   public void changePhase(String id) {
      this.nextPhase = id;
      if (currentPhase == null) {
         currentPhase = nextPhase;
         phases.get(currentPhase).enable(context, treeObject);
      }
   }

   @Override
   public void update(float delta) {
      if (currentPhase != null && !phases.get(currentPhase).isFinished()) {
         phases.get(currentPhase).update(delta);
      }
      if (nextPhase == null || currentPhase == null) {
         return;
      }
      if (!currentPhase.equals(nextPhase)) {
         GamePhase phase = phases.get(currentPhase);
         phase.disable(context, treeObject);
         if (phase.isFinished()) {
            context.getBehaviorManager().clear();
            context.getInputManager().clear();
            currentPhase = nextPhase;
            treeObject.getAttribute(HealthData.class).reset();
            treeObject.getAttribute(TreeStatus.class).reset();
            if (!phases.containsKey(currentPhase)) {
               throw new GdxRuntimeException("No phase defined with key " + currentPhase);
            }
            phases.get(currentPhase).enable(context, treeObject);
         }
      }
   }
}
