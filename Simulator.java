import java.io.*;
import java.util.*;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits, foxes and traps.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 50;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 50;
    // The probability that a trap will be created in any given grid position.
    private static final double TRAP_CREATION_PROBABILITY = 0.008;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;    

    // List of animals in the field.
    private List<Animal> animals;
    // List of objects in the field.
    private List<Objects> objects;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    private List<String> logs;
    
    private boolean paused = false;
    private int stepsToBeTaken;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<Animal>();
        objects = new ArrayList<Objects>();
        logs = new ArrayList<String>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(this, depth, width);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Trap.class, Color.red);
        this.writeToLogs("[Init]");
        // Setup a valid starting point.
        reset();
    }
    
    public void pause() {
    	this.paused = true;
    	System.out.println("Simulation paused");
    }
    
    public void resume() {
    	this.paused = false;
    	this.simulate(this.getRemainingSteps());
    	System.out.println("Simulation resuming");
    }
    
    public int getRemainingSteps() {
    	return this.stepsToBeTaken - this.step;
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * e.g. 500 steps.
     */
    public void runLongSimulation()
    {
        simulate(500);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps; step++) {
            simulateOneStep();
        }
        if(!view.isViable(field))
        {
        	this.writeToLogs("[Halt]");
        	this.writeLog(this.logs);
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<Animal>();        
        // Let all rabbits act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            for(Iterator<Objects> it2 = objects.iterator(); it2.hasNext(); ) {
            	Objects object = it2.next();
            	if(object.getLocation().equals(animal.getLocation()))
            	{object.react(animal);}
            }
            if(! animal.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);
        this.view.showStatus(step, field);
        String logMessage = view.stats.getPopulationDetails(field);
        this.writeToLogs(logMessage);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes, rabbits and traps.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= TRAP_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Trap trap = new Trap(field, location);
                    objects.add(trap);
                    field.place(trap, location);
                }
                else if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    animals.add(fox);
                    field.place(fox, location);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    animals.add(rabbit);
                    field.place(rabbit, location);
                }
                // else leave the location empty.
            }
        }
    }
    
    private void writeToLogs(String message) {
    	logs.add(message);
    }
    
    private void writeLog(List<String> logs) {
		try {
			FileWriter log = new FileWriter("Logs.dat", true);
		   	   try {
		   		Iterator<String> it = logs.iterator();
		   		while(it.hasNext()){
			    log.write(it.next()+"\n");}
			    log.close();
		   	   }
		   	   catch(NullPointerException e){
		   		   System.out.println("Error writing logs \n"+e);
		   	   }
		}
		catch(IOException e) {
		    System.out.println("Error writing logs \n"+e);
		}
    }
    
    public int getCurrentStep(){
    	return this.step;
    }
}
