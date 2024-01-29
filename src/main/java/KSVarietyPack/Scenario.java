package KSVarietyPack;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

public class Scenario {
  
  RandomGenerator r;
  boolean isNotConverged = true;
  
  int[] focalIndexArray;
  int[] targetIndexArray;
  int[] mIndexArray;
  
  boolean isRatio;
  boolean isOneOnOne;
  
  double beta;
  double pSharing;
  double[] pSharingOf;
  int networkType;
  
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
  double[] contributionOf;
  double[] contributionOfPositive;
  double[] contributionOfNegative;
  int[] rank;
  int[] rankKnowledge;
  double[] rankContribution;
  double[] rankContributionPositive;
  double[] rankContributionNegative;
  
  boolean[][] network;
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
    degree = new int[Main.N];
    double[][] shortestDistance;
    boolean[] isPresent;
    efficiency = 0;
    groupOf = new int[Main.N];
    switch (networkType) {
      case 0:
        // Random spanning tree
        isPresent = new boolean[Main.N];
        shuffleFisherYates(focalIndexArray);
        isPresent[focalIndexArray[0]] = true;
        isPresent[focalIndexArray[1]] = true;
        network[focalIndexArray[0]][focalIndexArray[1]] = true;
        network[focalIndexArray[1]][focalIndexArray[0]] = true;
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
              network[focal][target2Link] = true;
              network[target2Link][focal] = true;
              degree[focal]++;
              degree[target2Link]++;
              break;
            }
          }
        }
        for (int focal : focalIndexArray) {
          for (int target = focal; target < Main.N; target++) {
            if (network[focal][target] ||
                focal == target) {
              continue;
            }
            if (r.nextDouble() < beta) {
              network[focal][target] = true;
              network[target][focal] = true;
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
              network[focal][target] = true;
              network[target][focal] = true;
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
        }
        //Rewiring
        shuffleFisherYates(focalIndexArray);
        for (int focal : focalIndexArray) {
          int focalGroup = groupOf[focal];
          int inUnitLast = (focalGroup + 1) * Main.N_IN_GROUP;
          for (int targetInUnit = focalGroup * Main.N_IN_GROUP; targetInUnit < inUnitLast; targetInUnit++) {
            if (network[focal][targetInUnit] &&
                degree[targetInUnit] > 1 &&
                r.nextDouble() < beta / Main.GAMMA) {
              shuffleFisherYates(targetIndexArray);
              for (int targetOutUnit : targetIndexArray) {
                if (!network[focal][targetOutUnit] &&
                    focalGroup != groupOf[targetOutUnit]) {
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
        break;
      case 2: // Preferential Attachement
        //Starting lattice
        int degreeHalf = (Main.N_IN_GROUP - 1) / 2; // degree to the clockwise
        int degreeEach = degreeHalf * 2;
        int degreeSum = degreeEach * Main.N;
        int[] tieIndexArray = new int[degreeSum];
        int[][] fromTo = new int[degreeSum][2];
        double[] gravity = new double[Main.N];
        int d = 0;
        for (int focal : focalIndexArray) {
          degree[focal] = degreeEach;
          gravity[focal] = FastMath.exp(degree[focal] / Main.TAU);
          for (int i = 0; i < degreeHalf; i++) {
            int target = (focal + i + 1) % Main.N;
            tieIndexArray[d] = d;
            fromTo[d][0] = focal;
            fromTo[d][1] = target;
            network[focal][target] = true;
            network[target][focal] = true;
            d++;
          }
        }
        shuffleFisherYates(tieIndexArray);
        for (int tie2Rewire : tieIndexArray) {
          int focal = fromTo[tie2Rewire][0];
          int targetFrom = fromTo[tie2Rewire][1];
          if (degree[targetFrom] > 1 &&
              r.nextDouble() < beta) {
            boolean[] isCandidateTo = new boolean[Main.N];
            double gravitySum = 0;
            for (int target : targetIndexArray) {
              if (!network[focal][target] &&
                  focal != target &&
                  degree[target] > 1
              ) {
                isCandidateTo[target] = true;
                gravitySum += gravity[target];
              }
            }
            double marker = r.nextDouble();
            double accumulatedProbability = 0;
            for (int target : targetIndexArray) { //@@ Is this correct?
              if (isCandidateTo[target]) {
                accumulatedProbability += gravity[target] / gravitySum;
                if (marker < accumulatedProbability) {
                  network[focal][targetFrom] = false;
                  network[targetFrom][focal] = false;
                  degree[targetFrom]--;
                  gravity[targetFrom] = FastMath.exp(degree[target] / Main.TAU);
                  network[focal][target] = true;
                  network[target][focal] = true;
                  degree[target]++;
                  gravity[target] = FastMath.exp(degree[target] / Main.TAU);
                  break;
                }
              }
            }
          }
        }
        break;
      // old code below
        /*
        isPresent = new boolean[Main.N];
        double[] gravity;
        //Starting lattice
        for (int focal = 0; focal < Main.N; focal += Main.N_IN_GROUP) {
          int next = (focal + Main.N_IN_GROUP) % Main.N;
          isPresent[focal] = true;
          isPresent[next] = true;
          network[focal][next] = true;
          network[next][focal] = true;
          degree[focal] = 2;
        }
        //Invite one by one, randomly
        shuffleFisherYates(focalIndexArray);
        for (int focal : focalIndexArray) {
          if (isPresent[focal]) {
            continue;
          } else {
            isPresent[focal] = true;
          }
          gravity = new double[Main.N];
          boolean[] isTarget = new boolean[Main.N];
          double gravitySum = 0;
          double degreeSum = 0;
          double[] distance = new double[Main.N];
          double distanceSum = 0;
          for (int target : targetIndexArray) {
            if (isPresent[target] && focal != target) {
              isTarget[target] = true;
              degreeSum += degree[target];
              double absoluteDistance = FastMath.abs(focal - target);
              distance[target] = FastMath.min(absoluteDistance, Main.N - absoluteDistance);
              distanceSum += distance[target];
            }
          }
          for (int target : targetIndexArray) {
            if (isTarget[target]) {
//                            gravity[target] = FastMath.exp(Main.GAMMA * (beta * (degree[target] / degreeSum) + (1D - beta) * (distance[target] / distanceSum)));
              gravity[target] = FastMath.exp((beta * degree[target] / Main.GAMMA) + (1D - beta) * (distance[target]));
              gravitySum += gravity[target];
//                            System.out.println(focal+" to "+target+": "+(degree[target] / degreeSum)+" & "+(distance[target] / distanceSum)+" -> "+gravity[target]/gravitySum);
//                            System.out.println("@"+beta+" ... "+focal + " to " + target + ": " + (degree[target]) + " & " + (distance[target]) + " -> " + gravity[target]);
            }
          }
          for (int z = 1; z < Main.N_IN_GROUP; z++) {
            double marker = r.nextDouble();
            double accumulatedProbability = 0;
            for (int target : targetIndexArray) { //@@ Is this correct?
              if (isTarget[target]) {
                accumulatedProbability += gravity[target] / gravitySum;
                if (marker < accumulatedProbability) {
                  network[focal][target] = true;
                  network[target][focal] = true;
                  degree[focal]++;
                  degree[target]++;
                  gravitySum -= gravity[target];
                  gravity[target] = 0;
                  break;
                }
              }
            }
          }
        }
        break;
         */
    }
    
    shortestDistance = new double[Main.N][Main.N];
    for (int i = 0; i < Main.N; i++) {
      for (int j = 0; j < Main.N; j++) {
        if (network[i][j]) {
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
      for (int target = focal; target < Main.N; target++) {
        if (focal == target) {
          continue;
        }
        efficiency += 1D / shortestDistance[focal][target];
      }
    }
    efficiency /= Main.N * (Main.N - 1D) / 2D;
  }
  
  
  void stepForward() {
    if (isNotConverged) {
      doLearning();
      setRank();
      setOutcome();
    }
  }
  
  void doLearning() {
    boolean[] isBusy = new boolean[Main.N];
    isNotConverged = false;
    shuffleFisherYates(focalIndexArray);
    for (int focal : focalIndexArray) {
      if (isOneOnOne &&
          isBusy[focal]) {
        continue;
      }
      if (r.nextDouble() < pSharingOf[focal]) {
        shuffleFisherYates(targetIndexArray);
        for (int target : targetIndexArray) {
          if (isOneOnOne && isBusy[target]) {
            continue;
          }
          if (network[focal][target] &&
              knowledge[focal] > knowledge[target] &&
              r.nextDouble() < Main.P_ACCEPT
          ) {
            isBusy[focal] = true;
            isBusy[target] = true;
            for (int m : mIndexArray) {
              if (belief[target][m] != belief[focal][m]) {
                isNotConverged = true;
                if (r.nextDouble() < Main.P_LEARNING) {
                  belief[target][m] = belief[focal][m];
                  beliefSourceCount[target][beliefSource[target][m]]--;
                  beliefSource[target][m] = beliefSource[focal][m];
                  beliefSourceCount[target][beliefSource[target][m]]++; // Not necessarily focal
                }
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
          if (network[focal][target] &&
              knowledge[focal] < knowledge[target] &&
              r.nextDouble() < Main.P_ACCEPT
          ) {
            isBusy[focal] = true;
            isBusy[target] = true;
            for (int m : mIndexArray) {
              if (belief[target][m] != belief[focal][m]) {
                isNotConverged = true;
                if (r.nextDouble() < Main.P_LEARNING) {
                  belief[focal][m] = belief[target][m];
                  beliefSourceCount[focal][beliefSource[focal][m]]--;
                  beliefSource[focal][m] = beliefSource[target][m];
                  beliefSourceCount[focal][beliefSource[focal][m]]++;
                }
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
    contributionOf = new double[Main.N];
    contributionOfPositive = new double[Main.N];
    contributionOfNegative = new double[Main.N];
    
    rankContribution = new double[Main.N];
    rankContributionPositive = new double[Main.N];
    rankContributionNegative = new double[Main.N];
    
    for (int focal : focalIndexArray) {
      for (int m : mIndexArray) {
        int source = beliefSource[focal][m];
        contributionOf[source]++;
        if (reality[m] == belief[focal][m]) {
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
      int rankFocal = rank[focal];
      rankContribution[rank[focal]] = contributionOf[focal];
      rankContributionPositive[rank[focal]] = contributionOfPositive[focal];
      rankContributionNegative[rank[focal]] = contributionOfNegative[focal];
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
    
    for (int rank : focalIndexArray) {
      centrality[rank] /= Main.M_N;
      if (centrality[rank] > maxCentrality) {
        maxCentrality = centrality[rank];
      }
    }
    
    for (int rank : focalIndexArray) {
      centralization += (maxCentrality - centrality[rank]);
    }
    
    centralization /= (double) Main.N; // Theoretical maximum of Sum[Cx(p*)-Cx(pi)] over 1:N
  }
  
  void shuffleFisherYates(int[] nArray) {
    for (int i = Main.N - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      int temp = nArray[i];
      nArray[i] = nArray[j];
      nArray[j] = temp;
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
          csvWriter.append(Integer.toString(this.network[focal][target] ? 1 : 0));
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
