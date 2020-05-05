package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;

public class ParallaxMap {

   private final float initialOffsetX;
   private Sprite sprite;

   private GameCamera camera;

   private float parallaxity;

   private float width, height;

   private float scale;

   private Vector2 offset;

   private float alpha = 1f;

   public ParallaxMap(String assetId, GameCamera camera, float parallaxity) {
      this.camera = camera;
      this.sprite = new Sprite(SharedAssetManager.getInstance().get(assetId, Texture.class));
      this.parallaxity = parallaxity;
      this.width = sprite.getWidth();
      this.height = sprite.getHeight();

      float targetX = getTargetX(camera.getLeft());
      this.initialOffsetX = targetX - camera.getLeft();
   }

   public void setAlpha(float alpha) {
      this.alpha = alpha;
   }

   public void scale(float scale) {
      this.scale = scale;
   }

   public void setColor(Color color) {
      sprite.setColor(color);
   }

   public void draw(Batch batch) {
      float scaleOffetX = -(width * this.scale - width) / 2f;
      float scaleOffetY = -(height * this.scale - height) / 2f;

      float baseX = camera.getLeft();
      float baseY = camera.getTop();
      // correct X position
      float targetX = getTargetX(baseX);
      sprite.setPosition(targetX - initialOffsetX + scaleOffetX, baseY + scaleOffetY);
      sprite.setSize(width * this.scale, height * this.scale);
      sprite.draw(batch, alpha);
   }

   private float getTargetX(float focusX) {
      return focusX / parallaxity;
   }
}
