import java.awt.*;

import Utilities.UI.*;

public class Agent{

   
    private Coord location;

    private static Image sokoban;
    private static Image sokobanPushing;
    private Image sourceImage;
    private Direction orientation = Direction.EAST;
    private boolean recomputeImage = true;

    public Agent(){
	try{
	    if(sokoban == null)
		sokoban = ImageUtilities.getImage("sokoban.gif");

	    if(sokobanPushing == null)
		sokobanPushing = ImageUtilities.getImage("sokobanPushing.gif");

	    sourceImage = sokoban;
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public Agent copy(){
	Agent copy = new Agent();
	copy.orientation = orientation;
	copy.location = location;
	copy.sourceImage = sourceImage;
	return copy;
    }

    public void setSourceImage(Image s){
	sourceImage = s;
    }
    
    public Coord getLocation(){
	return location;
    }

    /*    
    public int getX(){
	return location.x;
    }

    public int getY(){
	return location.y;
	}
    */

    public void setOrientation(Direction o){
	orientation = o;
    }

    public void setPushing(boolean pushing){
	if(pushing)
	    sourceImage = sokobanPushing;
	else
	    sourceImage = sokoban;
    }

    public void setLocation(Coord loc){
	location = loc;
    }

    public void draw(Graphics g, int x, int y, int width, int height){
	Image imageToDraw = sourceImage;

	try{
	    imageToDraw = ImageUtilities.getRotatedImage(sourceImage, orientation.degrees);
	    recomputeImage = false;
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}

	g.drawImage(imageToDraw, x, y, x+width, y+height, 0, 0, imageToDraw.getWidth(null), imageToDraw.getHeight(null), null);
    }

    public boolean equals(Object o){
	Agent other = (Agent) o;
	return location.equals(other.location);
    }

    public int hashCode(){
	return location.hashCode();
    }

    public String toString(){
	return "(" + location.x + "," + location.y + ")";
    }
}
