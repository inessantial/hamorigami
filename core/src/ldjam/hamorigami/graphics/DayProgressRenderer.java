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
      float curentProgress = dayProgress.getCurrentProgress();
      if (curentProgress > 0f && curentProgress < 1f) {
         super.render(object, batch, delta);
      }
      if (curentProgress != 0.5f) {
         float currentAlpha = object.getColor().a;
         object.getColor().a = 1f - (float) Math.sin(Math.PI * curentProgress);
         overlay.render(object, batch, delta);
         object.getColor().a = currentAlpha;
      }
   }
}
