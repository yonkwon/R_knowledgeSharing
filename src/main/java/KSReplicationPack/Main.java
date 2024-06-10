package KSReplicationPack;

import java.io.File;
import java.util.HashMap;

public class Main {

  //Output Setup
  static final boolean GET_NET = true; // Print *.csv file describing network structure if true
  static final boolean GET_MAT = true; // Print *.mat file with outcome variables if true

  //Computation Parameters
  static int ITERATION = 100_000;

  //Global Parameters
  static int M = 100;
  static int S = 5;
  static int TIME = 500 + 1;

  static int N = 50;
  static int N_PRIME = 5; // an odd number
  static int N_FACTOR = N / N_PRIME;

  //Moving Params
  static double[] BETA = {0, .05, .1, .15, .2, .25, .3, .35, .4, .45, .5, .55, .6, .65, .7, .75, .8, .85, .9, .95, 1};
  static int LENGTH_BETA = BETA.length;

  static double[] P_SHARING = {0, .05, .1, .15, .2, .25, .3, .35, .4, .45, .5, .55, .6, .65, .7, .75, .8, .85, .9, .95, 1};
  static int LENGTH_P_SHARING = P_SHARING.length;

  static double P_ACCEPT = .5;
  static double P_LEARNING = .2;

  //Instrumental Parameters
  static final long TIC = System.currentTimeMillis();

  static HashMap<Integer, String> NETWORK_TYPE = new HashMap<Integer, String>() {{
    put(0, "Minimum Spanning Tree");
    put(1, "Connected Cavemen");
    put(2, "Preferential Attachment");
  }};
  static int LENGTH_NETWORK_TYPE = NETWORK_TYPE.size();

  static double M_N = M * N;

  static final int[] RESULT_KEY_VALUE = {
      LENGTH_NETWORK_TYPE, LENGTH_BETA, LENGTH_P_SHARING, TIME
  };

  static final int[] RESULT_KEY_VALUE_OPTIMAL = {
      LENGTH_NETWORK_TYPE, LENGTH_P_SHARING, TIME
  };

  static final int[] RESULT_KEY_VALUE_RANK = {
      LENGTH_NETWORK_TYPE, LENGTH_BETA, LENGTH_P_SHARING, TIME, N
  };

  static String RUN_ID = "Test";
  static String PARAMS =
      "I"
          + ITERATION
          + "T"
          + TIME
          + "N"
          + N_PRIME + "x" + N_FACTOR
          + "(M"
          + M
          + "S"
          + S
          + ")"
          + "Beta"
          + LENGTH_BETA
          + "Ps"
          + LENGTH_P_SHARING
          + "Pl"
          + P_LEARNING
          + "Pa"
          + P_ACCEPT;

  static String PATH_CSV = new File(".").getAbsolutePath() + "\\" + RUN_ID + "_" + PARAMS + "\\";

  public static void main(String[] args) {
    Computation c = new Computation();
    if (GET_NET) {
      System.out.println(PATH_CSV);
      c.printNetwork();
    }
    if (GET_MAT) {
      System.out.println(RUN_ID + ":\t" + PARAMS);
      c.doExperiment();
      new MatWriter(c);
      System.out.println(RUN_ID + ":\t" + PARAMS);
    }
  }

  private static void fillArray(double[] array) {
    double stride = 1D / (array.length - 1);
    for (int i = 0; i < array.length; i++) {
      array[i] = i * stride;
    }
  }
}
