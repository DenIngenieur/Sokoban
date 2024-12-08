import java.util.*;

public class SimplePlanner{


    /** Does a search to find a free path through the warehouse from the agent's
        current position to the destination position.
        It returns a List of the directions to move.

	Note, Direction is an enum type; you can loop through each
	possible direction as in the loop below

	The warehouse has a moveAgent(Direction) method which tries
	to move the agent in the specified diretion.
	If the agent can't move, it returns null.
	If the agent can move, it returns a new warehouse with the
	  agent (and possibly a box) in a new position.
	
        The example code below constructs a trivial path; you won't
	want to use this code, but it shows how to use Directions
	and the moveAgent method.
        */

    public List<Direction> findPath(Warehouse warehouse, Coord dest){

	List path = new ArrayList();

	Warehouse wh = warehouse.moveAgentFree(Direction.NORTH);
	if (wh != null)
	    path.add(Direction.NORTH);


	//Try to move 5 steps
	// At each step, loop through the four possible directions.

	for (int i=0; i<5; i++){
	    for(Direction dir : Direction.values()){
		Warehouse w = warehouse.moveAgentFree(dir);
		if (w != null){
		    path.add(dir);
		    warehouse = w;
		    break;
		}
	    }	
	}

	return path;
    }

  public List<Direction> findSolutionPath(Warehouse warehouse){
     return null;
  }
}
