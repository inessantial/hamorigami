package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.setup.GameplaySetup;

public class DayProgressRenderer extends SpriteRenderer {

   private final SpriteRenderer overlay;
   private final GameplaySetup setup;

   public DayProgressRenderer(GameplaySetup setup, String dayTextureId, String eveningTextureId) {
      super(dayTextureId);
      overlay = new SpriteRenderer(eveningTextureId);
      this.setup = setup;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      float curentProgress = setup.getDayProgress();
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
