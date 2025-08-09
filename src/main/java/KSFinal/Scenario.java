package KSFinal;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

public class Scenario {

  RandomGenerator r;
  boolean isNotConverged = true;

  int[] focalIndexArray;
  int[] targetIndexArray;
  int[] dyadIndexArray;
  int[][] dyad2DIndexArray;
  int[] mIndexArray;
  final BitSet buffer = new BitSet(Main.M);

  boolean isRatio;
  boolean isOneOnOne;

  double beta;
  double pSharing;
  double[] pSharingOf;
  int networkType;

  int nSharer;
  int nSeeker;

  BitSet reality;
  BitSet[] belief;
  BitSet[] belief0;
  double beliefDiversity;
  int[][] beliefSource;
  int[][] beliefSourceCount;
  double beliefSourceDiversity;

  double[][] transferWeight;
  double[] transferWeightSum;
  int[] knowledge;
  int[] knowledge0;
  int[] knowledgeCum;
  int[] nCorrectBelief;
  int[] nCorrectBelief0;
  int knowledgeBest;
  int knowledgeBestIndex;
  int knowledgeWorst;
  double knowledgeBestSourceDiversity;
  double knowledgeMinMax;
  double[] contributionOf;
  double[] contributionOfPositive;
  double[] contributionOfNegative;
  int[] rank;
  int[] rankKnowledge;
  int[] rank0;
  int[] rank0Knowledge;
  double[] rank0Contribution;
  double[] rank0ContributionPositive;
  double[] rank0ContributionNegative;

  BitSet[] network;
  List<Integer>[] neighborList;
  int[] degree;
  int[] groupOf;

  double knowledgeAvg;

  double centralization;
  double efficiency;

  Scenario(int networkType, double beta, double pSharing) {
    this.networkType = networkType;
    this.beta = beta;
    this.pSharing = pSharing;
    isRatio = Main.IS_RATIO;
    isOneOnOne = Main.IS_ONE_ON_ONE;
    initialize();
  }

  Scenario(int networkType, double beta, double pSharing, boolean isRatio, boolean isOneOnOne) {
    this.networkType = networkType;
    this.beta = beta;
    this.pSharing = pSharing;
    this.isRatio = isRatio;
    this.isOneOnOne = isOneOnOne;
    initialize();
  }

  void initialize() {
    initializeInstrument();
    initializeRealityIndividual();
    initializeNetwork();
    initializeRank();
    setOutcome();
  }

  void initializeInstrument() {
    r = new MersenneTwister();
    focalIndexArray = new int[Main.N];
    targetIndexArray = new int[Main.N];
    int nDyad = Main.N * (Main.N - 1) / 2;
    dyadIndexArray = new int[nDyad];
    dyad2DIndexArray = new int[nDyad][2];
    mIndexArray = new int[Main.M];
    for (int n = 0; n < Main.N; n++) {
      focalIndexArray[n] = n;
      targetIndexArray[n] = n;
    }
    int d = 0;
    for (int i = 0; i < Main.N; i++) {
      for (int j = i; j < Main.N; j++) {
        if (i == j) {
          continue;
        }
        dyadIndexArray[d] = d;
        dyad2DIndexArray[d][0] = i;
        dyad2DIndexArray[d][1] = j;
        d++;
      }
    }
    for (int m = 0; m < Main.M; m++) {
      mIndexArray[m] = m;
    }
  }

