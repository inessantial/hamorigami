package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.model.HealthData;

public class TreeRenderer extends SpriteRenderer {

   public TreeRenderer() {
      super(Assets.Textures.TREE);
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      Color health = Color.RED.cpy().lerp(Color.WHITE, object.getAttribute(HealthData.class).getHealthPercentage());
      object.setColor(health);
      super.render(object, batch, delta);
   }
}
