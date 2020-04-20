package ldjam.hamorigami.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.graphics.EntityOrderComparator;
import ldjam.hamorigami.graphics.GaugeRenderer;
import ldjam.hamorigami.graphics.TreeRenderer;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.ui.Styles;

import static ldjam.hamorigami.Assets.Textures.CITYSCAPE;

public class TitleScreen extends BrainGdxScreen2D<HamorigamiGame> {

   private boolean exiting;

   private GameContext2D context;
   private GameObject treeObject;

   public TitleScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      context.getRenderPipeline().putAfter(RenderPipeIds.BACKGROUND, "cityscape", new RenderLayer2D() {


         @Override
         public void render(Batch batch, float delta) {
            Texture background = SharedAssetManager.getInstance().get(CITYSCAPE, Texture.class);
            batch.begin();
            batch.draw(background, context.getGameCamera().getLeft(), context.getGameCamera().getTop() + 50f);
            batch.end();
         }
      });
      this.context = context;
      context.getScreenTransitions().in(1.5f);

      Table layout = new Table();
      layout.setFillParent(true);

      Label pressAnyButton = new Label(Bundle.get(Messages.PLAY_GAME), Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(120f).row();

      context.getWorldStage().addActor(layout);

      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);

      pressAnyButton.getColor().a = 0f;
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f)
            .target(1f)
            .ease(TweenEquations.easeInCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(context.getTweenManager());

      // add tree
      EntityFactory entityFactory = new EntityFactory(context);
      this.treeObject = entityFactory.spawnTree();

      // add floor
      GameObject floorObject = entityFactory.spawnFloor();

      // add gauge
      GameObject gaugeObject = entityFactory.spawnGauge(360, 65);

      context.getRenderManager().setRenderOrderComparator(new EntityOrderComparator());
      context.getRenderManager().register(ObjectType.TREE, new TreeRenderer());
      context.getRenderManager().register(ObjectType.FLOOR, new SpriteRenderer(Assets.Textures.BACKGROUND_FLOOR));
      context.getRenderManager().register(ObjectType.GAUGE, new GaugeRenderer(treeObject));
   }

   @Override
   protected void onUpdate(float delta) {
      if (!exiting && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         exiting = true;
         context.getInputManager().clear();
         final IngameScreen initialScreen = new IngameScreen(getGame());
         //
         context.getScreenTransitions().out(new StoryScreen(getGame(), initialScreen,
               Messages.STORY_INTRO_1,
               Messages.STORY_INTRO_2,
               Messages.STORY_INTRO_3,
               Messages.STORY_INTRO_4
         ), 1f);
      }
   }
}
