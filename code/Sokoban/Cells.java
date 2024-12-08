import java.awt.*;
import java.awt.image.*;

import Utilities.UI.*;

public class Cells{
    public static final Cell BLANK =
	new Cell(){
	    public void draw(Graphics g, int x, int y, int width, int height){
		Color previous = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(x,y,width,height);
		g.setColor(previous);
	    }
	};

    public static final Cell WALL =
	new Cell(){
	    public void draw(Graphics g, int x, int y, int width, int height){
		Color previous = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(x,y,width,height);
		g.setColor(previous);
	    }
	};

    public static final Cell AGENT = 
	new Cell(){
	    public void draw(Graphics g, int x, int y, int width, int height){}
	};
    public static final Cell BOX = 
	new Cell(){
	    public void draw(Graphics g, int x, int y, int width, int height){}
	};
    public static final Cell BOX_ON_SHELF = 
	new Cell(){
	    public void draw(Graphics g, int x, int y, int width, int height){}
	};

    public static final Cell SHELF = new Cell(getImage("shelf.gif"));

    public static Image getImage(String filename){
	try{
	    return ImageUtilities.getImage(filename);
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}

	return null;
    }

    public static Cell getCorrespondingCell(char symbol){
        if(symbol == ' ') return BLANK;	 
        if(symbol == '#') return WALL;
        if(symbol == '@') return AGENT;	 
        if(symbol == '$') return BOX;
        if(symbol == '.') return SHELF;
        if(symbol == '*') return BOX_ON_SHELF;

        return null;
    }
}
