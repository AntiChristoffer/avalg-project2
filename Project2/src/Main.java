import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {

	int size;
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		Main main = new Main();
		main.print();
		main.run();
	}
	
	public void run() throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader("sample.in")); // TODO - change to (new InputStreamReader(System.in));
		size = Integer.parseInt(br.readLine());
		ArrayList<Tuple> coords = new ArrayList<Tuple>();
		System.out.println("created coords");
		for(int i = 0; i<size; i++){
			String temp = br.readLine();
			String[] t = temp.split(" ");
			Tuple tmpTuple = new Tuple(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
			coords.add(i,tmpTuple);
		}
		System.out.println("created array");
	}
	
	public void print(){
		System.out.println("Hello World! Testing git from Eclipse");
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
			int best = -1;
			for(int j = 0; j<coords.size(); j++){
			}
		}
		return tour;
	}

}
