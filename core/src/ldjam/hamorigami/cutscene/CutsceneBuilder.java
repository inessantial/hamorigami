package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.cutscene.emotes.EmoteManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ldjam.hamorigami.cutscene.emotes.EmoteManager.*;

public class CutsceneBuilder {

   private final CutsceneListener cutsceneListener;
   private final GameContext2D context;
   private final Map<Float, List<CutsceneStep>> steps = new HashMap<>();
   private final EmoteManager emoteManager;
   private float currentTime = 0f;
   private float emoteTime = 0f;


   public CutsceneBuilder(final CutsceneListener cutsceneListener, final EmoteManager emoteManager, final GameContext2D context) {
      this.cutsceneListener = cutsceneListener;
      this.emoteManager = emoteManager;
      this.context = context;
   }

   public CutsceneBuilder positionCamera(final float x, final float y) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getGameCamera().setPosition(x, y);
         }
      });
      return this;
   }

   public CutsceneBuilder moveCameraTo(float x, float y, float duration) {
      // TODO add braingdx tween support for this
      return this;
   }

   public CutsceneBuilder wait(float seconds) {
      currentTime += seconds + emoteTime;
      emoteTime = 0;
      return this;
   }

   public CutsceneBuilder say(final String text, final GameObject target) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            emoteManager.say(text, target);
         }
      });
      emoteTime += SPEECH_FADE_IN_DURATION + SPEECH_DELAY_DURATION_PER_CHARACTER * text.length() + SPEECH_FADE_OUT_DURATION;
      return this;
   }

   public CutsceneBuilder emote(final Emote emote, final GameObject target) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            emoteManager.emote(emote, target);
         }
      });
      emoteTime += EMOTE_FADE_IN_DURATION + EMOTE_DELAY_DURATION + EMOTE_FADE_OUT_DURATION;
      return this;
   }

   public CutsceneBuilder cameraZoom(final float value, final GameCamera.ZoomMode zoomMode, final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            if (zoomMode == GameCamera.ZoomMode.TO_WIDTH) {
               Tween.to(context.getGameCamera(), GameCameraTween.ZOOM_WIDTH, seconds)
                     .target(value)
                     .start(SharedTweenManager.getInstance());
            }
            if (zoomMode == GameCamera.ZoomMode.TO_HEIGHT) {
               Tween.to(context.getGameCamera(), GameCameraTween.ZOOM_HEIGHT, seconds)
                     .target(value)
                     .start(SharedTweenManager.getInstance());
            } else {
               Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, seconds)
                     .target(value)
                     .start(SharedTweenManager.getInstance());
            }
         }
      });
      return this;
   }

   public CutsceneBuilder fadeIn(final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getScreenTransitions().in(seconds);
         }
      });
      return this;
   }

   public CutsceneBuilder fadeOut(final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getScreenTransitions().out(seconds);
         }
      });
      return this;
   }

   public CutsceneBuilder shakeScreen(final float intensity, final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getGameCamera().shake(intensity, seconds);
         }
      });
      return this;
   }

   public CutsceneBuilder playSound(final String soundPath, final float x, final float y) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getAudioManager().spawnSound(soundPath, x, y, 1f, 1f, 300);
         }
      });
      return this;
   }

   public CutsceneBuilder playSound(String soundPath, GameObject target) {
      return playSound(soundPath, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
   }

   public CutsceneBuilder playMusic(final String musicPath) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getAudioManager().playMusic(musicPath);
         }
      });
      return this;
   }

   public CutsceneBuilder stopMusic(final String musicPath) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getAudioManager().stopMusic(musicPath);
         }
      });
      return this;
   }

   public Cutscene build() {
      return new Cutscene(steps);
   }

   private List<CutsceneStep> getCurrentSteps() {
      if (!steps.containsKey(currentTime)) {
         steps.put(currentTime, new ArrayList<CutsceneStep>());
      }
      return steps.get(currentTime);
   }
}