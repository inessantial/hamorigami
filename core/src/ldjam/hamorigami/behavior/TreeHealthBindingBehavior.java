package ldjam.hamorigami.behavior;

import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.audio.JukeBox;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.screens.GameOverScreen;

public class TreeHealthBindingBehavior extends BehaviorAdapter {

   private final GameObject treeObject;
   private final JukeBox jukeBox;
   private final GameContext2D context;

   public TreeHealthBindingBehavior(GameObject treeObject, AudioManager audioManager, GameContext2D context) {
      this.treeObject = treeObject;
      this.context = context;
      this.jukeBox = new JukeBox(audioManager, 400, Assets.Sounds.DEATH_01, Assets.Sounds.DEATH_02);
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      HealthData treeHealth = treeObject.getAttribute(HealthData.class);
      source.getColor().a = treeHealth.getHealthPercentage();
      if (treeHealth.isDead()) {
         jukeBox.playSound(source.getLeft(), source.getTop());
         // game over! let's restart the day
         context.getBehaviorManager().clear();
         ColorTransition colorTransition = new ColorTransition();
         colorTransition.setColor(Color.WHITE.cpy());
         context.getAudioManager().spawnSound(Assets.Sounds.DEATH, source.getLeft(), source.getTop(), 1f, 0.8f, 200f);
         context.getScreenTransitions().out(colorTransition, new GameOverScreen((HamorigamiGame) context.getGame()), 1f);
      }
   }
}
