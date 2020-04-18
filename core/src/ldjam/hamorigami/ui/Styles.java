package ldjam.hamorigami.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import ldjam.hamorigami.Assets;

public class Styles {

   public static final Label.LabelStyle LABEL_DEBUG = new Label.LabelStyle();

   public static void init() {
      LABEL_DEBUG.font = bake(Assets.Fonts.VISITOR, 28);
   }

   public static BitmapFont bake(String fontPath, int size) {
      return bake(fontPath, size, false);
   }

   public static BitmapFont bake(String fontPath, int size, boolean mono) {
      FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = mono;
      BitmapFont font = generator.generateFont(param);
      // disable integer positions so UI aligns correctly
      font.setUseIntegerPositions(false);
      return font;
   }

}
