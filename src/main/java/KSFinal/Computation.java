package KSFinal;

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
  AtomicDouble[][][][] knowledgeSSQAtomic;
  
  AtomicDouble[][][][] knowledgeBestAVGAtomic;
  AtomicDouble[][][][] knowledgeBestSSQAtomic;
  AtomicDouble[][][][] knowledgeBestSourceDiversityAVGAtomic;
  AtomicDouble[][][][] knowledgeBestSourceDiversitySSQAtomic;
  
  AtomicDouble[][][][] knowledgeMinMaxAVGAtomic;
  AtomicDouble[][][][] knowledgeMinMaxSSQAtomic;
  
  AtomicDouble[][][][] beliefDiversityAVGAtomic;
  AtomicDouble[][][][] beliefDiversitySSQAtomic;
  AtomicDouble[][][][] beliefSourceDiversityAVGAtomic;
  AtomicDouble[][][][] beliefSourceDiversitySSQAtomic;
  
  AtomicDouble[][][][] centralizationAVGAtomic;
  AtomicDouble[][][][] centralizationSSQAtomic;
  AtomicDouble[][][][] efficiencyAVGAtomic;
  AtomicDouble[][][][] efficiencySSQAtomic;
  
  AtomicDouble[][][] optimalBetaAVGAtomic;
  AtomicDouble[][][] optimalBetaSSQAtomic;
  
  AtomicDouble[][][][][] rankContributionAVGAtomic;
  AtomicDouble[][][][][] rankContributionSSQAtomic;
  AtomicDouble[][][][][] rankContributionPositiveAVGAtomic;
  AtomicDouble[][][][][] rankContributionPositiveSSQAtomic;
  AtomicDouble[][][][][] rankContributionNegativeAVGAtomic;
  AtomicDouble[][][][][] rankContributionNegativeSSQAtomic;
  
  //In double arrays
  double[][][][] knowledgeAVG;
  double[][][][] knowledgeSSQ;
  
  double[][][][] knowledgeBestAVG;
  double[][][][] knowledgeBestSSQ;
  double[][][][] knowledgeBestSourceDiversityAVG;
  double[][][][] knowledgeBestSourceDiversitySSQ;
  
  double[][][][] knowledgeMinMaxAVG;
  double[][][][] knowledgeMinMaxSSQ;
  
  double[][][][] beliefDiversityAVG;
  double[][][][] beliefDiversitySSQ;
  double[][][][] beliefSourceDiversityAVG;
  double[][][][] beliefSourceDiversitySSQ;
  
  double[][][][] centralizationAVG;
  double[][][][] centralizationSSQ;
  double[][][][] efficiencyAVG;
  double[][][][] efficiencySSQ;
  
  double[][][] optimalBetaAVG;
  double[][][] optimalBetaSSQ;
  
  double[][][][][] rankContributionAVG;
  double[][][][][] rankContributionSSQ;
  double[][][][][] rankContributionPositiveAVG;
  double[][][][][] rankContributionPositiveSSQ;
  double[][][][][] rankContributionNegativeAVG;
  double[][][][][] rankContributionNegativeSSQ;
  
  ProgressBar pb;
  
  Computation() {
  }
  
  public void printNetwork() {
    DecimalFormat df = new DecimalFormat("0.00");
    
    try {
      Files.createDirectories(Main.PATH_CSV);
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
          src.printCSV(Main.PATH_CSV.resolve(  Main.RUN_ID + params).toString());
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
    knowledgeSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversitySSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversitySSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversityAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversitySSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencyAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencySSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    optimalBetaAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    rankContributionAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVGAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSSQAtomic = new AtomicDouble[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    
    knowledgeAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversityAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversitySSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversityAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversitySSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversityAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversitySSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencyAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    efficiencySSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    optimalBetaAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    rankContributionAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVG = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSSQ = new double[Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
  }
  
  private void setAtomic() {
    for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
      for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
        for (int t = 0; t < Main.TIME; t++) {
          optimalBetaAVGAtomic[nt][ps][t] = new AtomicDouble();
          optimalBetaSSQAtomic[nt][ps][t] = new AtomicDouble();
          for (int b = 0; b < Main.LENGTH_BETA; b++) {
            knowledgeAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeSSQAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestSSQAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestSourceDiversityAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeBestSourceDiversitySSQAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeMinMaxAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            knowledgeMinMaxSSQAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefDiversityAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefDiversitySSQAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefSourceDiversityAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            beliefSourceDiversitySSQAtomic[nt][ps][b][t] = new AtomicDouble();
            centralizationAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            centralizationSSQAtomic[nt][ps][b][t] = new AtomicDouble();
            efficiencyAVGAtomic[nt][ps][b][t] = new AtomicDouble();
            efficiencySSQAtomic[nt][ps][b][t] = new AtomicDouble();
            for( int n = 0; n < Main.N; n++ ){
              rankContributionAVGAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionSSQAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionPositiveAVGAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionPositiveSSQAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionNegativeAVGAtomic[nt][ps][b][t][n] = new AtomicDouble();
              rankContributionNegativeSSQAtomic[nt][ps][b][t][n] = new AtomicDouble();
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
          for (int b = 0; b < Main.LENGTH_BETA; b++) {
            knowledgeAVG[nt][ps][b][t] = knowledgeAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeBestAVG[nt][ps][b][t] = knowledgeBestAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeBestSourceDiversityAVG[nt][ps][b][t] = knowledgeBestSourceDiversityAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            knowledgeMinMaxAVG[nt][ps][b][t] = knowledgeMinMaxAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            beliefDiversityAVG[nt][ps][b][t] = beliefDiversityAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            beliefSourceDiversityAVG[nt][ps][b][t] = beliefSourceDiversityAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            centralizationAVG[nt][ps][b][t] = centralizationAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            efficiencyAVG[nt][ps][b][t] = efficiencyAVGAtomic[nt][ps][b][t].get() / Main.ITERATION;
            for( int n = 0; n < Main.N; n ++ ){
              rankContributionAVG[nt][ps][b][t][n] = rankContributionAVGAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionPositiveAVG[nt][ps][b][t][n] = rankContributionPositiveAVGAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
              rankContributionNegativeAVG[nt][ps][b][t][n] = rankContributionNegativeAVGAtomic[nt][ps][b][t][n].get() / Main.ITERATION;
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
    AtomicDouble[][] knowledgeSSQAtomicPart;
    AtomicDouble[][] knowledgeBestAVGAtomicPart;
    AtomicDouble[][] knowledgeBestSSQAtomicPart;
    AtomicDouble[][] knowledgeBestSourceDiversityAVGAtomicPart;
    AtomicDouble[][] knowledgeBestSourceDiversitySSQAtomicPart;
    AtomicDouble[][] knowledgeMinMaxAVGAtomicPart;
    AtomicDouble[][] knowledgeMinMaxSSQAtomicPart;
    AtomicDouble[][] beliefDiversityAVGAtomicPart;
    AtomicDouble[][] beliefDiversitySSQAtomicPart;
    AtomicDouble[][] beliefSourceDiversityAVGAtomicPart;
    AtomicDouble[][] beliefSourceDiversitySSQAtomicPart;
    AtomicDouble[][] centralizationAVGAtomicPart;
    AtomicDouble[][] centralizationSSQAtomicPart;
    AtomicDouble[][] efficiencyAVGAtomicPart;
    AtomicDouble[][] efficiencySSQAtomicPart;
    AtomicDouble[] optimalBetaAVGAtomicPart;
    AtomicDouble[] optimalBetaSSQAtomicPart;
    AtomicDouble[][][] rankContributionAVGAtomicPart;
    AtomicDouble[][][] rankContributionSSQAtomicPart;
    AtomicDouble[][][] rankContributionPositiveAVGAtomicPart;
    AtomicDouble[][][] rankContributionPositiveSSQAtomicPart;
    AtomicDouble[][][] rankContributionNegativeAVGAtomicPart;
    AtomicDouble[][][] rankContributionNegativeSSQAtomicPart;
    
    SingleRun(int networkType, int pSharingIndex) {
      this.networkType = networkType;
      this.pSharingIndex = pSharingIndex;
      pSharing = Main.P_SHARING[pSharingIndex];
      initializeResultSpace();
      run();
    }
    
    void initializeResultSpace() {
      knowledgeAVGAtomicPart = knowledgeAVGAtomic[networkType][pSharingIndex];
      knowledgeSSQAtomicPart = knowledgeSSQAtomic[networkType][pSharingIndex];
      knowledgeBestAVGAtomicPart = knowledgeBestAVGAtomic[networkType][pSharingIndex];
      knowledgeBestSSQAtomicPart = knowledgeBestSSQAtomic[networkType][pSharingIndex];
      knowledgeBestSourceDiversityAVGAtomicPart = knowledgeBestSourceDiversityAVGAtomic[networkType][pSharingIndex];
      knowledgeBestSourceDiversitySSQAtomicPart = knowledgeBestSourceDiversitySSQAtomic[networkType][pSharingIndex];
      knowledgeMinMaxAVGAtomicPart = knowledgeMinMaxAVGAtomic[networkType][pSharingIndex];
      knowledgeMinMaxSSQAtomicPart = knowledgeMinMaxSSQAtomic[networkType][pSharingIndex];
      beliefDiversityAVGAtomicPart = beliefDiversityAVGAtomic[networkType][pSharingIndex];
      beliefDiversitySSQAtomicPart = beliefDiversitySSQAtomic[networkType][pSharingIndex];
      beliefSourceDiversityAVGAtomicPart = beliefSourceDiversityAVGAtomic[networkType][pSharingIndex];
      beliefSourceDiversitySSQAtomicPart = beliefSourceDiversitySSQAtomic[networkType][pSharingIndex];
      centralizationAVGAtomicPart = centralizationAVGAtomic[networkType][pSharingIndex];
      centralizationSSQAtomicPart = centralizationSSQAtomic[networkType][pSharingIndex];
      efficiencyAVGAtomicPart = efficiencyAVGAtomic[networkType][pSharingIndex];
      efficiencySSQAtomicPart = efficiencySSQAtomic[networkType][pSharingIndex];
      optimalBetaAVGAtomicPart = optimalBetaAVGAtomic[networkType][pSharingIndex];
      optimalBetaSSQAtomicPart = optimalBetaSSQAtomic[networkType][pSharingIndex];
      rankContributionAVGAtomicPart = rankContributionAVGAtomic[networkType][pSharingIndex];
      rankContributionSSQAtomicPart = rankContributionSSQAtomic[networkType][pSharingIndex];
      rankContributionPositiveAVGAtomicPart = rankContributionPositiveAVGAtomic[networkType][pSharingIndex];
      rankContributionPositiveSSQAtomicPart = rankContributionPositiveSSQAtomic[networkType][pSharingIndex];
      rankContributionNegativeAVGAtomicPart = rankContributionNegativeAVGAtomic[networkType][pSharingIndex];
      rankContributionNegativeSSQAtomicPart = rankContributionNegativeSSQAtomic[networkType][pSharingIndex];
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
            knowledgeSSQAtomicPart[b][t].addAndGet(pow(sc.knowledgeAvg, 2));
            knowledgeBestAVGAtomicPart[b][t].addAndGet(sc.knowledgeBest);
            knowledgeBestSSQAtomicPart[b][t].addAndGet(pow(sc.knowledgeBest, 2));
            knowledgeBestSourceDiversityAVGAtomicPart[b][t].addAndGet(sc.knowledgeBestSourceDiversity);
            knowledgeBestSourceDiversitySSQAtomicPart[b][t].addAndGet(pow(sc.knowledgeBestSourceDiversity, 2));
            knowledgeMinMaxAVGAtomicPart[b][t].addAndGet(sc.knowledgeMinMax);
            knowledgeMinMaxSSQAtomicPart[b][t].addAndGet(pow(sc.knowledgeMinMax, 2));
            beliefDiversityAVGAtomicPart[b][t].addAndGet(sc.beliefDiversity);
            beliefDiversitySSQAtomicPart[b][t].addAndGet(pow(sc.beliefDiversity, 2));
            beliefSourceDiversityAVGAtomicPart[b][t].addAndGet(sc.beliefSourceDiversity);
            beliefSourceDiversitySSQAtomicPart[b][t].addAndGet(pow(sc.beliefSourceDiversity, 2));
            centralizationAVGAtomicPart[b][t].addAndGet(sc.centralization);
            centralizationSSQAtomicPart[b][t].addAndGet(pow(sc.centralization, 2));
            efficiencyAVGAtomicPart[b][t].addAndGet(sc.efficiency);
            efficiencySSQAtomicPart[b][t].addAndGet(pow(sc.efficiency, 2));
            for( int n = 0; n < Main.N; n ++ ){
              rankContributionAVGAtomicPart[b][t][n].addAndGet(sc.rank0Contribution[n]);
              rankContributionSSQAtomicPart[b][t][n].addAndGet(pow(sc.rank0Contribution[n], 2));
              rankContributionPositiveAVGAtomicPart[b][t][n].addAndGet(sc.rank0ContributionPositive[n]);
              rankContributionPositiveSSQAtomicPart[b][t][n].addAndGet(pow(sc.rank0ContributionPositive[n], 2));
              rankContributionNegativeAVGAtomicPart[b][t][n].addAndGet(sc.rank0ContributionNegative[n]);
              rankContributionNegativeSSQAtomicPart[b][t][n].addAndGet(pow(sc.rank0ContributionNegative[n], 2));
            }
            sc.stepForward();
          }
          optimalBetaAVGAtomicPart[t].addAndGet(Main.BETA[maxKnowledgeAvgB]);
          optimalBetaSSQAtomicPart[t].addAndGet(pow(Main.BETA[maxKnowledgeAvgB], 2));
        }
      }
    }
    
  }
  
}
