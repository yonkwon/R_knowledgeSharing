package KSFinal;

import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Computation {

  // ----- Striped locks -----
  final Object[] STRIPE = new Object[Main.SHARDS];

  // Hash mixer (FNV-1a style) for lock striping over 5D/6D indices
  private static int shardOf5(int a, int b, int c, int d, int e) {
    int h = 0x811C9DC5;           // 2166136261
    h = (h ^ a) * 0x01000193;     // 16777619
    h = (h ^ b) * 0x01000193;
    h = (h ^ c) * 0x01000193;
    h = (h ^ d) * 0x01000193;
    h = (h ^ e) * 0x01000193;
    return (h & 0x7fffffff) & (Main.SHARDS - 1);
  }
  private static int shardOf6(int a, int b, int c, int d, int e, int f) {
    int h = 0x811C9DC5;
    h = (h ^ a) * 0x01000193;
    h = (h ^ b) * 0x01000193;
    h = (h ^ c) * 0x01000193;
    h = (h ^ d) * 0x01000193;
    h = (h ^ e) * 0x01000193;
    h = (h ^ f) * 0x01000193;
    return (h & 0x7fffffff) & (Main.SHARDS - 1);
  }

  // ----- Accumulators: primitive arrays (hold SUMs; divide by ITERATION later) -----
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

  double[][][][][] connectednessAVG;
  double[][][][][] connectednessSSQ;
  double[][][][][] concentrationAVG;
  double[][][][][] concentrationSSQ;

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
    // init striped locks
    for (int i = 0; i < Main.SHARDS; i++) STRIPE[i] = new Object();
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
            String ntString = switch (nt) {
              case 0 -> "RanTree";
              case 1 -> "Cavemen";
              case 2 -> "PrefAtt";
              default -> "Unknown";
            };
            String params = (isRatio ? "ratio" : "tendency")
                    + "_" + ntString
                    + "_beta" + df.format(beta)
                    + "_psha" + df.format(pSharing)
                    + "_t" + Main.TIME;

            Scenario src = new Scenario(isRatio, nt, beta, pSharing);
            for (int t = 0; t < Main.TIME; t++) src.stepForward();
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
    runFullExperiment();
    averageFullExperiment();
  }

  private void setSpace() {
    // allocate primitive accumulators; JVM zeros them
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

    connectednessAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    connectednessSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    concentrationAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];
    concentrationSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME];

    optimalBetaAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];
    optimalBetaSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.TIME];

    rankContributionAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionPositiveSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeAVG = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
    rankContributionNegativeSSQ = new double[2][Main.LENGTH_NETWORK_TYPE][Main.LENGTH_P_SHARING][Main.LENGTH_BETA][Main.TIME][Main.N];
  }

  // ----- striped add helpers -----
  private void add(double[][][][] array, int isRatioIdx, int nt, int ps, int t, double value2Add) {
    int s = shardOf5(isRatioIdx, nt, ps, t, 0);
    synchronized (STRIPE[s]) {
      array[isRatioIdx][nt][ps][t] += value2Add;
    }
  }
  private void add(double[][][][][] array, int isRatioIdx, int nt, int ps, int b, int t, double value2Add) {
    int s = shardOf5(isRatioIdx, nt, ps, b, t);
    synchronized (STRIPE[s]) {
      array[isRatioIdx][nt][ps][b][t] += value2Add;
    }
  }
  private void add(double[][][][][][] array, int isRatioIdx, int nt, int ps, int b, int t, int n, double value2Add) {
    int s = shardOf6(isRatioIdx, nt, ps, b, t, n);
    synchronized (STRIPE[s]) {
      array[isRatioIdx][nt][ps][b][t][n] += value2Add;
    }
  }

  private void runFullExperiment() {
    int workers = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(workers, r -> {
      Thread t = new Thread(r);
      t.setDaemon(false);
      return t;
    });

    try {
      for (int iteration = 0; iteration < Main.ITERATION; iteration++) {
        pool.execute(() -> {
          try {
            new iterationWrapper().run();
          } catch (Throwable th) {
            System.err.println("runFullExperiment(): Iteration task failed - " + th.getMessage());
            th.printStackTrace(System.err);
          }
        });
      }
    } finally {
      pool.shutdown();
      try {
        boolean ok = pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        if (!ok) System.err.println("runFullExperiment(): Timeout while waiting for tasks.");
      } catch (InterruptedException ie) {
        System.err.println("runFullExperiment(): Interrupted.");
        Thread.currentThread().interrupt();
      }
    }
  }

  private void averageFullExperiment() {
    // Divide in place by ITERATION to convert sums to averages
    for (int isRatio = 0; isRatio < 2; isRatio++) {
      for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
        for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
          for (int t = 0; t < Main.TIME; t++) {
            optimalBetaAVG[isRatio][nt][ps][t] /= Main.ITERATION;
            optimalBetaSSQ[isRatio][nt][ps][t] /= Main.ITERATION;
            for (int b = 0; b < Main.LENGTH_BETA; b++) {
              knowledgeAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              knowledgeSSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;

              knowledgeBestAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              knowledgeBestSSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;
              knowledgeBestSourceDiversityAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              knowledgeBestSourceDiversitySSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;

              knowledgeMinMaxAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              knowledgeMinMaxSSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;

              beliefDiversityAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              beliefDiversitySSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;
              beliefSourceDiversityAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              beliefSourceDiversitySSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;

              connectednessAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              connectednessSSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;
              concentrationAVG[isRatio][nt][ps][b][t] /= Main.ITERATION;
              concentrationSSQ[isRatio][nt][ps][b][t] /= Main.ITERATION;

              for (int n = 0; n < Main.N; n++) {
                rankContributionAVG[isRatio][nt][ps][b][t][n] /= Main.ITERATION;
                rankContributionSSQ[isRatio][nt][ps][b][t][n] /= Main.ITERATION;
                rankContributionPositiveAVG[isRatio][nt][ps][b][t][n] /= Main.ITERATION;
                rankContributionPositiveSSQ[isRatio][nt][ps][b][t][n] /= Main.ITERATION;
                rankContributionNegativeAVG[isRatio][nt][ps][b][t][n] /= Main.ITERATION;
                rankContributionNegativeSSQ[isRatio][nt][ps][b][t][n] /= Main.ITERATION;
              }
            }
          }
        }
      }
    }
  }

  class iterationWrapper implements Runnable {
    @Override
    public void run() {
      for (int isRatioIdx = 0; isRatioIdx < 2; isRatioIdx++) {
        for (int nt = 0; nt < Main.LENGTH_NETWORK_TYPE; nt++) {
          for (int ps = 0; ps < Main.LENGTH_P_SHARING; ps++) {
            new SingleRun(isRatioIdx, nt, ps).run();
          }
        }
      }
      pb.stepNext();
    }
  }

  class SingleRun {

    final int isRatioIdx;
    final int networkType;
    final int pSharingIndex;
    final double pSharing;

    SingleRun(int isRatioIdx, int networkType, int pSharingIndex) {
      this.isRatioIdx = isRatioIdx;
      this.networkType = networkType;
      this.pSharingIndex = pSharingIndex;
      this.pSharing = Main.P_SHARING[pSharingIndex];
    }

    void run() {
      boolean isRatio = isRatioIdx == 0;
      Scenario[] scs = new Scenario[Main.LENGTH_BETA];

      for (int b = 0; b < Main.LENGTH_BETA; b++) {
        double beta = Main.BETA[b];
        while (true) {
          scs[b] = new Scenario(isRatio, networkType, beta, pSharing);
          if (scs[b].connectedness == Double.POSITIVE_INFINITY) {
            System.out.println("Broken network at " + networkType + " " + beta + " " + pSharing + " -> Resample");
          } else break;
        }
      }

      for (int t = 0; t < Main.TIME; t++) {
        int maxB = -1;
        double maxPerf = -Double.MAX_VALUE;

        for (int b = 0; b < Main.LENGTH_BETA; b++) {
          Scenario sc = scs[b];
          if (sc.performance > maxPerf) { maxPerf = sc.performance; maxB = b; }

          add(knowledgeAVG, isRatioIdx, networkType, pSharingIndex, b, t, sc.performance);
          add(knowledgeSSQ, isRatioIdx, networkType, pSharingIndex, b, t, sc.performance * sc.performance);

          add(knowledgeBestSourceDiversityAVG, isRatioIdx, networkType, pSharingIndex, b, t, sc.beliefSourceDiversity);
          add(knowledgeBestSourceDiversitySSQ, isRatioIdx, networkType, pSharingIndex, b, t, sc.beliefSourceDiversity * sc.beliefSourceDiversity);

          add(beliefDiversityAVG, isRatioIdx, networkType, pSharingIndex, b, t, sc.beliefDiversity);
          add(beliefDiversitySSQ, isRatioIdx, networkType, pSharingIndex, b, t, sc.beliefDiversity * sc.beliefDiversity);

          add(beliefSourceDiversityAVG, isRatioIdx, networkType, pSharingIndex, b, t, sc.beliefSourceDiversity);
          add(beliefSourceDiversitySSQ, isRatioIdx, networkType, pSharingIndex, b, t, sc.beliefSourceDiversity * sc.beliefSourceDiversity);

          add(connectednessAVG, isRatioIdx, networkType, pSharingIndex, b, t, sc.connectedness);
          add(connectednessSSQ, isRatioIdx, networkType, pSharingIndex, b, t, sc.connectedness * sc.connectedness);

          add(concentrationAVG, isRatioIdx, networkType, pSharingIndex, b, t, sc.concentration);
          add(concentrationSSQ, isRatioIdx, networkType, pSharingIndex, b, t, sc.concentration * sc.concentration);

          for (int n = 0; n < Main.N; n++) {
            double v   = sc.rank0Contribution[n];
            double vp  = sc.rank0ContributionPositive[n];
            double vn  = sc.rank0ContributionNegative[n];

            add(rankContributionAVG, isRatioIdx, networkType, pSharingIndex, b, t, n, v);
            add(rankContributionSSQ, isRatioIdx, networkType, pSharingIndex, b, t, n, v * v);

            add(rankContributionPositiveAVG, isRatioIdx, networkType, pSharingIndex, b, t, n, vp);
            add(rankContributionPositiveSSQ, isRatioIdx, networkType, pSharingIndex, b, t, n, vp * vp);

            add(rankContributionNegativeAVG, isRatioIdx, networkType, pSharingIndex, b, t, n, vn);
            add(rankContributionNegativeSSQ, isRatioIdx, networkType, pSharingIndex, b, t, n, vn * vn);
          }

          sc.stepForward();
        }

        add(optimalBetaAVG, isRatioIdx, networkType, pSharingIndex, t, Main.BETA[maxB]);
        double beta = Main.BETA[maxB];
        add(optimalBetaSSQ, isRatioIdx, networkType, pSharingIndex, t, beta * beta);
      }
    }
  }
}
