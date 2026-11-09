import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;

    public static void main(String[] args) throws FileNotFoundException {

        Scanner in = new Scanner(new File("tiny.txt"));

        String symbol = in.next();
        int n = in.nextInt();

        int[] years = new int[n];
        int[] months = new int[n];
        double[] prices = new double[n];

        loadData(in, years, months, prices);
        printData(years, months, prices);

        int lowest = indexOfLowest(prices);
        int highest = indexOfHighest(prices);

        System.out.println();
        System.out.println(symbol + " lowest: $" + 
						   String.format("%.2f", prices[lowest]) +
                           " in " + monthName(months[lowest]) +
                           " " + years[lowest]);

        System.out.println(symbol + " highest: $" +
                           String.format("%.2f", prices[highest]) +
                           " in " + monthName(months[highest]) +
                           " " + years[highest]);

        double result = simulate(years, months, prices);

        System.out.println("Final value: $" + String.format("%,.2f", result));
    }

    public static void loadData(Scanner in, int[] years,int[] months, double[] prices) {

        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
    }

    public static String monthName(int month) {

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        return months[month - 1];
    }

    public static void printData(int[] years, int[] months,double[] prices) {

        for (int i = 0; i < prices.length; i++) {

            System.out.println(
                monthName(months[i]) + " " +
                years[i] + ": $" +
                String.format("%.2f", prices[i])
            );
        }
    }

    public static int indexOfLowest(double[] prices) {

        int lowest = 0;

        for (int i = 1; i < prices.length; i++) {

            if (prices[i] < prices[lowest]) {
                lowest = i;
            }
        }

        return lowest;
    }

    public static int indexOfHighest(double[] prices) {

        int highest = 0;

        for (int i = 1; i < prices.length; i++) {

            if (prices[i] > prices[highest]) {
                highest = i;
            }
        }

        return highest;
    }

 
    public static int indexOfBiggestDrop(double[] prices) {

        int drop = 1;

        for (int i = 2; i < prices.length; i++) {

            double currentDrop = (prices[i - 1] - prices[i]) / prices[i - 1];

            double biggestDrop = (prices[drop - 1] - prices[drop]) / prices[drop - 1];

            if (currentDrop > biggestDrop) {
                drop = i;
            }
        }

        return drop;
    }

    public static String buyAndHold(double[] history, boolean holding, double boughtAt) {

        return "BUY";
    }

    public static String dipBuyer(double[] history,boolean holding,double boughtAt) {

        int n = history.length;
        double today = history[n - 1];

        
        if (!holding) {

            if (n >= 3 &&
                today < history[n - 2] &&
                history[n - 2] < history[n - 3]) {

                return "BUY";
            }
        }

        if (holding) {

            if (today >= boughtAt * 1.10) {
                return "SELL";
            }
        }

        return "HOLD";
    }

    public static String movingAverage(double[] history, boolean holding,double boughtAt) {

        if (history.length < 12) {
            return "HOLD";
        }

        double sum = 0;

        for (int i = history.length - 12;
             i < history.length;
             i++) {

            sum += history[i];
        }

        double avg = sum / 12.0;
        double today = history[history.length - 1];

        if (!holding && today > avg) {
            return "BUY";
        }

        if (holding && today < avg) {
            return "SELL";
        }

        return "HOLD";
    }

    /*
     * My strategy:
     * If I do not own shares, I buy when the price has dropped
     * for three consecutive months and today's price is at least
     * 5% lower than the price four months ago.
     *
     * If I own shares, I sell when the price is either 15% higher
     * than my buying price or 10% lower than my buying price.
     *
     * The idea is to buy after a significant downward trend and
     * sell when I make a good profit or limit a large loss.
     */
    public static String myStrategy(double[] history,boolean holding,double boughtAt) {

        int n = history.length;
        double today = history[n - 1];

        if (!holding) {

            if (n >= 4 &&
                today < history[n - 2] &&
                history[n - 2] < history[n - 3] &&
                today <= history[n - 4] * 0.95) {

                return "BUY";
            }
        }

        if (holding) {

            if (today >= boughtAt * 1.15 ||today <= boughtAt * 0.90) {

                return "SELL";
            }
        }

        return "HOLD";
    }

    public static String decide(double[] history,boolean holding,double boughtAt) {

        return buyAndHold(history, holding, boughtAt);
    }

    public static double simulate(int[] years,int[] months,double[] prices) {

        double cash = START_CASH;
        int shares = 0;
        double boughtAt = 0.0;

        for (int i = 0; i < prices.length; i++) {

            double[] history = Arrays.copyOf(prices, i + 1);

            String action = decide(history, shares > 0, boughtAt);

            if (action.equals("BUY") && shares == 0) {

                int canBuy = (int)((cash - FEE) / prices[i]);

                if (canBuy > 0) {

                    shares = canBuy;
                    cash = cash - shares * prices[i] - FEE;
                    boughtAt = prices[i];

                    System.out.println(
                        monthName(months[i]) + " " +
                        years[i] + ": BUY " +
                        shares + " shares at $" +
                        String.format("%.2f", prices[i])
                    );
                }

            } else if (action.equals("SELL") && shares > 0) {

                cash = cash + shares * prices[i] - FEE;

                System.out.println(
                    monthName(months[i]) + " " +
                    years[i] + ": SELL " +
                    shares + " shares at $" +
                    String.format("%.2f", prices[i]));

                shares = 0;
                boughtAt = 0.0;
            }
        }

        return cash + shares * prices[prices.length - 1];
    }
}
