/** A pair of x and y representing a coordinate in the warehouse.
*/

 public class Coord{
     public final int x;
     public final int y;

     Coord (int x, int y){
	 this.x = x;
	 this.y = y;
     }

     public Coord move(Direction dir){
	 return new Coord(x+dir.x, y+dir.y);
     }

     public boolean equals(Object o){
	 Coord other = (Coord) o;
	 return x==other.x && y==other.y;
     }

     public int hashCode(){
	 return x*100 + y;
     }

 }
