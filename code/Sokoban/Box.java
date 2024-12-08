import java.awt.*;

import Utilities.UI.*;

public class Box{
    private Image box;
    private Image boxOnShelf;
 
    public Box(){
	try{
	    box = ImageUtilities.getImage("box.gif");
	    boxOnShelf = ImageUtilities.getImage("boxOnShelf.gif");
	}
	catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public void draw(Graphics g, int x, int y, int width, int height, Cell location){
	Image imageToDraw = null;

	if(location == Cells.SHELF)
	    imageToDraw = boxOnShelf;
	else
	    imageToDraw = box;

	g.drawImage(imageToDraw, x, y, x+width, y+height, 0, 0, imageToDraw.getWidth(null), imageToDraw.getHeight(null), null);
    }
}
