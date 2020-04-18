package ldjam.hamorigami.i18n;

public enum Messages {

   PLAY_GAME("");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
