package KSCost;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.Matrix;
import us.hebi.matlab.mat.types.Sinks;

class MatWriter {

  MatWriter(Computation com ){
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
    
    Matrix rankKnowledgeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankKnowledgeSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionPositiveAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionPositiveSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionNegativeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionNegativeSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionBestAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionBestSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionBestPositiveAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionBestPositiveSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionBestNegativeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankContributionBestNegativeSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankApplicationRateAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankApplicationRateSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankApplicationRatePositiveAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankApplicationRatePositiveSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankApplicationRateNegativeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankApplicationRateNegativeSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankCentralityAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);
    Matrix rankCentralitySTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_RANK);

    for (int b = 0; b < Main.LENGTH_BETA; b++) {
      for (int c = 0; c < Main.LENGTH_COST; c++) {
      for (int p1 = 0; p1 < Main.LENGTH_P_SHARING; p1++) {
        for (int p2 = 0; p2 < Main.LENGTH_P_SEEKING; p2++) {
            for (int t = 0; t < Main.TIME; t++) {
              int[] indices = {b, c, p1, p2, t};
              knowledgeAVG.setDouble(indices, com.knowledgeAVG[b][c][p1][p2][t]);
              knowledgeSTD.setDouble(indices, com.knowledgeSTD[b][c][p1][p2][t]);
              knowledgeBestAVG.setDouble(indices, com.knowledgeBestAVG[b][c][p1][p2][t]);
              knowledgeBestSTD.setDouble(indices, com.knowledgeBestSTD[b][c][p1][p2][t]);
              knowledgeBestSourceDiversityAVG.setDouble(indices, com.knowledgeBestSourceDiversityAVG[b][c][p1][p2][t]);
              knowledgeBestSourceDiversitySTD.setDouble(indices, com.knowledgeBestSourceDiversitySTD[b][c][p1][p2][t]);
              knowledgeMinMaxAVG.setDouble(indices, com.knowledgeMinMaxAVG[b][c][p1][p2][t]);
              knowledgeMinMaxSTD.setDouble(indices, com.knowledgeMinMaxSTD[b][c][p1][p2][t]);
              beliefDiversityAVG.setDouble(indices, com.beliefDiversityAVG[b][c][p1][p2][t]);
              beliefDiversitySTD.setDouble(indices, com.beliefDiversitySTD[b][c][p1][p2][t]);
              beliefSourceDiversityAVG.setDouble(indices, com.beliefSourceDiversityAVG[b][c][p1][p2][t]);
              beliefSourceDiversitySTD.setDouble(indices, com.beliefSourceDiversitySTD[b][c][p1][p2][t]);
              centralizationAVG.setDouble(indices, com.centralizationAVG[b][c][p1][p2][t]);
              centralizationSTD.setDouble(indices, com.centralizationSTD[b][c][p1][p2][t]);

              for (int n = 0; n < Main.N; n++) {
                int[] indicesRank = {b, c, p1, p2, t, n};
                rankKnowledgeAVG.setDouble(indicesRank, com.rankKnowledgeAVG[b][c][p1][p2][t][n]);
                rankKnowledgeSTD.setDouble(indicesRank, com.rankKnowledgeSTD[b][c][p1][p2][t][n]);
                rankContributionAVG.setDouble(indicesRank, com.rankContributionAVG[b][c][p1][p2][t][n]);
                rankContributionSTD.setDouble(indicesRank, com.rankContributionSTD[b][c][p1][p2][t][n]);
                rankContributionPositiveAVG.setDouble(indicesRank, com.rankContributionPositiveAVG[b][c][p1][p2][t][n]);
                rankContributionPositiveSTD.setDouble(indicesRank, com.rankContributionPositiveSTD[b][c][p1][p2][t][n]);
                rankContributionNegativeAVG.setDouble(indicesRank, com.rankContributionNegativeAVG[b][c][p1][p2][t][n]);
                rankContributionNegativeSTD.setDouble(indicesRank, com.rankContributionNegativeSTD[b][c][p1][p2][t][n]);
                rankContributionBestAVG.setDouble(indicesRank, com.rankContributionBestAVG[b][c][p1][p2][t][n]);
                rankContributionBestSTD.setDouble(indicesRank, com.rankContributionBestSTD[b][c][p1][p2][t][n]);
                rankContributionBestPositiveAVG.setDouble(indicesRank, com.rankContributionBestPositiveAVG[b][c][p1][p2][t][n]);
                rankContributionBestPositiveSTD.setDouble(indicesRank, com.rankContributionBestPositiveSTD[b][c][p1][p2][t][n]);
                rankContributionBestNegativeAVG.setDouble(indicesRank, com.rankContributionBestNegativeAVG[b][c][p1][p2][t][n]);
                rankContributionBestNegativeSTD.setDouble(indicesRank, com.rankContributionBestNegativeSTD[b][c][p1][p2][t][n]);
                rankApplicationRateAVG.setDouble(indicesRank, com.rankApplicationRateAVG[b][c][p1][p2][t][n]);
                rankApplicationRateSTD.setDouble(indicesRank, com.rankApplicationRateSTD[b][c][p1][p2][t][n]);
                rankApplicationRatePositiveAVG.setDouble(indicesRank, com.rankApplicationRatePositiveAVG[b][c][p1][p2][t][n]);
                rankApplicationRatePositiveSTD.setDouble(indicesRank, com.rankApplicationRatePositiveSTD[b][c][p1][p2][t][n]);
                rankApplicationRateNegativeAVG.setDouble(indicesRank, com.rankApplicationRateNegativeAVG[b][c][p1][p2][t][n]);
                rankApplicationRateNegativeSTD.setDouble(indicesRank, com.rankApplicationRateNegativeSTD[b][c][p1][p2][t][n]);
                rankCentralityAVG.setDouble(indicesRank, com.rankCentralityAVG[b][c][p1][p2][t][n]);
                rankCentralitySTD.setDouble(indicesRank, com.rankCentralitySTD[b][c][p1][p2][t][n]);
              }
            }
          }
        }
      }
    }

    Matrix matrixArrayBeta = Mat5.newMatrix(new int[] {1, Main.LENGTH_BETA});
    IntStream.range(0, Main.LENGTH_BETA).forEach(i -> matrixArrayBeta.setDouble(new int[] {0, i}, Main.BETA[i]));
    Matrix matrixArrayPSharing = Mat5.newMatrix(new int[] {1, Main.LENGTH_P_SHARING});
    IntStream.range(0, Main.LENGTH_P_SHARING).forEach(i -> matrixArrayPSharing.setDouble(new int[] {0, i}, Main.P_SHARING[i]));
    Matrix matrixArrayPSeeking = Mat5.newMatrix(new int[] {1, Main.LENGTH_P_SEEKING});
    IntStream.range(0, Main.LENGTH_P_SEEKING).forEach(i -> matrixArrayPSeeking.setDouble(new int[] {0, i}, Main.P_SEEKING[i]));
    Matrix matrixArrayCost = Mat5.newMatrix(new int[] {1, Main.LENGTH_COST});
    IntStream.range(0, Main.LENGTH_COST).forEach(i -> matrixArrayCost.setDouble(new int[] {0, i}, Main.COST[i]));

    Mat5File mat5File = Mat5.newMatFile();

    try {
      mat5File
          .addArray("para_is_cavemen", Mat5.newScalar(Main.IS_CAVEMEN?1:0))

          .addArray("para_iteration", Mat5.newScalar(Main.ITERATION))
          .addArray("para_time", Mat5.newScalar(Main.TIME))

          .addArray("para_n", Mat5.newScalar(Main.N))
          .addArray("para_n_in", Mat5.newScalar(Main.N_IN_GROUP))
          .addArray("para_n_of", Mat5.newScalar(Main.N_OF_GROUP))
          .addArray("para_m", Mat5.newScalar(Main.M))
          .addArray("para_s", Mat5.newScalar(Main.S))
          .addArray("para_p_l", Mat5.newScalar(Main.P_LEARNING))

          .addArray("para_l_b", Mat5.newScalar(Main.LENGTH_BETA))
          .addArray("para_a_b", matrixArrayBeta)
          .addArray("para_l_p1", Mat5.newScalar(Main.LENGTH_P_SHARING))
          .addArray("para_a_p1", matrixArrayPSharing)
          .addArray("para_l_p2", Mat5.newScalar(Main.LENGTH_P_SEEKING))
          .addArray("para_a_p2", matrixArrayPSharing)
          .addArray("para_l_c", Mat5.newScalar(Main.LENGTH_COST))
          .addArray("para_a_c", matrixArrayCost)

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

          .addArray("r_r_know_avg", rankKnowledgeAVG)
          .addArray("r_r_know_std", rankKnowledgeSTD)
          .addArray("r_r_cont_avg", rankContributionAVG)
          .addArray("r_r_cont_std", rankContributionSTD)
          .addArray("r_r_conp_avg", rankContributionPositiveAVG)
          .addArray("r_r_conp_std", rankContributionPositiveSTD)
          .addArray("r_r_conn_avg", rankContributionNegativeAVG)
          .addArray("r_r_conn_std", rankContributionNegativeSTD)

          .addArray("r_r_cbst_avg", rankContributionBestAVG)
          .addArray("r_r_cbst_std", rankContributionBestSTD)
          .addArray("r_r_cbsp_avg", rankContributionBestPositiveAVG)
          .addArray("r_r_cbsp_std", rankContributionBestPositiveSTD)
          .addArray("r_r_cbsn_avg", rankContributionBestNegativeAVG)
          .addArray("r_r_cbsn_std", rankContributionBestNegativeSTD)

          .addArray("r_r_appr_avg", rankApplicationRateAVG)
          .addArray("r_r_appr_std", rankApplicationRateSTD)
          .addArray("r_r_appp_avg", rankApplicationRatePositiveAVG)
          .addArray("r_r_appp_std", rankApplicationRatePositiveSTD)
          .addArray("r_r_appn_avg", rankApplicationRateNegativeAVG)
          .addArray("r_r_appn_std", rankApplicationRateNegativeSTD)

          .addArray("r_r_cent_avg", rankCentralityAVG)
          .addArray("r_r_cent_std", rankCentralitySTD)
      ;

      mat5File
        .addArray("perf_seconds", Mat5.newScalar((System.currentTimeMillis() - Main.TIC)/1000))
        .writeTo(Sinks.newStreamingFile(new File(Main.LABEL + Main.PARAMS +".mat")));

      System.out.println("File Printed");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
