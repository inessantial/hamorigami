package ldjam.hamorigami.audio;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.audio.AudioManager;

public class JukeBox {

   public static float MINIMUM_INTERVAL_MILLIS = 400f;

   private final AudioManager audioManager;
   private final String[] audioFiles;
   private float pitchVariation = 0.1f;
   private final float range;
   private long interval = -1;
   private float volume = 0.3f;
   private float basePitch = 1f;
   private float minimumIntervalMillis = MINIMUM_INTERVAL_MILLIS;

   public JukeBox(AudioManager audioManager, float range, String... audioFiles) {
      this.audioManager = audioManager;
      this.range = range;
      this.audioFiles = audioFiles;
   }

   public void setBasePitch(float basePitch) {
      this.basePitch = basePitch;
   }

   public void setVolume(float volume) {
      this.volume = volume;
   }

   public void setPitchVariation(float pitchVariation) {
      this.pitchVariation = pitchVariation;
   }

   public void setMinimumIntervalMillis(float minimumIntervalMillis) {
      this.minimumIntervalMillis = minimumIntervalMillis;
   }



   public void playSound(float x, float y) {
      if (audioFiles == null || audioFiles.length == 0) {
         Gdx.app.error("SOUND", "Missing audio files in JukeBox!");
         return;
      }
      if (interval >= 0 && System.currentTimeMillis() - interval < minimumIntervalMillis) {
         return;
      }
      String audioFile = audioFiles[(int) (audioFiles.length * Math.random())];
      float pitch = (float) ((basePitch - pitchVariation / 2f) + (pitchVariation * Math.random()));
      audioManager.spawnSound(audioFile, x, y, pitch, volume, range);
      System.out.println("spawned sound ");
      interval = System.currentTimeMillis();
   }
}