package KSChannel;

import static org.apache.commons.math3.util.FastMath.pow;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Computation {

  AtomicDouble[][][][][] knowledgeAVGAtomic;
  AtomicDouble[][][][][] knowledgeSTDAtomic;

  AtomicDouble[][][][][] knowledgeBestAVGAtomic;
  AtomicDouble[][][][][] knowledgeBestSTDAtomic;
  AtomicDouble[][][][][] knowledgeBestSourceDiversityAVGAtomic;
  AtomicDouble[][][][][] knowledgeBestSourceDiversitySTDAtomic;

  AtomicDouble[][][][][] knowledgeMinMaxAVGAtomic;
  AtomicDouble[][][][][] knowledgeMinMaxSTDAtomic;

  AtomicDouble[][][][][] beliefDiversityAVGAtomic;
  AtomicDouble[][][][][] beliefDiversitySTDAtomic;
  AtomicDouble[][][][][] beliefSourceDiversityAVGAtomic;
  AtomicDouble[][][][][] beliefSourceDiversitySTDAtomic;

  AtomicDouble[][][][][] centralizationAVGAtomic;
  AtomicDouble[][][][][] centralizationSTDAtomic;

  AtomicDouble[][][][][][] rankKnowledgeAVGAtomic;
  AtomicDouble[][][][][][] rankKnowledgeSTDAtomic;
  AtomicDouble[][][][][][] rankContributionAVGAtomic;
  AtomicDouble[][][][][][] rankContributionSTDAtomic;
  AtomicDouble[][][][][][] rankContributionPositiveAVGAtomic;
  AtomicDouble[][][][][][] rankContributionPositiveSTDAtomic;
  AtomicDouble[][][][][][] rankContributionNegativeAVGAtomic;
  AtomicDouble[][][][][][] rankContributionNegativeSTDAtomic;

  AtomicDouble[][][][][][] rankContributionBestAVGAtomic;
  AtomicDouble[][][][][][] rankContributionBestSTDAtomic;
  AtomicDouble[][][][][][] rankContributionBestPositiveAVGAtomic;
  AtomicDouble[][][][][][] rankContributionBestPositiveSTDAtomic;
  AtomicDouble[][][][][][] rankContributionBestNegativeAVGAtomic;
  AtomicDouble[][][][][][] rankContributionBestNegativeSTDAtomic;

  AtomicDouble[][][][][][] rankApplicationRateAVGAtomic;
  AtomicDouble[][][][][][] rankApplicationRateSTDAtomic;
  AtomicDouble[][][][][][] rankApplicationRatePositiveAVGAtomic;
  AtomicDouble[][][][][][] rankApplicationRatePositiveSTDAtomic;
  AtomicDouble[][][][][][] rankApplicationRateNegativeAVGAtomic;
  AtomicDouble[][][][][][] rankApplicationRateNegativeSTDAtomic;

  AtomicDouble[][][][][][] rankCentralityAVGAtomic;
  AtomicDouble[][][][][][] rankCentralitySTDAtomic;

  AtomicDouble[][][][][][] typeKnowledgeAVGAtomic;
  AtomicDouble[][][][][][] typeKnowledgeSTDAtomic;
  AtomicDouble[][][][][][] typeContributionAVGAtomic;
  AtomicDouble[][][][][][] typeContributionSTDAtomic;
  AtomicDouble[][][][][][] typeContributionPositiveAVGAtomic;
  AtomicDouble[][][][][][] typeContributionPositiveSTDAtomic;

  //In double arrays
  double[][][][][] knowledgeAVG;
  double[][][][][] knowledgeSTD;

  double[][][][][] knowledgeBestAVG;
  double[][][][][] knowledgeBestSTD;
  double[][][][][] knowledgeBestSourceDiversityAVG;
  double[][][][][] knowledgeBestSourceDiversitySTD;

  double[][][][][] knowledgeMinMaxAVG;
  double[][][][][] knowledgeMinMaxSTD;

  double[][][][][] beliefDiversityAVG;
  double[][][][][] beliefDiversitySTD;
  double[][][][][] beliefSourceDiversityAVG;
  double[][][][][] beliefSourceDiversitySTD;

  double[][][][][] centralizationAVG;
  double[][][][][] centralizationSTD;

  double[][][][][][] rankKnowledgeAVG;
  double[][][][][][] rankKnowledgeSTD;
  double[][][][][][] rankContributionAVG;
  double[][][][][][] rankContributionSTD;
  double[][][][][][] rankContributionPositiveAVG;
  double[][][][][][] rankContributionPositiveSTD;
  double[][][][][][] rankContributionNegativeAVG;
  double[][][][][][] rankContributionNegativeSTD;

  double[][][][][][] rankContributionBestAVG;
  double[][][][][][] rankContributionBestSTD;
  double[][][][][][] rankContributionBestPositiveAVG;
  double[][][][][][] rankContributionBestPositiveSTD;
  double[][][][][][] rankContributionBestNegativeAVG;
  double[][][][][][] rankContributionBestNegativeSTD;

  double[][][][][][] rankApplicationRateAVG;
  double[][][][][][] rankApplicationRateSTD;
  double[][][][][][] rankApplicationRatePositiveAVG;
  double[][][][][][] rankApplicationRatePositiveSTD;
  double[][][][][][] rankApplicationRateNegativeAVG;
  double[][][][][][] rankApplicationRateNegativeSTD;

  double[][][][][][] rankCentralityAVG;
  double[][][][][][] rankCentralitySTD;

  double[][][][][][] typeKnowledgeAVG;
  double[][][][][][] typeKnowledgeSTD;
  double[][][][][][] typeContributionAVG;
  double[][][][][][] typeContributionSTD;
  double[][][][][][] typeContributionPositiveAVG;
  double[][][][][][] typeContributionPositiveSTD;

  ProgressBar pb;

  Computation() {
    compute();
  }

  public void compute() {
    pb = new ProgressBar(Main.ITERATION);
    setSpace();
    setAtomic();
    runFullExperiment();
    averageFullExperiment();
  }

  private void setSpace() {
    knowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestSourceDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeMinMaxAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeMinMaxSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefSourceDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    centralizationAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    centralizationSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    rankKnowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankKnowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRatePositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRatePositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankCentralityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankCentralitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    typeKnowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][2];
    typeKnowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][2];
    typeContributionAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
    typeContributionSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
    typeContributionPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
    typeContributionPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];

    knowledgeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestSourceDiversityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeBestSourceDiversitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeMinMaxAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    knowledgeMinMaxSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefDiversityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefDiversitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefSourceDiversityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    beliefSourceDiversitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    centralizationAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    centralizationSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME];
    rankKnowledgeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankKnowledgeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionPositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionPositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionNegativeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionNegativeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestPositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestPositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestNegativeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankContributionBestNegativeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRatePositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRatePositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateNegativeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankApplicationRateNegativeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankCentralityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    rankCentralitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][Main.N];
    typeKnowledgeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][2];
    typeKnowledgeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][2];
    typeContributionAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
    typeContributionSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
    typeContributionPositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
    typeContributionPositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_EPSILON][Main.LENGTH_P_SHARING][Main.LENGTH_P_SEEKING][Main.TIME][4];
  }

  private void setAtomic() {
    for (int b = 0; b < Main.LENGTH_BETA; b++) {
      for (int e = 0; e < Main.LENGTH_EPSILON; e++) {
        for (int p1 = 0; p1 < Main.LENGTH_P_SHARING; p1++) {
          for (int p2 = 0; p2 < Main.LENGTH_P_SEEKING; p2++) {
            for (int t = 0; t < Main.TIME; t++) {
              knowledgeAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeSTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeBestAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeBestSTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeBestSourceDiversityAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeBestSourceDiversitySTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeMinMaxAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              knowledgeMinMaxSTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              beliefDiversityAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              beliefDiversitySTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              beliefSourceDiversityAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              beliefSourceDiversitySTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              centralizationAVGAtomic[b][e][p1][p2][t] = new AtomicDouble();
              centralizationSTDAtomic[b][e][p1][p2][t] = new AtomicDouble();
              for (int n = 0; n < Main.N; n++) {
                rankKnowledgeAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankKnowledgeSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionPositiveAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionPositiveSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionNegativeAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionNegativeSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionBestAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionBestSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionBestPositiveAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionBestPositiveSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionBestNegativeAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankContributionBestNegativeSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankApplicationRateAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankApplicationRateSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankApplicationRatePositiveAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankApplicationRatePositiveSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankApplicationRateNegativeAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankApplicationRateNegativeSTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankCentralityAVGAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
                rankCentralitySTDAtomic[b][e][p1][p2][t][n] = new AtomicDouble();
              }
              typeKnowledgeAVGAtomic[b][e][p1][p2][t][0] = new AtomicDouble();
              typeKnowledgeSTDAtomic[b][e][p1][p2][t][0] = new AtomicDouble();
              typeKnowledgeAVGAtomic[b][e][p1][p2][t][1] = new AtomicDouble();
              typeKnowledgeSTDAtomic[b][e][p1][p2][t][1] = new AtomicDouble();
              for (int type2type = 0; type2type < 4; type2type++) {
                typeContributionAVGAtomic[b][e][p1][p2][t][type2type] = new AtomicDouble();
                typeContributionSTDAtomic[b][e][p1][p2][t][type2type] = new AtomicDouble();
                typeContributionPositiveAVGAtomic[b][e][p1][p2][t][type2type] = new AtomicDouble();
                typeContributionPositiveSTDAtomic[b][e][p1][p2][t][type2type] = new AtomicDouble();
              }
            }
          }
        }
      }
    }
  }

  private void runFullExperiment() {
    ExecutorService workStealingPool = Executors.newWorkStealingPool();
    for (int iteration = 0; iteration < Main.ITERATION; iteration++) {
      iterationWrapper iterationWrap = new iterationWrapper();
      workStealingPool.execute(iterationWrap);
    }
    workStealingPool.shutdown();
    try {
      workStealingPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (InterruptedException e) {
      System.out.println(e);
    }
  }

  private void averageFullExperiment() {
    for (int b = 0; b < Main.LENGTH_BETA; b++) {
      for (int e = 0; e < Main.LENGTH_EPSILON; e++) {
        for (int p1 = 0; p1 < Main.LENGTH_P_SHARING; p1++) {
          for (int p2 = 0; p2 < Main.LENGTH_P_SEEKING; p2++) {
            for (int t = 0; t < Main.TIME; t++) {
              knowledgeAVG[b][e][p1][p2][t] = knowledgeAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeSTD[b][e][p1][p2][t] = knowledgeSTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeSTD[b][e][p1][p2][t] = pow(knowledgeSTD[b][e][p1][p2][t] - pow(knowledgeAVG[b][e][p1][p2][t], 2), .5);
              knowledgeBestAVG[b][e][p1][p2][t] = knowledgeBestAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeBestSTD[b][e][p1][p2][t] = knowledgeBestSTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeBestSTD[b][e][p1][p2][t] = pow(knowledgeBestSTD[b][e][p1][p2][t] - pow(knowledgeBestAVG[b][e][p1][p2][t], 2), .5);
              knowledgeBestSourceDiversityAVG[b][e][p1][p2][t] = knowledgeBestSourceDiversityAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeBestSourceDiversitySTD[b][e][p1][p2][t] = knowledgeBestSourceDiversitySTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeBestSourceDiversitySTD[b][e][p1][p2][t] = pow(knowledgeBestSourceDiversitySTD[b][e][p1][p2][t] - pow(knowledgeBestSourceDiversityAVG[b][e][p1][p2][t], 2), .5);
              knowledgeMinMaxAVG[b][e][p1][p2][t] = knowledgeMinMaxAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeMinMaxSTD[b][e][p1][p2][t] = knowledgeMinMaxSTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              knowledgeMinMaxSTD[b][e][p1][p2][t] = pow(knowledgeMinMaxSTD[b][e][p1][p2][t] - pow(knowledgeMinMaxAVG[b][e][p1][p2][t], 2), .5);
              beliefDiversityAVG[b][e][p1][p2][t] = beliefDiversityAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              beliefDiversitySTD[b][e][p1][p2][t] = beliefDiversitySTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              beliefDiversitySTD[b][e][p1][p2][t] = pow(beliefDiversitySTD[b][e][p1][p2][t] - pow(beliefDiversityAVG[b][e][p1][p2][t], 2), .5);
              beliefSourceDiversityAVG[b][e][p1][p2][t] = beliefSourceDiversityAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              beliefSourceDiversitySTD[b][e][p1][p2][t] = beliefSourceDiversitySTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              beliefSourceDiversitySTD[b][e][p1][p2][t] = pow(beliefSourceDiversitySTD[b][e][p1][p2][t] - pow(beliefSourceDiversityAVG[b][e][p1][p2][t], 2), .5);
              centralizationAVG[b][e][p1][p2][t] = centralizationAVGAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              centralizationSTD[b][e][p1][p2][t] = centralizationSTDAtomic[b][e][p1][p2][t].get() / Main.ITERATION;
              centralizationSTD[b][e][p1][p2][t] = pow(centralizationSTD[b][e][p1][p2][t] - pow(centralizationAVG[b][e][p1][p2][t], 2), .5);

              for (int n = 0; n < Main.N; n++) {
                rankKnowledgeAVG[b][e][p1][p2][t][n] = rankKnowledgeAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankKnowledgeSTD[b][e][p1][p2][t][n] = rankKnowledgeSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankKnowledgeSTD[b][e][p1][p2][t][n] = pow(rankKnowledgeSTD[b][e][p1][p2][t][n] - pow(rankKnowledgeAVG[b][e][p1][p2][t][n], 2), .5);
                rankContributionAVG[b][e][p1][p2][t][n] = rankContributionAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionSTD[b][e][p1][p2][t][n] = rankContributionSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionSTD[b][e][p1][p2][t][n] = pow(rankContributionSTD[b][e][p1][p2][t][n] - pow(rankContributionAVG[b][e][p1][p2][t][n], 2), .5);
                rankContributionPositiveAVG[b][e][p1][p2][t][n] = rankContributionPositiveAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionPositiveSTD[b][e][p1][p2][t][n] = rankContributionPositiveSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionPositiveSTD[b][e][p1][p2][t][n] = pow(rankContributionPositiveSTD[b][e][p1][p2][t][n] - pow(rankContributionPositiveAVG[b][e][p1][p2][t][n], 2), .5);
                rankContributionNegativeAVG[b][e][p1][p2][t][n] = rankContributionNegativeAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionNegativeSTD[b][e][p1][p2][t][n] = rankContributionNegativeSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionNegativeSTD[b][e][p1][p2][t][n] = pow(rankContributionNegativeSTD[b][e][p1][p2][t][n] - pow(rankContributionNegativeAVG[b][e][p1][p2][t][n], 2), .5);
                rankContributionBestAVG[b][e][p1][p2][t][n] = rankContributionBestAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionBestSTD[b][e][p1][p2][t][n] = rankContributionBestSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionBestSTD[b][e][p1][p2][t][n] = pow(rankContributionBestSTD[b][e][p1][p2][t][n] - pow(rankContributionBestAVG[b][e][p1][p2][t][n], 2), .5);
                rankContributionBestPositiveAVG[b][e][p1][p2][t][n] = rankContributionBestPositiveAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionBestPositiveSTD[b][e][p1][p2][t][n] = rankContributionBestPositiveSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionBestPositiveSTD[b][e][p1][p2][t][n] = pow(rankContributionBestPositiveSTD[b][e][p1][p2][t][n] - pow(rankContributionBestPositiveAVG[b][e][p1][p2][t][n], 2), .5);
                rankContributionBestNegativeAVG[b][e][p1][p2][t][n] = rankContributionBestNegativeAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionBestNegativeSTD[b][e][p1][p2][t][n] = rankContributionBestNegativeSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankContributionBestNegativeSTD[b][e][p1][p2][t][n] = pow(rankContributionBestNegativeSTD[b][e][p1][p2][t][n] - pow(rankContributionBestNegativeAVG[b][e][p1][p2][t][n], 2), .5);
                rankApplicationRateAVG[b][e][p1][p2][t][n] = rankApplicationRateAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankApplicationRateSTD[b][e][p1][p2][t][n] = rankApplicationRateSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankApplicationRateSTD[b][e][p1][p2][t][n] = pow(rankApplicationRateSTD[b][e][p1][p2][t][n] - pow(rankApplicationRateAVG[b][e][p1][p2][t][n], 2), .5);
                rankApplicationRatePositiveAVG[b][e][p1][p2][t][n] = rankApplicationRatePositiveAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankApplicationRatePositiveSTD[b][e][p1][p2][t][n] = rankApplicationRatePositiveSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankApplicationRatePositiveSTD[b][e][p1][p2][t][n] = pow(rankApplicationRatePositiveSTD[b][e][p1][p2][t][n] - pow(rankApplicationRatePositiveAVG[b][e][p1][p2][t][n], 2), .5);
                rankApplicationRateNegativeAVG[b][e][p1][p2][t][n] = rankApplicationRateNegativeAVGAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankApplicationRateNegativeSTD[b][e][p1][p2][t][n] = rankApplicationRateNegativeSTDAtomic[b][e][p1][p2][t][n].get() / Main.ITERATION;
                rankApplicationRateNegativeSTD[b][e][p1][p2][t][n] = pow(rankApplicationRateNegativeSTD[b][e][p1][p2][t][n] - pow(rankApplicationRateNegativeAVG[b][e][p1][p2][t][n], 2), .5);
              }
            }
          }
        }
      }
    }
  }

  class iterationWrapper implements Runnable {

    iterationWrapper() {
    }

    @Override
    public void run() {
      for (int b = 0; b < Main.LENGTH_BETA; b++) {
        for (int e = 0; e < Main.LENGTH_EPSILON; e++) {
          for (int p1 = 0; p1 < Main.LENGTH_P_SHARING; p1++) {
            for (int p2 = 0; p2 < Main.LENGTH_P_SEEKING; p2++) {
              new SingleRun(b, e, p1, p2);
            }
          }
        }
      }
      pb.stepNext();
    }

  }

  class SingleRun {

    int betaIndex;
    int epsilonIndex;
    int p1Index, p2Index;
    double beta;
    double epsilon;
    double p1, p2;

    AtomicDouble[] knowledgeAVGAtomicPart;
    AtomicDouble[] knowledgeSTDAtomicPart;
    AtomicDouble[] knowledgeBestAVGAtomicPart;
    AtomicDouble[] knowledgeBestSTDAtomicPart;
    AtomicDouble[] knowledgeBestSourceDiversityAVGAtomicPart;
    AtomicDouble[] knowledgeBestSourceDiversitySTDAtomicPart;
    AtomicDouble[] knowledgeMinMaxAVGAtomicPart;
    AtomicDouble[] knowledgeMinMaxSTDAtomicPart;
    AtomicDouble[] beliefDiversityAVGAtomicPart;
    AtomicDouble[] beliefDiversitySTDAtomicPart;
    AtomicDouble[] beliefSourceDiversityAVGAtomicPart;
    AtomicDouble[] beliefSourceDiversitySTDAtomicPart;
    AtomicDouble[] centralizationAVGAtomicPart;
    AtomicDouble[] centralizationSTDAtomicPart;

    AtomicDouble[][] rankKnowledgeAVGAtomicPart;
    AtomicDouble[][] rankKnowledgeSTDAtomicPart;
    AtomicDouble[][] rankContributionAVGAtomicPart;
    AtomicDouble[][] rankContributionSTDAtomicPart;
    AtomicDouble[][] rankContributionPositiveAVGAtomicPart;
    AtomicDouble[][] rankContributionPositiveSTDAtomicPart;
    AtomicDouble[][] rankContributionNegativeAVGAtomicPart;
    AtomicDouble[][] rankContributionNegativeSTDAtomicPart;
    AtomicDouble[][] rankContributionBestAVGAtomicPart;
    AtomicDouble[][] rankContributionBestSTDAtomicPart;
    AtomicDouble[][] rankContributionBestPositiveAVGAtomicPart;
    AtomicDouble[][] rankContributionBestPositiveSTDAtomicPart;
    AtomicDouble[][] rankContributionBestNegativeAVGAtomicPart;
    AtomicDouble[][] rankContributionBestNegativeSTDAtomicPart;
    AtomicDouble[][] rankApplicationRateAVGAtomicPart;
    AtomicDouble[][] rankApplicationRateSTDAtomicPart;
    AtomicDouble[][] rankApplicationRatePositiveAVGAtomicPart;
    AtomicDouble[][] rankApplicationRatePositiveSTDAtomicPart;
    AtomicDouble[][] rankApplicationRateNegativeAVGAtomicPart;
    AtomicDouble[][] rankApplicationRateNegativeSTDAtomicPart;
    AtomicDouble[][] rankCentralityAVGAtomicPart;
    AtomicDouble[][] rankCentralitySTDAtomicPart;

    AtomicDouble[][] typeKnowledgeAVGAtomicPart;
    AtomicDouble[][] typeKnowledgeSTDAtomicPart;
    AtomicDouble[][] typeContributionAVGAtomicPart;
    AtomicDouble[][] typeContributionSTDAtomicPart;
    AtomicDouble[][] typeContributionPositiveAVGAtomicPart;
    AtomicDouble[][] typeContributionPositiveSTDAtomicPart;

    SingleRun(int betaIndex, int epsilonIndex, int p1Index, int p2Index) {
      this.betaIndex = betaIndex;
      this.epsilonIndex = epsilonIndex;
      this.p1Index = p1Index;
      this.p2Index = p2Index;
      beta = Main.BETA[betaIndex];
      epsilon = Main.EPSILON[epsilonIndex];
      p1 = Main.P_SHARING[p1Index];
      p2 = Main.P_SEEKING[p2Index];
      initializeResultSpace();
      run();
    }

    void initializeResultSpace() {
      knowledgeAVGAtomicPart = knowledgeAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeSTDAtomicPart = knowledgeSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeBestAVGAtomicPart = knowledgeBestAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeBestSTDAtomicPart = knowledgeBestSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeBestSourceDiversityAVGAtomicPart = knowledgeBestSourceDiversityAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeBestSourceDiversitySTDAtomicPart = knowledgeBestSourceDiversitySTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeMinMaxAVGAtomicPart = knowledgeMinMaxAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      knowledgeMinMaxSTDAtomicPart = knowledgeMinMaxSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      beliefDiversityAVGAtomicPart = beliefDiversityAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      beliefDiversitySTDAtomicPart = beliefDiversitySTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      beliefSourceDiversityAVGAtomicPart = beliefSourceDiversityAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      beliefSourceDiversitySTDAtomicPart = beliefSourceDiversitySTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      centralizationAVGAtomicPart = centralizationAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      centralizationSTDAtomicPart = centralizationSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];

      rankKnowledgeAVGAtomicPart = rankKnowledgeAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankKnowledgeSTDAtomicPart = rankKnowledgeSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionAVGAtomicPart = rankContributionAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionSTDAtomicPart = rankContributionSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionPositiveAVGAtomicPart = rankContributionPositiveAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionPositiveSTDAtomicPart = rankContributionPositiveSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionNegativeAVGAtomicPart = rankContributionNegativeAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionNegativeSTDAtomicPart = rankContributionNegativeSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionBestAVGAtomicPart = rankContributionBestAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionBestSTDAtomicPart = rankContributionBestSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionBestPositiveAVGAtomicPart = rankContributionBestPositiveAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionBestPositiveSTDAtomicPart = rankContributionBestPositiveSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionBestNegativeAVGAtomicPart = rankContributionBestNegativeAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankContributionBestNegativeSTDAtomicPart = rankContributionBestNegativeSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankApplicationRateAVGAtomicPart = rankApplicationRateAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankApplicationRateSTDAtomicPart = rankApplicationRateSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankApplicationRatePositiveAVGAtomicPart = rankApplicationRatePositiveAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankApplicationRatePositiveSTDAtomicPart = rankApplicationRatePositiveSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankApplicationRateNegativeAVGAtomicPart = rankApplicationRateNegativeAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankApplicationRateNegativeSTDAtomicPart = rankApplicationRateNegativeSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankCentralityAVGAtomicPart = rankCentralityAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      rankCentralitySTDAtomicPart = rankCentralitySTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];

      typeKnowledgeAVGAtomicPart = typeKnowledgeAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      typeKnowledgeSTDAtomicPart = typeKnowledgeSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      typeContributionAVGAtomicPart = typeContributionAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      typeContributionSTDAtomicPart = typeContributionSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      typeContributionPositiveAVGAtomicPart = typeContributionPositiveAVGAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
      typeContributionPositiveSTDAtomicPart = typeContributionPositiveSTDAtomic[betaIndex][epsilonIndex][p1Index][p2Index];
    }

    void run() {
      Scenario sc = new Scenario(beta, epsilon, p1, p2);
      sc.initialize();
      for (int t = 0; t < Main.TIME; t++) {
        //Recording first
        synchronized (this) {
          knowledgeAVGAtomicPart[t].addAndGet(sc.knowledgeAvg);
          knowledgeSTDAtomicPart[t].addAndGet(pow(sc.knowledgeAvg, 2));
          knowledgeBestAVGAtomicPart[t].addAndGet(sc.knowledgeBest);
          knowledgeBestSTDAtomicPart[t].addAndGet(pow(sc.knowledgeBest, 2));
          knowledgeBestSourceDiversityAVGAtomicPart[t].addAndGet(sc.knowledgeBestSourceDiversity);
          knowledgeBestSourceDiversitySTDAtomicPart[t].addAndGet(pow(sc.knowledgeBestSourceDiversity, 2));
          knowledgeMinMaxAVGAtomicPart[t].addAndGet(sc.knowledgeMinMax);
          knowledgeMinMaxSTDAtomicPart[t].addAndGet(pow(sc.knowledgeMinMax, 2));
          beliefDiversityAVGAtomicPart[t].addAndGet(sc.beliefDiversity);
          beliefDiversitySTDAtomicPart[t].addAndGet(pow(sc.beliefDiversity, 2));
          beliefSourceDiversityAVGAtomicPart[t].addAndGet(sc.beliefSourceDiversity);
          beliefSourceDiversitySTDAtomicPart[t].addAndGet(pow(sc.beliefSourceDiversity, 2));
          centralizationAVGAtomicPart[t].addAndGet(sc.centralization);
          centralizationSTDAtomicPart[t].addAndGet(pow(sc.centralization, 2));
          for (int n = 0; n < Main.N; n++) {
            rankKnowledgeAVGAtomicPart[t][n].addAndGet(sc.rankKnowledge[n]);
            rankKnowledgeSTDAtomicPart[t][n].addAndGet(pow(sc.rankKnowledge[n], 2));
            rankContributionAVGAtomicPart[t][n].addAndGet(sc.rankContribution[n]);
            rankContributionSTDAtomicPart[t][n].addAndGet(pow(sc.rankContribution[n], 2));
            rankContributionPositiveAVGAtomicPart[t][n].addAndGet(sc.rankContributionPositive[n]);
            rankContributionPositiveSTDAtomicPart[t][n].addAndGet(pow(sc.rankContributionPositive[n], 2));
            rankContributionNegativeAVGAtomicPart[t][n].addAndGet(sc.rankContributionNegative[n]);
            rankContributionNegativeSTDAtomicPart[t][n].addAndGet(pow(sc.rankContributionNegative[n], 2));
            rankContributionBestAVGAtomicPart[t][n].addAndGet(sc.rankContributionBest[n]);
            rankContributionBestSTDAtomicPart[t][n].addAndGet(pow(sc.rankContributionBest[n], 2));
            rankContributionBestPositiveAVGAtomicPart[t][n].addAndGet(sc.rankContributionBestPositive[n]);
            rankContributionBestPositiveSTDAtomicPart[t][n].addAndGet(pow(sc.rankContributionBestPositive[n], 2));
            rankContributionBestNegativeAVGAtomicPart[t][n].addAndGet(sc.rankContributionBestNegative[n]);
            rankContributionBestNegativeSTDAtomicPart[t][n].addAndGet(pow(sc.rankContributionBestNegative[n], 2));
            rankApplicationRateAVGAtomicPart[t][n].addAndGet(sc.rankApplicationRate[n]);
            rankApplicationRateSTDAtomicPart[t][n].addAndGet(pow(sc.rankApplicationRate[n], 2));
            rankApplicationRatePositiveAVGAtomicPart[t][n].addAndGet(sc.rankApplicationRatePositive[n]);
            rankApplicationRatePositiveSTDAtomicPart[t][n].addAndGet(pow(sc.rankApplicationRatePositive[n], 2));
            rankApplicationRateNegativeAVGAtomicPart[t][n].addAndGet(sc.rankApplicationRateNegative[n]);
            rankApplicationRateNegativeSTDAtomicPart[t][n].addAndGet(pow(sc.rankApplicationRateNegative[n], 2));
            rankCentralityAVGAtomicPart[t][n].addAndGet(sc.rankCentrality[n]);
            rankCentralitySTDAtomicPart[t][n].addAndGet(pow(sc.rankCentrality[n], 2));
          }
        }
        sc.stepForward();
      }
    }

  }

}
