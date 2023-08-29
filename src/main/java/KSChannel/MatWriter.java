package KSChannel;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.Matrix;
import us.hebi.matlab.mat.types.Sinks;

class MatWriter {

  MatWriter(Computation c ){
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
      for (int e = 0; e < Main.LENGTH_EPSILON; e++) {
      for (int p1 = 0; p1 < Main.LENGTH_P_SHARING; p1++) {
        for (int p2 = 0; p2 < Main.LENGTH_P_SEEKING; p2++) {
            for (int t = 0; t < Main.TIME; t++) {
              int[] indices = {b, e, p1, p2, t};
              knowledgeAVG.setDouble(indices, c.knowledgeAVG[b][e][p1][p2][t]);
              knowledgeSTD.setDouble(indices, c.knowledgeSTD[b][e][p1][p2][t]);
              knowledgeBestAVG.setDouble(indices, c.knowledgeBestAVG[b][e][p1][p2][t]);
              knowledgeBestSTD.setDouble(indices, c.knowledgeBestSTD[b][e][p1][p2][t]);
              knowledgeBestSourceDiversityAVG.setDouble(indices, c.knowledgeBestSourceDiversityAVG[b][e][p1][p2][t]);
              knowledgeBestSourceDiversitySTD.setDouble(indices, c.knowledgeBestSourceDiversitySTD[b][e][p1][p2][t]);
              knowledgeMinMaxAVG.setDouble(indices, c.knowledgeMinMaxAVG[b][e][p1][p2][t]);
              knowledgeMinMaxSTD.setDouble(indices, c.knowledgeMinMaxSTD[b][e][p1][p2][t]);
              beliefDiversityAVG.setDouble(indices, c.beliefDiversityAVG[b][e][p1][p2][t]);
              beliefDiversitySTD.setDouble(indices, c.beliefDiversitySTD[b][e][p1][p2][t]);
              beliefSourceDiversityAVG.setDouble(indices, c.beliefSourceDiversityAVG[b][e][p1][p2][t]);
              beliefSourceDiversitySTD.setDouble(indices, c.beliefSourceDiversitySTD[b][e][p1][p2][t]);
              centralizationAVG.setDouble(indices, c.centralizationAVG[b][e][p1][p2][t]);
              centralizationSTD.setDouble(indices, c.centralizationSTD[b][e][p1][p2][t]);

              for (int n = 0; n < Main.N; n++) {
                int[] indicesRank = {b, e, p1, p2, t, n};
                rankKnowledgeAVG.setDouble(indicesRank, c.rankKnowledgeAVG[b][e][p1][p2][t][n]);
                rankKnowledgeSTD.setDouble(indicesRank, c.rankKnowledgeSTD[b][e][p1][p2][t][n]);
                rankContributionAVG.setDouble(indicesRank, c.rankContributionAVG[b][e][p1][p2][t][n]);
                rankContributionSTD.setDouble(indicesRank, c.rankContributionSTD[b][e][p1][p2][t][n]);
                rankContributionPositiveAVG.setDouble(indicesRank, c.rankContributionPositiveAVG[b][e][p1][p2][t][n]);
                rankContributionPositiveSTD.setDouble(indicesRank, c.rankContributionPositiveSTD[b][e][p1][p2][t][n]);
                rankContributionNegativeAVG.setDouble(indicesRank, c.rankContributionNegativeAVG[b][e][p1][p2][t][n]);
                rankContributionNegativeSTD.setDouble(indicesRank, c.rankContributionNegativeSTD[b][e][p1][p2][t][n]);
                rankContributionBestAVG.setDouble(indicesRank, c.rankContributionBestAVG[b][e][p1][p2][t][n]);
                rankContributionBestSTD.setDouble(indicesRank, c.rankContributionBestSTD[b][e][p1][p2][t][n]);
                rankContributionBestPositiveAVG.setDouble(indicesRank, c.rankContributionBestPositiveAVG[b][e][p1][p2][t][n]);
                rankContributionBestPositiveSTD.setDouble(indicesRank, c.rankContributionBestPositiveSTD[b][e][p1][p2][t][n]);
                rankContributionBestNegativeAVG.setDouble(indicesRank, c.rankContributionBestNegativeAVG[b][e][p1][p2][t][n]);
                rankContributionBestNegativeSTD.setDouble(indicesRank, c.rankContributionBestNegativeSTD[b][e][p1][p2][t][n]);
                rankApplicationRateAVG.setDouble(indicesRank, c.rankApplicationRateAVG[b][e][p1][p2][t][n]);
                rankApplicationRateSTD.setDouble(indicesRank, c.rankApplicationRateSTD[b][e][p1][p2][t][n]);
                rankApplicationRatePositiveAVG.setDouble(indicesRank, c.rankApplicationRatePositiveAVG[b][e][p1][p2][t][n]);
                rankApplicationRatePositiveSTD.setDouble(indicesRank, c.rankApplicationRatePositiveSTD[b][e][p1][p2][t][n]);
                rankApplicationRateNegativeAVG.setDouble(indicesRank, c.rankApplicationRateNegativeAVG[b][e][p1][p2][t][n]);
                rankApplicationRateNegativeSTD.setDouble(indicesRank, c.rankApplicationRateNegativeSTD[b][e][p1][p2][t][n]);
                rankCentralityAVG.setDouble(indicesRank, c.rankCentralityAVG[b][e][p1][p2][t][n]);
                rankCentralitySTD.setDouble(indicesRank, c.rankCentralitySTD[b][e][p1][p2][t][n]);
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
    Matrix matrixArrayEpsilon = Mat5.newMatrix(new int[] {1, Main.LENGTH_EPSILON});
    IntStream.range(0, Main.LENGTH_EPSILON).forEach(i -> matrixArrayEpsilon.setDouble(new int[] {0, i}, Main.EPSILON[i]));

    Mat5File mat5File = Mat5.newMatFile();

    try {
      mat5File
          .addArray("para_is_cavemen", Mat5.newScalar(Main.IS_CAVEMEN?1:0))
          .addArray("para_is_costly", Mat5.newScalar(Main.IS_LEARNING_COSTLY?1:0))
          .addArray("para_is_greedy", Mat5.newScalar(Main.IS_LEARNING_GREEDY?1:0))

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
          .addArray("para_l_e", Mat5.newScalar(Main.LENGTH_EPSILON))
          .addArray("para_a_e", matrixArrayEpsilon)

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
