package KSProbabilistic;

import static org.apache.commons.math3.util.FastMath.pow;

import com.google.common.util.concurrent.AtomicDouble;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Computation {
  
  AtomicDouble[][][][] knowledgeAVGAtomic;
  AtomicDouble[][][][] knowledgeSTDAtomic;
  
  AtomicDouble[][][][] knowledgeBestAVGAtomic;
  AtomicDouble[][][][] knowledgeBestSTDAtomic;
  AtomicDouble[][][][] knowledgeBestSourceDiversityAVGAtomic;
  AtomicDouble[][][][] knowledgeBestSourceDiversitySTDAtomic;
  
  AtomicDouble[][][][] knowledgeMinMaxAVGAtomic;
  AtomicDouble[][][][] knowledgeMinMaxSTDAtomic;
  
  AtomicDouble[][][][] beliefDiversityAVGAtomic;
  AtomicDouble[][][][] beliefDiversitySTDAtomic;
  AtomicDouble[][][][] beliefSourceDiversityAVGAtomic;
  AtomicDouble[][][][] beliefSourceDiversitySTDAtomic;
  
  AtomicDouble[][][][] centralizationAVGAtomic;
  AtomicDouble[][][][] centralizationSTDAtomic;
  AtomicDouble[][][][] efficiencyAVGAtomic;
  AtomicDouble[][][][] efficiencySTDAtomic;
  
  AtomicDouble[][][] optimalBetaAVGAtomic;
  AtomicDouble[][][] optimalBetaSTDAtomic;
  
  AtomicDouble[][][][][] rankContributionAVGAtomic;
  AtomicDouble[][][][][] rankContributionSTDAtomic;
  AtomicDouble[][][][][] rankContributionPositiveAVGAtomic;
  AtomicDouble[][][][][] rankContributionPositiveSTDAtomic;
  AtomicDouble[][][][][] rankContributionNegativeAVGAtomic;
  AtomicDouble[][][][][] rankContributionNegativeSTDAtomic;
  
  //In double arrays
  double[][][][] knowledgeAVG;
  double[][][][] knowledgeSTD;
  
  double[][][][] knowledgeBestAVG;
  double[][][][] knowledgeBestSTD;
  double[][][][] knowledgeBestSourceDiversityAVG;
  double[][][][] knowledgeBestSourceDiversitySTD;
  
  double[][][][] knowledgeMinMaxAVG;
  double[][][][] knowledgeMinMaxSTD;
  
  double[][][][] beliefDiversityAVG;
  double[][][][] beliefDiversitySTD;
  double[][][][] beliefSourceDiversityAVG;
  double[][][][] beliefSourceDiversitySTD;
  
  double[][][][] centralizationAVG;
  double[][][][] centralizationSTD;
  double[][][][] efficiencyAVG;
  double[][][][] efficiencySTD;
  
  double[][][] optimalBetaAVG;
  double[][][] optimalBetaSTD;
  
  double[][][][][] rankContributionAVG;
  double[][][][][] rankContributionSTD;
  double[][][][][] rankContributionPositiveAVG;
  double[][][][][] rankContributionPositiveSTD;
  double[][][][][] rankContributionNegativeAVG;
  double[][][][][] rankContributionNegativeSTD;
  
  ProgressBar pb;
  
  Computation() {
  }
  
  public void printNetwork() {
    DecimalFormat df = new DecimalFormat("0.00");
    
    try {
      Files.createDirectories(Paths.get(Main.PATH_CSV));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
      for (int b = 0; b < Main.LENGTH_BETA; b++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          double beta = Main.BETA[b];
          double pSharing = Main.P_SHARING[ps];
          String ntString = null;
          switch (nt) {
            case 0 -> ntString = "RanTree";
            case 1 -> ntString = "Cavemen";
            case 2 -> ntString = "PrefAtt";
          }
          String params
          = "_" + ntString
            + "_beta" + df.format(beta)
            + "_psha" + df.format(pSharing)
            + "_t" + Main.TIME;
          Scenario src = new Scenario(nt, beta, pSharing);
          for (int t = 0; t < Main.TIME; t++) {
            src.stepForward();
          }
          src.printCSV(Main.PATH_CSV + Main.RUN_ID + params);
          System.out.println("Network Printed: " + Main.RUN_ID + params);
        }
      }
    }
  }
  
  public void doExperiment() {
    pb = new ProgressBar(Main.ITERATION);
    setSpace();
    setAtomic();
    runFullExperiment();
    averageFullExperiment();
  }
  
  private void setSpace() {
    knowledgeAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversitySTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencyAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencySTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    optimalBetaAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    rankContributionAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSTDAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    
    knowledgeAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversityAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversitySTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversityAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversitySTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversityAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversitySTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencyAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencySTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    optimalBetaAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    rankContributionAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSTD = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
  }
  
  private void setAtomic() {
    for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
      for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
        for (int t = 0; t < Main.TIME; t++) {
          optimalBetaAVGAtomic[nt][ps][t] = new AtomicDouble();
          optimalBetaSTDAtomic[nt][ps][t] = new AtomicDouble();
          for (int b = 0; b < Main.LENGTH_BETA; b++) {
            knowledgeAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeSTDAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestSTDAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestSourceDiversityAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestSourceDiversitySTDAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeMinMaxAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeMinMaxSTDAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefDiversityAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefDiversitySTDAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefSourceDiversityAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefSourceDiversitySTDAtomic[nt][ps][b][t] = new AtomicDouble();
            centralizationAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            centralizationSTDAtomic[nt][ps][b][t] = new AtomicDouble();
            efficiencyAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            efficiencySTDAtomic[nt][ps][b][t] = new AtomicDouble();
            for( int n = 0; n < Main.N; n++ ){
              rankContributionAVGAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionSTDAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionPositiveAVGAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionPositiveSTDAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionNegativeAVGAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionNegativeSTDAtomic[nt][ps][b][t][n] = new AtomicDouble();
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
    for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
      for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
        for (int t = 0; t < Main.TIME; t++) {
          optimalBetaAVG[nt][ps][t] = optimalBetaAVGAtomic[nt][ps][t].get() / Main.ITERATION;
          optimalBetaSTD[nt][ps][t] = optimalBetaSTDAtomic[nt][ps][t].get() / Main.ITERATION;
          optimalBetaSTD[nt][ps][t] = pow(optimalBetaSTD[nt][ps][t] - pow(optimalBetaAVG[nt][ps][t], 2), .5);
          for (int b = 0; b < Main.LENGTH_BETA; b++) {
            knowledgeAVG[nt][ps][b][t] = knowledgeAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeSTD[nt][ps][b][t] = knowledgeSTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeSTD[nt][ps][b][t] = pow(knowledgeSTD[nt][ps][b][t] - pow(knowledgeAVG[nt][ps][b][t], 2), .5);
            knowledgeBestAVG[nt][ps][b][t] = knowledgeBestAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeBestSTD[nt][ps][b][t] = knowledgeBestSTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeBestSTD[nt][ps][b][t] = pow(knowledgeBestSTD[nt][ps][b][t] - pow(knowledgeBestAVG[nt][ps][b][t], 2), .5);
            knowledgeBestSourceDiversityAVG[nt][ps][b][t] = knowledgeBestSourceDiversityAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeBestSourceDiversitySTD[nt][ps][b][t] = knowledgeBestSourceDiversitySTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeBestSourceDiversitySTD[nt][ps][b][t] = pow(knowledgeBestSourceDiversitySTD[nt][ps][b][t] - pow(knowledgeBestSourceDiversityAVG[nt][ps][b][t], 2), .5);
            knowledgeMinMaxAVG[nt][ps][b][t] = knowledgeMinMaxAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeMinMaxSTD[nt][ps][b][t] = knowledgeMinMaxSTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeMinMaxSTD[nt][ps][b][t] = pow(knowledgeMinMaxSTD[nt][ps][b][t] - pow(knowledgeMinMaxAVG[nt][ps][b][t], 2), .5);
            beliefDiversityAVG[nt][ps][b][t] = beliefDiversityAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            beliefDiversitySTD[nt][ps][b][t] = beliefDiversitySTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            beliefDiversitySTD[nt][ps][b][t] = pow(beliefDiversitySTD[nt][ps][b][t] - pow(beliefDiversityAVG[nt][ps][b][t], 2), .5);
            beliefSourceDiversityAVG[nt][ps][b][t] = beliefSourceDiversityAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            beliefSourceDiversitySTD[nt][ps][b][t] = beliefSourceDiversitySTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            beliefSourceDiversitySTD[nt][ps][b][t] = pow(beliefSourceDiversitySTD[nt][ps][b][t] - pow(beliefSourceDiversityAVG[nt][ps][b][t], 2), .5);
            centralizationAVG[nt][ps][b][t] = centralizationAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            centralizationSTD[nt][ps][b][t] = centralizationSTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            centralizationSTD[nt][ps][b][t] = pow(centralizationSTD[nt][ps][b][t] - pow(centralizationAVG[nt][ps][b][t], 2), .5);
            efficiencyAVG[nt][ps][b][t] = efficiencyAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            efficiencySTD[nt][ps][b][t] = efficiencySTDAtomic[nt][ps][b][t].get() / Main.ITERATION;
            efficiencySTD[nt][ps][b][t] = pow(efficiencySTD[nt][ps][b][t] - pow(efficiencyAVG[nt][ps][b][t], 2), .5);
            for( int n = 0; n < Main.N; n ++ ){
              rankContributionAVG[nt][ps][b][t][n] = rankContributionAVGAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionSTD[nt][ps][b][t][n] = rankContributionSTDAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionSTD[nt][ps][b][t][n] = pow(rankContributionSTD[nt][ps][b][t][n] - pow(rankContributionAVG[nt][ps][b][t][n], 2), .5);
              rankContributionPositiveSTD[nt][ps][b][t][n] = rankContributionPositiveSTDAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionPositiveAVG[nt][ps][b][t][n] = rankContributionPositiveAVGAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionPositiveSTD[nt][ps][b][t][n] = pow(rankContributionPositiveSTD[nt][ps][b][t][n] - pow(rankContributionPositiveAVG[nt][ps][b][t][n], 2), .5);
              rankContributionNegativeAVG[nt][ps][b][t][n] = rankContributionNegativeAVGAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionNegativeSTD[nt][ps][b][t][n] = rankContributionNegativeSTDAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionNegativeSTD[nt][ps][b][t][n] = pow(rankContributionNegativeSTD[nt][ps][b][t][n] - pow(rankContributionNegativeAVG[nt][ps][b][t][n], 2), .5);
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
      for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          new SingleRun(nt, ps);
        }
      }
      pb.stepNext();
    }
    
  }
  
  class SingleRun {
    
    int networkType;
    int pSharingIndex;
    double pSharing;
    
    AtomicDouble[][] knowledgeAVGAtomicPart;
    AtomicDouble[][] knowledgeSTDAtomicPart;
    AtomicDouble[][] knowledgeBestAVGAtomicPart;
    AtomicDouble[][] knowledgeBestSTDAtomicPart;
    AtomicDouble[][] knowledgeBestSourceDiversityAVGAtomicPart;
    AtomicDouble[][] knowledgeBestSourceDiversitySTDAtomicPart;
    AtomicDouble[][] knowledgeMinMaxAVGAtomicPart;
    AtomicDouble[][] knowledgeMinMaxSTDAtomicPart;
    AtomicDouble[][] beliefDiversityAVGAtomicPart;
    AtomicDouble[][] beliefDiversitySTDAtomicPart;
    AtomicDouble[][] beliefSourceDiversityAVGAtomicPart;
    AtomicDouble[][] beliefSourceDiversitySTDAtomicPart;
    AtomicDouble[][] centralizationAVGAtomicPart;
    AtomicDouble[][] centralizationSTDAtomicPart;
    AtomicDouble[][] efficiencyAVGAtomicPart;
    AtomicDouble[][] efficiencySTDAtomicPart;
    AtomicDouble[] optimalBetaAVGAtomicPart;
    AtomicDouble[] optimalBetaSTDAtomicPart;
    AtomicDouble[][][] rankContributionAVGAtomicPart;
    AtomicDouble[][][] rankContributionSTDAtomicPart;
    AtomicDouble[][][] rankContributionPositiveAVGAtomicPart;
    AtomicDouble[][][] rankContributionPositiveSTDAtomicPart;
    AtomicDouble[][][] rankContributionNegativeAVGAtomicPart;
    AtomicDouble[][][] rankContributionNegativeSTDAtomicPart;
    
    SingleRun(int networkType, int pSharingIndex) {
      this.networkType = networkType;
      this.pSharingIndex = pSharingIndex;
      pSharing = Main.P_SHARING[pSharingIndex];
      initializeResultSpace();
      run();
    }
    
    void initializeResultSpace() {
      knowledgeAVGAtomicPart = knowledgeAVGAtomic[networkType][pSharingIndex];
      knowledgeSTDAtomicPart = knowledgeSTDAtomic[networkType][pSharingIndex];
      knowledgeBestAVGAtomicPart = knowledgeBestAVGAtomic[networkType][pSharingIndex];
      knowledgeBestSTDAtomicPart = knowledgeBestSTDAtomic[networkType][pSharingIndex];
      knowledgeBestSourceDiversityAVGAtomicPart = knowledgeBestSourceDiversityAVGAtomic[networkType][pSharingIndex];
      knowledgeBestSourceDiversitySTDAtomicPart = knowledgeBestSourceDiversitySTDAtomic[networkType][pSharingIndex];
      knowledgeMinMaxAVGAtomicPart = knowledgeMinMaxAVGAtomic[networkType][pSharingIndex];
      knowledgeMinMaxSTDAtomicPart = knowledgeMinMaxSTDAtomic[networkType][pSharingIndex];
      beliefDiversityAVGAtomicPart = beliefDiversityAVGAtomic[networkType][pSharingIndex];
      beliefDiversitySTDAtomicPart = beliefDiversitySTDAtomic[networkType][pSharingIndex];
      beliefSourceDiversityAVGAtomicPart = beliefSourceDiversityAVGAtomic[networkType][pSharingIndex];
      beliefSourceDiversitySTDAtomicPart = beliefSourceDiversitySTDAtomic[networkType][pSharingIndex];
      centralizationAVGAtomicPart = centralizationAVGAtomic[networkType][pSharingIndex];
      centralizationSTDAtomicPart = centralizationSTDAtomic[networkType][pSharingIndex];
      efficiencyAVGAtomicPart = efficiencyAVGAtomic[networkType][pSharingIndex];
      efficiencySTDAtomicPart = efficiencySTDAtomic[networkType][pSharingIndex];
      optimalBetaAVGAtomicPart = optimalBetaAVGAtomic[networkType][pSharingIndex];
      optimalBetaSTDAtomicPart = optimalBetaSTDAtomic[networkType][pSharingIndex];
      rankContributionAVGAtomicPart = rankContributionAVGAtomic[networkType][pSharingIndex];
      rankContributionSTDAtomicPart = rankContributionSTDAtomic[networkType][pSharingIndex];
      rankContributionPositiveAVGAtomicPart = rankContributionPositiveAVGAtomic[networkType][pSharingIndex];
      rankContributionPositiveSTDAtomicPart = rankContributionPositiveSTDAtomic[networkType][pSharingIndex];
      rankContributionNegativeAVGAtomicPart = rankContributionNegativeAVGAtomic[networkType][pSharingIndex];
      rankContributionNegativeSTDAtomicPart = rankContributionNegativeSTDAtomic[networkType][pSharingIndex];
    }
    
    void run() {
      Scenario[] scs = new Scenario[Main.LENGTH_BETA];
      for (int b = 0; b < Main.LENGTH_BETA; b++) {
        double beta = Main.BETA[b];
        for (; ; ) {
          scs[b] = new Scenario(networkType, beta, pSharing);
          if (scs[b].efficiency == Double.POSITIVE_INFINITY) {
            System.out.println("Broken network at " + networkType + " " + beta + " " + pSharing + " -> Resample");
            scs[b] = new Scenario(networkType, beta, pSharing);
          } else {
            break;
          }
        }
      }
      for (int t = 0; t < Main.TIME; t++) {
        //Recording first
        synchronized (this) {
          int maxKnowledgeAvgB = -1;
          double maxKnowledgeAvg = Double.MIN_VALUE;
          for (int b = 0; b < Main.LENGTH_BETA; b++) {
            Scenario sc = scs[b];
            if (sc.knowledgeAvg > maxKnowledgeAvg) {
              maxKnowledgeAvg = sc.knowledgeAvg;
              maxKnowledgeAvgB = b;
            }
            knowledgeAVGAtomicPart[b][t].addAndGet(sc.knowledgeAvg);
            knowledgeSTDAtomicPart[b][t].addAndGet(pow(sc.knowledgeAvg, 2));
            knowledgeBestAVGAtomicPart[b][t].addAndGet(sc.knowledgeBest);
            knowledgeBestSTDAtomicPart[b][t].addAndGet(pow(sc.knowledgeBest, 2));
            knowledgeBestSourceDiversityAVGAtomicPart[b][t].addAndGet(sc.knowledgeBestSourceDiversity);
            knowledgeBestSourceDiversitySTDAtomicPart[b][t].addAndGet(pow(sc.knowledgeBestSourceDiversity, 2));
            knowledgeMinMaxAVGAtomicPart[b][t].addAndGet(sc.knowledgeMinMax);
            knowledgeMinMaxSTDAtomicPart[b][t].addAndGet(pow(sc.knowledgeMinMax, 2));
            beliefDiversityAVGAtomicPart[b][t].addAndGet(sc.beliefDiversity);
            beliefDiversitySTDAtomicPart[b][t].addAndGet(pow(sc.beliefDiversity, 2));
            beliefSourceDiversityAVGAtomicPart[b][t].addAndGet(sc.beliefSourceDiversity);
            beliefSourceDiversitySTDAtomicPart[b][t].addAndGet(pow(sc.beliefSourceDiversity, 2));
            centralizationAVGAtomicPart[b][t].addAndGet(sc.centralization);
            centralizationSTDAtomicPart[b][t].addAndGet(pow(sc.centralization, 2));
            efficiencyAVGAtomicPart[b][t].addAndGet(sc.efficiency);
            efficiencySTDAtomicPart[b][t].addAndGet(pow(sc.efficiency, 2));
            for( int n = 0; n < Main.N; n ++ ){
              rankContributionAVGAtomicPart[b][t][n].addAndGet(sc.rank0Contribution[n]);
              rankContributionSTDAtomicPart[b][t][n].addAndGet(pow(sc.rank0Contribution[n], 2));
              rankContributionPositiveAVGAtomicPart[b][t][n].addAndGet(sc.rank0ContributionPositive[n]);
              rankContributionPositiveSTDAtomicPart[b][t][n].addAndGet(pow(sc.rank0ContributionPositive[n], 2));
              rankContributionNegativeAVGAtomicPart[b][t][n].addAndGet(sc.rank0ContributionNegative[n]);
              rankContributionNegativeSTDAtomicPart[b][t][n].addAndGet(pow(sc.rank0ContributionNegative[n], 2));
            }
            sc.stepForward();
          }
          optimalBetaAVGAtomicPart[t].addAndGet(Main.BETA[maxKnowledgeAvgB]);
          optimalBetaSTDAtomicPart[t].addAndGet(pow(Main.BETA[maxKnowledgeAvgB], 2));
        }
      }
    }
    
  }
  
}
