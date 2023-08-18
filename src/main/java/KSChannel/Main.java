package KSChannel;

import java.io.File;

public class Main {

  //Computation Parameters
  static int ITERATION = 10_000;
  static final long TIC = System.currentTimeMillis();

  //Output Setup
  static final boolean GET_CSV = false;
  static final boolean GET_MAT = true;

  //Key Assumptions
  static boolean IS_CAVEMEN = false;
  static boolean IS_LEARNING_COSTLY = true;
  static boolean IS_LEARNING_GREEDY = false;

  //Global Parameters
  static int N_OF_GROUP = 5;
  static int N_IN_GROUP = 8;
  static int N = N_OF_GROUP * N_IN_GROUP;
  static int M = 50;
  static int S = 5;
  static int TIME = 1000 + 1;

  //Moving Params
  static double[] BETA = {0};
//  static double[] BETA = {0, .08, 1};
//  static double[] BETA = {0, .05, .1, .15, .2, .25, .3, .35, .4, .45, .5, .55, .6, .65, .7, .75, .8, .85, .9, .95, 1};
//  static double[] BETA = {0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1};
  static int LENGTH_BETA = BETA.length;

  static double[] P_SHARING = new double[]{.1, .5, .9};
  static int LENGTH_P_SHARING = P_SHARING.length;

  static double[] P_SEEKING = new double[]{.1, .5, .9};
  static int LENGTH_P_SEEKING = P_SEEKING.length;

  static double[] EPSILON = new double[]{0, .1, .2};
  static int LENGTH_EPSILON = P_SEEKING.length;

  static double P_LEARNING = .2;

  //Instrumental Params
  static double M_N = M * N;

  static final int[] RESULT_KEY_VALUE = {
      LENGTH_BETA, LENGTH_P_SHARING, LENGTH_P_SEEKING, LENGTH_EPSILON, TIME
  };

  static final int[] RESULT_KEY_VALUE_RANK = {
      LENGTH_BETA, LENGTH_P_SHARING, LENGTH_P_SEEKING, LENGTH_EPSILON, TIME, N
  };

  static String LABEL = "KSChannel";
  static String PARAMS =
      "["
      + "cave"
      + (IS_CAVEMEN ? 1 : 0)
      + "cost"
      + (IS_LEARNING_COSTLY ? 1 : 0)
      + "greed"
      + (IS_LEARNING_GREEDY ? 1 : 0)
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
      + "p1"
      + LENGTH_P_SHARING
      + "p2"
      + LENGTH_P_SEEKING
      + "e"
      + LENGTH_EPSILON;

  static String PATH_CSV = new File(".").getAbsolutePath() + "\\" + LABEL + PARAMS + "\\";

  public static void main(String[] args) {
    if (GET_CSV) {
      new CSVWriter();
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
