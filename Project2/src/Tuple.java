
public class Tuple {
	
	double x;
	double y;
	
	public Tuple(double x, double y){
		x = this.x;
		y = this.y;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double dist(){
		return Math.abs(x-y);
	}
	
	
}
