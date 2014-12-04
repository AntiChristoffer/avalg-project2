import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;


/*
 * All reference distances using sample.txt sample input
 * distance with greedyTour (0 8 5 4 3 9 6 2 1 7) = 225.7290569637806
 * distance with 2-opt (4 5 8 0 2 6 9 3 1 7) = 188.74784321058627
 */

public class Main {

	Tuple[] coords;
	double[][] distances;
	int listsize;
	final boolean DEBUG = false;
	final boolean MONITORING = false;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Main main = new Main();
		main.run();
	}
	
	public void run() throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader("sample.txt")); // TODO - change to (new InputStreamReader(System.in)) on Kattis submission;
		listsize = Integer.parseInt(br.readLine());
		coords = new Tuple[listsize];
		if(DEBUG)System.out.println("created coords");
		for(int i = 0; i<listsize; i++){
			String temp = br.readLine();
			String[] t = temp.split(" ");
			double tmpX = Double.parseDouble(t[0]);
			double tmpY = Double.parseDouble(t[1]);
			Tuple tmpTuple = new Tuple(tmpX, tmpY);
			coords[i] = tmpTuple;
		}
		br.close();
		
		distances = new double[listsize][listsize];
		for(int i=0; i<listsize;i++){
			distances[i][i] = 0;
			for(int j=0; j<i; j++){
				double dist = calcDist(coords[i], coords[j]);
				distances[i][j] = dist;
				distances[j][i] = dist;
			}
		}
		//if(DEBUG)printMatrix();
		
		if(MONITORING){
			BufferedReader brtemp = new BufferedReader(new InputStreamReader(System.in));//(new FileReader("sample.txt")); // TODO - change to (new InputStreamReader(System.in)) on Kattis submission;
			System.out.println("Enter Digit 1:");
			while(Integer.parseInt(brtemp.readLine()) != 1){
				System.out.println("1 sa jag ju!");
			}
		}
		
		int[] tour = new int[listsize];
		if(DEBUG)System.out.println("created array");
		
		tour = greedyTour(coords);
		if(DEBUG){
			double dist = calcTotalTourLength(tour);
			print(tour);
			System.out.println("distance after greedy = " +Double.toString(dist));
		}
		
		tour = twoOPT(tour);
		print(tour);
			//double dist = calcTotalTourLength(tour);
			//System.out.println("distance after 2opt = " +Double.toString(dist));
		
		
	}
	
	public void printMatrix(){
		for(int i=0; i<listsize; i++){
			for(int j=0; j<listsize; j++){
				System.out.print(distances[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	public void print(int[] tour){
		for(int i = 0; i<listsize; i++){
			System.out.println(tour[i]);
		}
	}
	
	public int[] twoOPT(int[] tour){
		boolean foundBetter = true;
		int iter = 0;
		while(iter <10){
			foundBetter = false;
			for(int i = 0; i<listsize-1; i++){
				for(int j = i+1; j<listsize; j++){
					foundBetter = calcDistSwitch(tour, i, j);
					if(foundBetter){
						tour = twoOptSwap(tour, i, j);
					}
				}
			}
			iter++;
		}
		return tour;
	}
	
	public int[] greedyTour(Tuple[] coords){
		int[] tour = new int[listsize];
		boolean[] used = new boolean[listsize];
		tour[0] = 0;
		used[0] = true;
		for(int i = 1; i < listsize; i++){
			used[i] = false;
			tour[i] = 0;
		}
		for(int i = 1; i < listsize; i++){
			int best = -1;
			for(int j = 0; j<listsize; j++){
				if(!used[j] && (best == -1 || distances[tour[i-1]][j] < distances[tour[i-1]][best])){
					best = j;
				}
			}
			tour[i] = best;
			used[best] = true;
		}
		return tour;
	}

	public double calcDist(Tuple first, Tuple second){
		double a = Math.abs(first.getX()-second.getX());
		double b = Math.abs(first.getY()-second.getY());
		a *= a;
		b *= b;
		return Math.sqrt(a+b);
	}
	
	public double calcTotalTourLength(int[] tour){
		double distance = 0;
		for(int i = 0; i < listsize-1; i++){
			distance += distances[tour[i]][tour[i+1]];
		}
		distance += distances[tour[0]][tour[tour.length-1]];
		return distance;
	}
	
	public boolean calcDistSwitch(int[] tour, int i, int j){
		int iminone = i-1;
		int jplusone = j+1;
		if(i == 0) iminone = listsize-1;
		if(j == listsize-1) jplusone = 0;
		
		double difference = 0;
		difference += distances[tour[iminone]][tour[i]];
		difference += distances[tour[j]][tour[jplusone]];
		
		difference -= distances[tour[iminone]][tour[j]];
		difference -= distances[tour[i]][tour[jplusone]];
		
		if(difference > 0){
			return true;
		}
		return false;
	}
	
	public int[] twoOptSwap(int[] route, int i, int k){
		int start = i;
		int end = k;
		while(start < end){
			int tmp = route[start];
			route[start] = route[end];
			route[end] = tmp;
			start ++;
			end --;
		}
		return route;
	}
}
