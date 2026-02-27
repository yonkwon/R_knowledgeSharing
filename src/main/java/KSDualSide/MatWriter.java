package KSDualSide;

import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.MatFile;
import us.hebi.matlab.mat.types.Matrix;
import us.hebi.matlab.mat.types.Sinks;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

class MatWriter {

  MatWriter(Computation c) {
    File outDir;
    File outFile;
    Matrix performanceAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix performanceSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestSourceDiversityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeBestSourceDiversitySSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeMinMaxAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix knowledgeMinMaxSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefDiversityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefDiversitySSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefSourceDiversityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix beliefSourceDiversitySSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix concentrationAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix concentrationSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix connectednessAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix connectednessSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE);
    Matrix optimalBetaAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_OPTIMAL);
    Matrix optimalBetaSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE_OPTIMAL);
    Matrix rankContributionAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionPositiveAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionPositiveSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionNegativeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionNegativeSSQ = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);

    outDir = new File("mat");
    if (!outDir.exists()) {
      outDir.mkdirs();
    }
    outFile = new File(outDir, Main.RUN_ID + Main.PARAMS + ".mat");

    for (int isRatioIdx = 0; isRatioIdx < 2; isRatioIdx++) {
      for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          for (int t = 0; t < Main.TIME; t++) {
            for (int b = 0; b < Main.LENGTH_BETA; b++) {
              int[] indices = {isRatioIdx, nt, b, ps, t};
              performanceAVG.setDouble(indices, c.knowledgeAVG[isRatioIdx][nt][ps][b][t]);
              performanceSSQ.setDouble(indices, c.knowledgeSSQ[isRatioIdx][nt][ps][b][t]);
              knowledgeBestAVG.setDouble(indices, c.knowledgeBestAVG[isRatioIdx][nt][ps][b][t]);
              knowledgeBestSSQ.setDouble(indices, c.knowledgeBestSSQ[isRatioIdx][nt][ps][b][t]);
              knowledgeBestSourceDiversityAVG.setDouble(indices, c.knowledgeBestSourceDiversityAVG[isRatioIdx][nt][ps][b][t]);
              knowledgeBestSourceDiversitySSQ.setDouble(indices, c.knowledgeBestSourceDiversitySSQ[isRatioIdx][nt][ps][b][t]);
              knowledgeMinMaxAVG.setDouble(indices, c.knowledgeMinMaxAVG[isRatioIdx][nt][ps][b][t]);
              knowledgeMinMaxSSQ.setDouble(indices, c.knowledgeMinMaxSSQ[isRatioIdx][nt][ps][b][t]);
              beliefDiversityAVG.setDouble(indices, c.beliefDiversityAVG[isRatioIdx][nt][ps][b][t]);
              beliefDiversitySSQ.setDouble(indices, c.beliefDiversitySSQ[isRatioIdx][nt][ps][b][t]);
              beliefSourceDiversityAVG.setDouble(indices, c.beliefSourceDiversityAVG[isRatioIdx][nt][ps][b][t]);
              beliefSourceDiversitySSQ.setDouble(indices, c.beliefSourceDiversitySSQ[isRatioIdx][nt][ps][b][t]);
              connectednessAVG.setDouble(indices, c.connectednessAVG[isRatioIdx][nt][ps][b][t]);
              connectednessSSQ.setDouble(indices, c.connectednessSSQ[isRatioIdx][nt][ps][b][t]);
              concentrationAVG.setDouble(indices, c.concentrationAVG[isRatioIdx][nt][ps][b][t]);
              concentrationSSQ.setDouble(indices, c.concentrationSSQ[isRatioIdx][nt][ps][b][t]);
              for (int n = 0; n < Main.N; n++) {
                int[] indicesRank = {isRatioIdx, nt, b, ps, t, n};
                rankContributionAVG.setDouble(indicesRank, c.rankContributionAVG[isRatioIdx][nt][ps][b][t][n]);
                rankContributionSSQ.setDouble(indicesRank, c.rankContributionSSQ[isRatioIdx][nt][ps][b][t][n]);
                rankContributionPositiveAVG.setDouble(indicesRank, c.rankContributionPositiveAVG[isRatioIdx][nt][ps][b][t][n]);
                rankContributionPositiveSSQ.setDouble(indicesRank, c.rankContributionPositiveSSQ[isRatioIdx][nt][ps][b][t][n]);
                rankContributionNegativeAVG.setDouble(indicesRank, c.rankContributionNegativeAVG[isRatioIdx][nt][ps][b][t][n]);
                rankContributionNegativeSSQ.setDouble(indicesRank, c.rankContributionNegativeSSQ[isRatioIdx][nt][ps][b][t][n]);
              }
            }
            int[] indices = {isRatioIdx, nt, ps, t};
            optimalBetaAVG.setDouble(indices, c.optimalBetaAVG[isRatioIdx][nt][ps][t]);
            optimalBetaSSQ.setDouble(indices, c.optimalBetaSSQ[isRatioIdx][nt][ps][t]);
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
        .addArray("para_iteration", Mat5.newScalar(Main.ITERATION))
        .addArray("para_time", Mat5.newScalar(Main.TIME))

        .addArray("para_n", Mat5.newScalar(Main.N))
        .addArray("para_n_in", Mat5.newScalar(Main.N_IN_GROUP))
        .addArray("para_n_of", Mat5.newScalar(Main.N_OF_GROUP))
        .addArray("para_m", Mat5.newScalar(Main.M))
        .addArray("para_s", Mat5.newScalar(Main.S))
        .addArray("para_p_l", Mat5.newScalar(Main.P_LEARNING))
        .addArray("para_t_max", Mat5.newScalar(Main.T_MAX))

        .addArray("para_l_b", Mat5.newScalar(Main.LENGTH_BETA))
        .addArray("para_a_b", matrixArrayBeta)
        .addArray("para_l_p_s", Mat5.newScalar(Main.LENGTH_P_SHARING))
        .addArray("para_a_p_s", matrixArrayPSharing)
        .addArray("para_l_net_type", Mat5.newScalar(Main.LENGTH_NETWORK_TYPE))
        .addArray("para_a_net_type", matrixArrayNetworkType)
        .addArray("r_perf_avg", performanceAVG)
        .addArray("r_perf_ssq", performanceSSQ)
        .addArray("r_kbst_avg", knowledgeBestAVG)
        .addArray("r_kbst_ssq", knowledgeBestSSQ)
        .addArray("r_kbsd_avg", knowledgeBestSourceDiversityAVG)
        .addArray("r_kbsd_ssq", knowledgeBestSourceDiversitySSQ)
        .addArray("r_kmmx_avg", knowledgeMinMaxAVG)
        .addArray("r_kmmx_ssq", knowledgeMinMaxSSQ)
        .addArray("r_bfdv_avg", beliefDiversityAVG)
        .addArray("r_bfdv_ssq", beliefDiversitySSQ)
        .addArray("r_bsdv_avg", beliefSourceDiversityAVG)
        .addArray("r_bsdv_ssq", beliefSourceDiversitySSQ)
        .addArray("r_conc_avg", concentrationAVG)
        .addArray("r_conc_ssq", concentrationSSQ)
        .addArray("r_conn_avg", connectednessAVG)
        .addArray("r_conn_ssq", connectednessSSQ)
        .addArray("r_opti_avg", optimalBetaAVG)
        .addArray("r_opti_ssq", optimalBetaSSQ)
        .addArray("r_r_cont_avg", rankContributionAVG)
        .addArray("r_r_cont_ssq", rankContributionSSQ)
        .addArray("r_r_conp_avg", rankContributionPositiveAVG)
        .addArray("r_r_conp_ssq", rankContributionPositiveSSQ)
        .addArray("r_r_conn_avg", rankContributionNegativeAVG)
        .addArray("r_r_conn_ssq", rankContributionNegativeSSQ)
        .addArray("perf_seconds", Mat5.newScalar((System.currentTimeMillis() - Main.TIC) / 1000))
        .writeTo(Sinks.newStreamingFile(outFile));

      System.out.println("File Printed: " + outFile.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
