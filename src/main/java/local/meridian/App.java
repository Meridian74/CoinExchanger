package local.meridian;

public class App {
   public static void main(String[] args) {
      int[] coins = { 1000, 2000, 5000, 10000, 20000 };
      int amount = 145000;
      CoinExchanger exchanger = new CoinExchanger(coins);
      int min = exchanger.calculateMinPiece(amount);
      System.out.println(min);
      exchanger.printResult(amount);
   }
   
}