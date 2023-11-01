package KSCost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;


public class Scenario {

  RandomGenerator r;
  int[] focalIndexArray;
  int[] targetIndexArray;
  int[] mIndexArray;

  boolean isCavemen;

  double beta;
  double cost;
  double pSharing;
  double[] pSharingOf;
  double pSeeking;
  double[] pSeekingOf;

  boolean[] reality;
  boolean[][] belief;
  boolean[][] belief0;
  int[][] beliefSource;
  int[][] beliefSourceCount;
  double beliefSourceDiversity;
  double beliefDiversity;
  int[] nCorrectBelief0;
  int[] nIncorrectBelief0;

  int[] knowledge;
  int[] knowledge0;
  int[] knowledgeCum;
  int knowledgeBest;
  int knowledgeBestIndex;
  int knowledgeWorst;
  double knowledgeBestSourceDiversity;
  double knowledgeMinMax;
  double[] contribution;
  double[] applicationRate;
  double[] applicationRatePositive;
  double[] applicationRateNegative;

  boolean[][] networkWhole;
  int[][] networkEgo;
  int[] isInGroup;

  double knowledgeAvg;

  double centralization;

  int[] rank;
  int[] rank0;
  double[] rankKnowledge;
  double[] rankContribution;
  double[] rankContributionPositive;
  double[] rankContributionNegative;
  double[] rankContributionBest;
  double[] rankContributionBestPositive;
  double[] rankContributionBestNegative;
  double[] rankApplicationRate;
  double[] rankApplicationRatePositive;
  double[] rankApplicationRateNegative;
  double[] rankCentrality;

  Scenario(double beta, double cost, double pSharing, double pSeeking) {
    this.beta = beta;
    this.cost = cost;
    this.pSharing = pSharing;
    this.pSeeking = pSeeking;
    this.isCavemen = Main.IS_CAVEMEN;
  }

  Scenario(double beta, double cost, double pSharing, double pSeeking, boolean isCavemen) {
    this.beta = beta;
    this.cost = cost;
    this.pSharing = pSharing;
    this.pSeeking = pSeeking;
    this.isCavemen = isCavemen;
  }

  void initialize() {
    initializeInstrument();
    initializeRealityIndividual();
    initializeNetwork();
    setOutcome();
  }

  void initializeInstrument() {
    r = new MersenneTwister();
    focalIndexArray = new int[Main.N];
    targetIndexArray = new int[Main.N];
    mIndexArray = new int[Main.M];
    for (int n = 0; n < Main.N; n++) {
      focalIndexArray[n] = n;
      targetIndexArray[n] = n;
    }
    for (int m = 0; m < Main.M; m++) {
      mIndexArray[m] = m;
    }
  }

  public void initializeRealityIndividual() {
    reality = new boolean[Main.M];
    belief = new boolean[Main.N][Main.M];
    belief0 = new boolean[Main.N][];
    beliefSource = new int[Main.N][Main.M];
    beliefSourceCount = new int[Main.N][Main.N];
    nCorrectBelief0 = new int[Main.N];
    nIncorrectBelief0 = new int[Main.N];
    knowledge = new int[Main.N];
    knowledgeAvg = 0;
    knowledgeBest = Integer.MIN_VALUE;
    knowledgeWorst = Integer.MAX_VALUE;
    pSharingOf = new double[Main.N];
    pSeekingOf = new double[Main.N];

    // Reality & Belief
    for (int m : mIndexArray) {
      reality[m] = r.nextBoolean();
      for (int n : focalIndexArray) {
        belief[n][m] = r.nextBoolean();
        beliefSource[n][m] = n; // The belief comes from oneself
        if (belief[n][m] == reality[m]) {
          nCorrectBelief0[n]++;
        } else {
          nIncorrectBelief0[n]++;
        }
      }
    }
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      belief0[focal] = belief[focal].clone();
      knowledge[focal] = getKnowledgeOf(focal);
      knowledgeAvg += knowledge[focal];
      if (knowledge[focal] > knowledgeBest) {
        knowledgeBest = knowledge[focal];
        knowledgeBestIndex = focal;
      }
      if (knowledge[focal] < knowledgeWorst) {
        knowledgeWorst = knowledge[focal];
      }
      beliefSourceCount[focal][focal] = Main.M; // All M beliefs come from the self = n
    }

