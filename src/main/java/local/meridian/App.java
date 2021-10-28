package local.meridian;

public class App {
   public static void main(String[] args) {
      int[] coins = { 1, 5, 7, 9, 11 };
      int amount = 25;
      CoinExchanger exchanger = new CoinExchanger(coins);
      int min = exchanger.calculateMinPiece(amount);
      System.out.println(min);
      exchanger.printResult(amount);
   }
   
}