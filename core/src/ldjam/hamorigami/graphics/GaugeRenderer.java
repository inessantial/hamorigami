package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;

public class GaugeRenderer extends SpriteRenderer {

   private final GameObject treeObject;

   public GaugeRenderer(GameObject treeObject) {
      super(Assets.Textures.GAUGE_OVERLAY);
      this.treeObject = treeObject;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      super.render(object, batch, delta);
   }
}
