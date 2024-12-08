import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
   
public class Sokoban{
    private Warehouse originalWarehouse;
    private Warehouse warehouse;
    private JComponent canvas;
      
    public Sokoban(){
	try{
//	    originalWarehouse = new Warehouse("warehouse4");
	    originalWarehouse = new Warehouse("puzzels" + java.io.File.separatorChar + "01.txt");
	} catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}

	warehouse = originalWarehouse.copy();
	
	setupInterface();
    }

    private void setupInterface(){

	final JFrame frame = new JFrame("Sokoban");
	frame.setSize(800,550);

	canvas = makeCanvas();

	InputMap canvasInputMap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	ActionMap canvasActionMap = canvas.getActionMap();

	canvasInputMap.put(KeyStroke.getKeyStroke("LEFT"), "Left Arrow");
	canvasActionMap.put("Left Arrow",
			    new AbstractAction(){public void actionPerformed(ActionEvent e){
				doMove(Direction.WEST);
			    }});
	 
	canvasInputMap.put(KeyStroke.getKeyStroke("UP"), "Up Arrow");
	canvasActionMap.put("Up Arrow", 
			    new AbstractAction(){public void actionPerformed(ActionEvent e){
				doMove(Direction.NORTH);
			    }});

	canvasInputMap.put(KeyStroke.getKeyStroke("RIGHT"), "Right Arrow");
	canvasActionMap.put("Right Arrow", 
			    new AbstractAction(){public void actionPerformed(ActionEvent e){
				    doMove(Direction.EAST);
			    }});

	canvasInputMap.put(KeyStroke.getKeyStroke("DOWN"), "Down Arrow");
	canvasActionMap.put("Down Arrow", 
			    new AbstractAction(){public void actionPerformed(ActionEvent e){
				    doMove(Direction.SOUTH);
			    }});

	frame.getContentPane().add(canvas, BorderLayout.CENTER);

	frame.setVisible(true);
    }

    private void doMove(Direction dir){
	Warehouse newWH = warehouse.moveAgent(dir);
	if (newWH != null) warehouse = newWH;
	canvas.repaint();
    }
    private JComponent makeCanvas(){
	return
	new JComponent(){
	    public void paint(Graphics g){
		Dimension d = new Dimension();
		getSize(d);

		Cell[][] cells = warehouse.getCells();
		Agent agent = warehouse.getAgent();
		Map<Coord, Box> boxes = warehouse.getBoxes();

		int cellWidth = (int) Math.round(d.getWidth()/((double) cells.length));
		int cellHeight = (int) Math.round(d.getHeight()/((double) cells[0].length));

		for(int x = 0; x < cells.length; x++){
		    for(int y = 0; y < cells[0].length; y++)
			cells[x][y].draw(g, x*cellWidth, y*cellHeight, cellWidth, cellHeight);
		}

		agent.draw(g, agent.getLocation().x*cellWidth, agent.getLocation().y*cellHeight, cellWidth, cellHeight);

		for(Map.Entry<Coord, Box> entry : boxes.entrySet())
		    entry.getValue().draw(g, entry.getKey().x*cellWidth, entry.getKey().y*cellHeight, cellWidth, cellHeight, cells[entry.getKey().x][entry.getKey().y]);

		g.setXORMode(Color.WHITE);
		for(int x = 0; x < cells.length; x++)
		    g.drawLine(x*cellWidth, 0, x*cellWidth, (int) d.getHeight());

		for(int y = 0; y < cells[0].length; y++)
		    g.drawLine(0, y*cellHeight, (int) d.getWidth(), y*cellHeight);

		g.setPaintMode();
	    } 
	};
    }
    public static void main(String[] args){
	new Sokoban();
    }
}
