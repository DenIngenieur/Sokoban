import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
   
public class Sokoban{
    private Warehouse originalWarehouse;
    private Warehouse warehouse;
    private Dimension buttonDimension = new Dimension(500, 40);
    private Dimension textFieldDimension = new Dimension(500, 20);
    private JComponent canvas;

    private SimplePlanner planner = new SimplePlanner();

      
    public Sokoban(){
	try{
	    originalWarehouse = new Warehouse("warehouse4");
	} catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}

	warehouse = originalWarehouse.copy();
	
	setupInterface();
    }

    /** This method is called when the user clicks in a cell.
	It attempts to move the agent to the cell, as long as there is a free path
	to the cell.   It calls the findPath method on the SimplePlanner object, which
	will return a List of directions that the agent should move to get to the cell
	If there is no such path, it should return an empty List
    */
    public void goToCell(int x, int y){
	Coord destination = new Coord(x, y);
	java.util.List<Direction> path = planner.findPath(warehouse, destination);

	if ( path.isEmpty() )
	    System.out.println("No way to get to cell " + destination);
	else{
	    for(Direction step : path){
		Warehouse newWarehouse = warehouse.moveAgent(step);
		if (newWarehouse == null) break;
		warehouse = newWarehouse;
	    }

	    canvas.repaint();
	}
    }

    /** Method called by the "solve" button.
	It attempts to solve the current warehouse.
	It calls the findSolutionPath method on the SimplePlanner object, which
	will return a List of directions that the agent should move to solve the problem
	If the list is empty, then the planner was not able to find a solution.
    */
    public void solve(){
	java.util.List<Direction> path = planner.findSolutionPath(warehouse);
	if ( path.isEmpty() )
	    System.out.println("No way to solve ");
	else{
	    for(Direction step : path){
		Warehouse newWarehouse = warehouse.moveAgent(step);
		if (newWarehouse == null) break;
		warehouse = newWarehouse;
	    }

	    canvas.repaint();
	}
    }

    private void setupInterface(){

	final JFrame frame = new JFrame("Sokoban");
	frame.setSize(800,550);

	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

	addButton("Restart", buttonPanel, 
		  new ActionListener(){
		      public void actionPerformed(ActionEvent e){
			  warehouse = originalWarehouse.copy();
			  canvas.repaint();
		      }
		  });
	addButton("Solve", buttonPanel, 
		  new ActionListener(){
		      public void actionPerformed(ActionEvent e){
			  solve();
			  canvas.repaint();
		      }
		  });
	addButton("Quit", buttonPanel, 
		  new ActionListener(){
		      public void actionPerformed(ActionEvent e){
			  frame.dispose();
		      }
		  });

	addTextField("Enter file to load", buttonPanel, 
		     new ActionListener(){
			 public void actionPerformed(ActionEvent e){
			     try{
				 originalWarehouse = new Warehouse(e.getActionCommand());
				 warehouse = originalWarehouse.copy();
				 canvas.repaint();
				 canvas.repaint();
			     }
			     catch(Exception ex){
				 System.out.println("Can't open file " + e.getActionCommand());
			     }
			 }
		     });
	
	frame.getContentPane().add(buttonPanel, BorderLayout.WEST);


	canvas = makeCanvas();
	canvas.addMouseListener(makeMouseListener());


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


    private JButton addButton(String name, JPanel panel, ActionListener listener){
	JButton button = new JButton(name);
	button.addActionListener(listener);
	button.setMinimumSize(buttonDimension);
	button.setMaximumSize(buttonDimension);
	panel.add(button);
	return button;
    }

    private JTextField addTextField(String label, JPanel panel, ActionListener listener){
	final JTextField textField = new JTextField(10);
	textField.addActionListener(listener);
	textField.setMaximumSize(textFieldDimension);

	JPanel tpanel = new JPanel();
	tpanel.setLayout(new BoxLayout(tpanel, BoxLayout.Y_AXIS));

	tpanel.add(new JLabel(label));
	tpanel.add(textField);
	panel.add(tpanel);

	return textField;
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

    private MouseListener makeMouseListener(){
	return new MouseListener(){
		public void mouseClicked(MouseEvent e){
		    Dimension d = new Dimension();
		    canvas.getSize(d);

		    Cell[][] cells = warehouse.getCells();
		    Agent agent = warehouse.getAgent();
		    Map<Coord, Box> boxes = warehouse.getBoxes();

		    int cellWidth = (int) Math.round(d.getWidth()/((double) cells.length));
		    int cellHeight = (int) Math.round(d.getHeight()/((double) cells[0].length));

		    int cellX = e.getX()/cellWidth;
		    int cellY = e.getY()/cellHeight;
		    goToCell(cellX, cellY);

		}

		public void mouseExited(MouseEvent e){}

		public void mouseEntered(MouseEvent e){}

		public void mousePressed(MouseEvent e){}

		public void mouseReleased(MouseEvent e){}
	    };
    }

    public static void main(String[] args){
	new Sokoban();
    }
}
