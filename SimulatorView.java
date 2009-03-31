import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.LinkedHashMap;
import java.util.Map;

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
    
    private Simulator sim;
    
    // A map for storing colours for participants in the simulation
	private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    public FieldStats stats;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<Class, Color>();

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        setResizable(false);
        
      //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Run");
        menuBar.add(fileMenu);
        JMenuItem menuItem = new JMenuItem("Long simulation");
        fileMenu.add(menuItem);
        JMenuItem menuItem2 = new JMenuItem("Custom number of steps");
        fileMenu.add(menuItem2);
        fileMenu.addSeparator();
        JMenuItem menuItem7 = new JMenuItem("Reset");
        menuItem7.addActionListener(this);
        fileMenu.add(menuItem7);
        fileMenu.addSeparator();
        JMenuItem menuItem11 = new JMenuItem("Quit");
        menuItem11.addActionListener(this);
        fileMenu.add(menuItem11);
   
        JMenu insertMenu = new JMenu("Insert");
        menuBar.add(insertMenu);        
        JMenuItem menuItem4 = new JMenuItem("Rabbits");
        insertMenu.add(menuItem4);      
        JMenuItem menuItem5 = new JMenuItem("Foxes");
        insertMenu.add(menuItem5);      
        JMenuItem menuItem6 = new JMenuItem("Traps");
        insertMenu.add(menuItem6);      
        JMenuItem menuItem8 = new JMenuItem("Disease");
        insertMenu.add(menuItem8);      
        
        JMenu analysisMenu = new JMenu("Analysis");
        menuBar.add(analysisMenu);
        JMenuItem menuItem9 = new JMenuItem("Check number of logs");
        analysisMenu.add(menuItem9);
        JMenuItem menuItem10 = new JMenuItem("Analyse logs");
        analysisMenu.add(menuItem10);        
        
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        JMenuItem menuItem3 = new JMenuItem("About");
        helpMenu.add(menuItem3);
        
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
            setVisible(true);
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
	public static void main(String[] args) {
		Simulator main = new Simulator();
	}

	public void actionPerformed(ActionEvent arg0){
		if(arg0.getSource().toString().contains("Long simulation")){
			sim.runLongSimulation();}
		else if(arg0.getSource().toString().contains("Custom number of steps")){
			// Show dialog box
			JOptionPane.showMessageDialog(rootPane,
		    "How many steps do you wish to run through?");
			sim.simulate(0);}
		else if(arg0.getSource().toString().contains("Reset")){
			this.dispose();
			Simulator sim = new Simulator();}
		else if(arg0.getSource().toString().contains("Quit")){
			this.dispose();
			System.exit(0);}
	}
}
