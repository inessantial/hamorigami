package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.model.SpiritType;

public class IntroCutscene implements CutsceneSetup {

   private GameObject kodama;

   @Override
   public void cleanup(GameContext2D context) {
      SharedTweenManager.getInstance().killTarget(kodama);
   }

   @Override
   public void setup(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);
      kodama = entityFactory.spawnSpirit(SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 3f, 0f);
      kodama.setDimensions(64f, 64f);
      kodama.getColor().a = 0f;
      float currentX = kodama.getLeft();
      Tween.to(kodama, GameObjectTween.ALPHA, 4f).delay(6f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(kodama, GameObjectTween.POS_X, 4f).delay(9f)
            .target(currentX - 200f)
            .repeatYoyo(Tween.INFINITY, 0f)
            .ease(TweenEquations.easeInOutCubic)
            .start(SharedTweenManager.getInstance());
   }
}
