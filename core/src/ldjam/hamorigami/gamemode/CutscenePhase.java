package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.cutscene.emotes.EmoteManager;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.model.SpiritType;

public class CutscenePhase implements GamePhase, Proceedable {

   private final GamePhaseHandler phaseHandler;
   private final String nextPhase;
   private boolean aborted;
   private EmoteManager emoteManager;
   private Cutscene cutscene;

   public CutscenePhase(GamePhaseHandler phaseHandler, String nextPhase) {
      this.phaseHandler = phaseHandler;
      this.nextPhase = nextPhase;
   }

   @Override
   public void disable(final GameContext2D context, GameObject treeObject) {
      emoteManager.clear();
      cutscene.stop();
   }

   @Override
   public void enable(GameContext2D context, GameObject treeObject) {
      context.getInputManager().register(new ProceedableControllerAdapter(this));
      aborted = false;

      EntityFactory entityFactory = new EntityFactory(context);
      this.emoteManager = new EmoteManager(context);
      this.cutscene = new CutsceneBuilder(entityFactory, null, emoteManager, context)
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
            .moveByYoyo("player", -30f, 0f, 2f)
            .wait(0.5f)
            .say("WHO ARE YOU?!", "player")
            .emote(Emote.SMILE, "player")
            .wait(0.3f)
            .say("I come from far above. To give you the elixir of life.", "ame")
            .wait(1f)
            .shakeScreen(5, 1f)
            .wait(0.5f)
            .say("???", "player")
            .say("???", "ame")
            .wait(2f)
            .spawn("hi", SpiritType.SPIRIT_SUN, 400f, 100f)
            .fadeIn("hi", 3)
            .wait(3f)
            .say("What are you doing here, Ame?", "hi")
            .say("You are not supposed to be here.", "hi")
            .wait(0.5f)
            .emote(Emote.SMILE, "ame")
            .say("I... I need to fulfill my destiny.", "ame")
            .build();
      cutscene.play();
   }

   @Override
   public boolean isFinished() {
      return aborted || cutscene.isOver();
   }

   @Override
   public void update(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && !aborted) {
         skip();
      }
   }

   @Override
   public void proceed() {

   }

   @Override
   public void skip() {
      aborted = true;
      phaseHandler.changePhase(nextPhase);
   }
}
