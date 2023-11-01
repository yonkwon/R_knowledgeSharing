package KSDyadic;

import java.io.File;

public class Main {

  //Computation Parameters
  static int ITERATION = 5000;
  //    static final int NUM_THREAD = Runtime.getRuntime().availableProcessors();
  static final long TIC = System.currentTimeMillis();

  //Output Setup
  static final boolean GET_CSV = false;
  static final boolean GET_MAT = true;

  //Key Assumptions
  static boolean IS_RATIO = false;
//    static boolean IS_RATIO = false;
//    static boolean IS_CAVEMEN = false;
  static boolean IS_CAVEMEN = false;
//    static boolean IS_ONE_ON_ONE = true;
  static boolean IS_ONE_ON_ONE = true;

  //Global Parameters
  static int N_OF_GROUP = 6;
  static int N_IN_GROUP = 8;
  static int N = N_OF_GROUP * N_IN_GROUP;
  static int M = 80;
  static int S = 5;
  static int TIME = 2000 + 1;

  //Moving Params
//  static double[] BETA = {0};
//    static double[] BETA = {0, .08, 1};
  static double[] BETA = {0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1};
//    static double[] BETA = {0, .05, .1, .15, .2, .25, .3, .35, .4, .45, .5, .55, .6, .65, .7, .75, .8, .85, .9, .95, 1};
  static int LENGTH_BETA = BETA.length;

//  static double[] P_SHARING = new double[]{0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1};
  static double[] P_SHARING = new double[]{0, .125, .25, .375, .5, .625, .75, .875, 1};
//    static double[] P_SHARING = new double[]{0, .08, 1};
//    static double[] P_SHARING = new double[]{0, .5, 1};
//    static double[] P_SHARING = new double[]{0, 1};
  static int LENGTH_P_SHARING = P_SHARING.length;

  static double P_LEARNING = .2;

  //Instrumental Params
  static double M_N = M * N;

  static final int[] RESULT_KEY_VALUE = {
      LENGTH_BETA, LENGTH_P_SHARING, TIME
  };

  static final int[] RESULT_KEY_VALUE_RANK = {
      LENGTH_BETA, LENGTH_P_SHARING, TIME, N
  };

  static final int[] RESULT_KEY_VALUE_TYPE = {
      LENGTH_BETA, LENGTH_P_SHARING, TIME, 2
  };

  static final int[] RESULT_KEY_VALUE_TYPE_TO_TYPE = {
      LENGTH_BETA, LENGTH_P_SHARING, TIME, 4
  };

  static String LABEL = "KSDyadic";
  static String PARAMS =
      "[r"
          + (IS_RATIO ? 1 : 0)
          + "c"
          + (IS_CAVEMEN ? 1 : 0)
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
          + "B"
          + LENGTH_BETA
          + "Pg"
          + LENGTH_P_SHARING;

  static String PATH_CSV = new File(".").getAbsolutePath() + "\\" + LABEL + PARAMS + "\\";

  public static void main(String[] args) {
    if (GET_CSV) {
      new NetworkCSV();
    }
    if (GET_MAT) {
      System.out.println(LABEL + ":\t" + PARAMS);
      Computation c = new Computation();
      new MatWriter(c);
      System.out.println(LABEL + ":\t" + PARAMS);
    }
  }

  private static void fillArray(double[] array) {
    double stride = 1D / (array.length - 1);
    for (int i = 0; i < array.length; i++) {
      array[i] = i * stride;
    }
  }
}
