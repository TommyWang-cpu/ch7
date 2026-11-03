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
    }
    
    public static void loadData(Scanner in, int[] years, int[] months, double[] prices){
		for(int i = 0; i < years.length; i++){
			years[i] = in.nextInt();
			months[i] = in.nextInt();
			prices[i] = in.nextInt();	
		}
		
	}
	
	public static String monthName(int month){
		String[] months = {"January","Feburary","March","April","May",
						   "June","July","August","September","Octuber",
						   "November","December"};
		return months[month - 1];
	}
}
