package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ValidGameplayTest {

   private GameplaySetupLoader loader;

   @Mock
   private HamorigamiContext context;

   @Before
   public void beforeEach() {
      Gdx.files = new LwjglFiles();
      this.loader = new GameplaySetupLoader(context);

   }

   @Test
   public void testValidGameplayFile() throws IOException {
      GameplaySetup setup = loader.load("game.play");
      assertThat(setup.getCurrentDaySetup()).isNotNull();
      assertThat(setup.getNumberOfDays()).isGreaterThan(0);
   }
}
