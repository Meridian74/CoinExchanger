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
      int amount = 25;                             // example amount
      
      int minPiece = calculateMinPiece(coins, amount, resultsList);
      printResult(coins, amount, minPiece, resultsList);
   }
   
   
   public static int calculateMinPiece(int[] coins, int amount, List<int[]> resultsList) {
      validateCoinsAndAmount(coins, amount);       // if incorrect values are given
      int[] variation = new int[coins.length + 1]; // storing pieces of the coins AND sum of these pieces here
      int minPiece = amount / coins[0];            // previously excluded the lesser than 1 values!
      int[] commonMultiples = calculateCommonMultiples(coins, amount);

      // searching variations will be ended when the pointer is overflowed!
      int pointer = 0;
      while (pointer < coins.length) {
         int sumCoinsValue = calculateSum(coins, variation);
         int pieceOfCoins = calculatePieces(coins, variation);
         variation[coins.length] = pieceOfCoins;

         // update list of result with appropriate coin variations
         if (sumCoinsValue == amount) {
            resultsList.add(Arrays.copyOf(variation, variation.length));
            // update minimum coins piece
            if (pieceOfCoins < minPiece) {
               minPiece = pieceOfCoins;
            }
         }

         pointer = nextVariation(coins, variation, pointer, amount);
      }

      return minPiece;
   }
   
   
   private static int[] calculateCommonMultiples(int[] coins, int amount) {
      int[] multiples = new int[coins.length];
      // TODO: next day...
      return null;
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
   private static int nextVariation(int[] coins, int[] variation, int coinsIndex, int amount) {
      boolean overflow;
      do {
         int nextCoinValue;
         if (coinsIndex < coins.length - 1) {
            nextCoinValue = coins[coinsIndex] * coins[coinsIndex + 1];
         }
         else {
            nextCoinValue = amount / coins[coinsIndex];
         }

         int value = variation[coinsIndex] * coins[coinsIndex];
         if (variation[coinsIndex] < nextCoinValue && value < amount) {
            variation[coinsIndex]++;
            overflow = false;
            coinsIndex = 0;
         } else { // stepping the next coins index
            variation[coinsIndex] = 0;
            coinsIndex++;
            overflow = true;
         }

         if (coinsIndex == coins.length) {
            break;
         }
      } while (overflow);

      return coinsIndex;
   }

   
   // printing result
   public static void printResult(int[] coins, int amount, int calulatedMinPiece, List<int[]> results) {
      System.out.println("\nVariation of minimal coins need for change:");
      System.out.println("-------------------------------------------");
      int goodResults = 0;
      for (int[] list : results) {
         if (list[coins.length] == calulatedMinPiece) {
            for (int index = 0; index < list.length - 1; index++) {
               if (list[index] > 0) {
                  System.out.printf("(%d): %d  ", coins[index], list[index]);
               }
            }
            goodResults++;
            System.out.println();
         }
      }
      System.out.println("-------------------------------------------");
      System.out.println(calulatedMinPiece + 
            " coin(s) need for this(these) variation(s) to change the amount: " + amount);
      System.out.println("Number of correct variations: " + goodResults + ".\n");
   }

}