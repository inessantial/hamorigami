package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.setup.GameplaySetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GameplayLoaderTest {

   private GameplaySetupLoader loader;
   private GameplaySetup setup;

   @Mock
   private HamorigamiContext context;

   @Before
   public void beforeEach() throws IOException {
      Gdx.files = new LwjglFiles();
      this.loader = new GameplaySetupLoader(context);
      this.setup = loader.load("mock-game.play");
   }

   @Test
   public void testLoadGameplayFile() {
      assertThat(setup.getCurrentDaySetup()).isNotNull();
   }

   @Test
   public void testNumberOfDays() {
      assertThat(setup.getNumberOfDays()).isEqualTo(3);
   }

   @Test
   public void testFirstDayHasMorningCutscene() {
      assertThat(setup.getCurrentDaySetup().getStartCutscene()).isNotNull();
      assertThat(setup.getCurrentDaySetup().getEndCutscene()).isNull();
      final Cutscene cutscene = setup.getCurrentDaySetup().getStartCutscene();
      assertThat(cutscene.size()).isEqualTo(33);
      assertThat(setup.getCurrentDaySetup().getSpawns()).hasSize(2);
   }

   @Test
   public void testSecondDayHasEveningCutscene() {
      setup.triggerNextDay();
      assertThat(setup.getCurrentDaySetup().getStartCutscene()).isNull();
      assertThat(setup.getCurrentDaySetup().getEndCutscene()).isNotNull();
      assertThat(setup.getCurrentDaySetup().getSpawns()).hasSize(18);
      final Cutscene cutscene = setup.getCurrentDaySetup().getEndCutscene();
      assertThat(cutscene.size()).isEqualTo(33);
   }

   @Test
   public void testThirdDayHasNoCutscene() {
      setup.triggerNextDay();
      setup.triggerNextDay();
      assertThat(setup.getCurrentDaySetup().getStartCutscene()).isNull();
      assertThat(setup.getCurrentDaySetup().getEndCutscene()).isNull();
   }

   @Test
   public void testThirdDayHasSpawns() {
      setup.triggerNextDay();
      setup.triggerNextDay();
      assertThat(setup.getCurrentDaySetup().getSpawns()).hasSize(16);
   }
}
