package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.model.TreeStatus;

public class TreeRenderer extends SpriteRenderer {

   public TreeRenderer() {
      super(Assets.Textures.TREE);
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      TreeStatus treeStatus = object.getAttribute(TreeStatus.class);
      if (treeStatus.getTreeWateredLevel() < 0f) {
         Color health = Color.RED.cpy().lerp(Color.WHITE, 1f - treeStatus.getTreeWateredLevel() / -1f);
         object.setColor(health);
      } else {
         Color health = Color.BLUE.cpy().lerp(Color.WHITE, 1f - treeStatus.getTreeWateredLevel() / 1f);
         object.setColor(health);
      }
      super.render(object, batch, delta);
   }
}
