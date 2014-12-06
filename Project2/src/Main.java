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
		distances = new double[listsize][listsize];
		coords = new Tuple[listsize];
		if(DEBUG)System.out.println("created coords");
		for(int i = 0; i<listsize; i++){
			String temp = br.readLine();
			String[] t = temp.split(" ");
			double tmpX = Double.parseDouble(t[0]);
			double tmpY = Double.parseDouble(t[1]);
			distances[i][i] = 0;
			
			Tuple tmpTuple = new Tuple(tmpX, tmpY);
			coords[i] = tmpTuple;
			for(int j=i-1; j>=0; j--){
				double dist = calcDist(tmpTuple, coords[j]);
				distances[i][j] = dist;
				distances[j][i] = dist;
			}
		}
		br.close();
		
		/*
		for(int i=0; i<listsize;i++){
			distances[i][i] = 0;
			for(int j=0; j<i; j++){
				double dist = calcDist(coords[i], coords[j]);
				distances[i][j] = dist;
				distances[j][i] = dist;
			}
		}*/
		printMatrix();
		
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
			double dist = calcTotalTourLength(tour);
			System.out.println("distance after 2opt/2.5opt = " +Double.toString(dist));
		
		
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
		Double resTwo;
		Double resTwoFive;
		int iter = 0;
		while(iter <10){
			for(int i = 0; i<listsize-1; i++){
				for(int j = i+2; j<listsize; j++){
					resTwo = null;
					resTwoFive = null; 
					if(i > 1){
						resTwoFive = twoFive(tour, i, j);
					}
					resTwo = calcDistSwitch(tour, i, j);
					
					if(resTwo != null && resTwoFive != null){
						//System.out.println("Twofive: "+resTwoFive+" Two: "+resTwo);
						if(resTwoFive > resTwo){
							tour = twoOptSwap(tour, i, j);
						}else{
							tour = twoHalfOptSwap(tour, i, j);
						}
					}else if(resTwo != null){
						tour = twoOptSwap(tour, i, j);
					}else if(resTwoFive != null){
						tour = twoHalfOptSwap(tour, i, j);
					}else{
						continue;
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
		/*for(int i = 1; i < listsize; i++){
			used[i] = false;
			tour[i] = 0;
		}*/
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
	
	public Double calcDistSwitch(int[] tour, int i, int j){
		int iminone = i-1;
		int jplusone = j+1;
		if(i == 0) iminone = listsize-1;
		if(j == listsize-1) jplusone = 0;
		
		double distold = 0;
		distold += distances[tour[iminone]][tour[i]];
		distold += distances[tour[j]][tour[jplusone]];
		
		double distnew = 0;
		distnew += distances[tour[iminone]][tour[j]];
		distnew += distances[tour[i]][tour[jplusone]];
		
		if(distold > distnew){
			return distold-distnew;
		}
		return null;
	}
	
	public int[] twoOptSwap(int[] tour, int i, int k){
		int start = i;
		int end = k;
		while(start < end){
			int tmp = tour[start];
			tour[start] = tour[end];
			tour[end] = tmp;
			start ++;
			end --;
		}
		return tour;
	}
	
	public int[] twoHalfOptSwap(int[] tour, int start, int end){
		int toMove;

		if(end == listsize -1){
			toMove = tour[0];
		}else{
			toMove = tour[end+1];
		}
		
		int first = tour[end];
		
		//int i = start+1;
		/*while(i <= end-1){
			int next = tour[i+1];
			tour[i+1] = first;
			first = next;
			i++;
		}*/
		int elements=end-start-1;
		System.arraycopy(tour, start+1, tour, start+2, elements);
		
		tour[start+1] = toMove;
		if(end == listsize -1){
			tour[0] = first;
		}else{
			tour[end+1] = first;
		}
		
		return tour;
	}
	
	public Double twoFive(int[] tour, int start, int end){
		int i, ione, j, jone, jtwo;
		i = tour[start];
		ione = tour[start+1];
		j = tour[end];
		if(end == listsize-2){
			jone = tour[end+1];
			jtwo = tour[0];
		}else if(end == listsize-1){
			jone = tour[0];
			jtwo = tour[1];
		}else{
			jone = tour[end+1];
			jtwo = tour[end+2];
		}
		
		double distold = 0;
		distold += distances[i][ione];
		distold += distances[j][jone];
		distold += distances[jone][jtwo];
		
		double distnew = 0;
		distnew += distances[i][jone];
		distnew += distances[jone][ione];
		distnew += distances[j][jtwo];
		
		
		if(distold > distnew){
			return distold-distnew;
		}
		return null;
	}
}
