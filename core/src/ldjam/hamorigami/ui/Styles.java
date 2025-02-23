package ldjam.hamorigami.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import ldjam.hamorigami.Assets;

public class Styles {

   public static final Label.LabelStyle LABEL_DEBUG = new Label.LabelStyle();
   public static final Label.LabelStyle DIALOG_TEXT = new Label.LabelStyle();
   public static final Label.LabelStyle STORY = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_CAPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_LOGO = new Label.LabelStyle();

   public static void init() {
      LABEL_DEBUG.font = bake(Assets.Fonts.VISITOR, 28);
      DIALOG_TEXT.font = bake(Assets.Fonts.OPENSANS, 22, true);
      DIALOG_TEXT.fontColor = Color.WHITE.cpy();
      STORY.font = bake(Assets.Fonts.OPENSANS, 18, true);
      STORY.fontColor = Color.WHITE.cpy();
      LABEL_CAPTION.font = bake(Assets.Fonts.OPENSANS, 38, true);
      LABEL_CAPTION.fontColor = Color.WHITE.cpy();
      LABEL_LOGO.font = bake(Assets.Fonts.OPENSANS, 85, true);
      LABEL_LOGO.fontColor = Color.WHITE.cpy();
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
