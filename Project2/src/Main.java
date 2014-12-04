import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


/*
 * All reference distances using sample.txt sample input
 * distance with greedyTour (0 8 5 4 3 9 6 2 1 7) = 225.7290569637806
 * distance with 2-opt (4 5 8 0 2 6 9 3 1 7) = 188.74784321058627
 */

public class Main {

	double size;
	ArrayList<Tuple> coords;
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
		size = Double.parseDouble(br.readLine());
		coords = new ArrayList<Tuple>();
		if(DEBUG)System.out.println("created coords");
		for(int i = 0; i<size; i++){
			String temp = br.readLine();
			String[] t = temp.split(" ");
			double tmpX = Double.parseDouble(t[0]);
			double tmpY = Double.parseDouble(t[1]);
			Tuple tmpTuple = new Tuple(tmpX, tmpY);
			coords.add(i,tmpTuple);
		}
		br.close();
		listsize = coords.size();
		distances = new double[listsize][listsize];
		for(int i=0; i<listsize;i++){
			distances[i][i] = 0;
			for(int j=0; j<i; j++){
				double dist = calcDist(coords.get(i), coords.get(j));
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
		
		ArrayList<Integer> tour = new ArrayList<Integer>();
		if(DEBUG)System.out.println("created array");
		
		tour = greedyTour(coords);
		if(DEBUG){
			double dist = calcTotalTourLength(tour);
			print(tour);
			System.out.println("distance after greedy = " +Double.toString(dist));
		}
		
		tour = twoOPT(tour);
		print(tour);
			double dist = calcTotalTourLength(tour);
			System.out.println("distance after 2opt = " +Double.toString(dist));
		
		
	}
	
	public void printMatrix(){
		for(int i=0; i<listsize; i++){
			for(int j=0; j<listsize; j++){
				System.out.print(distances[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	public void print(ArrayList<Integer> tour){
		for(int i = 0; i<listsize; i++){
			System.out.println(tour.get(i));
		}
	}
	
	public ArrayList<Integer> twoOPT(ArrayList<Integer> tour){
		ArrayList<Integer> newTour = new ArrayList<Integer>();
		boolean foundBetter = true;
		int iter = 0;
		while(iter <10){
			foundBetter = false;
			for(int i = 0; i<listsize-1; i++){
				for(int j = i+1; j<listsize; j++){
					newTour = twoOptSwap(tour, i, j);
					foundBetter = calcDistSwitch(newTour, tour, i,j);
					if(foundBetter){
						tour = new ArrayList<Integer>(newTour);
					}
				}
			}
			iter++;
		}
		return tour;
	}
	
	public ArrayList<Integer> greedyTour(ArrayList<Tuple> coords){
		ArrayList<Integer> tour = new ArrayList<Integer>();
		ArrayList<Boolean> used = new ArrayList<Boolean>();
		tour.add(0, 0);
		used.add(0, true);
		for(int i = 1; i < listsize; i++){
			used.add(i, false);
			tour.add(i, 0);
		}
		for(int i = 1; i < listsize; i++){
			int best = -1;
			for(int j = 0; j<listsize; j++){
				if(!used.get(j) && (best == -1 || distances[tour.get(i-1)][j] < distances[tour.get(i-1)][best])){
					best = j;
				}
			}
			tour.set(i, best);
			used.set(best, true);
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
	
	public double calcTotalTourLength(ArrayList<Integer> tour){
		double distance = 0;
		for(int i = 0; i < listsize-1; i++){
			distance += distances[tour.get(i)][tour.get(i+1)];
		}
		distance += distances[tour.get(0)][tour.get(tour.size()-1)];
		return distance;
	}
	
	public boolean calcDistSwitch(ArrayList<Integer> newRoute, ArrayList<Integer> bestRoute, int i, int j){
		int iminone = i-1;
		int jplusone = (j+1)%(listsize);
		if(i == 0) iminone = listsize-1;
		
		double difference = 0;
		difference += distances[bestRoute.get(iminone)][bestRoute.get(i)];
		difference -= distances[newRoute.get(iminone)][newRoute.get(i)];
		
		difference += distances[bestRoute.get(j)][bestRoute.get(jplusone)];
		difference -= distances[newRoute.get(j)][newRoute.get(jplusone)];
		
		if(difference > 0){
			return true;
		}
		return false;
		/*
		
		double bestDist = 0;
		double newDist = 0;
		if(i == 0 && j == newRoute.size()-1){
			return false;
		}else if(j == newRoute.size()-1){
			bestDist += calcDist(coords.get(bestRoute.get(i)), coords.get(bestRoute.get(i-1)));
			newDist += calcDist(coords.get(newRoute.get(i)), coords.get(newRoute.get(i-1)));
		} else if (i == 0){
			bestDist += calcDist(coords.get(bestRoute.get(j)), coords.get(bestRoute.get(j+1)));
			newDist += calcDist(coords.get(newRoute.get(j)), coords.get(newRoute.get(j+1)));
		} else {
			bestDist += calcDist(coords.get(bestRoute.get(j)), coords.get(bestRoute.get(j+1))) + calcDist(coords.get(bestRoute.get(i)), coords.get(bestRoute.get(i-1)));
			newDist += calcDist(coords.get(newRoute.get(j)), coords.get(newRoute.get(j+1))) + calcDist(coords.get(newRoute.get(i)), coords.get(newRoute.get(i-1)));
		}
		if(newDist < bestDist){
			return true;
		} else {
			return false;
		}
		*/
	}
	
	public ArrayList<Integer> twoOptSwap(ArrayList<Integer> route, int i, int k){
		@SuppressWarnings("unchecked")
		ArrayList<Integer> temp = (ArrayList<Integer>) route.clone();
		int start = i;
		int end = k;
		while(start < end){
			int tmp = route.get(start);
			temp.set(start, route.get(end));
			temp.set(end, tmp);
			start ++;
			end --;
		}
		return temp;
	}
}
