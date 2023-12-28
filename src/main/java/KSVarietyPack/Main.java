package KSVarietyPack;
import java.io.File;
import java.util.HashMap;

public class Main {

  //Computation Parameters
  static int ITERATION = 1_000;
  //    static final int NUM_THREAD = Runtime.getRuntime().availableProcessors();
  static final long TIC = System.currentTimeMillis();

  //Output Setup
  static final boolean GET_CSV = true;
  static final boolean GET_MAT = false;

  //Key Assumptions
  static boolean IS_RATIO = false;
  static boolean IS_ONE_ON_ONE = false;

  //Global Parameters
  static int M = 100;
  static int S = 5;
  static int TIME = 1500 + 1;

  //Network Parameters
  static HashMap<Integer,String> NETWORK_TYPE = new HashMap<Integer,String>(){{
    put(0, "Random Spanning Tree");
    put(1, "Connected Cavemen");
    put(2, "Preferential Attachment");
  }};
  static int LENGTH_NETWORK_TYPE = NETWORK_TYPE.size();
  static int N_OF_GROUP = 8;
  static int N_IN_GROUP = 10;
  static int N = N_OF_GROUP * N_IN_GROUP;

  //Moving Params
//  static double[] BETA = {0};
//  static double[] BETA = {0, .08, 1};
//  static double[] BETA = {0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1};
  static double[] BETA = {0, .05, .1, .15, .2, .25, .3, .35, .4, .45, .5, .55, .6, .65, .7, .75, .8, .85, .9, .95, 1};
  static int LENGTH_BETA = BETA.length;

  static double[] P_SHARING = new double[]{0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1};
//  static double[] P_SHARING = new double[]{0, .125, .25, .375, .5, .625, .75, .875, 1};

  static int LENGTH_P_SHARING = P_SHARING.length;

  static double P_ACCEPT = .8;
  static double P_LEARNING = .2;

  //Instrumental Params
  static double M_N = M * N;

  static final int[] RESULT_KEY_VALUE = {
      LENGTH_NETWORK_TYPE, LENGTH_BETA, LENGTH_P_SHARING, TIME
  };

  static String RUN_ID = "KSVariety";
  static String PARAMS =
      "[r"
          + (IS_RATIO ? 1 : 0)
          + "o"
          + (IS_ONE_ON_ONE ? 1 : 0)
          + "]"
          + "I"
          + ITERATION
          + "T"
          + TIME
          + "N"
          + N_OF_GROUP + "x" + N_IN_GROUP
          + "(M"
          + M
          + "S"
          + S
          + ")"
          + "Pl"
          + P_LEARNING
          + "Pa"
          + P_ACCEPT
          + "B"
          + LENGTH_BETA
          + "Pg"
          + LENGTH_P_SHARING;

  static String PATH_CSV = new File(".").getAbsolutePath() + "\\" + RUN_ID + PARAMS + "\\";

  public static void main(String[] args) {
    Computation c = new Computation();
    if (GET_CSV) {
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
