package ldjam.hamorigami.cutscene;

import ldjam.hamorigami.dialog.Dialog;

public class CutsceneBuilder {

   private final CutsceneListener cutsceneListener;

   public CutsceneBuilder(final CutsceneListener cutsceneListener) {
      this.cutsceneListener = cutsceneListener;
   }

   public CutsceneBuilder positionCamera(float x, float y) {
      // TODO
      return this;
   }

   public CutsceneBuilder initiateDialog(Dialog dialog) {
      // TODO
      return this;
   }

   public CutsceneBuilder moveCameraTo(float x, float y, float duration) {
      // TODO
      return this;
   }

   public CutsceneBuilder delay(float seconds) {
      // TODO
      return this;
   }

   public CutsceneBuilder zoomCameraIn(float factor) {
      // TODO
      return this;
   }

   public CutsceneBuilder zoomCameraOut(float factor) {
      // TODO
      return this;
   }

   public CutsceneBuilder fadeIn(float seconds) {
      return this;
   }

   public CutsceneBuilder fadeOut(float seconds) {
      return this;
   }

   public CutsceneBuilder shakeScreen(float intensity, float seconds) {
      return this;
   }

   public CutsceneBuilder playSound(String soundPath, float x, float y) {
      return this;
   }

   public CutsceneBuilder playMusic(String musicPath) {
      return this;
   }

   public CutsceneBuilder stopMusic() {
      return this;
   }

   public Cutscene build() {
      // TODO
      return new Cutscene();
   }
}