  public void initializeRealityIndividual() {
    reality = new BitSet(Main.M);
    belief = new BitSet[Main.N];
    belief0 = new BitSet[Main.N];
    beliefSource = new int[Main.N][Main.M];
    beliefSourceCount = new int[Main.N][Main.N];
    nCorrectBelief = new int[Main.N];
    nCorrectBelief0 = new int[Main.N];
    knowledge = new int[Main.N];
    knowledgeAvg = 0;
    knowledgeBest = Integer.MIN_VALUE;
    knowledgeWorst = Integer.MAX_VALUE;
    pSharingOf = new double[Main.N];

    for (int n : focalIndexArray) {
      belief[n] = new BitSet(Main.M);
    }

    for (int m : mIndexArray) {
      if (r.nextBoolean()) {
        reality.set(m);
      } else {
        reality.clear(m);
      }
      for (int n : focalIndexArray) {
        if (r.nextBoolean()) {
          belief[n].set(m);
        } else {
          belief[n].clear(m);
        }
        beliefSource[n][m] = n;
      }
    }
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      belief0[focal] = (BitSet) belief[focal].clone();
      setKnowledgeAndNCorrectBelief(focal);
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
    } else { //!isRatio
      for (int n : focalIndexArray) {
        pSharingOf[n] = pSharing;
      }
    }
  }

  void initializeNetwork() {
    network = new BitSet[Main.N];
    neighborList = new ArrayList[Main.N];
    degree = new int[Main.N];
    float[][] shortestDistance; // faster than double[][]
    boolean[] isPresent;
    efficiency = 0;
    groupOf = new int[Main.N];
    for (int n = 0; n < Main.N; n++) {
      network[n] = new BitSet(Main.N);
      neighborList[n] = new ArrayList<>();
    }
    switch (networkType) {
      case 0:
        // Random spanning tree
        isPresent = new boolean[Main.N];
        shuffleFisherYates(focalIndexArray);
        isPresent[focalIndexArray[0]] = true;
        isPresent[focalIndexArray[1]] = true;
        network[focalIndexArray[0]].set(focalIndexArray[1]);
        network[focalIndexArray[1]].set(focalIndexArray[0]);
        degree[focalIndexArray[0]]++;
        degree[focalIndexArray[1]]++;
        for (int focal : focalIndexArray) {
          if (isPresent[focal]) {
            continue;
          }
          shuffleFisherYates(targetIndexArray);
          for (int target2Link : targetIndexArray) {
            if (isPresent[target2Link]) {
              isPresent[focal] = true;
              network[focal].set(target2Link);
              network[target2Link].set(focal);
              degree[focal]++;
              degree[target2Link]++;
              break;
            }
          }
        }
        for (int focal : focalIndexArray) {
          for (int target = focal; target < Main.N; target++) {
            if (focal == target || network[focal].get(target)) {
              continue;
            }
            if (r.nextDouble() < beta) {
              network[focal].set(target);
              network[target].set(focal);
              degree[focal]++;
              degree[target]++;
            }
          }
        }
        break;
      case 1:
        // Cavemen
        for (int group = 0; group < Main.N_OF_GROUP; group++) {
          for (int focalInGroup = 0; focalInGroup < Main.N_IN_GROUP; focalInGroup++) {
            int focal = group * Main.N_IN_GROUP + focalInGroup;
            groupOf[focal] = group;
            degree[focal] = Main.N_IN_GROUP - 1;
            for (int targetInGroup = 0; targetInGroup < Main.N_IN_GROUP; targetInGroup++) {
              int target = group * Main.N_IN_GROUP + targetInGroup;
              if (focal == target) {
                continue;
              }
              network[focal].set(target);
              network[target].set(focal);
            }
          }
        }
        //Limited spanning between group
        for (int group = 0; group < Main.N_OF_GROUP; group++) {
          int firstInThisGroup = group * Main.N_IN_GROUP; // First one in each group
          int secondInThisGroup = firstInThisGroup + 1; // Second one in each group
          int firstInNextGroup = (firstInThisGroup + Main.N_IN_GROUP) % Main.N; //First one in the next group
          network[firstInThisGroup].clear(secondInThisGroup);
          network[secondInThisGroup].clear(firstInThisGroup);
          network[secondInThisGroup].set(firstInNextGroup);
          network[firstInNextGroup].set(secondInThisGroup);
        }
        //Rewiring
        shuffleFisherYates(focalIndexArray);
        for (int focal : focalIndexArray) {
          int focalGroup = groupOf[focal];
          int inUnitLast = (focalGroup + 1) * Main.N_IN_GROUP;
          for (int targetInUnit = focalGroup * Main.N_IN_GROUP; targetInUnit < inUnitLast; targetInUnit++) {
            if (network[focal].get(targetInUnit) && degree[targetInUnit] > 1 && r.nextDouble() < beta / Main.GAMMA) {
              shuffleFisherYates(targetIndexArray);
              for (int targetOutUnit : targetIndexArray) {
                if (!network[focal].get(targetOutUnit) && focalGroup != groupOf[targetOutUnit]) {
                  network[focal].clear(targetInUnit);
                  network[targetInUnit].clear(focal);
                  network[focal].set(targetOutUnit);
                  network[targetOutUnit].set(focal);
                  degree[targetInUnit]--;
                  degree[targetOutUnit]++;
                  break;
                }
              }
            }
          }
        }
        break;
      case 2: // Preferential Attachement
        //Starting lattice
//        int degreeEach = Main.N_IN_GROUP - 1;
        int degreeEach = Main.N_OF_GROUP;
        int degreeSum = degreeEach * Main.N;
        int[] tieIndexArray = new int[degreeSum];
        int[][] tieFromTo = new int[degreeSum][2];
        double[] gravity = new double[Main.N];
        int tieID = 0;
        for (int focal : focalIndexArray) {
          degree[focal] = degreeEach;
          gravity[focal] = FastMath.exp(degree[focal] / Main.TAU);
          for (int i = 0; i < degreeEach; i++) {
            int target = (focal + i + 1) % Main.N;
            tieIndexArray[tieID] = tieID;
            tieFromTo[tieID][0] = focal;
            tieFromTo[tieID][1] = target;
            network[focal].set(target);
            network[target].set(focal);
            tieID++;
          }
        }
        //Rewiring by Gravity
        shuffleFisherYates(tieIndexArray);
        for (int focalTie : tieIndexArray) {
          int tieFrom = tieFromTo[focalTie][0];
          int tieToOld = tieFromTo[focalTie][1];
          if (degree[tieToOld] > 1 && r.nextDouble() < beta) { // FIXED 240415 [tieFrom] -> [tieTo]
            boolean[] isCandidateTo = new boolean[Main.N];
            double marker = r.nextDouble();
            double gravitySum = 0;
            double accumulatedProbabiltiy = 0;
            for (int target : targetIndexArray) {
              if (!network[tieFrom].get(target) && tieFrom != target // FIXED 240415: removed degree[target] > 1
              ) {
                isCandidateTo[target] = true;
                gravitySum += gravity[target];
              }
            }
            for (int tieToNew : targetIndexArray) {
              if (isCandidateTo[tieToNew]) {
                accumulatedProbabiltiy += gravity[tieToNew] / gravitySum;
                if (marker < accumulatedProbabiltiy) {
                  network[tieFrom].clear(tieToOld);
                  network[tieToOld].clear(tieFrom);
                  degree[tieToOld]--;
                  gravity[tieToOld] = FastMath.exp(degree[tieToNew] / Main.TAU);
                  network[tieFrom].set(tieToNew);
                  network[tieToNew].set(tieFrom);
                  degree[tieToNew]++;
                  gravity[tieToNew] = FastMath.exp(degree[tieToNew] / Main.TAU);
                  break;
                }
              }
            }
          }
        }
    }
    for (int i = 0; i < Main.N; i++) {
      for (int j = network[i].nextSetBit(i + 1); j >= 0; j = network[i].nextSetBit(j + 1)) {
        neighborList[i].add(j);
        neighborList[j].add(i);
      }
    }

    shortestDistance = new float[Main.N][Main.N];
    for (int i = 0; i < Main.N; i++) {
      for (int j = 0; j < Main.N; j++) {
        if (network[i].get(j)) {
          shortestDistance[i][j] = 1;
        } else {
          shortestDistance[i][j] = Main.N; // Impossible distance
          //Do not try to work with it 0 = INF.
        }
      }
      shortestDistance[i][i] = 0; // Do not delete this line
    }

    for (int k = 0; k < Main.N; k++) {
      for (int i = 0; i < Main.N; i++) {
        for (int j = 0; j < Main.N; j++) {
          if (shortestDistance[i][k] + shortestDistance[k][j] < shortestDistance[i][j]) {
            shortestDistance[i][j] = shortestDistance[i][k] + shortestDistance[k][j];
          }
        }
      }
    }

    for (int i = 0; i < Main.N; i++) {
      for (int j = 0; j < Main.N; j++) {
        if (shortestDistance[i][j] == Main.N) {
          shortestDistance[i][j] = 0;
        }
      }
    }

    efficiency = 0;
    for (int focal : focalIndexArray) {
      for (int target = focal + 1; target < Main.N; target++) {
        efficiency += 1D / shortestDistance[focal][target];
      }
    }
    efficiency /= Main.N * (Main.N - 1D) / 2D;
  }

  void initializeRank(){
    setRank();
    rank0 = rank.clone();
    rank0Knowledge = rankKnowledge.clone();
  }

  void stepForward() {
    if (isNotConverged) {
      doLearning();
      setOutcome();
    }
  }

  record Candidate(int index, double weight) {}

  void doLearning() {
    isNotConverged = false;
    int[] numTransferred = new int[Main.N];
    shuffleFisherYates(focalIndexArray);

    for (int focal : focalIndexArray) {
        if (numTransferred[focal] < Main.MAX_TRANSFER) {
        List<Candidate> candidates = new ArrayList<>();
        double weightSum = 0;
        shuffleFisherYates(neighborList[focal]);
        for (int target : neighborList[focal]) {
          if( numTransferred[target] >= Main.MAX_TRANSFER || r.nextDouble() < Main.P_ACCEPT ){
            continue;
          }
          double diff = knowledge[focal] - knowledge[target];
          if (diff != 0) {
            isNotConverged = true;
          }
          diff = FastMath.abs(diff);
          double w = (knowledge[focal] > knowledge[target]) ? diff * pSharingOf[focal] : diff * (1D - pSharingOf[focal]);
          if( w > 0 ){
            candidates.add(new Candidate(target, w));
            weightSum += w;
          }
        }
        if (weightSum == 0) {
          continue;
        }
        double marker = r.nextDouble();
        double acc = 0;
        for (Candidate c : candidates) {
          acc += c.weight() / weightSum;
          if (acc > marker) {
            int target = c.index();
            int source = (knowledge[focal] > knowledge[target]) ? focal : target;
            int recipient = (source == focal) ? target : focal;
            for (int m : mIndexArray) {
              if (r.nextDouble() < Main.P_LEARNING) {
                boolean bit = belief[source].get(m);
                if (bit) {
                  belief[recipient].set(m);
                } else {
                  belief[recipient].clear(m);
                }
                int oldSource = beliefSource[recipient][m];
                int newSource = beliefSource[source][m];
                if (oldSource != newSource) {
                  beliefSourceCount[recipient][oldSource]--;
                  beliefSource[recipient][m] = newSource;
                  beliefSourceCount[recipient][newSource]++;
                }
              }
            }
            setKnowledgeAndNCorrectBelief(recipient);
            numTransferred[focal]++;
            numTransferred[target]++;
            break;
          }
        }
      }
    }
    for (int focal : focalIndexArray) {
      knowledgeCum[focal] += knowledge[focal];
    }
  }

  void setKnowledgeAndNCorrectBelief(int focal) {
    int knowledgeFocal = 0;
    buffer.clear();
    buffer.or(belief[focal]);
    buffer.xor(reality);
    nCorrectBelief[focal] = buffer.cardinality();
    if( Main.S != 1 ){
      for (int m = 0; m < Main.M; m += Main.S) {
        int mismatchIndex = buffer.nextSetBit(m);
        if (mismatchIndex == -1 || mismatchIndex >= m + Main.S) {
          knowledgeFocal += Main.S;
        }
      }
    }else{
      knowledgeFocal = nCorrectBelief[focal];
    }
    knowledge[focal] = knowledgeFocal;
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
    rankKnowledge = new int[Main.N];
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
      int rankFocal = rank[focal];
      rankKnowledge[rankFocal] = knowledge[focal];
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
      BitSet beliefFocal = belief[focal];
      for (int target = focal + 1; target < Main.N; target++) {
        beliefDiversity += getBitDifferenceCount(beliefFocal, belief[target]);
      }
    }
    beliefDiversity /= Main.M_N;
  }

  int getBitDifferenceCount(BitSet a, BitSet b){
    buffer.clear();
    buffer.or(a);
    buffer.xor(b);
    return buffer.cardinality();
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
    contributionOf = new double[Main.N];
    contributionOfPositive = new double[Main.N];
    contributionOfNegative = new double[Main.N];

    rank0Contribution = new double[Main.N];
    rank0ContributionPositive = new double[Main.N];
    rank0ContributionNegative = new double[Main.N];

    for (int focal : focalIndexArray) {
      for (int m : mIndexArray) {
        int source = beliefSource[focal][m];
        contributionOf[source]++;
        if (reality.get(m) == belief[focal].get(m)) {
          contributionOfPositive[source]++;
        } else {
          contributionOfNegative[source]++;
        }
      }
    }
    for (int focal : focalIndexArray) {
      contributionOf[focal] /= Main.M_N;
      contributionOfPositive[focal] /= Main.M_N;
      contributionOfNegative[focal] /= Main.M_N;
    }
    for (int focal : focalIndexArray) {
      int rank0Focal = rank0[focal];
      rank0Contribution[rank0Focal] = contributionOf[focal];
      rank0ContributionPositive[rank0Focal] = contributionOfPositive[focal];
      rank0ContributionNegative[rank0Focal] = contributionOfNegative[focal];
    }
  }

  void setCentralization() {
    centralization = 0;
    double[] centrality = new double[Main.N];
    double maxCentrality = Double.MIN_VALUE;

    for (int focal : focalIndexArray) {
      for (int target : targetIndexArray) {
        centrality[focal] += beliefSourceCount[target][focal];
      }
    }

    for (int focal : focalIndexArray) {
      centrality[focal] /= Main.M_N;
      if (centrality[focal] > maxCentrality) {
        maxCentrality = centrality[focal];
      }
    }

    for (int focal : focalIndexArray) {
      centralization += (maxCentrality - centrality[focal]);
    }

    centralization /= (double) Main.N; // Theoretical maximum of Sum[Cx(p*)-Cx(pi)] over 1:N
  }

  void shuffleFisherYates(int[] nArray) {
    for (int i = nArray.length - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      int temp = nArray[i];
      nArray[i] = nArray[j];
      nArray[j] = temp;
    }
  }

  void shuffleFisherYates(List<Integer> nList) {
    for (int i = nList.size() - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      int temp = nList.get(i);
      nList.set(i, nList.get(j));
      nList.set(j, temp);
    }
  }

  void printCSV(String fileName) {
    try {
      FileWriter csvWriter;
      csvWriter = new FileWriter(fileName + ".csv");
      csvWriter.append("SOURCE");
      csvWriter.append(",");
      csvWriter.append("TARGET");
      csvWriter.append(",");
      csvWriter.append("SOURCE_P_SHARING");
      csvWriter.append(",");
      csvWriter.append("TARGET_P_SHARING");
      csvWriter.append(",");
      csvWriter.append("SOURCE_UNIT");
      csvWriter.append(",");
      csvWriter.append("SOURCE_INIT_KNOWLEDGE");
      csvWriter.append(",");
      csvWriter.append("SOURCE_CONTRIBUTION");
      csvWriter.append(",");
      csvWriter.append("SOURCE_CONTRIBUTION_POS");
      csvWriter.append(",");
      csvWriter.append("SOURCE_CONTRIBUTION_NEG");
      csvWriter.append(",");
      csvWriter.append("IS_CONNECTED");
      csvWriter.append(",");
      csvWriter.append("SOURCE_DEGREE");
      csvWriter.append(",");
      csvWriter.append("CONTRIBUTION");
      csvWriter.append("\n");

      //Edge
      for (int focal = 0; focal < Main.N; focal++) {
        for (int target = 0; target < Main.N; target++) {
          if (focal == target) {
            continue;
          }
//                    csvWriter.append("SOURCE");
          csvWriter.append(Integer.toString(focal));
          csvWriter.append(",");
//                    csvWriter.append("TARGET");
          csvWriter.append(Integer.toString(target));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_P_SHARING");
          csvWriter.append(Double.toString(this.pSharingOf[focal]));
          csvWriter.append(",");
//                    csvWriter.append("TARGET_P_SHARING");
          csvWriter.append(Double.toString(this.pSharingOf[target]));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_UNIT");
          csvWriter.append(Integer.toString(this.groupOf[focal]));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_INIT_KNOWLEDGE");
          csvWriter.append(Double.toString(this.knowledge0[focal] / (double) Main.M));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_CONTRIBUTION");
          csvWriter.append(Double.toString(this.contributionOf[focal]));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_CONTRIBUTION_POS");
          csvWriter.append(Double.toString(this.contributionOfPositive[focal]));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_CONTRIBUTION_NEG");
          csvWriter.append(Double.toString(this.contributionOfNegative[focal]));
          csvWriter.append(",");
//                    csvWriter.append("IS_CONNECTED");
          csvWriter.append(Integer.toString(this.network[focal].get(target) ? 1 : 0));
          csvWriter.append(",");
//                    csvWriter.append("SOURCE_DEGREE");
          csvWriter.append(Integer.toString(this.degree[focal]));
          csvWriter.append(",");
//                    csvWriter.append("CONTRIBUTION");
          csvWriter.append(Double.toString(this.beliefSourceCount[target][focal] / (double) Main.M)); // Changed 221002 [focal][target] to [target][focal]
          csvWriter.append("\n");
        }
      }
      csvWriter.flush();
      csvWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
