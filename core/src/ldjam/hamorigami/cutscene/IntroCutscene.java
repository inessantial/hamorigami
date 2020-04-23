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

   private GameObject kodama;
   private EmoteManager emoteManager;

   @Override
   public void cleanup(GameContext2D context) {
      SharedTweenManager.getInstance().killTarget(kodama);
      emoteManager.clear();
      kodama.removeAttribute("swiping");
   }

   @Override
   public void setup(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);
      kodama = entityFactory.spawnSpirit(SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 3.5f, 0f);
      kodama.setDimensions(64f, 64f);
      kodama.getColor().a = 0f;
      kodama.setAttribute("swiping", true);
      float currentX = kodama.getLeft();
      Tween.to(kodama, GameObjectTween.ALPHA, 3f).delay(4f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(kodama, GameObjectTween.POS_X, 4f).delay(7f)
            .target(currentX - 200f)
            .repeatYoyo(Tween.INFINITY, 0f)
            .ease(TweenEquations.easeInOutCubic)
            .start(SharedTweenManager.getInstance());

      this.emoteManager = new EmoteManager(context);
      Cutscene cutscene = new CutsceneBuilder(null, emoteManager, context)
            .wait(4f)
            .say("Oh!", kodama)
            .wait(2f)
            .emote(Emote.SMILE, kodama)
            .say("What is this?", kodama)
            .wait(2f)
            .shakeScreen(10, 2f)
            .say("AAAAHHH!!! HELP!!!!", kodama)
            .wait(1f)
            .say("That must have been the neighbours.", kodama)
            .build();
      cutscene.play();
   }
}
