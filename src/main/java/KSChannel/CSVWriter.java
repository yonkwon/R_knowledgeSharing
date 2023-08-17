package KSChannel;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

  String fileID = "KSChannel";

  boolean isCavemen = false;
  boolean isCostly = true;
  boolean isGreedy = false;
  double beta = 0;
  double pSharing = .5;
  double pSeeking = .5;
  int time = 3000;

  Scenario sc;

  CSVWriter() {
    initializeScenario();
    runScenario();
    printCSV(fileID +
        "[" +
        "cave" + (isCavemen ? 1 : 0) +
        "cost" + (isCostly ? 1 : 0) +
        "greed" + (isGreedy ? 1 : 0) +
        "]" +
        "b" + beta +
        "p1_" + pSharing +
        "p2_" + pSeeking +
        "t" + time);
  }

  void initializeScenario() {
    sc = new Scenario(beta, pSharing, pSeeking, isCavemen, isCostly, isGreedy);
    sc.initialize();
  }

  void runScenario() {
    for (int t = 0; t < time; t++) {
      sc.stepForward();
    }
  }

  void printCSV(String fileName) {
    try {
      FileWriter csvWriter;
      csvWriter = new FileWriter(fileName + ".csv");
      csvWriter.append("SOURCE");
      csvWriter.append(",");
      csvWriter.append("TARGET");
      csvWriter.append(",");
      csvWriter.append("SOURCE_P_SHARING");
      csvWriter.append(",");
      csvWriter.append("TARGET_P_SHARING");
      csvWriter.append(",");
      csvWriter.append("SOURCE_P_SEEKING");
      csvWriter.append(",");
      csvWriter.append("TARGET_P_SEEKING");
      csvWriter.append(",");
      csvWriter.append("SOURCE_UNIT");
      csvWriter.append(",");
      csvWriter.append("SOURCE_INIT_KNOWLEDGE");
      csvWriter.append(",");
      csvWriter.append("SOURCE_INIT_RANK");
      csvWriter.append(",");
      csvWriter.append("SOURCE_CONTRIBUTION");
      csvWriter.append(",");
      csvWriter.append("SOURCE_CONTRIBUTION_UPOS");
      csvWriter.append(",");
      csvWriter.append("SOURCE_CONTRIBUTION_UNEG");
      csvWriter.append(",");
      csvWriter.append("IS_CONNECTED");
      csvWriter.append(",");
      csvWriter.append("WEIGHT");
      csvWriter.append("\n");

      //Edge
      for (int focal = 0; focal < Main.N; focal++) {
        for (int target = 0; target < Main.N; target++) {
          if (focal == target) {
            continue;
          }
//          csvWriter.append("SOURCE");
          csvWriter.append(Integer.toString(focal));
          csvWriter.append(",");
//          csvWriter.append("TARGET");
          csvWriter.append(Integer.toString(target));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_P_SHARING");
          csvWriter.append(Double.toString(sc.pSharingOf[focal]));
          csvWriter.append(",");
//          csvWriter.append("TARGET_P_SHARING");
          csvWriter.append(Double.toString(sc.pSharingOf[target]));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_P_SEEKING");
          csvWriter.append(Double.toString(sc.pSeekingOf[focal]));
          csvWriter.append(",");
//          csvWriter.append("TARGET_P_SEEKING");
          csvWriter.append(Double.toString(sc.pSeekingOf[target]));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_UNIT");
          csvWriter.append(Integer.toString(sc.isInGroup[focal]));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_INIT_KNOWLEDGE");
          csvWriter.append(Double.toString(sc.knowledge0[focal] / (double) Main.M));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_INIT_RANK");
          csvWriter.append(Integer.toString(sc.rank0[focal]));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_CONTRIBUTION");
          csvWriter.append(Double.toString(sc.contribution[focal]));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_CONTRIBUTION_UPOS");
          csvWriter.append(Double.toString(sc.applicationRatePositive[focal]));
          csvWriter.append(",");
//          csvWriter.append("SOURCE_CONTRIBUTION_UNEG");
          csvWriter.append(Double.toString(sc.applicationRateNegative[focal]));
          csvWriter.append(",");
//          csvWriter.append("IS_CONNECTED");
          csvWriter.append(Integer.toString(sc.networkWhole[focal][target] ? 1 : 0));
          csvWriter.append(",");
//          csvWriter.append("WEIGHT");
          csvWriter.append(Double.toString(sc.beliefSourceCount[target][focal] / (double) Main.M)); // Changed 221002 [focal][target] to [target][focal]
          csvWriter.append("\n");
        }
      }
      csvWriter.flush();
      csvWriter.close();
      System.out.println(fileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
