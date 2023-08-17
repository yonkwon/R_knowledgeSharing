package KSDyadic;

import static org.apache.commons.math3.util.FastMath.pow;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Computation {
    AtomicDouble[][][] knowledgeAVGAtomic;
    AtomicDouble[][][] knowledgeSTDAtomic;

    AtomicDouble[][][] knowledgeBestAVGAtomic;
    AtomicDouble[][][] knowledgeBestSTDAtomic;
    AtomicDouble[][][] knowledgeBestSourceDiversityAVGAtomic;
    AtomicDouble[][][] knowledgeBestSourceDiversitySTDAtomic;

    AtomicDouble[][][] knowledgeMinMaxAVGAtomic;
    AtomicDouble[][][] knowledgeMinMaxSTDAtomic;

    AtomicDouble[][][] beliefDiversityAVGAtomic;
    AtomicDouble[][][] beliefDiversitySTDAtomic;
    AtomicDouble[][][] beliefSourceDiversityAVGAtomic;
    AtomicDouble[][][] beliefSourceDiversitySTDAtomic;

    AtomicDouble[][][] centralizationAVGAtomic;
    AtomicDouble[][][] centralizationSTDAtomic;

    AtomicDouble[][][][] rankKnowledgeAVGAtomic;
    AtomicDouble[][][][] rankKnowledgeSTDAtomic;
    AtomicDouble[][][][] rankContributionAVGAtomic;
    AtomicDouble[][][][] rankContributionSTDAtomic;
    AtomicDouble[][][][] rankContributionPositiveAVGAtomic;
    AtomicDouble[][][][] rankContributionPositiveSTDAtomic;
    AtomicDouble[][][][] rankContributionNegativeAVGAtomic;
    AtomicDouble[][][][] rankContributionNegativeSTDAtomic;

    AtomicDouble[][][][] rankContributionBestAVGAtomic;
    AtomicDouble[][][][] rankContributionBestSTDAtomic;
    AtomicDouble[][][][] rankContributionBestPositiveAVGAtomic;
    AtomicDouble[][][][] rankContributionBestPositiveSTDAtomic;
    AtomicDouble[][][][] rankContributionBestNegativeAVGAtomic;
    AtomicDouble[][][][] rankContributionBestNegativeSTDAtomic;

    AtomicDouble[][][][] rankApplicationRateAVGAtomic;
    AtomicDouble[][][][] rankApplicationRateSTDAtomic;
    AtomicDouble[][][][] rankApplicationRatePositiveAVGAtomic;
    AtomicDouble[][][][] rankApplicationRatePositiveSTDAtomic;
    AtomicDouble[][][][] rankApplicationRateNegativeAVGAtomic;
    AtomicDouble[][][][] rankApplicationRateNegativeSTDAtomic;

    AtomicDouble[][][][] rankCentralityAVGAtomic;
    AtomicDouble[][][][] rankCentralitySTDAtomic;

    AtomicDouble[][][][] typeKnowledgeAVGAtomic;
    AtomicDouble[][][][] typeKnowledgeSTDAtomic;
    AtomicDouble[][][][] typeContributionAVGAtomic;
    AtomicDouble[][][][] typeContributionSTDAtomic;
    AtomicDouble[][][][] typeContributionPositiveAVGAtomic;
    AtomicDouble[][][][] typeContributionPositiveSTDAtomic;
    
    //In double arrays
    double[][][] knowledgeAVG;
    double[][][] knowledgeSTD;

    double[][][] knowledgeBestAVG;
    double[][][] knowledgeBestSTD;
    double[][][] knowledgeBestSourceDiversityAVG;
    double[][][] knowledgeBestSourceDiversitySTD;

    double[][][] knowledgeMinMaxAVG;
    double[][][] knowledgeMinMaxSTD;

    double[][][] beliefDiversityAVG;
    double[][][] beliefDiversitySTD;
    double[][][] beliefSourceDiversityAVG;
    double[][][] beliefSourceDiversitySTD;

    double[][][] centralizationAVG;
    double[][][] centralizationSTD;

    double[][][][] rankKnowledgeAVG;
    double[][][][] rankKnowledgeSTD;
    double[][][][] rankContributionAVG;
    double[][][][] rankContributionSTD;
    double[][][][] rankContributionPositiveAVG;
    double[][][][] rankContributionPositiveSTD;
    double[][][][] rankContributionNegativeAVG;
    double[][][][] rankContributionNegativeSTD;

    double[][][][] rankContributionBestAVG;
    double[][][][] rankContributionBestSTD;
    double[][][][] rankContributionBestPositiveAVG;
    double[][][][] rankContributionBestPositiveSTD;
    double[][][][] rankContributionBestNegativeAVG;
    double[][][][] rankContributionBestNegativeSTD;

    double[][][][] rankApplicationRateAVG;
    double[][][][] rankApplicationRateSTD;
    double[][][][] rankApplicationRatePositiveAVG;
    double[][][][] rankApplicationRatePositiveSTD;
    double[][][][] rankApplicationRateNegativeAVG;
    double[][][][] rankApplicationRateNegativeSTD;

    double[][][][] rankCentralityAVG;
    double[][][][] rankCentralitySTD;

    double[][][][] typeKnowledgeAVG;
    double[][][][] typeKnowledgeSTD;
    double[][][][] typeContributionAVG;
    double[][][][] typeContributionSTD;
    double[][][][] typeContributionPositiveAVG;
    double[][][][] typeContributionPositiveSTD;
    
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
        knowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestSourceDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeMinMaxAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeMinMaxSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefSourceDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        centralizationAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        centralizationSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        rankKnowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankKnowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRatePositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRatePositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankCentralityAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankCentralitySTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        typeKnowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][2];
        typeKnowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][2];
        typeContributionAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
        typeContributionSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
        typeContributionPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
        typeContributionPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];

        knowledgeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestSourceDiversityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeBestSourceDiversitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeMinMaxAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        knowledgeMinMaxSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefDiversityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefDiversitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefSourceDiversityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        beliefSourceDiversitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        centralizationAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        centralizationSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME];
        rankKnowledgeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankKnowledgeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionPositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionPositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionNegativeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionNegativeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestPositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestPositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestNegativeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankContributionBestNegativeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRatePositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRatePositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateNegativeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankApplicationRateNegativeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankCentralityAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        rankCentralitySTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][Main.N];
        typeKnowledgeAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][2];
        typeKnowledgeSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][2];
        typeContributionAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
        typeContributionSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
        typeContributionPositiveAVG = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
        typeContributionPositiveSTD = new double[Main.LENGTH_BETA][Main.LENGTH_P_SHARING][Main.TIME][4];
    }

    private void setAtomic() {
        for (int b = 0; b < Main.LENGTH_BETA; b++) {
            for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
                for (int t = 0; t < Main.TIME; t++) {
                    knowledgeAVGAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeSTDAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeBestAVGAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeBestSTDAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeBestSourceDiversityAVGAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeBestSourceDiversitySTDAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeMinMaxAVGAtomic[b][ps][t] = new AtomicDouble();
                    knowledgeMinMaxSTDAtomic[b][ps][t] = new AtomicDouble();
                    beliefDiversityAVGAtomic[b][ps][t] = new AtomicDouble();
                    beliefDiversitySTDAtomic[b][ps][t] = new AtomicDouble();
                    beliefSourceDiversityAVGAtomic[b][ps][t] = new AtomicDouble();
                    beliefSourceDiversitySTDAtomic[b][ps][t] = new AtomicDouble();
                    centralizationAVGAtomic[b][ps][t] = new AtomicDouble();
                    centralizationSTDAtomic[b][ps][t] = new AtomicDouble();
                    for (int n = 0; n < Main.N; n++) {
                        rankKnowledgeAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankKnowledgeSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionPositiveAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionPositiveSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionNegativeAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionNegativeSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionBestAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionBestSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionBestPositiveAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionBestPositiveSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionBestNegativeAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankContributionBestNegativeSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankApplicationRateAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankApplicationRateSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankApplicationRatePositiveAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankApplicationRatePositiveSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankApplicationRateNegativeAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankApplicationRateNegativeSTDAtomic[b][ps][t][n] = new AtomicDouble();
                        rankCentralityAVGAtomic[b][ps][t][n] = new AtomicDouble();
                        rankCentralitySTDAtomic[b][ps][t][n] = new AtomicDouble();
                    }
                    typeKnowledgeAVGAtomic[b][ps][t][0] = new AtomicDouble();
                    typeKnowledgeSTDAtomic[b][ps][t][0] = new AtomicDouble();
                    typeKnowledgeAVGAtomic[b][ps][t][1] = new AtomicDouble();
                    typeKnowledgeSTDAtomic[b][ps][t][1] = new AtomicDouble();
                    for( int t2t = 0; t2t < 4; t2t ++ ){
                        typeContributionAVGAtomic[b][ps][t][t2t] = new AtomicDouble();
                        typeContributionSTDAtomic[b][ps][t][t2t] = new AtomicDouble();
                        typeContributionPositiveAVGAtomic[b][ps][t][t2t] = new AtomicDouble();
                        typeContributionPositiveSTDAtomic[b][ps][t][t2t] = new AtomicDouble();
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
            for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
                for (int t = 0; t < Main.TIME; t++) {
                    knowledgeAVG[b][ps][t] = knowledgeAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeSTD[b][ps][t] = knowledgeSTDAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeSTD[b][ps][t] = pow(knowledgeSTD[b][ps][t] - pow(knowledgeAVG[b][ps][t], 2), .5);
                    knowledgeBestAVG[b][ps][t] = knowledgeBestAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeBestSTD[b][ps][t] = knowledgeBestSTDAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeBestSTD[b][ps][t] = pow(knowledgeBestSTD[b][ps][t] - pow(knowledgeBestAVG[b][ps][t], 2), .5);
                    knowledgeBestSourceDiversityAVG[b][ps][t] = knowledgeBestSourceDiversityAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeBestSourceDiversitySTD[b][ps][t] = knowledgeBestSourceDiversitySTDAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeBestSourceDiversitySTD[b][ps][t] = pow(knowledgeBestSourceDiversitySTD[b][ps][t] - pow(knowledgeBestSourceDiversityAVG[b][ps][t], 2), .5);
                    knowledgeMinMaxAVG[b][ps][t] = knowledgeMinMaxAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeMinMaxSTD[b][ps][t] = knowledgeMinMaxSTDAtomic[b][ps][t].get() / Main.ITERATION;
                    knowledgeMinMaxSTD[b][ps][t] = pow(knowledgeMinMaxSTD[b][ps][t] - pow(knowledgeMinMaxAVG[b][ps][t], 2), .5);
                    beliefDiversityAVG[b][ps][t] = beliefDiversityAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    beliefDiversitySTD[b][ps][t] = beliefDiversitySTDAtomic[b][ps][t].get() / Main.ITERATION;
                    beliefDiversitySTD[b][ps][t] = pow(beliefDiversitySTD[b][ps][t] - pow(beliefDiversityAVG[b][ps][t], 2), .5);
                    beliefSourceDiversityAVG[b][ps][t] = beliefSourceDiversityAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    beliefSourceDiversitySTD[b][ps][t] = beliefSourceDiversitySTDAtomic[b][ps][t].get() / Main.ITERATION;
                    beliefSourceDiversitySTD[b][ps][t] = pow(beliefSourceDiversitySTD[b][ps][t] - pow(beliefSourceDiversityAVG[b][ps][t], 2), .5);
                    centralizationAVG[b][ps][t] = centralizationAVGAtomic[b][ps][t].get() / Main.ITERATION;
                    centralizationSTD[b][ps][t] = centralizationSTDAtomic[b][ps][t].get() / Main.ITERATION;
                    centralizationSTD[b][ps][t] = pow(centralizationSTD[b][ps][t] - pow(centralizationAVG[b][ps][t], 2), .5);

                    for (int n = 0; n < Main.N; n++) {
                        rankKnowledgeAVG[b][ps][t][n] = rankKnowledgeAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankKnowledgeSTD[b][ps][t][n] = rankKnowledgeSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankKnowledgeSTD[b][ps][t][n] = pow(rankKnowledgeSTD[b][ps][t][n] - pow(rankKnowledgeAVG[b][ps][t][n], 2), .5);
                        rankContributionAVG[b][ps][t][n] = rankContributionAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionSTD[b][ps][t][n] = rankContributionSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionSTD[b][ps][t][n] = pow(rankContributionSTD[b][ps][t][n] - pow(rankContributionAVG[b][ps][t][n], 2), .5);
                        rankContributionPositiveAVG[b][ps][t][n] = rankContributionPositiveAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionPositiveSTD[b][ps][t][n] = rankContributionPositiveSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionPositiveSTD[b][ps][t][n] = pow(rankContributionPositiveSTD[b][ps][t][n] - pow(rankContributionPositiveAVG[b][ps][t][n], 2), .5);
                        rankContributionNegativeAVG[b][ps][t][n] = rankContributionNegativeAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionNegativeSTD[b][ps][t][n] = rankContributionNegativeSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionNegativeSTD[b][ps][t][n] = pow(rankContributionNegativeSTD[b][ps][t][n] - pow(rankContributionNegativeAVG[b][ps][t][n], 2), .5);
                        rankContributionBestAVG[b][ps][t][n] = rankContributionBestAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionBestSTD[b][ps][t][n] = rankContributionBestSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionBestSTD[b][ps][t][n] = pow(rankContributionBestSTD[b][ps][t][n] - pow(rankContributionBestAVG[b][ps][t][n], 2), .5);
                        rankContributionBestPositiveAVG[b][ps][t][n] = rankContributionBestPositiveAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionBestPositiveSTD[b][ps][t][n] = rankContributionBestPositiveSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionBestPositiveSTD[b][ps][t][n] = pow(rankContributionBestPositiveSTD[b][ps][t][n] - pow(rankContributionBestPositiveAVG[b][ps][t][n], 2), .5);
                        rankContributionBestNegativeAVG[b][ps][t][n] = rankContributionBestNegativeAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionBestNegativeSTD[b][ps][t][n] = rankContributionBestNegativeSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankContributionBestNegativeSTD[b][ps][t][n] = pow(rankContributionBestNegativeSTD[b][ps][t][n] - pow(rankContributionBestNegativeAVG[b][ps][t][n], 2), .5);
                        rankApplicationRateAVG[b][ps][t][n] = rankApplicationRateAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankApplicationRateSTD[b][ps][t][n] = rankApplicationRateSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankApplicationRateSTD[b][ps][t][n] = pow(rankApplicationRateSTD[b][ps][t][n] - pow(rankApplicationRateAVG[b][ps][t][n], 2), .5);
                        rankApplicationRatePositiveAVG[b][ps][t][n] = rankApplicationRatePositiveAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankApplicationRatePositiveSTD[b][ps][t][n] = rankApplicationRatePositiveSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankApplicationRatePositiveSTD[b][ps][t][n] = pow(rankApplicationRatePositiveSTD[b][ps][t][n] - pow(rankApplicationRatePositiveAVG[b][ps][t][n], 2), .5);
                        rankApplicationRateNegativeAVG[b][ps][t][n] = rankApplicationRateNegativeAVGAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankApplicationRateNegativeSTD[b][ps][t][n] = rankApplicationRateNegativeSTDAtomic[b][ps][t][n].get() / Main.ITERATION;
                        rankApplicationRateNegativeSTD[b][ps][t][n] = pow(rankApplicationRateNegativeSTD[b][ps][t][n] - pow(rankApplicationRateNegativeAVG[b][ps][t][n], 2), .5);
                    }
                    
                    if( Main.IS_RATIO ){
                        typeKnowledgeAVG[b][ps][t][0] = typeKnowledgeAVGAtomic[b][ps][t][0].get() / Main.ITERATION;
                        typeKnowledgeSTD[b][ps][t][0] = typeKnowledgeSTDAtomic[b][ps][t][0].get() / Main.ITERATION;
                        typeKnowledgeSTD[b][ps][t][0] = pow(typeKnowledgeSTD[b][ps][t][0] - pow(typeKnowledgeAVG[b][ps][t][0], 2), .5);
                        typeKnowledgeAVG[b][ps][t][1] = typeKnowledgeAVGAtomic[b][ps][t][1].get() / Main.ITERATION;
                        typeKnowledgeSTD[b][ps][t][1] = typeKnowledgeSTDAtomic[b][ps][t][1].get() / Main.ITERATION;
                        typeKnowledgeSTD[b][ps][t][1] = pow(typeKnowledgeSTD[b][ps][t][1] - pow(typeKnowledgeAVG[b][ps][t][1], 2), .5);
                        for( int t2t = 0; t2t < 4; t2t ++ ){
                            typeContributionAVG[b][ps][t][t2t] = typeContributionAVGAtomic[b][ps][t][t2t].get() / Main.ITERATION;
                            typeContributionSTD[b][ps][t][t2t] = typeContributionSTDAtomic[b][ps][t][t2t].get() / Main.ITERATION;
                            typeContributionSTD[b][ps][t][t2t] = pow(typeContributionSTD[b][ps][t][t2t] - pow(typeContributionAVG[b][ps][t][t2t], 2), .5);
                            typeContributionPositiveAVG[b][ps][t][t2t] = typeContributionPositiveAVGAtomic[b][ps][t][t2t].get() / Main.ITERATION;
                            typeContributionPositiveSTD[b][ps][t][t2t] = typeContributionPositiveSTDAtomic[b][ps][t][t2t].get() / Main.ITERATION;
                            typeContributionPositiveSTD[b][ps][t][t2t] = pow(typeContributionPositiveSTD[b][ps][t][t2t] - pow(typeContributionPositiveAVG[b][ps][t][t2t], 2), .5);
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
                for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
                    new SingleRun(b, ps);
                }
            }
            pb.stepNext();
        }

    }

    class SingleRun {
        int betaIndex;
        int pSharingIndex;
        double beta;
        double pGiving;

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

        SingleRun(int betaIndex, int pSharingIndex) {
            this.betaIndex = betaIndex;
            this.pSharingIndex = pSharingIndex;
            beta = Main.BETA[betaIndex];
            pGiving = Main.P_SHARING[pSharingIndex];
            initializeResultSpace();
            run();
        }

        void initializeResultSpace() {
            knowledgeAVGAtomicPart = knowledgeAVGAtomic[betaIndex][pSharingIndex];
            knowledgeSTDAtomicPart = knowledgeSTDAtomic[betaIndex][pSharingIndex];
            knowledgeBestAVGAtomicPart = knowledgeBestAVGAtomic[betaIndex][pSharingIndex];
            knowledgeBestSTDAtomicPart = knowledgeBestSTDAtomic[betaIndex][pSharingIndex];
            knowledgeBestSourceDiversityAVGAtomicPart = knowledgeBestSourceDiversityAVGAtomic[betaIndex][pSharingIndex];
            knowledgeBestSourceDiversitySTDAtomicPart = knowledgeBestSourceDiversitySTDAtomic[betaIndex][pSharingIndex];
            knowledgeMinMaxAVGAtomicPart = knowledgeMinMaxAVGAtomic[betaIndex][pSharingIndex];
            knowledgeMinMaxSTDAtomicPart = knowledgeMinMaxSTDAtomic[betaIndex][pSharingIndex];
            beliefDiversityAVGAtomicPart = beliefDiversityAVGAtomic[betaIndex][pSharingIndex];
            beliefDiversitySTDAtomicPart = beliefDiversitySTDAtomic[betaIndex][pSharingIndex];
            beliefSourceDiversityAVGAtomicPart = beliefSourceDiversityAVGAtomic[betaIndex][pSharingIndex];
            beliefSourceDiversitySTDAtomicPart = beliefSourceDiversitySTDAtomic[betaIndex][pSharingIndex];
            centralizationAVGAtomicPart = centralizationAVGAtomic[betaIndex][pSharingIndex];
            centralizationSTDAtomicPart = centralizationSTDAtomic[betaIndex][pSharingIndex];

            rankKnowledgeAVGAtomicPart = rankKnowledgeAVGAtomic[betaIndex][pSharingIndex];
            rankKnowledgeSTDAtomicPart = rankKnowledgeSTDAtomic[betaIndex][pSharingIndex];
            rankContributionAVGAtomicPart = rankContributionAVGAtomic[betaIndex][pSharingIndex];
            rankContributionSTDAtomicPart = rankContributionSTDAtomic[betaIndex][pSharingIndex];
            rankContributionPositiveAVGAtomicPart = rankContributionPositiveAVGAtomic[betaIndex][pSharingIndex];
            rankContributionPositiveSTDAtomicPart = rankContributionPositiveSTDAtomic[betaIndex][pSharingIndex];
            rankContributionNegativeAVGAtomicPart = rankContributionNegativeAVGAtomic[betaIndex][pSharingIndex];
            rankContributionNegativeSTDAtomicPart = rankContributionNegativeSTDAtomic[betaIndex][pSharingIndex];
            rankContributionBestAVGAtomicPart = rankContributionBestAVGAtomic[betaIndex][pSharingIndex];
            rankContributionBestSTDAtomicPart = rankContributionBestSTDAtomic[betaIndex][pSharingIndex];
            rankContributionBestPositiveAVGAtomicPart = rankContributionBestPositiveAVGAtomic[betaIndex][pSharingIndex];
            rankContributionBestPositiveSTDAtomicPart = rankContributionBestPositiveSTDAtomic[betaIndex][pSharingIndex];
            rankContributionBestNegativeAVGAtomicPart = rankContributionBestNegativeAVGAtomic[betaIndex][pSharingIndex];
            rankContributionBestNegativeSTDAtomicPart = rankContributionBestNegativeSTDAtomic[betaIndex][pSharingIndex];
            rankApplicationRateAVGAtomicPart = rankApplicationRateAVGAtomic[betaIndex][pSharingIndex];
            rankApplicationRateSTDAtomicPart = rankApplicationRateSTDAtomic[betaIndex][pSharingIndex];
            rankApplicationRatePositiveAVGAtomicPart = rankApplicationRatePositiveAVGAtomic[betaIndex][pSharingIndex];
            rankApplicationRatePositiveSTDAtomicPart = rankApplicationRatePositiveSTDAtomic[betaIndex][pSharingIndex];
            rankApplicationRateNegativeAVGAtomicPart = rankApplicationRateNegativeAVGAtomic[betaIndex][pSharingIndex];
            rankApplicationRateNegativeSTDAtomicPart = rankApplicationRateNegativeSTDAtomic[betaIndex][pSharingIndex];
            rankCentralityAVGAtomicPart = rankCentralityAVGAtomic[betaIndex][pSharingIndex];
            rankCentralitySTDAtomicPart = rankCentralitySTDAtomic[betaIndex][pSharingIndex];

            typeKnowledgeAVGAtomicPart = typeKnowledgeAVGAtomic[betaIndex][pSharingIndex];
            typeKnowledgeSTDAtomicPart = typeKnowledgeSTDAtomic[betaIndex][pSharingIndex];
            typeContributionAVGAtomicPart = typeContributionAVGAtomic[betaIndex][pSharingIndex];
            typeContributionSTDAtomicPart = typeContributionSTDAtomic[betaIndex][pSharingIndex];
            typeContributionPositiveAVGAtomicPart = typeContributionPositiveAVGAtomic[betaIndex][pSharingIndex];
            typeContributionPositiveSTDAtomicPart = typeContributionPositiveSTDAtomic[betaIndex][pSharingIndex];
        }

        void run() {
            Scenario sc = new Scenario(beta, pGiving);
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
                    if( Main.IS_RATIO ){
                        typeKnowledgeAVGAtomicPart[t][0].addAndGet(sc.typeKnowledgeAvg[0]);
                        typeKnowledgeSTDAtomicPart[t][0].addAndGet(pow(sc.typeKnowledgeAvg[0], 2));
                        typeKnowledgeAVGAtomicPart[t][1].addAndGet(sc.typeKnowledgeAvg[1]);
                        typeKnowledgeSTDAtomicPart[t][1].addAndGet(pow(sc.typeKnowledgeAvg[1], 2));
                        for( int t2t = 0; t2t < 4; t2t ++ ){
                            typeContributionAVGAtomicPart[t][t2t].addAndGet(sc.typeContribution[t2t]);
                            typeContributionSTDAtomicPart[t][t2t].addAndGet(pow(sc.typeContribution[t2t], 2));
                            typeContributionPositiveAVGAtomicPart[t][t2t].addAndGet(sc.typeContributionPositive[t2t]);
                            typeContributionPositiveSTDAtomicPart[t][t2t].addAndGet(pow(sc.typeContributionPositive[t2t], 2));
                        }
                    }
                }
                sc.stepForward();
            }
        }

    }

}
