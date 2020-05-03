package ldjam.hamorigami.screens;

import de.bitbrain.braingdx.debug.DebugMetric;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.gamemode.*;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.model.TreeStatus;
import ldjam.hamorigami.setup.DaySetupBuilder;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.setup.GameplaySetupBuilder;

public class StoryModeScreen extends BaseScreen {

   private GamePhaseHandler phaseHandler;

   public StoryModeScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final HamorigamiContext context) {
      super.onCreate(context);
      this.phaseHandler = new GamePhaseHandler(context, treeObject);
      this.phaseHandler.addPhase(Phases.TITLE, new TitlePhase(phaseHandler, setup));
      this.phaseHandler.addPhase(Phases.CUTSCENE, new CutscenePhase(phaseHandler, setup));
      this.phaseHandler.addPhase(Phases.GAMEPLAY, new GameplayPhase(phaseHandler, setup));
      this.phaseHandler.addPhase(Phases.GAMEOVER, new GameOverPhase(phaseHandler));
      this.phaseHandler.addPhase(Phases.CREDITS, new CreditsPhase(phaseHandler));

      setupDebugUi(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      phaseHandler.update(delta);

   }

   @Override
   protected GameplaySetup buildGameplaySetup(HamorigamiContext context) {
      // TODO make gameplay setup filebased
      return new GameplaySetupBuilder()
            .addDay(new DaySetupBuilder()
                  .addSpawns(0f, SpiritType.SPIRIT_WATER)
                  .addSpawns(0.1f, SpiritType.SPIRIT_WATER)
                  .addSpawns(0.5f, SpiritType.SPIRIT_WATER, SpiritType.SPIRIT_WATER, SpiritType.SPIRIT_WATER)
                  .startOfDayCutscene(new CutsceneBuilder(context)
                        .wait(3f)
                        .spawn("player", SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 3.5f, 0f, true)
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
                        .removeAttribute("player", "swiping")
                        .build())
                  .build())
            .build();
   }

   private void setupDebugUi(HamorigamiContext context) {
      context.getDebugPanel().addMetric("tree watered level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getTreeWateredLevel());
         }
      });
      context.getDebugPanel().addMetric("soil water level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel());
         }
      });
      context.getDebugPanel().addMetric("tree health", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return (treeObject.getAttribute(HealthData.class).getHealthPercentage() * 100f) + "%";
         }
      });
   }
}
