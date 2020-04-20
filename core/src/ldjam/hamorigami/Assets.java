package ldjam.hamorigami;

public interface Assets {

   interface Fonts {
      String VISITOR = "font/visitor.ttf";
      String OPENSANS = "font/opensans.ttf";
   }

   interface Textures {
      String SPIRIT_EARTH_KODAMA_SRITESHEET = "texture/kodama-sprite-sheet.png";
      String SPIRIT_WATER_AME_SPRITESHEET = "texture/ame-sprite-sheet.png";
      String SPIRIT_FIRE_HI_SPRITESHEET = "texture/hi-sprite-sheet.png";

      String TREE = "texture/tree.png";

      String BACKGROUND_FLOOR = "texture/background-floor.png";

      String DROPSHADOW = "texture/dropshadow.png";

      String GAUGE_WATER = "texture/gauge-water.png";
      String GAUGE_WATER_TOP = "texture/gauge-water-top.png";
      String GAUGE_OVERLAY = "texture/gauge-overlay.png";
      String GAUGE_GRASS = "texture/gauge-grass.png";

      String CITYSCAPE = "texture/cityscape.png";
   }

   interface Musics {
      String BACKGROUND_01 = "music/bgm_01.ogg";
      String OUTRO = "music/outro.ogg";
   }

   interface Sounds {
      String WATER_DEATH_01 = "sound/waterdeath_01.ogg";
      String WATER_DEATH_02 = "sound/waterdeath_02.ogg";
      String WATER_DEATH_03 = "sound/waterdeath_03.ogg";

      String DEATH_01 = "sound/death_01.ogg";
      String DEATH_02 = "sound/death_02.ogg";

      String BRUSH_01 = "sound/brush_01.ogg";
      String BRUSH_02 = "sound/brush_02.ogg";
      String BRUSH_03 = "sound/brush_03.ogg";

      String WOOSH = "sound/woosh_short.ogg";
   }

   interface Particles {
      String EARTH = "particles/earth.p";
      String FIRE = "particles/fire.p";
      String WATER = "particles/water.p";
   }
}
