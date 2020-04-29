package ldjam.hamorigami.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.Assets.Textures.CITY_DAY;
import static ldjam.hamorigami.Assets.Textures.CITY_EVENING;

public class Cityscape extends RenderLayer2D {

   private final GameplaySetup setup;

   public Cityscape(GameplaySetup setup) {
      this.setup = setup;
   }

   @Override
   public void render(Batch batch, float delta) {
      batch.begin();
      float x = Gdx.graphics.getWidth() / 2f - 400;
      float y = Gdx.graphics.getHeight() / 2f - 300;

      Texture background = SharedAssetManager.getInstance().get(CITY_DAY, Texture.class);
      batch.draw(background, x, y);
      Texture background_noon = SharedAssetManager.getInstance().get(CITY_EVENING, Texture.class);
      Color color = batch.getColor();
      float alpha = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
      batch.setColor(1f, 1f, 1f, alpha);
      batch.draw(background_noon, x, y);
      batch.setColor(color);
      batch.end();
   }
}
