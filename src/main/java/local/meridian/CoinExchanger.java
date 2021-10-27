package local.meridian;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CoinExchanger {
   public static void main(String[] args) {
      final int[] COINS = { 1, 5, 7, 9, 11 };      // values of the changing coins
      List<int[]> resultsList = new ArrayList<>(); // storing appropriate coins variations
      int givenAmount = 65;                        // example value
      
      int calculatedMinPiece = calculateMinPiece(COINS, givenAmount, resultsList);
      printResult(COINS, calculatedMinPiece, resultsList);
   }

   
   public static int calculateMinPiece(int[] COINS, int givenAmount, List<int[]> resultsList) {
      int[] variation = new int[COINS.length + 1]; // storing pieces of the coins, AND sum of these pieces
      int minPiece = Integer.MAX_VALUE;
      int sumCoinsValue;
      int coinsIndexPointer = 0;

      // calculating will be ended when the coinsIndexPointer is overflowed
      while (coinsIndexPointer < COINS.length) {
         sumCoinsValue = 0;
         int pieceOfCoins = 0;

         for (int i = 0; i < COINS.length; i++) {
            sumCoinsValue += variation[i] * COINS[i];
            pieceOfCoins += variation[i];
         }
         variation[COINS.length] = pieceOfCoins;

         // saving appropriate coins variation
         if (sumCoinsValue == givenAmount) {
            resultsList.add(Arrays.copyOf(variation, variation.length));
            // searching and saving the minimum value
            if (pieceOfCoins < minPiece) {
               minPiece = pieceOfCoins;
            }
         }

         coinsIndexPointer = stepCounters(COINS, variation, coinsIndexPointer, givenAmount);
      }
      return minPiece;
   }


   // incremeting of the counters in the variation by the pointer
   public static int stepCounters(int[] COINS, int[] variation, int coinsIndexPointer, int givenAmount) {
      boolean overflow;
      do {
         int maxStep = givenAmount / COINS[coinsIndexPointer];
         if (variation[coinsIndexPointer] < maxStep) {
            variation[coinsIndexPointer]++;
            overflow = false;
            coinsIndexPointer = 0;
         } else { // stepping the next counter
            variation[coinsIndexPointer] = 0;
            coinsIndexPointer++;
            overflow = true;
         }

         if (coinsIndexPointer == COINS.length) {
            break;
         }
      } while (overflow);

      return coinsIndexPointer;
   }

   
   // printing result
   public static void printResult(int[] COINS, int calulatedMinPiece, List<int[]> results) {
      System.out.println("\nVariation of minimal coins need for change:");
      System.out.println("-------------------------------------------");
      for (int[] list : results) {
         if (list[COINS.length] == calulatedMinPiece) {
            for (int index = 0; index < list.length - 1; index++) {
               if (list[index] > 0) {
                  System.out.printf("(%d): %d  ", COINS[index], list[index]);
               }
            }
            System.out.println();
         }
      }
      System.out.println("-------------------------------------------");
      System.out.println(calulatedMinPiece + " coin(s) need for this(these) variation(s).\n");
   }

}