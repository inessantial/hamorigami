package ldjam.hamorigami.screens;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.debug.DebugMetric;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.cutscene.IntroCutscene;
import ldjam.hamorigami.cutscene.OutroCutscene;
import ldjam.hamorigami.effects.DayProgress;
import ldjam.hamorigami.gamemode.*;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.TreeStatus;

public class IngameScreen extends BaseScreen {

   private GamePhaseHandler phaseHandler;
   private GameplayPhase gameplayPhase;

   public IngameScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      super.onCreate(context);
      this.phaseHandler.addPhase(Phases.TITLE, new TitlePhase(getDayProgress(context), phaseHandler));
      this.phaseHandler.addPhase(Phases.INTRO, new CutscenePhase(new IntroCutscene(), phaseHandler, Phases.GAMEPLAY,
            Messages.STORY_INTRO_1,
            Messages.STORY_INTRO_2,
            Messages.STORY_INTRO_3,
            Messages.STORY_INTRO_4,
            Messages.STORY_INTRO_5,
            Messages.STORY_INTRO_6
      ));
      this.phaseHandler.addPhase(Phases.GAMEPLAY, gameplayPhase);
      this.phaseHandler.addPhase(Phases.GAMEOVER, new GameOverPhase(phaseHandler));
      this.phaseHandler.addPhase(Phases.OUTRO, new CutscenePhase(new OutroCutscene(), phaseHandler, Phases.CREDITS,
            Messages.STORY_OUTRO_1,
            Messages.STORY_OUTRO_2,
            Messages.STORY_OUTRO_3,
            Messages.STORY_OUTRO_4
      ));
      this.phaseHandler.addPhase(Phases.CREDITS, new CreditsPhase(phaseHandler));

      setupDebugUi(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      phaseHandler.update(delta);

   }

   @Override
   protected DayProgress getDayProgress(GameContext2D context) {
      if (gameplayPhase == null) {
         this.phaseHandler = new GamePhaseHandler(context, treeObject);
         this.gameplayPhase = new GameplayPhase(phaseHandler);
      }
      return gameplayPhase;
   }

   private void setupDebugUi(GameContext2D context) {
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
