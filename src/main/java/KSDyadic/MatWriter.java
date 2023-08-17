package KSDyadic;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import us.hebi.matlab.mat.format.Mat5;
import us.hebi.matlab.mat.format.Mat5File;
import us.hebi.matlab.mat.types.Matrix;
import us.hebi.matlab.mat.types.Sinks;

class MatWriter {

  MatWriter( Computation c ){
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
    
    Matrix typeKnowledgeAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_TYPE);
    Matrix typeKnowledgeSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_TYPE);
    Matrix typeContributionAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_TYPE_TO_TYPE);
    Matrix typeContributionSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_TYPE_TO_TYPE);
    Matrix typeContributionPositiveAVG = Mat5.newMatrix(Main.RESULT_KEY_VALUE_TYPE_TO_TYPE);
    Matrix typeContributionPositiveSTD = Mat5.newMatrix(Main.RESULT_KEY_VALUE_TYPE_TO_TYPE);

    for (int b = 0; b < Main.LENGTH_BETA; b++) {
      for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
        for (int t = 0; t < Main.TIME; t++) {
          int[] indices = {b, ps, t};
          knowledgeAVG.setDouble(indices, c.knowledgeAVG[b][ps][t]);
          knowledgeSTD.setDouble(indices, c.knowledgeSTD[b][ps][t]);
          knowledgeBestAVG.setDouble(indices, c.knowledgeBestAVG[b][ps][t]);
          knowledgeBestSTD.setDouble(indices, c.knowledgeBestSTD[b][ps][t]);
          knowledgeBestSourceDiversityAVG.setDouble(indices, c.knowledgeBestSourceDiversityAVG[b][ps][t]);
          knowledgeBestSourceDiversitySTD.setDouble(indices, c.knowledgeBestSourceDiversitySTD[b][ps][t]);
          knowledgeMinMaxAVG.setDouble(indices, c.knowledgeMinMaxAVG[b][ps][t]);
          knowledgeMinMaxSTD.setDouble(indices, c.knowledgeMinMaxSTD[b][ps][t]);
          beliefDiversityAVG.setDouble(indices, c.beliefDiversityAVG[b][ps][t]);
          beliefDiversitySTD.setDouble(indices, c.beliefDiversitySTD[b][ps][t]);
          beliefSourceDiversityAVG.setDouble(indices, c.beliefSourceDiversityAVG[b][ps][t]);
          beliefSourceDiversitySTD.setDouble(indices, c.beliefSourceDiversitySTD[b][ps][t]);
          centralizationAVG.setDouble(indices, c.centralizationAVG[b][ps][t]);
          centralizationSTD.setDouble(indices, c.centralizationSTD[b][ps][t]);

          for( int n = 0; n < Main.N; n ++ ){
            int[] indicesRank = {b, ps, t, n};
            rankKnowledgeAVG.setDouble(indicesRank, c.rankKnowledgeAVG[b][ps][t][n]);
            rankKnowledgeSTD.setDouble(indicesRank, c.rankKnowledgeSTD[b][ps][t][n]);
            rankContributionAVG.setDouble(indicesRank, c.rankContributionAVG[b][ps][t][n]);
            rankContributionSTD.setDouble(indicesRank, c.rankContributionSTD[b][ps][t][n]);
            rankContributionPositiveAVG.setDouble(indicesRank, c.rankContributionPositiveAVG[b][ps][t][n]);
            rankContributionPositiveSTD.setDouble(indicesRank, c.rankContributionPositiveSTD[b][ps][t][n]);
            rankContributionNegativeAVG.setDouble(indicesRank, c.rankContributionNegativeAVG[b][ps][t][n]);
            rankContributionNegativeSTD.setDouble(indicesRank, c.rankContributionNegativeSTD[b][ps][t][n]);
            rankContributionBestAVG.setDouble(indicesRank, c.rankContributionBestAVG[b][ps][t][n]);
            rankContributionBestSTD.setDouble(indicesRank, c.rankContributionBestSTD[b][ps][t][n]);
            rankContributionBestPositiveAVG.setDouble(indicesRank, c.rankContributionBestPositiveAVG[b][ps][t][n]);
            rankContributionBestPositiveSTD.setDouble(indicesRank, c.rankContributionBestPositiveSTD[b][ps][t][n]);
            rankContributionBestNegativeAVG.setDouble(indicesRank, c.rankContributionBestNegativeAVG[b][ps][t][n]);
            rankContributionBestNegativeSTD.setDouble(indicesRank, c.rankContributionBestNegativeSTD[b][ps][t][n]);
            rankApplicationRateAVG.setDouble(indicesRank, c.rankApplicationRateAVG[b][ps][t][n]);
            rankApplicationRateSTD.setDouble(indicesRank, c.rankApplicationRateSTD[b][ps][t][n]);
            rankApplicationRatePositiveAVG.setDouble(indicesRank, c.rankApplicationRatePositiveAVG[b][ps][t][n]);
            rankApplicationRatePositiveSTD.setDouble(indicesRank, c.rankApplicationRatePositiveSTD[b][ps][t][n]);
            rankApplicationRateNegativeAVG.setDouble(indicesRank, c.rankApplicationRateNegativeAVG[b][ps][t][n]);
            rankApplicationRateNegativeSTD.setDouble(indicesRank, c.rankApplicationRateNegativeSTD[b][ps][t][n]);
            rankCentralityAVG.setDouble(indicesRank, c.rankCentralityAVG[b][ps][t][n]);
            rankCentralitySTD.setDouble(indicesRank, c.rankCentralitySTD[b][ps][t][n]);
          }

          if( Main.IS_RATIO ){
            for( int type = 0; type < 2; type ++ ){
              int[] indicesType = {b, ps, t, type};
              typeKnowledgeAVG.setDouble(indicesType, c.typeKnowledgeAVG[b][ps][t][type]);
              typeKnowledgeSTD.setDouble(indicesType, c.typeKnowledgeSTD[b][ps][t][type]);
            }

            for( int type2Type = 0; type2Type < 4; type2Type ++ ){
              int[] indicesType2Type = {b, ps, t, type2Type};
              typeContributionAVG.setDouble(indicesType2Type, c.typeContributionAVG[b][ps][t][type2Type]);
              typeContributionSTD.setDouble(indicesType2Type, c.typeContributionSTD[b][ps][t][type2Type]);
              typeContributionPositiveAVG.setDouble(indicesType2Type, c.typeContributionPositiveAVG[b][ps][t][type2Type]);
              typeContributionPositiveSTD.setDouble(indicesType2Type, c.typeContributionPositiveSTD[b][ps][t][type2Type]);
            }
          }

        }
      }
    }

    Matrix matrixArrayBeta = Mat5.newMatrix(new int[] {1, Main.LENGTH_BETA});
    IntStream.range(0, Main.LENGTH_BETA).forEach(i -> matrixArrayBeta.setDouble(new int[] {0, i}, Main.BETA[i]));
    Matrix matrixArrayPSharing = Mat5.newMatrix(new int[] {1, Main.LENGTH_P_SHARING});
    IntStream.range(0, Main.LENGTH_P_SHARING).forEach(i -> matrixArrayPSharing.setDouble(new int[] {0, i}, Main.P_SHARING[i]));

    Mat5File mat5File = Mat5.newMatFile();

    try {
      mat5File
          .addArray("para_is_ratio", Mat5.newScalar(Main.IS_RATIO?1:0))
          .addArray("para_is_cavemen", Mat5.newScalar(Main.IS_CAVEMEN?1:0))
          .addArray("para_is_one_on_one", Mat5.newScalar(Main.IS_ONE_ON_ONE?1:0))

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
          .addArray("para_l_p_s", Mat5.newScalar(Main.LENGTH_P_SHARING))
          .addArray("para_a_p_s", matrixArrayPSharing)

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

      if( Main.IS_RATIO ){
        mat5File
          .addArray("r_t_know_avg", typeKnowledgeAVG)
          .addArray("r_t_know_std", typeKnowledgeSTD)
          .addArray("r_t_cont_avg", typeContributionAVG)
          .addArray("r_t_cont_std", typeContributionSTD)
          .addArray("r_t_conp_avg", typeContributionPositiveAVG)
          .addArray("r_t_conp_std", typeContributionPositiveSTD)
        ;
      }

      mat5File
        .addArray("perf_seconds", Mat5.newScalar((System.currentTimeMillis() - Main.TIC)/1000))
        .writeTo(Sinks.newStreamingFile(new File(Main.LABEL + Main.PARAMS +".mat")));

      System.out.println("File Printed");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
