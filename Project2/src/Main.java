import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {

	double size;
	public static void main(String[] args) throws NumberFormatException, IOException {
		Main main = new Main();
		main.run();
	}
	
	public void run() throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));//(new FileReader("sample.txt")); // TODO - change to (new InputStreamReader(System.in));
		size = Double.parseDouble(br.readLine());
		ArrayList<Tuple> coords = new ArrayList<Tuple>();
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
	}
	
	public void print(ArrayList<Integer> tour){
		for(int i = 0; i<tour.size(); i++){
			System.out.println(tour.get(i));
		}
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
	
}
