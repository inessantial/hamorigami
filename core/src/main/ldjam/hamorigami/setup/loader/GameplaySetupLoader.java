package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.setup.DaySetup;
import ldjam.hamorigami.setup.DaySetupBuilder;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.setup.GameplaySetupBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class GameplaySetupLoader {

   private enum ScanMode {
      MORNING_CUTSCENE,
      EVENING_CUTSCENE,
      GAMEPLAY;
   }

   private final HamorigamiContext context;

   public GameplaySetupLoader(HamorigamiContext context) {
      this.context = context;
   }

   public GameplaySetup load(String gameplayFile) throws IOException {
      FileHandle handle = Gdx.files.internal(gameplayFile);
      BufferedReader br = new BufferedReader(handle.reader("UTF-8"));
      String line;
      GameplaySetupBuilder builder = new GameplaySetupBuilder();
      DaySetupBuilder daySetupBuilder = new DaySetupBuilder();
      CutsceneBuilder cutsceneBuilder = new CutsceneBuilder(context);
      int linesRead = 0;
      ScanMode previousScanmode = ScanMode.EVENING_CUTSCENE;
      ScanMode scanMode = ScanMode.GAMEPLAY;
      boolean morningCutscene = false;
      while((line = br.readLine()) != null) {
         line = line.trim();
         // skip comments and empty lines
         if (line.isEmpty() || line.startsWith("#")) {
            continue;
         }
         linesRead++;
         // a new day has started
         if (line.startsWith("day")) {
            if (linesRead > 1) {
               builder.addDay(daySetupBuilder.build());
               daySetupBuilder = new DaySetupBuilder();
               previousScanmode = ScanMode.EVENING_CUTSCENE;
               scanMode = ScanMode.GAMEPLAY;
               morningCutscene = false;
            }
            continue;
         }
         // begin cutscene
         if (line.startsWith("begin cutscene")) {
            ScanMode oldScanMode = scanMode;
            if (scanMode == ScanMode.GAMEPLAY && previousScanmode == ScanMode.EVENING_CUTSCENE && !morningCutscene) {
               morningCutscene = true;
               scanMode = ScanMode.MORNING_CUTSCENE;
            } else {
               scanMode = ScanMode.EVENING_CUTSCENE;
            }
            previousScanmode = oldScanMode;
            continue;
         }
         // end cutscene
         if (line.startsWith("end cutscene")) {
            if (scanMode == ScanMode.EVENING_CUTSCENE) {
               daySetupBuilder.endOfDayCutscene(cutsceneBuilder.build());
            } else {
               daySetupBuilder.startOfDayCutscene(cutsceneBuilder.build());
               previousScanmode = scanMode;
               scanMode = ScanMode.GAMEPLAY;
            }
            cutsceneBuilder = new CutsceneBuilder(context);
            continue;
         }
         if (scanMode == ScanMode.GAMEPLAY && line.startsWith("spawn")) {
            morningCutscene = true;
         }
         if (scanMode == ScanMode.GAMEPLAY) {
            processGameplay(line, daySetupBuilder);
         }
         if (scanMode == ScanMode.EVENING_CUTSCENE || scanMode == ScanMode.MORNING_CUTSCENE) {
            processCutscene(line, cutsceneBuilder);
         }
      }
      builder.addDay(daySetupBuilder.build());
      return builder.build();
   }

   private void processGameplay(String line, DaySetupBuilder daySetupBuilder) {
      final int spawnAmount = getSpawnAmount(line);
      final SpiritType spiritType = getSpiritType(line);
      float timeValue = getTimeValue(line);
      SpiritType[] types = new SpiritType[spawnAmount];
      Arrays.fill(types, spiritType);
      if (line.contains(" every ")) {
         // spawn every x
         if (timeValue == 0f) {
            throw new GdxRuntimeException("Time interval cannot be 0: " + line);
         }
         if (line.contains(" second")) {
            for (float currentSeconds = timeValue; currentSeconds <= GameplaySetup.SECONDS_PER_DAY; currentSeconds += timeValue) {
               daySetupBuilder.addSpawns(currentSeconds, types);
            }
         } else if (line.contains("%")) {
            float convertedSeconds = GameplaySetup.SECONDS_PER_DAY / 100f * timeValue;
            for (float currentSeconds = convertedSeconds; currentSeconds <= GameplaySetup.SECONDS_PER_DAY; currentSeconds += convertedSeconds) {
               daySetupBuilder.addSpawns(currentSeconds, types);
            }
         } else {
            throw new GdxRuntimeException("Invalid spawn interval: " + line);
         }
      } else if (line.contains(" at ")) {
         if (line.contains(" second")) {
            daySetupBuilder.addSpawns(timeValue, types);
         } else if (line.contains("%")) {
            float convertedSeconds = GameplaySetup.SECONDS_PER_DAY / 100f * timeValue;
            daySetupBuilder.addSpawns(convertedSeconds, types);
         } else {
            throw new GdxRuntimeException("Invalid spawn interval: " + line);
         }
      } else {
         throw new GdxRuntimeException("Invalid line: " + line);
      }
   }

   private SpiritType getSpiritType(String line) {
      int endIndex = line.indexOf(" at");
      if (endIndex == -1) {
         endIndex = line.indexOf(" every");
      }
      if (endIndex == -1) {
         throw new GdxRuntimeException("Invalid syntax: " + line);
      }
      int startIndex = line.indexOf("x ");
      if (startIndex == -1) {
         startIndex = 6;
      } else {
         startIndex += 2;
      }
      String spiritName = line.substring(startIndex, endIndex);
      SpiritType resolvedType = SpiritType.resolveByName(spiritName);
      if (resolvedType == null) {
         throw new GdxRuntimeException("Unsupported spirit type: " + spiritName + " at @ " + line);
      }
      return resolvedType;
   }

   private float getTimeValue(String line) {
      int startIndex = line.indexOf("every ");
      if (startIndex != -1) {
         startIndex += 6;
      } else {
         startIndex = line.indexOf("at ");
         if (startIndex != -1) {
            startIndex += 3;
         }
      }
      int endIndex = line.indexOf(" second");
      if (endIndex == -1) {
         endIndex = line.indexOf("%");
      }
      if (startIndex == -1 || endIndex == -1) {
         throw new GdxRuntimeException("Invalid syntax: " + line);
      }
      String timeValue = line.substring(startIndex, endIndex);
      return Float.parseFloat(timeValue.trim());
   }

   private int getSpawnAmount(String line) {
      int startIndex = line.indexOf("x ");
      if (startIndex == -1) {
         return 1;
      }
      String number = line.replace("spawn", "").replace(line.substring(startIndex), "");
      return Integer.parseInt(number.trim());
   }

   private void processCutscene(String line, CutsceneBuilder cutsceneBuilder) {

   }
}
