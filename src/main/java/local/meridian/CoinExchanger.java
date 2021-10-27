package local.meridian;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;

public class CoinExchanger {
   public static void main(String[] args) {
      int[] coins = { 1, 5, 7, 9, 11 };            // values of the changing coins
      List<int[]> resultsList = new ArrayList<>(); // storing appropriate coins variations
      int amount = 52;                             // example value

      int minPiece = calculateMinPiece(coins, amount, resultsList);
      printResult(coins, amount, minPiece, resultsList);
   }
   
   
   public static int calculateMinPiece(int[] coins, int givenAmount, List<int[]> resultsList) {
      validateCoinsAndAmount(coins, givenAmount);  // if incorrect values are given
      int[] variation = new int[coins.length + 1]; // storing pieces of the coins AND sum of these pieces here
      int minPiece = givenAmount / coins[0];       // previously excluded the lesser than 1 values!

      // this precalulation needs for faster calculating if the given amount is a big number!
      int reducedAmount = precalculatingWithTheBiggestCoin(coins, givenAmount, variation);

      // searching variations will be ended when the counterPointer is overflowed!
      int counterPointer = 0;
      while (counterPointer < coins.length) {
         int sumCoinsValue = calculateSum(coins, variation);
         int pieceOfCoins = calculatePieces(coins, variation);
         variation[coins.length] = pieceOfCoins;

         // update list of result with appropriate coin variations
         if (sumCoinsValue == givenAmount) {
            resultsList.add(Arrays.copyOf(variation, variation.length));
            // update minimum coins piece
            if (pieceOfCoins < minPiece) {
               minPiece = pieceOfCoins;
            }
         }

         counterPointer = stepCounters(coins, variation, counterPointer, reducedAmount);
      }

      return minPiece;
   }
   
   
   private static int calculateSum(int[] coins, int[] variation) {
      int sum = 0;
      for (int i = 0; i < coins.length; i++) {
         sum += variation[i] * coins[i];
      }
      return sum;
   }

   private static int calculatePieces(int[] coins, int[] variation) {
      int sum = 0;
      for (int i = 0; i < coins.length; i++) {
         sum += variation[i];
      }
      return sum;
   }


   // preset the biggest coin counter and return the reduced amount
   private static int precalculatingWithTheBiggestCoin(int[] coins, int givenAmount, int[] variation) {
      int indexOfBiggestCoin = coins.length - 1;
      int biggestCoinValue = coins[indexOfBiggestCoin];

      if (givenAmount < biggestCoinValue) {
         return givenAmount;
      }

      int reducedAmount = givenAmount % biggestCoinValue + biggestCoinValue;

      // need to preset the pointer value for faster counting variations
      int pointerValue = givenAmount / biggestCoinValue - 1;
      variation[indexOfBiggestCoin] = pointerValue;

      return reducedAmount;
   }

   
   // values validating - filtering out faulty cases
   private static void validateCoinsAndAmount(int[] coins, int givenAmount) {
      if (coins == null)
         throw new IllegalArgumentException("You must give at least one coin value!");

      coins = sortCoins(coins);
      if (coins[0] < 1)
         throw new IllegalArgumentException("Coins must have positive value!");

      if (givenAmount < coins[0])
         throw new IllegalArgumentException("Amount must be bigger or equals than smallest coin!");
   }


   // simplier calulating need to sort coins by its values
   private static int[] sortCoins(int[] coins) {
      int[] sorted;
      Set<Integer> treeSet = new TreeSet<Integer>();
      for (Integer i : coins) {
         treeSet.add(i);
      }

      if (treeSet.size() != coins.length)
         throw new IllegalArgumentException("Coins must have different values!");

      sorted = treeSet.stream().mapToInt(x -> x).toArray();
      return sorted;
   }
   

   // incremeting of the counters in the variation by the pointer
   public static int stepCounters(int[] coins, int[] variation, int coinsIndexPointer, int givenAmount) {
      boolean overflow;
      do {
         int maxStep = givenAmount / coins[coinsIndexPointer];
         if (variation[coinsIndexPointer] < maxStep) {
            variation[coinsIndexPointer]++;
            overflow = false;
            coinsIndexPointer = 0;
         } else { // stepping the next counter
            variation[coinsIndexPointer] = 0;
            coinsIndexPointer++;
            overflow = true;
         }

         if (coinsIndexPointer == coins.length) {
            break;
         }
      } while (overflow);

      return coinsIndexPointer;
   }

   
   // printing result
   public static void printResult(int[] coins, int amount, int calulatedMinPiece, List<int[]> results) {
      System.out.println("\nVariation of minimal coins need for change:");
      System.out.println("-------------------------------------------");
      for (int[] list : results) {
         if (list[coins.length] == calulatedMinPiece) {
            for (int index = 0; index < list.length - 1; index++) {
               if (list[index] > 0) {
                  System.out.printf("(%d): %d  ", coins[index], list[index]);
               }
            }
            System.out.println();
         }
      }
      System.out.println("-------------------------------------------");
      System.out.println(calulatedMinPiece + 
            " coin(s) need for this(these) variation(s) to change the amount: " + amount + ".\n");
   }

}