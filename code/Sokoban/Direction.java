/** An enumerated type for the four directions.
    Each direction has an angle associated with it, and
     an x and y coordinate 
    You can use EAST as a constant to refer to that direction,
    and you can use EAST.degrees to get its associated angle.
*/

 public enum Direction{
     EAST (1, 0, 0),
     SOUTH (0, 1, 90),
     WEST  (-1, 0, 180),
     NORTH (0, -1, 270);

     public final int degrees;
     public final int x;
     public final int y;

     Direction (int x, int y, int degrees){
	 this.x = x;
	 this.y = y;
	 this.degrees = degrees;
     }

 }
