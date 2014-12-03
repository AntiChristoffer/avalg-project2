import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


/*
 * All reference distances using sample.txt sample input
 * distance with greedyTour (0 8 5 4 3 9 6 2 1 7) = 225.7290569637806
 */

public class Main {

	double size;
	ArrayList<Tuple> coords;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		Main main = new Main();
		main.run();
	}
	
	public void run() throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader (new FileReader("sample.txt")); // TODO - change to (new InputStreamReader(System.in)) on Kattis submission;
		size = Double.parseDouble(br.readLine());
		coords = new ArrayList<Tuple>();
		//System.out.println("created coords");
		for(int i = 0; i<size; i++){
			String temp = br.readLine();
			String[] t = temp.split(" ");
			double tmpX = Double.parseDouble(t[0]);
			double tmpY = Double.parseDouble(t[1]);
			Tuple tmpTuple = new Tuple(tmpX, tmpY);
			coords.add(i,tmpTuple);
		}
		//System.out.println("created array");
		ArrayList<Integer> tour = greedyTour(coords);
		print(tour);
		double dist = calcTotalTourLength(tour);
		System.out.println("distance after greedy = " +Double.toString(dist));
		tour = twoOPT(tour);
		dist = calcTotalTourLength(tour);
		print(tour);
		System.out.println("distance after 2opt = " +Double.toString(dist));
		
		/*test swap
		ArrayList<Integer> test = new ArrayList<Integer>();
		for(int i=0; i<5; i++){
			test.add(i);
		}
		twoOptSwap(test, 0, 4);
		for(int i=0; i<test.size(); i++){
			System.out.println(test.get(i));
		}*/
	}
	
	public void print(ArrayList<Integer> tour){
		for(int i = 0; i<tour.size(); i++){
			System.out.println(tour.get(i));
		}
	}
	
	public ArrayList<Integer> twoOPT(ArrayList<Integer> tour){
		
		double newDistance = Double.MAX_VALUE;
		double bestDistance = calcTotalTourLength(tour);
		ArrayList<Integer> bestTour = tour;
		ArrayList<Integer> newTour = new ArrayList<Integer>();
		boolean foundBetter = true;
		while(foundBetter){
			foundBetter = false;
			for(int i = 0; i<tour.size()-1; i++){
				for(int j = i+1; j<tour.size(); j++){
					newTour = twoOptSwap(bestTour, i, j);
					newDistance = calcTotalTourLength(newTour);
					if(newDistance < bestDistance){
						//bestTour.addAll(newTour);
						//Collections.copy(bestTour, newTour);
						bestTour = new ArrayList<Integer>(newTour);
						bestDistance = newDistance;
						foundBetter = true;
					}
				}
			}
		}
		return bestTour;
	}
	
	/*GreedyTour - given on Kattis
   tour[0] = 0
   used[0] = true
   for i = 1 to n-1
      best = -1
      for j = 0 to n-1
         if not used[j] and (best = -1 or dist(tour[i-1],j) < dist(tour[i-1],best))
	    best = j
      tour[i] = best
      used[best] = true
   return tour*/
	public ArrayList<Integer> greedyTour(ArrayList<Tuple> coords){
		ArrayList<Integer> tour = new ArrayList<Integer>();
		ArrayList<Boolean> used = new ArrayList<Boolean>();
		tour.add(0, 0);
		used.add(0, true);
		for(int i = 1; i < coords.size(); i++){
			used.add(i, false);
			tour.add(i, 0);
		}
		for(int i = 1; i < coords.size(); i++){
			int best = -1;
			for(int j = 0; j<coords.size(); j++){
				if(!used.get(j) && (best == -1 || dist(coords.get(tour.get(i-1)), coords.get(j)) < dist(coords.get(tour.get(i-1)), coords.get(best)))){
					best = j;
				}
			}
			tour.set(i, best);
			used.set(best, true);
		}
		return tour;
	}

	public double dist(Tuple first, Tuple second){
		double a = Math.abs(first.getX()-second.getX());
		double b = Math.abs(first.getY()-second.getY());
		return Math.sqrt(Math.pow(a, 2)+Math.pow(b, 2));
	}
	
	public double calcTotalTourLength(ArrayList<Integer> tour){
		double distance = 0;
		for(int i = 1; i < tour.size(); i++){
			distance += dist(coords.get(tour.get(i-1)), coords.get(tour.get(i)));
		}
		return distance;
	}
	
	public ArrayList<Integer> twoOptSwap(ArrayList<Integer> route, int i, int k){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int j = 0; j<route.size(); j++){
			temp.add(j, route.get(j));
		}
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
