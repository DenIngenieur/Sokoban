import java.awt.*;

public class Cell{
    private Image image;
 
    public Cell(){}

    public Cell(Image i){
	image = i;
    }

    public void draw(Graphics g, int x, int y, int width, int height){	 
	g.drawImage(image, x, y, x+width, y+height, 0, 0, image.getWidth(null), image.getHeight(null), null);
    }
}
