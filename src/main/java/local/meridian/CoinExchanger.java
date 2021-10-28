package local.meridian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CoinExchanger {
   private int counter;
   private int minPiece;
   private int[] coins;
   private int[] variation;
   private int[] commonMultiples;
   private List<int[]> correctVariations = new ArrayList<>();

   // constructor
   public CoinExchanger(int[] coins) {
      this.coins = coins; // list of coins
      validateCoins(); // if incorrect values are given
      this.variation = new int[coins.length + 1]; // storing pieces of the coins AND sum of these pieces here
   }

   public List<int[]> getCorrectVariations() {
      return new ArrayList<>(correctVariations);
   }

   // validator
   private void validateCoins() {
      if (this.coins == null)
         throw new IllegalArgumentException("You must give at least one coin value!");

      this.coins = sortCoins(this.coins);
      if (this.coins[0] < 1)
         throw new IllegalArgumentException("Coins must have positive value!");

   }

   // validator
   private void validateAmount(int amount) {
      if (amount < this.coins[0])
         throw new IllegalArgumentException("Amount must be bigger or equals than smallest coin!");
   }

   // initializer
   private void init(int amount) {
      this.correctVariations.clear();
      this.counter = 0;
      this.minPiece = amount / coins[0];
      this.commonMultiples = calculateCommonMultiples(amount);
   }

   // class main operator
   public int calculateMinPiece(int amount) {
      validateAmount(amount);
      init(amount);

      // searching variations will be ended when the pointer is overflowed!
      int pointer = 0;
      while (pointer < coins.length) {
         counter++; // measure the effectivness of the algorythm
         pointer = nextVariation(pointer, amount);

         int pieceOfCoins = calculatePieces();
         if (pieceOfCoins > minPiece) {
            continue;
         } else {
            variation[coins.length] = pieceOfCoins;
            storeCorrectVariation(amount, pieceOfCoins);
         }
      }

      return minPiece;
   }

   private void storeCorrectVariation(int amount, int pieceOfCoins) {
      int sumCoinsValue = calculateSum();
      if (sumCoinsValue == amount) {
         if (pieceOfCoins < minPiece) {
            this.minPiece = pieceOfCoins;
            correctVariations.clear();
         }
         this.correctVariations.add(Arrays.copyOf(this.variation, this.variation.length));
      }
   }

   private int[] calculateCommonMultiples(int amount) {
      int[] multiples = new int[coins.length];

      for (int i = 0; i < coins.length - 1; i++) {
         int num = coins[i];
         while (num % coins[i + 1] != 0) {
            num = num + coins[i];
         }
         multiples[i] = num / coins[i];
      }
      multiples[coins.length - 1] = amount / coins[coins.length - 1];

      return multiples;
   }

   private int calculatePieces() {
      int sum = 0;
      for (int i = 0; i < coins.length; i++) {
         sum += variation[i];
      }
      return sum;
   }

   private int calculateSum() {
      int sum = 0;
      for (int i = 0; i < coins.length; i++) {
         sum += variation[i] * coins[i];
      }
      return sum;
   }

   // simplier calulating need to sort coins by its values
   private int[] sortCoins(int[] coins) {
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

   // incremeting the counter at the pointer in the variation
   private int nextVariation(int pointer, int amount) {
      boolean overflow;
      do {
         int value = variation[pointer] * coins[pointer];
         if (variation[pointer] < commonMultiples[pointer] && value < amount) {
            variation[pointer]++;
            overflow = false;
            pointer = 0;
         } else { // stepping the next coins index
            variation[pointer] = 0;
            pointer++;
            overflow = true;
         }

         if (pointer == coins.length) {
            break;
         }
      } while (overflow);

      return pointer;
   }

   // class result printer -- instead of toString()
   public void printResult(int amount) {
      System.out.println("\n" + this.counter + " cycle(s) were required for this calculation.");
      System.out.println("Variation of minimal coins need for change:");
      System.out.println("------------------------------------------------------");
      for (int[] list : this.correctVariations) {
         for (int index = 0; index < list.length - 1; index++) {
            if (list[index] > 0) {
               System.out.printf("(%d): %d  ", this.coins[index], list[index]);
            }
         }
         System.out.println();
      }
      System.out.println("-------------------------------------------------------");
      System.out.println(this.minPiece + " coin(s) need for this(these) variation(s) to change the amount: " + amount);
      System.out.println("Number of correct variations: " + this.correctVariations.size() + ".\n");
   }

} // end of class