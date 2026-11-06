import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("tiny.txt"));
        String symbol = in.next();
        int n = in.nextInt();
		
		int[] years = new int[n];
		int[] months = new int[n];
		double[] prices = new double[n];
		
		loadData(in,years,months,prices);
		printData(years,months,prices);
		
		int lowest = indexOfLowest(prices);
		int highest = indexOfHighest(prices);
		
		System.out.println(years[lowest] + " " + months[lowest] + " " + prices[lowest]);
		System.out.println(years[highest] + " " + months[highest] + " " + prices[highest]);
		
		indexOfHighest(prices);
		buyAndHold(months,10000,prices);
		dipBuyer(months,10000,prices);
    }
    
    public static void loadData(Scanner in, int[] years, int[] months, double[] prices){
		for(int i = 0; i < years.length; i++){
			years[i] = in.nextInt();
			months[i] = in.nextInt();
			prices[i] = in.nextDouble();	
		}
		
	}
	
	public static String monthName(int months){
		String[] month = {"January","Feburary","March","April","May",
						   "June","July","August","September","Octuber",
						   "November","December"};
		//if(months >= 1 && months <= 12){				   
			return month[months - 1];
		//}
	}
	
	public static void printData(int[] years, int[] months, double[] prices){
		
		for(int i = 0; i < prices.length; i++){
			System.out.println(years[i] + " " + months[i] + " " + prices[i]);
		}
		
	}
	
	public static int indexOfLowest(double[] prices){
		
		int lowest = 0;
		
		
		for(int i = 0; i < prices.length; i++){
			if(prices[i] < prices[lowest]){
				lowest = i;
			}
		}
		
		return lowest; 
	}
	
	public static int indexOfHighest(double[] prices){
		
		int highest = 0;
		
		for(int i = 0; i < prices.length; i++){
			if(prices[i] > prices[highest]){
				highest = i;
			}
		}
		
		return highest;
	}
	
	public static double indexOfBiggestDrop(double[] prices){
		
		int drop = 0;
		double percentage = 0;
		
		for(int i = 0; i < prices.length; i++){
			if(Math.abs(prices[drop] - prices[drop+1]) < Math.abs(prices[i] - prices[i+1])){
				drop = i;
			}
			percentage = prices[drop-1] / prices[drop];
		}
		
		return percentage;
	}
	
	public static void buyAndHold(int[] months,double money,double[] prices){
		
		String result;
		int share;
		double moneyFinal;
		double moneyLeft = 0;
		
		if(months[0] == 1){
			result = "BUY";
			share = (int)((money-10)/prices[0]);
			moneyLeft = (int)((money-10)%prices[0]);
		} else {
			result = "HOLD";
			share = 0;
			moneyFinal = money;
		}
			
		moneyFinal = prices[prices.length - 1] * share + moneyLeft;

		System.out.println(result + " " + share + " shares at " + prices[0]);
		System.out.println("Final value: " + moneyFinal);
	}
	
	public static void dipBuyer(int[] months,double money,double[] prices){
		
		String result = "Unknown";
		int share = 0;
		double moneyFinal = money;
		double moneyLeft = 0;
		
		for(int i = 0; i < months.length; i++){
			if( months[i] >= 2){
				if(prices[i] > prices[i-1] && prices[i] > prices[i-2]){
					result = "BUY";
					share = (int)((money-10)/prices[0]);
					moneyLeft = (int)((money-10)%prices[0]);
					
					if(prices[i] > prices[i-1] * 1.1){
						moneyFinal = prices[prices.length - 1] * share + moneyLeft;
					}
				}
			}
		}
		System.out.println(result + " " + share + " shares at " + prices[0]);
		System.out.println("Final value: " + moneyFinal);
	}
	
}
