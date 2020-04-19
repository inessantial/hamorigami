package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.animation.SpiritAnimationTypeResolver;

public class SpiritRenderer extends AnimationRenderer {

   private final GameCamera gameCamera;

   public SpiritRenderer(GameCamera gameCamera, AnimationSpriteSheet spriteSheet, AnimationConfig config) {
      super(spriteSheet, config, new SpiritAnimationTypeResolver());
      this.gameCamera = gameCamera;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      Color originalColor = batch.getColor();
      float heightPercentage = 1f - (object.getTop() - gameCamera.getTop()) / gameCamera.getScaledCameraHeight();
      float alpha  = 1f * heightPercentage * object.getColor().a;
      batch.setColor(1f, 1f, 1f, alpha);
      float size = 32f * heightPercentage;
      float offset = 16f + 32f * (1f - heightPercentage);
      final Texture texture = SharedAssetManager.getInstance().get(Assets.Textures.DROPSHADOW, Texture.class);
      float y = Math.min(object.getTop() , gameCamera.getTop() + offset);
      batch.draw(texture, object.getLeft(), y, size, size);
      batch.setColor(originalColor);
      super.render(object, batch, delta);

   }
}
