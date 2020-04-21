package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.effects.DayProgress;

public class DayProgressRenderer extends SpriteRenderer {

   private final DayProgress dayProgress;
   private final SpriteRenderer overlay;

   public DayProgressRenderer(DayProgress dayProgress, String dayTextureId, String eveningTextureId) {
      super(dayTextureId);
      this.dayProgress = dayProgress;
      overlay = new SpriteRenderer(eveningTextureId);
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      if (dayProgress.getCurrentProgress() < 1f) {
         super.render(object, batch, delta);
      }
      if (dayProgress.getCurrentProgress() > 0f) {
         float currentAlpha = object.getColor().a;
         object.getColor().a = dayProgress.getCurrentProgress();
         overlay.render(object, batch, delta);
         object.getColor().a = currentAlpha;
      }
   }
}