    knowledge0 = knowledge.clone();
    knowledgeCum = knowledge.clone();
    knowledgeAvg /= Main.M_N;
    knowledgeMinMax = (double) (knowledgeBest - knowledgeWorst) / Main.M;

    // Rank
    setRank();
    rank0 = rank.clone();

    // pSharing & pSeeking
    for (int n : focalIndexArray) {
      pSharingOf[n] = pSharing;
      pSeekingOf[n] = pSeeking;
    }

  }

  void initializeNetwork() {
    networkWhole = new boolean[Main.N][Main.N];
    networkEgo = new int[Main.N][];
    isInGroup = new int[Main.N];
    int[] degree = new int[Main.N];

    if (isCavemen) {
      // Is Cavemen Structure
      for (int group = 0; group < Main.N_OF_GROUP; group++) {
        for (int focalInGroup = 0; focalInGroup < Main.N_IN_GROUP; focalInGroup++) {
          int focal = group * Main.N_IN_GROUP + focalInGroup;
          isInGroup[focal] = group;
          for (int targetInGroup = 0; targetInGroup < Main.N_IN_GROUP; targetInGroup++) {
            int target = group * Main.N_IN_GROUP + targetInGroup;
            if (focal == target) {
              continue;
            }
            networkWhole[focal][target] = true;
            networkWhole[target][focal] = true;
            degree[focal]++;
            degree[target]++;
          }
        }
      }
      //Limited spanning between group
      for (int group = 0; group < Main.N_OF_GROUP; group++) {
        int firstInThisGroup = group * Main.N_IN_GROUP; // First one in each group
        int secondInThisGroup = firstInThisGroup + 1; // Second one in each group
        int firstInNextGroup = (firstInThisGroup + Main.N_IN_GROUP) % Main.N; //First one in the next group
        networkWhole[firstInThisGroup][secondInThisGroup] = false;
        networkWhole[secondInThisGroup][firstInThisGroup] = false;
        networkWhole[secondInThisGroup][firstInNextGroup] = true;
        networkWhole[firstInNextGroup][secondInThisGroup] = true;
//        degree[firstInThisGroup]--; //@Unnecessary?
//        degree[firstInNextGroup]++; //@Unnecessary?
      }
      //Rewiring
      shuffleFisherYates(focalIndexArray);
      for (int focal : focalIndexArray) {
        int focalGroup = isInGroup[focal];
        int inUnitLast = (focalGroup + 1) * Main.N_IN_GROUP;
        for (int targetInUnit = focalGroup * Main.N_IN_GROUP; targetInUnit < inUnitLast; targetInUnit++) {
          if (networkWhole[focal][targetInUnit] && degree[targetInUnit] > 1 && r.nextDouble() < beta) {
            shuffleFisherYates(targetIndexArray);
            for (int targetOutUnit : targetIndexArray) {
              if (!networkWhole[focal][targetOutUnit] && focalGroup != isInGroup[targetOutUnit]) {
                networkWhole[focal][targetInUnit] = false;
                networkWhole[targetInUnit][focal] = false;
                networkWhole[focal][targetOutUnit] = true;
                networkWhole[targetOutUnit][focal] = true;
                degree[targetInUnit]--;
                degree[targetOutUnit]++;
                break;
              }
            }
          }
        }
      }
    } else {
      // Is Not Cavemen Structure
      for (int focal : focalIndexArray) {
        degree[focal] = Main.N - 1;
        isInGroup[focal] = 0;
        for (int target : targetIndexArray) {
          if (focal != target) {
            networkWhole[focal][target] = true;
          }
        }
      }
    }

    for (int focal : focalIndexArray) {
      List<Integer> contact = new ArrayList<Integer>();
      for (int target : targetIndexArray) {
        if (networkWhole[focal][target]) {
          contact.add(target);
        }
      }
      networkEgo[focal] = contact.stream().mapToInt(i -> i).toArray();
    }
  }

  void stepForward() {
    doLearning();
    setRank();
    setOutcome();
  }

  void doLearning() {
    boolean[] isSharing = new boolean[Main.N];
    boolean[] isSeeking = new boolean[Main.N];
    int[] numKnowledgeTransferInvovled = new int[Main.N];
    for (int focal : focalIndexArray) {
      isSharing[focal] = r.nextDouble() < pSharing;
      isSeeking[focal] = r.nextDouble() < pSeeking;
    }
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      if (numKnowledgeTransferInvovled[focal] >= cost){
        continue;
      }
      if (isSeeking[focal]) {
        shuffleFisherYates(networkEgo[focal]);
        int target = -1;
        double targetKnowledge = knowledge[focal];
        for (int contact : networkEgo[focal]) {
          if (numKnowledgeTransferInvovled[contact] >= cost){
            continue;
          }
          if (isSharing[contact] && getIsBetterThan(knowledge[contact], targetKnowledge)){
            target = contact;
            targetKnowledge = knowledge[contact];
          }
        }
        if( target != -1 ){
          numKnowledgeTransferInvovled[focal]++;
          numKnowledgeTransferInvovled[target]++;
          for (int m : mIndexArray) {
            if (belief[focal][m] != belief[target][m] && r.nextDouble() < Main.P_LEARNING) {
              belief[focal][m] = belief[target][m];
              beliefSourceCount[focal][beliefSource[focal][m]]--;
              beliefSource[focal][m] = beliefSource[target][m];
              beliefSourceCount[focal][beliefSource[focal][m]]++;
            }
          }
          knowledge[focal] = getKnowledgeOf(focal);
        }
      }
    }
    for (int focal : focalIndexArray) {
      knowledgeCum[focal] += knowledge[focal];
    }
  }

  boolean getIsBetterThan(double focal, double target){
    return focal > target;
  }

  int getKnowledgeOf(int focal) {
    int knowledge = 0;
    boolean[] beliefOfFocal = belief[focal];
    for (int m = 0; m < Main.M; m += Main.S) {
      boolean matchAll = true;
      for (int s = 0; s < Main.S; s++) {
        if (beliefOfFocal[m + s] != reality[m + s]) { //@220629 fix: reality[m] to reality[m+s]
          matchAll = false;
          break;
        }
      }
      if (matchAll) {
        knowledge += Main.S;
      }
    }
    return knowledge;
  }

  double getBeliefSourceDiversityOf(int focal) {
    //Gini-Simpson Index: https://en.wikipedia.org/wiki/Diversity_index#Gini%E2%80%93Simpson_index
    int beliefSourceDiversity = 0;
    for (int target : targetIndexArray) {
      beliefSourceDiversity += (beliefSourceCount[focal][target] * beliefSourceCount[focal][target]);
    }
    return 1D - (beliefSourceDiversity / (double) (Main.M * Main.M));
  }

  void setRank() {
    rank = new int[Main.N];
    rankKnowledge = new double[Main.N];
    for (int focal : focalIndexArray) {
      int knowledgeCumFocal = knowledgeCum[focal];
      for (int target = focal; target < Main.N; target++) {
        if (focal == target) {
          continue;
        }
        if (knowledgeCumFocal <= knowledgeCum[target]) { // Ascending order; Focal==Target Canceled out
          rank[focal]++;
        } else {
          rank[target]++;
        }
      }
    }
    for (int focal : focalIndexArray) {
      int rankOf = rank[focal];
      rankKnowledge[rankOf] = knowledge[focal];
    }
  }

  void setOutcome() {
    setBestRankKnowledge();
    setBeliefDiversity();
    setBeliefSourceDiversity();
    setContribution();
    setCentralization();
  }

  void setBestRankKnowledge() {
    knowledgeAvg = 0; //@Fix 220624
    knowledgeBest = Integer.MIN_VALUE;
    knowledgeBestSourceDiversity = 0;
    int knowledgeWorst = Integer.MAX_VALUE;
    for (int focal : focalIndexArray) {
      knowledgeAvg += knowledge[focal];
      if (knowledge[focal] > knowledgeBest) {
        knowledgeBest = knowledge[focal];
        knowledgeBestIndex = focal;
      }
      if (knowledge[focal] < knowledgeBest) {
        knowledgeWorst = knowledge[focal];
      }
    }
    knowledgeAvg /= Main.M_N;
    knowledgeMinMax = (double) (knowledgeBest - knowledgeWorst) / Main.M;
  }

  void setBeliefDiversity() {
    beliefDiversity = 0;
    for (int focal : focalIndexArray) {
      for (int target = focal; target < Main.N; target++) {
        if (focal == target) {
          continue;
        }
        for (int m : mIndexArray) {
          if (belief[focal][m] != belief[target][m]) {
            beliefDiversity++;
          }
        }
      }
    }
    beliefDiversity /= Main.M_N;
  }

  void setBeliefSourceDiversity() {
    beliefSourceDiversity = 0;
    for (int focal : focalIndexArray) {
      double beliefSourceDiversityOf = getBeliefSourceDiversityOf(focal);
      beliefSourceDiversity += beliefSourceDiversityOf;
      if (focal == knowledgeBestIndex) {
        knowledgeBestSourceDiversity = beliefSourceDiversityOf;
      }
    }
    beliefSourceDiversity /= Main.N;
  }

  void setContribution() {
    contribution = new double[Main.N];
    applicationRate = new double[Main.N];
    applicationRatePositive = new double[Main.N];
    applicationRateNegative = new double[Main.N];
    rankContribution = new double[Main.N];
    rankContributionPositive = new double[Main.N];
    rankContributionNegative = new double[Main.N];
    rankContributionBest = new double[Main.N];
    rankContributionBestPositive = new double[Main.N];
    rankContributionBestNegative = new double[Main.N];
    rankApplicationRate = new double[Main.N];
    rankApplicationRatePositive = new double[Main.N];
    rankApplicationRateNegative = new double[Main.N];
    boolean[][] isAppliedAndPositive = new boolean[Main.N][Main.M];
    boolean[][] isAppliedAndNegative = new boolean[Main.N][Main.M];

    for (int focal : focalIndexArray) {
      for (int m : mIndexArray) {
        int source = beliefSource[focal][m];
        int sourceRank = rank[source];
        contribution[source]++;
        rankContribution[sourceRank]++;
        if (belief[focal][m] == reality[m]) {
          rankContributionPositive[sourceRank]++;
          isAppliedAndPositive[sourceRank][m] = true;
        } else {
          rankContributionNegative[sourceRank]++;
          isAppliedAndNegative[sourceRank][m] = true;
        }
      }
    }

    for (int sourceRank : focalIndexArray) {
      for (int m : mIndexArray) {
        if (isAppliedAndPositive[sourceRank][m]) {
          rankApplicationRate[sourceRank]++;
          rankApplicationRatePositive[sourceRank]++;
        } else if (isAppliedAndNegative[sourceRank][m]) {
          rankApplicationRate[sourceRank]++;
          rankApplicationRateNegative[sourceRank]++;
        }
      }
    }

    for (int m : mIndexArray) {
      int source = beliefSource[knowledgeBestIndex][m];
      int sourceRank = rank[source];
      rankContributionBest[sourceRank]++;
      if (belief[knowledgeBestIndex][m] == reality[m]) {
        rankContributionBestPositive[sourceRank]++;
      } else {
        rankContributionBestNegative[sourceRank]++;
      }
    }

    for (int focal : focalIndexArray) {
      contribution[focal] /= Main.M_N;
      rankContribution[focal] /= Main.M_N;
      rankContributionPositive[focal] /= Main.M_N;
      rankContributionNegative[focal] /= Main.M_N;
      rankContributionBest[focal] /= Main.M;
      rankContributionBestPositive[focal] /= Main.M;
      rankContributionBestNegative[focal] /= Main.M;
      rankApplicationRate[focal] /= Main.M;
      rankApplicationRatePositive[focal] /= nCorrectBelief0[focal];
      rankApplicationRateNegative[focal] /= nIncorrectBelief0[focal];
    }
  }

  void setCentralization() {
    centralization = 0;
    rankCentrality = new double[Main.N];
    double maxCentrality = Double.MIN_VALUE;

    for (int focal : focalIndexArray) {
      int focalRank = rank[focal];
      for (int target : targetIndexArray) {
        rankCentrality[focalRank] += beliefSourceCount[target][focal];
      }
    }

    for (int rank : focalIndexArray) {
      rankCentrality[rank] /= Main.M_N;
      if (rankCentrality[rank] > maxCentrality) {
        maxCentrality = rankCentrality[rank];
      }
    }

    for (int rank : focalIndexArray) {
      centralization += (maxCentrality - rankCentrality[rank]);
    }

    centralization /= Main.N; // Theoretical maximum of Sum[Cx(p*)-Cx(pi)] over 1:N
  }

  void shuffleFisherYates(int[] nArray) {
    for (int i = nArray.length - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      int temp = nArray[i];
      nArray[i] = nArray[j];
      nArray[j] = temp;
    }
  }

}
