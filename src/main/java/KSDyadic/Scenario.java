package KSDyadic;

import static org.apache.commons.math3.util.FastMath.max;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;


public class Scenario {

  RandomGenerator r;
  int[] focalIndexArray;
  int[] targetIndexArray;
  int[] mIndexArray;

  boolean isRatio;
  boolean isCavemen;
  boolean isOneOnOne;

  double beta;
  double pSharing;
  double[] pSharingOf;

  int nSharer;
  int nSeeker;

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

  boolean[][] network;
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

  double[] typeKnowledgeAvg;
  double[] typeContribution;
  double[] typeContributionPositive;

  Scenario(double beta, double pSharing) {
    this.beta = beta;
    this.pSharing = pSharing;
    isRatio = Main.IS_RATIO;
    isCavemen = Main.IS_CAVEMEN;
    isOneOnOne = Main.IS_ONE_ON_ONE;
  }

  Scenario(double beta, double pSharing, boolean isRatio, boolean isCavemen, boolean isOneOnOne) {
    this.beta = beta;
    this.pSharing = pSharing;
    this.isRatio = isRatio;
    this.isCavemen = isCavemen;
    this.isOneOnOne = isOneOnOne;
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

    // pSharing
    if (isRatio) {
      nSharer = (int) (pSharing * Main.N);
      nSeeker = Main.N - nSharer;
      shuffleFisherYates(focalIndexArray);
      for (int n = 0; n < nSharer; n++) {
        pSharingOf[focalIndexArray[n]] = 1D;
      }
      for (int n = nSharer; n < Main.N; n++) {
        pSharingOf[focalIndexArray[n]] = 0D;
      }
    } else {
      for (int n : focalIndexArray) {
        pSharingOf[n] = pSharing;
      }
    }
  }

  void initializeNetwork() {
    network = new boolean[Main.N][Main.N];
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
            network[focal][target] = true;
            network[target][focal] = true;
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
        network[firstInThisGroup][secondInThisGroup] = false;
        network[secondInThisGroup][firstInThisGroup] = false;
        network[secondInThisGroup][firstInNextGroup] = true;
        network[firstInNextGroup][secondInThisGroup] = true;
//        degree[firstInThisGroup]--; //@Unnecessary?
//        degree[firstInNextGroup]++; //@Unnecessary?
      }
      //Rewiring
      shuffleFisherYates(focalIndexArray);
      for (int focal : focalIndexArray) {
        int focalGroup = isInGroup[focal];
        int inUnitLast = (focalGroup + 1) * Main.N_IN_GROUP;
        for (int targetInUnit = focalGroup * Main.N_IN_GROUP; targetInUnit < inUnitLast; targetInUnit++) {
          if (network[focal][targetInUnit] && degree[targetInUnit] > 1 && r.nextDouble() < beta) {
            shuffleFisherYates(targetIndexArray);
            for (int targetOutUnit : targetIndexArray) {
              if (!network[focal][targetOutUnit] && focalGroup != isInGroup[targetOutUnit]) {
                network[focal][targetInUnit] = false;
                network[targetInUnit][focal] = false;
                network[focal][targetOutUnit] = true;
                network[targetOutUnit][focal] = true;
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
            network[focal][target] = true;
          }
        }
      }
    }

  }

  void stepForward() {
    doLearning();
    setRank();
    setOutcome();
  }

  void doLearning() {
    boolean[] isBusy = new boolean[Main.N];
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      if (isOneOnOne &&
          isBusy[focal]) {
        continue;
      }
      boolean isToShare = r.nextDouble() < pSharingOf[focal];
      shuffleFisherYates(targetIndexArray);
      if (isToShare) {
        for (int target : targetIndexArray) {
          if (isOneOnOne && isBusy[target]) {
            continue;
          }
          if (network[focal][target] && knowledge[focal] > knowledge[target]) {
            isBusy[focal] = true;
            isBusy[target] = true;
            for (int m : mIndexArray) {
              if (r.nextDouble() < Main.P_LEARNING && belief[target][m] != belief[focal][m]) { //@220627Fix: Added belief!=belief
                belief[target][m] = belief[focal][m];
                beliefSourceCount[target][beliefSource[target][m]]--;
                beliefSource[target][m] = beliefSource[focal][m];
                beliefSourceCount[target][beliefSource[target][m]]++; // Not necessarily focal
              }
            }
            knowledge[target] = getKnowledgeOf(target);
            break;
          }
        }
      } else {
        for (int target : targetIndexArray) {
          if (isOneOnOne && isBusy[target]) {
            continue;
          }
          if (network[focal][target] && knowledge[focal] < knowledge[target]) {
            isBusy[focal] = true;
            isBusy[target] = true;
            for (int m : mIndexArray) {
              if (r.nextDouble() < Main.P_LEARNING && belief[target][m] != belief[focal][m]) { //@220627Fix: Added belief!=belief
                belief[focal][m] = belief[target][m];
                beliefSourceCount[focal][beliefSource[focal][m]]--;
                beliefSource[focal][m] = beliefSource[target][m];
                beliefSourceCount[focal][beliefSource[focal][m]]++;
              }
            }
            knowledge[focal] = getKnowledgeOf(focal);
            break;
          }
        }
      }
    }
    for (int focal : focalIndexArray) {
      knowledgeCum[focal] += knowledge[focal];
    }
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
    if (Main.IS_RATIO) {
      setTypeOutcome();
    }
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
      rankContributionBest[focal] /= (double) Main.M;
      rankContributionBestPositive[focal] /= (double) Main.M;
      rankContributionBestNegative[focal] /= (double) Main.M;
      rankApplicationRate[focal] /= (double) Main.M;
      rankApplicationRatePositive[focal] /= (double) nCorrectBelief0[focal];
      rankApplicationRateNegative[focal] /= (double) nIncorrectBelief0[focal];
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

    centralization /= (double) Main.N; // Theoretical maximum of Sum[Cx(p*)-Cx(pi)] over 1:N
  }

  void setTypeOutcome() {
    typeKnowledgeAvg = new double[2];
    typeContribution = new double[4];
    typeContributionPositive = new double[4];
    int[] nType2Type = new int[4]; // Very inefficient
    for (int focal : focalIndexArray) {
      int typeOfFocal = (pSharingOf[focal] < .5) ? 0 : 1;
      typeKnowledgeAvg[typeOfFocal] += knowledge[focal];
      for (int target : targetIndexArray) {
        if (focal == target) {
          continue;
        }
        int typeOfTarget = (pSharingOf[target] < .5) ? 0 : 1;
        int type2Type = typeOfFocal * 2 + typeOfTarget;
        nType2Type[type2Type]++;
        for (int m : mIndexArray) {
          if (beliefSource[target][m] == focal) {
            typeContribution[type2Type]++;
            if (belief[target][m] == reality[m]) {
              typeContributionPositive[type2Type]++;
            }
          }
        }
      }
    }
    typeKnowledgeAvg[0] /= (double) max(nSeeker, 1) * (double) Main.M;
    typeKnowledgeAvg[1] /= (double) max(nSharer, 1) * (double) Main.M;
    for (int t2t = 0; t2t < 4; t2t++) {
      typeContribution[t2t] /= (double) nType2Type[t2t] * (double) Main.M;
      typeContributionPositive[t2t] /= (double) nType2Type[t2t] * (double) Main.M;
    }
  }

  void shuffleFisherYates(int[] nArray) {
    for (int i = Main.N - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      int temp = nArray[i];
      nArray[i] = nArray[j];
      nArray[j] = temp;
    }
  }

}
