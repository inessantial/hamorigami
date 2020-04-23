package ldjam.hamorigami.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.cutscene.emotes.Emote;

public class SpeechBubble extends Actor {

   private static final float PADDING = 6f;
   private static final float OFFSET_Y = 5f;
   private static final float EMOTE_SIZE = 16f;

   private final GameContext2D context;
   private final String targetId;
   private final NinePatch background;
   private final Sprite bottom;
   private final Label label;

   private Emote emote;

   private final DeltaTimer deltaTimer = new DeltaTimer();

   public SpeechBubble(GameContext2D context, String targetId) {
      this.label = new Label("", Styles.SPEECH);
      this.context = context;
      this.targetId = targetId;
      this.background = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.BUBBLE, Texture.class), 8);
      this.bottom = new Sprite(SharedAssetManager.getInstance().get(Assets.Textures.BUBBLE_BOTTOM, Texture.class));
   }

   public void setEmote(Emote emote) {
      this.emote = emote;
      deltaTimer.reset();
      setSize(EMOTE_SIZE + PADDING * 2f, EMOTE_SIZE + PADDING * 2f);
   }

   public void setText(String text) {
      this.label.setText(text);
      label.setAlignment(Align.left);
      setSize(label.getPrefWidth() + PADDING * 4f, label.getPrefHeight() + PADDING * 2f);
      this.emote = null;
   }

   @Override
   public void act(float delta) {
      deltaTimer.update(delta);
      GameObject target = context.getGameWorld().getObjectById(targetId);
      if (emote != null) {
         setPosition(target.getLeft() + target.getWidth() / 2f - (getWidth() / 2f), target.getTop() + target.getHeight() + OFFSET_Y);
      } else {
         setPosition(target.getLeft() + target.getWidth() / 2f - getWidth() / 2f, target.getTop() + target.getHeight() + OFFSET_Y);
      }

      if (context.getGameCamera().getLeft() + PADDING > getX()) {
         setX(context.getGameCamera().getLeft() + PADDING);
      }
      if (context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() - PADDING < getX() + getWidth()) {
         setX(context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() - PADDING - getWidth());
      }

      if (emote == null) {
         label.setPosition(getX() + PADDING * 2f, getY());
         label.setSize(getWidth(), getHeight());
      }

      bottom.setPosition(target.getLeft() + target.getWidth() / 2f - 5f, getY() - 5f);
      bottom.setSize(10f, 8f);
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      if (getColor().a == 0f) {
         return;
      }
      batch.setColor(getColor());
      background.draw(batch, getX(), getY(), getWidth(), getHeight());
      bottom.setColor(getColor());
      bottom.draw(batch, parentAlpha);
      if (emote != null) {
         Animation<TextureRegion> animation = emote.getAnimation();
         TextureRegion region = animation.getKeyFrame(deltaTimer.getTicks());
         batch.draw(region, getX() + PADDING, getY() + PADDING);
      } else {
         label.draw(batch, parentAlpha * getColor().a);
      }
   }
}
