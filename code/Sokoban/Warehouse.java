import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class Warehouse{
    private Cell[][] cells;
    private Agent agent;
    private Map<Coord, Box> boxes = new HashMap();  //A map containing the boxes, keyed by position

    private SimplePlanner planner = new SimplePlanner();  // The planner object that can find a path.

    public Warehouse(String filename) throws Exception{
	ArrayList<String> rows = new ArrayList();

	Scanner scanner = new Scanner(new File(filename));
	    
	while(scanner.hasNextLine())
	    rows.add(scanner.nextLine());
	    
	scanner.close();
	
	        int langsteRij = 0;      
        for(int idx = 0; idx < rows.size(); idx++)
        {
            if (rows.get(idx).length() > langsteRij) langsteRij = rows.get(idx).length();
        }

	cells = new Cell[langsteRij][rows.size()];
	for(int y = 0; y < rows.size(); y++){
	    String row = rows.get(y);
	    
	    for(int x = 0; x < langsteRij ; x++){
		 Cell cell;
		 
		 if (x < row.length()){ cell = Cells.getCorrespondingCell(row.charAt(x));}
		 else { cell = Cells.getCorrespondingCell(' ');}
		 
		Coord loc = new Coord(x, y);

		if(cell == Cells.AGENT){
		    agent = new Agent();
		    agent.setLocation(loc);
		    cell = Cells.BLANK;
		}
		else if(cell == Cells.BOX){
		    boxes.put(loc, new Box());
		    cell = Cells.BLANK;
		}
		else if(cell == Cells.BOX_ON_SHELF){
		    boxes.put(loc, new Box());
		    cell = Cells.SHELF;
		}

		cells[x][y] = cell;
	    }
	}
    }

    public Warehouse(Cell[][] c, Agent a, Map<Coord, Box> b){
	cells = c;
	agent = a;
	boxes = b;
    }

    /** Returns a new state of the warehouse (a modified copy) after the agent has
	moved in the specified direction.
	If the agent would move into a wall, or would push a box, it returns null
     */
    public Warehouse moveAgentFree(Direction dir){
	Coord newLoc = agent.getLocation().move(dir);
	Cell newCell = getCell(newLoc); 

	if ( newCell == null || newCell == Cells.WALL || containsBox(newLoc) )
	    // can't move freely
	    return null;
	
	// Otherwise, make new state 
	Warehouse newState = copy();
	newState.agent.setOrientation(dir);
	newState.agent.setLocation(newLoc);
	newState.agent.setPushing(false);

	return newState;
    }



    /** Returns a new state of the warehouse (a modified copy) after the agent has
	moved in the specified direction.
	If the agent would move into a wall, or an unpushable box, it returns null
     */

    public Warehouse moveAgent(Direction dir){

	Coord newLoc = agent.getLocation().move(dir);
	Cell newCell = getCell(newLoc); 

	if (newCell == null || newCell == Cells.WALL) // can't move
	    return null; 

	if ( containsBox(newLoc) ){  // trying to push a box
	    Cell newBoxCell = getCell(newLoc.move(dir));
	    if ( newBoxCell == null || newBoxCell == Cells.WALL || containsBox(newLoc.move(dir)) )   // can't push the box
		return null;
	}
	
	// make new state 
	Warehouse newState = copy();

	newState.agent.setOrientation(dir);
	newState.agent.setLocation(newLoc);

	Box box = newState.boxes.get(newLoc);  // The box being pushed, if any

	if(box == null)  // just moving
	    newState.agent.setPushing(false);
	else{  // pushing
	    newState.agent.setPushing(true);
	    newState.boxes.remove(newLoc);
	    newState.boxes.put(newLoc.move(dir), box);
	}

	return newState;
    }

    /** Returns true iff the warehouse is solved - all the boxes are on shelves */
    public boolean isSolved(){
	for(Coord loc : boxes.keySet()){
	    if(cells[loc.x][loc.y] != Cells.SHELF){
		return  false;
	    }
	}
	return true;
    }


    public Warehouse copy(){
	Map<Coord, Box> newBoxes = new HashMap();
	 for(Iterator<Coord> i = boxes.keySet().iterator(); i.hasNext(); ) {
	    Coord c = i.next();
	    Box b = boxes.get(c);
	    newBoxes.put(c, b);
	 }
	return new Warehouse(cells, agent.copy(), newBoxes);
    }

    public Cell[][] getCells(){
	return cells;
    }

    public Agent getAgent(){
	return agent;
    }

    public void setAgent(Agent a){
	agent = a;
    }

    public Map<Coord, Box> getBoxes(){
	return boxes;
    }

    private Cell getCell(Coord loc){
	try{
	    return cells[loc.x][loc.y];
	}
	catch(ArrayIndexOutOfBoundsException e){
	    return null;
	}
    }

    public boolean containsBox(Coord loc){
	return (boxes.get(loc) != null);
    }

    public boolean equals(Object o){
	if (this==o) return true;
	Warehouse other = (Warehouse) o;
	Coord otherAgentLoc = other.agent.getLocation();
	return boxes.keySet().equals(other.boxes.keySet()) && 
	    (agent.getLocation().equals(otherAgentLoc)  ||
	     !planner.findPath(this, otherAgentLoc).isEmpty());
    }

    public int hashCode(){
	return boxes.hashCode();
    }

}
