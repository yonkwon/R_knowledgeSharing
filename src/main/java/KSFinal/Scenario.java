package KSFinal;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Scenario {

  RandomGenerator r;

  boolean isRatio;
  int networkType;
  double beta;
  double pSharing;
  double[] pSharingOf;

  boolean isNotConverged = true;
  int[] focalIndexArray;
  int[] targetIndexArray;
  int[] mIndexArray;

  int nSharer;
  int nSeeker;

  BitSet reality;
  BitSet[] belief;
  BitSet[] belief0;
  int[][] beliefSource;
  int[][] beliefSourceCount;
  double beliefSourceDiversity;
  double beliefDiversity;
  int[] nCorrectBelief0;
  int[] nIncorrectBelief0;

  boolean[][][] isExposedToSourcePotential;
  boolean[][][] isExposedToSourceEffective;
  int[][] countPotentialExposure;
  int[][] countEffectiveExposure;
  double potentialExposure;
  double effectiveExposure;

  int[] knowledge;
  int[] knowledge0;
  double[] contributionOf;
  double[] contributionOfPositive;
  double[] contributionOfNegative;
  int[] rank;
  int[] rankKnowledge;
  double[] rank0Contribution;
  double[] rank0ContributionPositive;
  double[] rank0ContributionNegative;

  BitSet[] network;
  int[][] neighborList;
  int[] degree;
  int[] groupOf;

  double performance;
  double centralization;
  double connectedness;

  Scenario(boolean isRatio, int networkType, double beta, double pSharing) {
    this.isRatio = isRatio;
    this.networkType = networkType;
    this.beta = beta;
    this.pSharing = pSharing;
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
    reality = new BitSet(Main.M);
    belief = new BitSet[Main.N];
    belief0 = new BitSet[Main.N];
    beliefSource = new int[Main.N][Main.M];
    beliefSourceCount = new int[Main.N][Main.N];
    nCorrectBelief0 = new int[Main.N];
    nIncorrectBelief0 = new int[Main.N];
    knowledge = new int[Main.N];
    performance = 0;
    pSharingOf = new double[Main.N];

    for (int m : mIndexArray) {
      if (r.nextBoolean()) reality.set(m);
    }

    for (int n : focalIndexArray) {
      belief[n] = new BitSet(Main.M);
      for (int m : mIndexArray) {
        if (r.nextBoolean()) belief[n].set(m);
        beliefSource[n][m] = n;
        if (belief[n].get(m) == reality.get(m)) nCorrectBelief0[n]++;
        else nIncorrectBelief0[n]++;
      }
    }

    for (int focal : focalIndexArray) {
      belief0[focal] = (BitSet) belief[focal].clone();
      setKnowledgeOf(focal);
      performance += knowledge[focal];
      beliefSourceCount[focal][focal] = Main.M;
    }

    knowledge0 = knowledge.clone();
    performance /= Main.M_N;

    if (isRatio) {
      nSharer = (int) (pSharing * Main.N);
      nSeeker = Main.N - nSharer;
      shuffleFisherYates(focalIndexArray);
      for (int n = 0; n < nSharer; n++) pSharingOf[focalIndexArray[n]] = 1D;
      for (int n = nSharer; n < Main.N; n++) pSharingOf[focalIndexArray[n]] = 0D;
    } else {
      for (int n = 0; n < Main.N; n++) pSharingOf[n] = pSharing;
    }
  }

  void initializeNetwork() {
    network = new BitSet[Main.N];
    degree = new int[Main.N];
    groupOf = new int[Main.N];
    for (int i = 0; i < Main.N; i++) network[i] = new BitSet(Main.N);

    switch (networkType) {
      case 0:
        initializeNetworkRandomSpanningTree();
        break;
      case 1:
        initializeNetworkCavemen();
        break;
      case 2:
        initializeNetworkPreferentialAttachment();
        break;
    }
    setNeighborList();
    connectedness = getConnectedness();
  }

  void addEdge(int i, int j) {
    if (network[i].get(j)) return;
    network[i].set(j);
    network[j].set(i);
    degree[i]++;
    degree[j]++;
  }

  void removeEdge(int i, int j) {
    if (network[i].get(j)) {
      network[i].clear(j);
      network[j].clear(i);
      degree[i]--;
      degree[j]--;
    }
  }

  void setNeighborList() {
    neighborList = new int[Main.N][];
    for (int focal : focalIndexArray) {
      neighborList[focal] = new int[degree[focal]];
      BitSet networkFocal = network[focal];
      int neighborListIndex = 0;
      for (int target = networkFocal.nextSetBit(0); target >= 0; target = networkFocal.nextSetBit(target + 1)) {
        neighborList[focal][neighborListIndex] = target;
        neighborListIndex++;
      }
    }
  }

  void initializeNetworkRandomSpanningTree() {
    boolean[] isPresent = new boolean[Main.N];
    shuffleFisherYates(focalIndexArray);
    isPresent[focalIndexArray[0]] = true;
    isPresent[focalIndexArray[1]] = true;
    addEdge(focalIndexArray[0], focalIndexArray[1]);
    for (int focal : focalIndexArray) {
      if (isPresent[focal]) continue;
      shuffleFisherYates(targetIndexArray);
      for (int target2Link : targetIndexArray) {
        if (isPresent[target2Link]) {
          isPresent[focal] = true;
          addEdge(focal, target2Link);
          break;
        }
      }
    }
    for (int i = 0; i < Main.N; i++) {
      for (int j = i + 1; j < Main.N; j++) {
        if (!network[i].get(j) && r.nextDouble() < beta) addEdge(i, j);
      }
    }
  }

  void initializeNetworkCavemen() {
    for (int group = 0; group < Main.N_OF_GROUP; group++) {
      for (int focalInGroup = 0; focalInGroup < Main.N_IN_GROUP; focalInGroup++) {
        int focal = group * Main.N_IN_GROUP + focalInGroup;
        groupOf[focal] = group;
        for (int targetInGroup = 0; targetInGroup < Main.N_IN_GROUP; targetInGroup++) {
          int target = group * Main.N_IN_GROUP + targetInGroup;
          if (focal != target) addEdge(focal, target);
        }
      }
    }
    for (int group = 0; group < Main.N_OF_GROUP; group++) {
      int firstInThisGroup = group * Main.N_IN_GROUP;
      int secondInThisGroup = firstInThisGroup + 1;
      int firstInNextGroup = (firstInThisGroup + Main.N_IN_GROUP) % Main.N;
      removeEdge(firstInThisGroup, secondInThisGroup);
      addEdge(secondInThisGroup, firstInNextGroup);
    }
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      int focalGroup = groupOf[focal];
      int inUnitLast = (focalGroup + 1) * Main.N_IN_GROUP;
      for (int targetInUnit = focalGroup * Main.N_IN_GROUP; targetInUnit < inUnitLast; targetInUnit++) {
        if (network[focal].get(targetInUnit) && degree[targetInUnit] > 1 && r.nextDouble() < beta) {
          shuffleFisherYates(targetIndexArray);
          for (int targetOutUnit : targetIndexArray) {
            if (!network[focal].get(targetOutUnit) && focalGroup != groupOf[targetOutUnit]) {
              removeEdge(focal, targetInUnit);
              addEdge(focal, targetOutUnit);
              break;
            }
          }
        }
      }
    }
  }

  void initializeNetworkPreferentialAttachment() {
    int degreeEach = Main.Z;
    int degreeSum = degreeEach * Main.N;
    int[][] tieFromTo = new int[degreeSum][2];
    double[] gravity = new double[Main.N];
    int tieID = 0;
    for (int focal : focalIndexArray) {
      gravity[focal] = Math.exp(degreeEach);
      for (int i = 0; i < degreeEach; i++) {
        int target = (focal + i + 1) % Main.N;
        tieFromTo[tieID][0] = focal;
        tieFromTo[tieID][1] = target;
        addEdge(focal, target);
        tieID++;
      }
    }
    int[] tieIndexArray = new int[tieID];
    for (int i = 0; i < tieID; i++) tieIndexArray[i] = i;
    shuffleFisherYates(tieIndexArray);
    for (int focalTie : tieIndexArray) {
      int tieFrom = tieFromTo[focalTie][0];
      int tieToOld = tieFromTo[focalTie][1];
      if (degree[tieToOld] > 1 && r.nextDouble() < beta) {
        boolean[] isCandidate = new boolean[Main.N];
        double gravitySum = 0;
        for (int target : targetIndexArray) {
          if (!network[tieFrom].get(target) && tieFrom != target) {
            isCandidate[target] = true;
            gravitySum += gravity[target];
          }
        }
        double marker = r.nextDouble();
        double accProb = 0;
        for (int tieToNew : targetIndexArray) {
          if (isCandidate[tieToNew]) {
            accProb += gravity[tieToNew] / gravitySum;
            if (marker < accProb) {
              removeEdge(tieFrom, tieToOld);
              addEdge(tieFrom, tieToNew);
              gravity[tieToOld] = Math.exp(degree[tieToOld]);
              gravity[tieToNew] = Math.exp(degree[tieToNew]);
              break;
            }
          }
        }
      }
    }
  }

  double getConnectedness() {
    double total = 0;
    for (int start : focalIndexArray) {
      int[] dist = getShortestDistance(start);
      for (int j = start + 1; j < Main.N; j++) {
        if (dist[j] > 0) total += 1.0 / dist[j];
      }
    }
    return total / (Main.N * (Main.N - 1.0) / 2.0);
  }

  int[] getShortestDistance(int start) {
    int[] dist = new int[Main.N];
    Arrays.fill(dist, -1);
    Queue<Integer> q = new ArrayDeque<>();
    dist[start] = 0;
    q.add(start);
    while (!q.isEmpty()) {
      int u = q.poll();
      for (int v = network[u].nextSetBit(0); v >= 0; v = network[u].nextSetBit(v + 1)) {
        if (dist[v] == -1) {
          dist[v] = dist[u] + 1;
          q.add(v);
        }
      }
    }
    return dist;
  }

  void stepForward() {
    if (isNotConverged) {
      doLearning();
      setOutcome();
      isNotConverged = !getConvergence();
    }
  }

  boolean getConvergence() {
    for (int focal = 0; focal < Main.N; focal++) {
      int focalKnowledge = knowledge[focal];
      for (int target : neighborList[focal] ) {
        if (focalKnowledge != knowledge[target]) return false;
      }
    }
    return true;
  }

  void doLearning() {
    int[] numTransferred = new int[Main.N];
    isExposedToSourceEffective = new boolean[Main.N][Main.M][Main.N];
    isExposedToSourcePotential = new boolean[Main.N][Main.M][Main.N];
    countPotentialExposure = new int[Main.N][Main.M];
    countEffectiveExposure = new int[Main.N][Main.M];
    List<int[]> queue = new ArrayList<>();
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      List<int[]> candidate = new ArrayList<>();
      shuffleFisherYates(neighborList[focal]);
      if (r.nextDouble() < pSharingOf[focal]) {
        for (int target : neighborList[focal]) {
          for( int m : mIndexArray ){
            isExposedToSourcePotential[target][m][beliefSource[focal][m]] = true;
          }
          if (knowledge[focal] > knowledge[target]) {
            for( int m : mIndexArray ){
              isExposedToSourceEffective[target][m][beliefSource[focal][m]] = true;
            }
            candidate.add(new int[]{focal, target});
          }
        }
      } else {
        for (int target : neighborList[focal]) {
          for( int m : mIndexArray ){
            isExposedToSourcePotential[focal][m][beliefSource[target][m]] = true;
          }
          if (knowledge[focal] < knowledge[target]) {
            for( int m : mIndexArray ){
              isExposedToSourceEffective[focal][m][beliefSource[target][m]] = true;
            }
            candidate.add(new int[]{target, focal});
          }
        }
      }
      if(!candidate.isEmpty()){
        int idx = r.nextInt(candidate.size());
        queue.add(candidate.get(idx));
      }
    }

    for( int focal : focalIndexArray ){
      for( int m : mIndexArray ){
        for( int target : targetIndexArray ){
          if( isExposedToSourcePotential[focal][m][target] ){
            countPotentialExposure[focal][m] ++;
          }
          if( isExposedToSourceEffective[focal][m][target] ){
            countEffectiveExposure[focal][m] ++;
          }
        }
      }
    }

    Collections.shuffle(queue); // added later
    List<int[]> ops = new ArrayList<>();
    for (int[] q : queue) {
      int from = q[0], to = q[1];
      if (numTransferred[from] < Main.T_MAX &&
        numTransferred[to] < Main.T_MAX) {
        ops.add(q);
        numTransferred[from]++;
        numTransferred[to]++;
      }
    }

    for (int[] op : ops) {
      doKnowledgeTransfer(op[0], op[1]);
    }
  }

  void doKnowledgeTransfer(int source, int recipient) {
    BitSet diff = (BitSet) belief[source].clone();
    diff.xor(belief[recipient]);
    for (int m = diff.nextSetBit(0); m >= 0; m = diff.nextSetBit(m + 1)) {
      if (r.nextDouble() < Main.P_LEARNING) {
        if (belief[source].get(m)) belief[recipient].set(m);
        else belief[recipient].clear(m);
        int oldSource = beliefSource[recipient][m];
        beliefSourceCount[recipient][oldSource]--;
        beliefSource[recipient][m] = beliefSource[source][m];
        beliefSourceCount[recipient][beliefSource[recipient][m]]++;
      }
      setKnowledgeOf(recipient);
    }
  }

  void setKnowledgeOf(int focal) { // FIXED 250825
    knowledge[focal] = 0;
    BitSet diff = (BitSet) belief[focal].clone();
    diff.xor(reality);
    for (int bundleStart = 0; bundleStart < Main.M; bundleStart += Main.S) {
      int bundleEnd = bundleStart + Main.S;
      int wrongBeliefIndex = diff.nextSetBit(bundleStart);
      if (wrongBeliefIndex < 0 || wrongBeliefIndex >= bundleEnd) {
        knowledge[focal] += Main.S;
      }
    }
  }

  double getBeliefSourceDiversityOf(int focal) {
    int sumSq = 0;
    for (int target : targetIndexArray) {
      sumSq += beliefSourceCount[focal][target] * beliefSourceCount[focal][target];
    }
    return 1D - (sumSq / (double) (Main.M * Main.M));
  }

  void initializeRank() {
    rank = new int[Main.N];
    rankKnowledge = new int[Main.N];
    for (int i = 0; i < Main.N; i++) {
      for (int j = i + 1; j < Main.N; j++) {
        if (knowledge[i] <= knowledge[j]) rank[i]++;
        else rank[j]++;
      }
    }
    for (int focal : focalIndexArray) {
      int rankOf = rank[focal];
      rankKnowledge[rankOf] = knowledge[focal];
    }
  }

  void setOutcome() {
    setFirmPerformance();
    setBeliefDiversity();
    setBeliefSourceDiversity();
    setContribution();
    setCentralization();
    setExposure();
  }

  void setFirmPerformance() {
    performance = 0;
    for (int focal : focalIndexArray) performance += knowledge[focal];
    performance /= Main.M_N;
  }

  void setBeliefDiversity() {
    long diffSum = 0;
    for (int focal : focalIndexArray) {
      for (int target = focal + 1; target < Main.N; target++) {
        BitSet diff = (BitSet) belief[focal].clone();
        diff.xor(belief[target]);
        diffSum += diff.cardinality();
      }
    }
    beliefDiversity = diffSum / (double) Main.M_N_PAIR;
  }

  void setBeliefSourceDiversity() {
    beliefSourceDiversity = 0;
    for (int focal : focalIndexArray) {
      beliefSourceDiversity += getBeliefSourceDiversityOf(focal);
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
        if (reality.get(m) == belief[focal].get(m)) contributionOfPositive[source]++;
        else contributionOfNegative[source]++;
      }
    }
    for (int focal : focalIndexArray) {
      contributionOf[focal] /= Main.M_N;
      contributionOfPositive[focal] /= Main.M_N;
      contributionOfNegative[focal] /= Main.M_N;
    }
    for (int focal : focalIndexArray) {
      int rankFocal = rank[focal];
      rank0Contribution[rankFocal] = contributionOf[focal];
      rank0ContributionPositive[rankFocal] = contributionOfPositive[focal];
      rank0ContributionNegative[rankFocal] = contributionOfNegative[focal];
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
      if (centrality[focal] > maxCentrality) maxCentrality = centrality[focal];
    }
    for (int focal : focalIndexArray) {
      centralization += (maxCentrality - centrality[focal]);
    }
    centralization /= (Main.N - 1);
  }

  void setExposure(){
    potentialExposure = 0;
    effectiveExposure = 0;
    for( int focal : focalIndexArray ){
      for( int m : mIndexArray ){
        potentialExposure += countPotentialExposure[focal][m];
        effectiveExposure += countEffectiveExposure[focal][m];
      }
    }
    potentialExposure /= Main.M_N;
    effectiveExposure /= Main.M_N;
  }

  void shuffleFisherYates(int[] arr) {
    for (int i = arr.length - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      int tmp = arr[i];
      arr[i] = arr[j];
      arr[j] = tmp;
    }
  }

  void printCSV(String fileName) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + ".csv"))) {
      StringBuilder sb = new StringBuilder();
      sb.append("SOURCE,TARGET,SOURCE_UNIT,SOURCE_P_SHARING,SOURCE_INIT_KNOWLEDGE,");
      sb.append("SOURCE_CONTRIBUTION,SOURCE_CONTRIBUTION_POS,SOURCE_CONTRIBUTION_NEG,");
      sb.append("IS_CONNECTED,SOURCE_DEGREE,CONTRIBUTION\n");
      for (int focal = 0; focal < Main.N; focal++) {
        for (int target = 0; target < Main.N; target++) {
          if (focal == target) continue;
          sb.append(focal).append(",")
            .append(target).append(",")
            .append(groupOf[focal]).append(",")
            .append(pSharingOf[focal]).append(",")
            .append(knowledge0[focal] / (double) Main.M).append(",")
            .append(contributionOf[focal]).append(",")
            .append(contributionOfPositive[focal]).append(",")
            .append(contributionOfNegative[focal]).append(",")
            .append(network[focal].get(target) ? 1 : 0).append(",")
            .append(degree[focal]).append(",")
            .append(beliefSourceCount[target][focal] / (double) Main.M).append("\n");
        }
      }
      bw.write(sb.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
