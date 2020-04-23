package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.cutscene.emotes.EmoteManager;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.model.SpiritType;

public class IntroCutscene implements CutsceneSetup {

   private EmoteManager emoteManager;

   @Override
   public void cleanup(GameContext2D context) {
      GameObject player  = context.getGameWorld().getObjectById("player");
      SharedTweenManager.getInstance().killTarget(player);
      emoteManager.clear();
      context.getGameWorld().getObjectById("player").removeAttribute("swiping");
   }

   @Override
   public void setup(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);

      this.emoteManager = new EmoteManager(context);
      Cutscene cutscene = new CutsceneBuilder(entityFactory, null, emoteManager, context)
            .wait(3f)
            .spawn("player", SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 3.5f, 0f)
            .fadeIn("player", 2)
            .wait(4f)
            .say("Oh! So much dirt...", "player")
            .setAttribute("player", "swiping")
            .moveByYoyo("player", -200f, 0f, 3f)
            .wait(2f)
            .emote(Emote.SMILE, "player")
            .removeAttribute("player", "swiping")
            .clearTweens("player")
            .say("What is this?", "player")
            .wait(2f)
            .shakeScreen(10, 2f)
            .setAttribute("player", "swiping")
            .say("AAAAHHH!!! HELP!!!!", "player")
            .moveBy("player", 250f, 0f, 2f)
            .wait(2f)
            .removeAttribute("player", "swiping")
            .wait(1f)
            .spawn("ame", SpiritType.SPIRIT_WATER, 200f, 100f)
            .fadeIn("ame", 2)
            .wait(2f)
            .say("I must give water!", "ame")
            .wait(0.5f)
            .setAttribute("player", "swiping")
            .moveBy("player", -10f, 0f, 0.5f)
            .wait(0.5f)
            .say("WHO ARE YOU?!", "player")
            .emote(Emote.SMILE, "player")
            .build();
      cutscene.play();
   }
}
