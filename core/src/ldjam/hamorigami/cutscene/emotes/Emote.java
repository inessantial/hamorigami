package ldjam.hamorigami.cutscene.emotes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import ldjam.hamorigami.Assets;

public enum Emote {
   SMILE(0),
   CRY(0),
   SHAKE_HEAD(0),
   AGREE(0);

   private Animation<TextureRegion> animation;
   private final int yIndex;

   Emote(int yIndex) {
      this.yIndex = yIndex;
   }

   public Animation<TextureRegion> getAnimation() {
      if (this.animation == null) {
         final int frames = 8;
         final int size = 16;
         final float duration = 0.1f;
         final Texture texture = SharedAssetManager.getInstance().get(Assets.Textures.EMOTE_SPRITESHEET);
         Array<TextureRegion> regions = new Array<TextureRegion>();
         for (int i = 0; i < frames; ++i) {
            regions.add(new TextureRegion(texture, i * size, yIndex * size, size, size));
         }
         this.animation = new Animation<TextureRegion>(duration, regions);
      }
      return animation;
   }
}
