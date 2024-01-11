package KSVarietyPack;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.MatFile;
import us.hebi.matlab.mat.types.Matrix;
import us.hebi.matlab.mat.types.Sinks;

class MatWriter {

  MatWriter(Computation c) {
    Matrix knowledgeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestSourceDiversityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestSourceDiversitySTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeMinMaxAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeMinMaxSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefDiversityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefDiversitySTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefSourceDiversityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefSourceDiversitySTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix centralizationAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix centralizationSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix efficiencyAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix efficiencySTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE);

    for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
      for (int b = 0; b < Main.LENGTH_BETA; b++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          for (int t = 0; t < Main.TIME; t++) {
            int[] indices = {nt, b, ps, t};
            knowledgeAVG.setDouble(indices, c.knowledgeAVG[nt][b][ps][t]);
            knowledgeSTD.setDouble(indices, c.knowledgeSTD[nt][b][ps][t]);
            knowledgeBestAVG.setDouble(indices, c.knowledgeBestAVG[nt][b][ps][t]);
            knowledgeBestSTD.setDouble(indices, c.knowledgeBestSTD[nt][b][ps][t]);
            knowledgeBestSourceDiversityAVG.setDouble(indices, c.knowledgeBestSourceDiversityAVG[nt][b][ps][t]);
            knowledgeBestSourceDiversitySTD.setDouble(indices, c.knowledgeBestSourceDiversitySTD[nt][b][ps][t]);
            knowledgeMinMaxAVG.setDouble(indices, c.knowledgeMinMaxAVG[nt][b][ps][t]);
            knowledgeMinMaxSTD.setDouble(indices, c.knowledgeMinMaxSTD[nt][b][ps][t]);
            beliefDiversityAVG.setDouble(indices, c.beliefDiversityAVG[nt][b][ps][t]);
            beliefDiversitySTD.setDouble(indices, c.beliefDiversitySTD[nt][b][ps][t]);
            beliefSourceDiversityAVG.setDouble(indices, c.beliefSourceDiversityAVG[nt][b][ps][t]);
            beliefSourceDiversitySTD.setDouble(indices, c.beliefSourceDiversitySTD[nt][b][ps][t]);
            centralizationAVG.setDouble(indices, c.centralizationAVG[nt][b][ps][t]);
            centralizationSTD.setDouble(indices, c.centralizationSTD[nt][b][ps][t]);
            efficiencyAVG.setDouble(indices, c.efficiencyAVG[nt][b][ps][t]);
            efficiencySTD.setDouble(indices, c.efficiencySTD[nt][b][ps][t]);
          }
        }
      }
    }

    Matrix matrixArrayBeta = Mat5.newMatrix(new int[]{1, Main.LENGTH_BETA});
    IntStream.range(0, Main.LENGTH_BETA).forEach(i -> matrixArrayBeta.setDouble(new int[]{0, i}, Main.BETA[i]));
    Matrix matrixArrayPSharing = Mat5.newMatrix(new int[]{1, Main.LENGTH_P_SHARING});
    IntStream.range(0, Main.LENGTH_P_SHARING).forEach(i -> matrixArrayPSharing.setDouble(new int[]{0, i}, Main.P_SHARING[i]));
    Mat5File mat5File = Mat5.newMatFile();
    Matrix matrixArrayNetworkType = Mat5.newMatrix(new int[]{1, Main.LENGTH_NETWORK_TYPE});
    IntStream.range(0, Main.LENGTH_NETWORK_TYPE).forEach(i -> matrixArrayNetworkType.setDouble(new int[]{0, i}, i));

    try {
      MatFile matFile = mat5File
          .addArray("para_is_ratio", Mat5.newScalar(Main.IS_RATIO ? 1 : 0))
          .addArray("para_is_one_on_one", Mat5.newScalar(Main.IS_ONE_ON_ONE ? 1 : 0))

          .addArray("para_iteration", Mat5.newScalar(Main.ITERATION))
          .addArray("para_time", Mat5.newScalar(Main.TIME))

          .addArray("para_n", Mat5.newScalar(Main.N))
          .addArray("para_n_in", Mat5.newScalar(Main.N_IN_GROUP))
          .addArray("para_n_of", Mat5.newScalar(Main.N_OF_GROUP))
          .addArray("para_m", Mat5.newScalar(Main.M))
          .addArray("para_s", Mat5.newScalar(Main.S))
          .addArray("para_p_a", Mat5.newScalar(Main.P_ACCEPT))
          .addArray("para_p_l", Mat5.newScalar(Main.P_LEARNING))

          .addArray("para_l_b", Mat5.newScalar(Main.LENGTH_BETA))
          .addArray("para_a_b", matrixArrayBeta)
          .addArray("para_l_p_s", Mat5.newScalar(Main.LENGTH_P_SHARING))
          .addArray("para_a_p_s", matrixArrayPSharing)
          .addArray("para_l_net_type", Mat5.newScalar(Main.LENGTH_NETWORK_TYPE))
          .addArray("para_a_net_type", matrixArrayNetworkType)
          .addArray("para_alpha", Mat5.newScalar(Main.ALPHA))
          .addArray("para_gamma", Mat5.newScalar(Main.GAMMA))
          .addArray("r_know_avg", knowledgeAVG)
          .addArray("r_know_std", knowledgeSTD)
          .addArray("r_kbst_avg", knowledgeBestAVG)
          .addArray("r_kbst_std", knowledgeBestSTD)
          .addArray("r_kbsd_avg", knowledgeBestSourceDiversityAVG)
          .addArray("r_kbsd_std", knowledgeBestSourceDiversitySTD)
          .addArray("r_kmmx_avg", knowledgeMinMaxAVG)
          .addArray("r_kmmx_std", knowledgeMinMaxSTD)
          .addArray("r_bfdv_avg", beliefDiversityAVG)
          .addArray("r_bfdv_std", beliefDiversitySTD)
          .addArray("r_bsdv_avg", beliefSourceDiversityAVG)
          .addArray("r_bsdv_std", beliefSourceDiversitySTD)
          .addArray("r_cent_avg", centralizationAVG)
          .addArray("r_cent_std", centralizationSTD)
          .addArray("r_effi_avg", efficiencyAVG)
          .addArray("r_effi_std", efficiencySTD)
          .addArray("perf_seconds", Mat5.newScalar((System.currentTimeMillis() - Main.TIC) / 1000))
          .writeTo(Sinks.newStreamingFile(new File(Main.RUN_ID + Main.PARAMS + ".mat")));
      System.out.println("File Printed");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
