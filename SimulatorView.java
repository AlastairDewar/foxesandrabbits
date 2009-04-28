import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A graphical view of the simulation grid.
 * The view displays a coloured rectangle for each location 
 * representing its contents. It uses a default background colour.
 * Colours for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Alastair Fraser Dewar
 * @version 2009.03.27
 */
public class SimulatorView extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;

	// Colours used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Colour used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    
    public Simulator sim;
    
    // A map for storing colours for participants in the simulation
	private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    public FieldStats stats;

    private JMenuItem menuItemPause;
    
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(Simulator newSim, int height, int width)
    {
    	this.sim = newSim;
    	
        stats = new FieldStats();
        colors = new LinkedHashMap<Class, Color>();

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        setResizable(false);
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Simulator");
        menuBar.add(fileMenu);
        
        JMenuItem menuItemRun = new JMenuItem("Run long simulation");
        menuItemRun.setActionCommand("run");
        menuItemRun.addActionListener(this);
        fileMenu.add(menuItemRun);
        
        JMenuItem menuItemCustomRun = new JMenuItem("Run custom number of steps");
        menuItemCustomRun.setActionCommand("customrun");
        menuItemCustomRun.addActionListener(this);
        fileMenu.add(menuItemCustomRun);
        
        fileMenu.addSeparator();
        
        this.menuItemPause = new JMenuItem("Pause");
        menuItemPause.setActionCommand("pause");
        menuItemPause.addActionListener(this);
        fileMenu.add(menuItemPause);        
        
        fileMenu.addSeparator();
        
        JMenuItem menuItemReset = new JMenuItem("Reset");
        menuItemReset.setActionCommand("reset");
        menuItemReset.addActionListener(this);
        fileMenu.add(menuItemReset);
        
        JMenuItem menuItemQuit = new JMenuItem("Quit");
        menuItemQuit.setActionCommand("quit");
        menuItemQuit.addActionListener(this);
        fileMenu.add(menuItemQuit);
   
        JMenu insertMenu = new JMenu("Insert");
        menuBar.add(insertMenu);
        
        JMenuItem menuItemRabbits = new JMenuItem("Rabbits");
        menuItemRabbits.setActionCommand("rabbits");
        menuItemRabbits.addActionListener(this);
        insertMenu.add(menuItemRabbits);
        
        JMenuItem menuItemFoxes = new JMenuItem("Foxes");
        menuItemFoxes.setActionCommand("foxes");
        menuItemFoxes.addActionListener(this);
        insertMenu.add(menuItemFoxes);      
        
        JMenuItem menuItemTraps = new JMenuItem("Traps");
        menuItemTraps.setActionCommand("traps");
        menuItemTraps.addActionListener(this);
        insertMenu.add(menuItemTraps);
        
        JMenuItem menuItemDisease = new JMenuItem("Disease");
        menuItemDisease.setActionCommand("disease");
        menuItemDisease.addActionListener(this);
        insertMenu.add(menuItemDisease);
        
        JMenu analysisMenu = new JMenu("Analysis");
        menuBar.add(analysisMenu);
        
        JMenuItem menuItemAnalyse = new JMenuItem("Analyse logs");
        menuItemAnalyse.setActionCommand("analyse");
        menuItemAnalyse.addActionListener(this);
        analysisMenu.add(menuItemAnalyse);        
        
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        JMenuItem menuItemAbout = new JMenuItem("About");
        menuItemAbout.setActionCommand("about");
        menuItemAbout.addActionListener(this);
        helpMenu.add(menuItemAbout);
        
        setJMenuBar(menuBar);
        
        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
       
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class anyClass, Color color)
    {
        colors.put(anyClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class<? extends Object> anyClass)
    {
        Color col = colors.get(anyClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field)
    {
        if(!isVisible()) {
            this.setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();
        
        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object object = field.getObjectAt(row, col);
                if(object != null) {
                    stats.incrementCount(object.getClass());
                    fieldView.drawMark(col, row, getColor(object.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
        
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }
    
	public static void main(String[] args) {
		Simulator sim = new Simulator(80,80);
	}

	public void actionPerformed(ActionEvent arg0){
		if(arg0.getActionCommand().equalsIgnoreCase("run")){
			this.sim.runLongSimulation();}
		else if(arg0.getActionCommand().equalsIgnoreCase("customrun")){
			String runs = (String)JOptionPane.showInputDialog("How many steps would you like to iterate through?");
			this.sim.simulate(Integer.parseInt(runs));}
		else if(arg0.getActionCommand().equalsIgnoreCase("reset")){
			this.sim.reset();}
		else if(arg0.getActionCommand().equalsIgnoreCase("quit")){
			if(!this.sim.logged){this.sim.logger.finish();}
			this.dispose();
			System.exit(0);}
		else if(arg0.getActionCommand().equalsIgnoreCase("about")){
			JOptionPane.showConfirmDialog(rootPane, "Foxes and Rabbits Extended V1 is a simulator for \nthe population trends of foxes and rabbits in the wild.\n\nWritten by Alastair Fraser Dewar, David J. Barnes and Michael Kolling.", "About", JOptionPane.DEFAULT_OPTION);}
		else if(arg0.getActionCommand().equalsIgnoreCase("analyse")) {
			JOptionPane.showConfirmDialog(rootPane, "There are currently "+Integer.toString(this.getLogCount())+" logs.", "Log Analysis", JOptionPane.DEFAULT_OPTION);}
		else if(arg0.getActionCommand().equalsIgnoreCase("pause")) {
			if(menuItemPause.getText().equalsIgnoreCase("Pause")){
			this.sim.pause();
			menuItemPause.setText("Resume");}
			else if(menuItemPause.getText().equalsIgnoreCase("Resume")){
			this.sim.resume();
			menuItemPause.setText("Pause");}
		}
	}
    
    public int getLogCount() {
    	int logCount = 0;
    	try {
    	      FileReader fr = new FileReader("Logs.dat");
    	      BufferedReader reader = new BufferedReader(fr);
    	      String line = reader.readLine();
    	      Scanner scan = null;
    	      int haltCount = 0;
    	      while (line != null) {
  	    	  	if(line.equalsIgnoreCase("[Halt]")){
    	    	  	haltCount = haltCount+1;}
  	    	  	logCount = haltCount;
    	      }
    	      reader.close();
    	  }
    	  catch (FileNotFoundException e) {
    	      System.out.println("Couldnae find the file");
    	  }
    	  catch (IOException e) {
    	      System.out.println("Wee problem reading from file");
    	  }

    	return logCount;
    }	
	
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
