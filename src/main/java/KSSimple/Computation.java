package KSSimple;

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
        }
        sc.stepForward();
      }
    }

  }

}
