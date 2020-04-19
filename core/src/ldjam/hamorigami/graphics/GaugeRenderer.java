package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.model.TreeStatus;

public class GaugeRenderer extends SpriteRenderer {

   private final GameObject treeObject;
   private final Sprite waterLevelSprite;
   private final Sprite waterLevelTopSprite;

   public GaugeRenderer(GameObject treeObject) {
      super(Assets.Textures.GAUGE_OVERLAY);
      this.treeObject = treeObject;
      waterLevelSprite = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.GAUGE_WATER, Texture.class));
      waterLevelTopSprite = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.GAUGE_WATER_TOP, Texture.class));
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      float level = treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel() * 1.05f;
      if (level > 0.85f) {
         level = 0.85f;
      }
      if (level > 0.05f) {
         waterLevelSprite.setPosition(object.getLeft(), object.getTop());
         waterLevelSprite.setSize(object.getWidth(), object.getHeight() * level);
         waterLevelSprite.draw(batch, 1f);
         int height = (int) (object.getHeight() / 2f * level);
         waterLevelSprite.setRegion(0, height, 16, -height);
         waterLevelTopSprite.setPosition(object.getLeft(), object.getTop() + object.getHeight() * level - 8f);
         waterLevelTopSprite.setSize(32f, 16);
         waterLevelTopSprite.draw(batch, 1f);
      }
      super.render(object, batch, delta);
   }
}
