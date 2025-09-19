package KSFinal;

import java.util.concurrent.atomic.DoubleAdder;

import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Computation {

  DoubleAdder[][][][][] knowledgeAVGAtomic;
  DoubleAdder[][][][][] knowledgeSSQAtomic;

  DoubleAdder[][][][][] knowledgeBestAVGAtomic;
  DoubleAdder[][][][][] knowledgeBestSSQAtomic;
  DoubleAdder[][][][][] knowledgeBestSourceDiversityAVGAtomic;
  DoubleAdder[][][][][] knowledgeBestSourceDiversitySSQAtomic;

  DoubleAdder[][][][][] knowledgeMinMaxAVGAtomic;
  DoubleAdder[][][][][] knowledgeMinMaxSSQAtomic;

  DoubleAdder[][][][][] beliefDiversityAVGAtomic;
  DoubleAdder[][][][][] beliefDiversitySSQAtomic;
  DoubleAdder[][][][][] beliefSourceDiversityAVGAtomic;
  DoubleAdder[][][][][] beliefSourceDiversitySSQAtomic;

  DoubleAdder[][][][][] centralizationAVGAtomic;
  DoubleAdder[][][][][] centralizationSSQAtomic;
  DoubleAdder[][][][][] connectednessAVGAtomic;
  DoubleAdder[][][][][] connectednessSSQAtomic;

  DoubleAdder[][][][] optimalBetaAVGAtomic;
  DoubleAdder[][][][] optimalBetaSSQAtomic;

  DoubleAdder[][][][][][] rankContributionAVGAtomic;
  DoubleAdder[][][][][][] rankContributionSSQAtomic;
  DoubleAdder[][][][][][] rankContributionPositiveAVGAtomic;
  DoubleAdder[][][][][][] rankContributionPositiveSSQAtomic;
  DoubleAdder[][][][][][] rankContributionNegativeAVGAtomic;
  DoubleAdder[][][][][][] rankContributionNegativeSSQAtomic;

  //In double arrays
  double[][][][][] knowledgeAVG;
  double[][][][][] knowledgeSSQ;

  double[][][][][] knowledgeBestAVG;
  double[][][][][] knowledgeBestSSQ;
  double[][][][][] knowledgeBestSourceDiversityAVG;
  double[][][][][] knowledgeBestSourceDiversitySSQ;

  double[][][][][] knowledgeMinMaxAVG;
  double[][][][][] knowledgeMinMaxSSQ;

  double[][][][][] beliefDiversityAVG;
  double[][][][][] beliefDiversitySSQ;
  double[][][][][] beliefSourceDiversityAVG;
  double[][][][][] beliefSourceDiversitySSQ;

  double[][][][][] centralizationAVG;
  double[][][][][] centralizationSSQ;
  double[][][][][] connectednessAVG;
  double[][][][][] connectednessSSQ;

  double[][][][] optimalBetaAVG;
  double[][][][] optimalBetaSSQ;

  double[][][][][][] rankContributionAVG;
  double[][][][][][] rankContributionSSQ;
  double[][][][][][] rankContributionPositiveAVG;
  double[][][][][][] rankContributionPositiveSSQ;
  double[][][][][][] rankContributionNegativeAVG;
  double[][][][][][] rankContributionNegativeSSQ;

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

    for (int isRatioIdx = 0; isRatioIdx < 2; isRatioIdx++) {
      for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
        for (int b = 0; b < Main.LENGTH_BETA; b++) {
          for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
            boolean isRatio = isRatioIdx == 0;
            double beta = Main.BETA[b];
            double pSharing = Main.P_SHARING[ps];
            String ntString = null;
            switch (nt) {
              case 0 -> ntString = "RanTree";
              case 1 -> ntString = "Cavemen";
              case 2 -> ntString = "PrefAtt";
            }
            String params
                    = (isRatio ? "ratio" : "tendency")
                    + "_" + ntString
                    + "_beta" + df.format(beta)
                    + "_psha" + df.format(pSharing)
                    + "_t" + Main.TIME;
            Scenario src = new Scenario(isRatio, nt, beta, pSharing);
            for (int t = 0; t < Main.TIME; t++) {
              src.stepForward();
            }
            src.printCSV(Main.PATH_CSV.resolve(Main.RUN_ID + params).toString());
            System.out.println("Network Printed: " + Main.RUN_ID + params);
          }
        }
      }
    }
  }

  public void doExperiment() {
    pb = new ProgressBar(Main.ITERATION);
    setSpace();
    setDoubleAdder();
    runFullExperiment();
    averageFullExperiment();
  }

  private void setSpace() {
    knowledgeAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversityAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversitySSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversityAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversitySSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversityAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversitySSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    connectednessAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    connectednessSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    optimalBetaAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    rankContributionAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVGAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSSQAtomic = new DoubleAdder[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];

    knowledgeAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversityAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeBestSourceDiversitySSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    knowledgeMinMaxSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversityAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefDiversitySSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversityAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    beliefSourceDiversitySSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    centralizationSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    connectednessAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    connectednessSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    optimalBetaAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    rankContributionAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
  }

  private void setDoubleAdder() {
    for (int isRatioIdx = 0; isRatioIdx < 2; isRatioIdx++) {
      for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          for (int t = 0; t < Main.TIME; t++) {
            optimalBetaAVGAtomic[isRatioIdx][nt][ps][t] = new DoubleAdder();
            optimalBetaSSQAtomic[isRatioIdx][nt][ps][t] = new DoubleAdder();
            for (int b = 0; b < Main.LENGTH_BETA; b++) {
              knowledgeAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeSSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeBestAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeBestSSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeBestSourceDiversityAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeBestSourceDiversitySSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeMinMaxAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              knowledgeMinMaxSSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              beliefDiversityAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              beliefDiversitySSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              beliefSourceDiversityAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              beliefSourceDiversitySSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              centralizationAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              centralizationSSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              connectednessAVGAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              connectednessSSQAtomic[isRatioIdx][nt][ps][b][t] = new DoubleAdder();
              for (int n = 0; n < Main.N; n++) {
                rankContributionAVGAtomic[isRatioIdx][nt][ps][b][t][n] = new DoubleAdder();
                rankContributionSSQAtomic[isRatioIdx][nt][ps][b][t][n] = new DoubleAdder();
                rankContributionPositiveAVGAtomic[isRatioIdx][nt][ps][b][t][n] = new DoubleAdder();
                rankContributionPositiveSSQAtomic[isRatioIdx][nt][ps][b][t][n] = new DoubleAdder();
                rankContributionNegativeAVGAtomic[isRatioIdx][nt][ps][b][t][n] = new DoubleAdder();
                rankContributionNegativeSSQAtomic[isRatioIdx][nt][ps][b][t][n] = new DoubleAdder();
              }
            }
          }
        }
      }
    }
  }

  private void runFullExperiment() {
    // Use a fixed-size thread pool equal to the number of available CPU cores
    int workers = Runtime.getRuntime().availableProcessors();
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(workers, r -> {
      Thread t = new Thread(r);
      t.setName("experimenter-" + t.getId());
      t.setDaemon(false); // ensure worker threads stay alive until completion
      return t;
    });

    try {
      for (int iteration = 0; iteration < Main.ITERATION; iteration++) {
        fixedThreadPool.execute(() -> {
          try {
            iterationWrapper task = new iterationWrapper();
            task.run(); // execute the iteration
          } catch (Throwable th) {
            // Catch and log any exceptions so they are not silently dropped
            System.err.println("runFullExperiment(): Iteration task failed - " + th.getMessage());
            th.printStackTrace(System.err);
          }
        });
      }
    } finally {
      fixedThreadPool.shutdown();
      try {
        boolean terminated = fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        if (!terminated) {
          System.err.println("runFullExperiment(): Timeout while waiting for tasks to finish.");
        }
      } catch (InterruptedException ie) {
        System.err.println("runFullExperiment(): Interrupted while awaiting termination.");
        Thread.currentThread().interrupt();
      }
    }
  }

  private void averageFullExperiment() {
    for (int isRatio = 0; isRatio < 2; isRatio++) {
      for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          for (int t = 0; t < Main.TIME; t++) {
            optimalBetaAVG[isRatio][nt][ps][t] = optimalBetaAVGAtomic[isRatio][nt][ps][t].sum() / Main.ITERATION;
            for (int b = 0; b < Main.LENGTH_BETA; b++) {
              knowledgeAVG[isRatio][nt][ps][b][t] = knowledgeAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              knowledgeBestAVG[isRatio][nt][ps][b][t] = knowledgeBestAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              knowledgeBestSourceDiversityAVG[isRatio][nt][ps][b][t] = knowledgeBestSourceDiversityAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              knowledgeMinMaxAVG[isRatio][nt][ps][b][t] = knowledgeMinMaxAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              beliefDiversityAVG[isRatio][nt][ps][b][t] = beliefDiversityAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              beliefSourceDiversityAVG[isRatio][nt][ps][b][t] = beliefSourceDiversityAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              centralizationAVG[isRatio][nt][ps][b][t] = centralizationAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              connectednessAVG[isRatio][nt][ps][b][t] = connectednessAVGAtomic[isRatio][nt][ps][b][t].sum() / Main.ITERATION;
              for (int n = 0; n < Main.N; n++) {
                rankContributionAVG[isRatio][nt][ps][b][t][n] = rankContributionAVGAtomic[isRatio][nt][ps][b][t][n].sum() / Main.ITERATION;
                rankContributionPositiveAVG[isRatio][nt][ps][b][t][n] = rankContributionPositiveAVGAtomic[isRatio][nt][ps][b][t][n].sum() / Main.ITERATION;
                rankContributionNegativeAVG[isRatio][nt][ps][b][t][n] = rankContributionNegativeAVGAtomic[isRatio][nt][ps][b][t][n].sum() / Main.ITERATION;
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
      for (int isRatioIdx = 0; isRatioIdx < 2; isRatioIdx++) {
        for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
          for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
            new SingleRun(isRatioIdx, nt, ps);
          }
        }
      }
      pb.stepNext();
    }

  }

  class SingleRun {

    int isRatioIdx;
    int networkType;
    int pSharingIndex;
    double pSharing;

    DoubleAdder[][] knowledgeAVGAtomicPart;
    DoubleAdder[][] knowledgeSSQAtomicPart;
    DoubleAdder[][] knowledgeBestAVGAtomicPart;
    DoubleAdder[][] knowledgeBestSSQAtomicPart;
    DoubleAdder[][] knowledgeBestSourceDiversityAVGAtomicPart;
    DoubleAdder[][] knowledgeBestSourceDiversitySSQAtomicPart;
    DoubleAdder[][] knowledgeMinMaxAVGAtomicPart;
    DoubleAdder[][] knowledgeMinMaxSSQAtomicPart;
    DoubleAdder[][] beliefDiversityAVGAtomicPart;
    DoubleAdder[][] beliefDiversitySSQAtomicPart;
    DoubleAdder[][] beliefSourceDiversityAVGAtomicPart;
    DoubleAdder[][] beliefSourceDiversitySSQAtomicPart;
    DoubleAdder[][] centralizationAVGAtomicPart;
    DoubleAdder[][] centralizationSSQAtomicPart;
    DoubleAdder[][] connectednessAVGAtomicPart;
    DoubleAdder[][] connectednessSSQAtomicPart;
    DoubleAdder[] optimalBetaAVGAtomicPart;
    DoubleAdder[] optimalBetaSSQAtomicPart;
    DoubleAdder[][][] rankContributionAVGAtomicPart;
    DoubleAdder[][][] rankContributionSSQAtomicPart;
    DoubleAdder[][][] rankContributionPositiveAVGAtomicPart;
    DoubleAdder[][][] rankContributionPositiveSSQAtomicPart;
    DoubleAdder[][][] rankContributionNegativeAVGAtomicPart;
    DoubleAdder[][][] rankContributionNegativeSSQAtomicPart;

    SingleRun(int isRatioIdx, int networkType, int pSharingIndex) {
      this.isRatioIdx = isRatioIdx;
      this.networkType = networkType;
      this.pSharingIndex = pSharingIndex;
      pSharing = Main.P_SHARING[pSharingIndex];
      initializeResultSpace();
      run();
    }

    void initializeResultSpace() {
      knowledgeAVGAtomicPart = knowledgeAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeSSQAtomicPart = knowledgeSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeBestAVGAtomicPart = knowledgeBestAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeBestSSQAtomicPart = knowledgeBestSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeBestSourceDiversityAVGAtomicPart = knowledgeBestSourceDiversityAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeBestSourceDiversitySSQAtomicPart = knowledgeBestSourceDiversitySSQAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeMinMaxAVGAtomicPart = knowledgeMinMaxAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      knowledgeMinMaxSSQAtomicPart = knowledgeMinMaxSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      beliefDiversityAVGAtomicPart = beliefDiversityAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      beliefDiversitySSQAtomicPart = beliefDiversitySSQAtomic[isRatioIdx][networkType][pSharingIndex];
      beliefSourceDiversityAVGAtomicPart = beliefSourceDiversityAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      beliefSourceDiversitySSQAtomicPart = beliefSourceDiversitySSQAtomic[isRatioIdx][networkType][pSharingIndex];
      centralizationAVGAtomicPart = centralizationAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      centralizationSSQAtomicPart = centralizationSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      connectednessAVGAtomicPart = connectednessAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      connectednessSSQAtomicPart = connectednessSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      optimalBetaAVGAtomicPart = optimalBetaAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      optimalBetaSSQAtomicPart = optimalBetaSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      rankContributionAVGAtomicPart = rankContributionAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      rankContributionSSQAtomicPart = rankContributionSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      rankContributionPositiveAVGAtomicPart = rankContributionPositiveAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      rankContributionPositiveSSQAtomicPart = rankContributionPositiveSSQAtomic[isRatioIdx][networkType][pSharingIndex];
      rankContributionNegativeAVGAtomicPart = rankContributionNegativeAVGAtomic[isRatioIdx][networkType][pSharingIndex];
      rankContributionNegativeSSQAtomicPart = rankContributionNegativeSSQAtomic[isRatioIdx][networkType][pSharingIndex];
    }

    void run() {
      Scenario[] scs = new Scenario[Main.LENGTH_BETA];

      boolean isRatio = isRatioIdx == 0;
      for (int b = 0; b < Main.LENGTH_BETA; b++) {
        double beta = Main.BETA[b];
        for (; ; ) {
          scs[b] = new Scenario(isRatio, networkType, beta, pSharing);
          if (scs[b].connectedness == Double.POSITIVE_INFINITY) {
            System.out.println("Broken network at " + networkType + " " + beta + " " + pSharing + " -> Resample");
            scs[b] = new Scenario(isRatio, networkType, beta, pSharing);
          } else {
            break;
          }
        }
      }
      for (int t = 0; t < Main.TIME; t++) {
        //Recording first
        int maxKnowledgeAvgB = -1;
        double maxKnowledgeAvg = Double.MIN_VALUE;
        for (int b = 0; b < Main.LENGTH_BETA; b++) {
          Scenario sc = scs[b];
          if (sc.performance > maxKnowledgeAvg) {
            maxKnowledgeAvg = sc.performance;
            maxKnowledgeAvgB = b;
          }
          knowledgeAVGAtomicPart[b][t].add(sc.performance);
          knowledgeSSQAtomicPart[b][t].add(sc.performance * sc.performance);
          knowledgeBestSourceDiversityAVGAtomicPart[b][t].add(sc.beliefSourceDiversity);
          knowledgeBestSourceDiversitySSQAtomicPart[b][t].add(sc.beliefSourceDiversity * sc.beliefSourceDiversity);
          beliefDiversityAVGAtomicPart[b][t].add(sc.beliefDiversity);
          beliefDiversitySSQAtomicPart[b][t].add(sc.beliefDiversity * sc.beliefDiversity);
          beliefSourceDiversityAVGAtomicPart[b][t].add(sc.beliefSourceDiversity);
          beliefSourceDiversitySSQAtomicPart[b][t].add(sc.beliefSourceDiversity * sc.beliefSourceDiversity);
          centralizationAVGAtomicPart[b][t].add(sc.centralization);
          centralizationSSQAtomicPart[b][t].add(sc.centralization * sc.centralization);
          connectednessAVGAtomicPart[b][t].add(sc.connectedness);
          connectednessSSQAtomicPart[b][t].add(sc.connectedness * sc.connectedness);
          for (int n = 0; n < Main.N; n++) {
            rankContributionAVGAtomicPart[b][t][n].add(sc.rank0Contribution[n]);
            rankContributionSSQAtomicPart[b][t][n].add(sc.rank0Contribution[n] * sc.rank0Contribution[n]);
            rankContributionPositiveAVGAtomicPart[b][t][n].add(sc.rank0ContributionPositive[n]);
            rankContributionPositiveSSQAtomicPart[b][t][n].add(sc.rank0ContributionPositive[n] * sc.rank0ContributionPositive[n]);
            rankContributionNegativeAVGAtomicPart[b][t][n].add(sc.rank0ContributionNegative[n]);
            rankContributionNegativeSSQAtomicPart[b][t][n].add(sc.rank0ContributionNegative[n] * sc.rank0ContributionNegative[n]);
          }
          sc.stepForward();
        }
        optimalBetaAVGAtomicPart[t].add(Main.BETA[maxKnowledgeAvgB]);
        optimalBetaSSQAtomicPart[t].add(Main.BETA[maxKnowledgeAvgB] * Main.BETA[maxKnowledgeAvgB]);
      }
    }

  }

}
