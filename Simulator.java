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
    // The log for this simulation
    public Logger logger;
    // If the simulation has been paused
    private boolean paused = false;
    // The remaining number of steps to be taken
    private int stepsToBeTaken;
    // Wether the simulation has beed logged
    public boolean logged = false;
    
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
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(this, depth, width);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Trap.class, Color.red);

        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Pause the simulation
     */
    public void pause() {
    	paused = true;
    	System.out.println("Simulation paused");
    }
    
    /**
     * Resume the simulation after being paused
     */
    public void resume() {
    	paused = false;
    	simulate(getRemainingSteps());
    	System.out.println("Simulation resuming");
    }
    
    /**
     * Return the remaining number of steps to be taken
     * @return int Remaining number of steps to go
     */
    public int getRemainingSteps() {
    	return stepsToBeTaken - step;
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
    	if(numSteps > 0)
    	{
	    	int step = 1;
	        while(step <= numSteps && view.isViable(field) && !paused) {
	            simulateOneStep();
	            step++;
	        }
	        if(!view.isViable(field))
	        {
	        	logged = true;
	        	logger.finish();
	        }
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
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            for(Iterator<Objects> it2 = objects.iterator(); it2.hasNext(); ) {
            	Objects object = it2.next();
            	if(object.getLocation().equals(animal.getLocation()))
            	{object.react(animal);}
            }
            // If the animal is dead, lets get rid of it
            if(! animal.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);
        view.showStatus(step, field);
        String logMessage = view.stats.getPopulationDetails(field);
        logger.addRecord(logMessage);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();
        logged = false;
        // Show the starting state in the view.
        view.showStatus(step, field);
        logger = new Logger(this);
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
    
    /**
     * Returns the current step
     * @return int the current step
     */
    public int getCurrentStep(){
    	return step;
    }
    
    /**
     * Returns the simulators field
     * @return field The field being used by the simulator
     */
    public Field getField() {
    	return field;
    }
    
    /**
     * Returns the SimulatorView being used by the simulator
     * @return the SimulatorView being used by the simulator
     */
    public SimulatorView getView() {
    	return view;
    }
    
    /**
     * Add multiple rabbits to the simulation
     * @param count The number of rabbits to added to the simulation
     */
    public void addRabbits(int count) {
    	if(count > 0 && count <= field.getLocationsLeft()){
    	for(int counter = 0; counter < count; counter++) {
    		addRabbit();
    	}}
    }
    
    /**
     * Add a rabbit to the simulation
     */
    public void addRabbit() {
        Location randomFreeLocation = field.getRandomFreeLocation();
        Rabbit rabbit = new Rabbit(true, field, randomFreeLocation);
        animals.add(rabbit);
        field.place(rabbit, randomFreeLocation);
        view.showStatus(step, field);
    }
    
    /**
     * Add multiple foxes to the simulation
     * @param count The number of foxes to added to the simulation
     */
    public void addFoxes(int count) {
    	if(count > 0 && count <= field.getLocationsLeft()){
	    for(int counter = 0; counter < count; counter++) {
	    	addFox();
	    }}
    }
    
    /**
     * Add a fox to the simulation
     */
    public void addFox() {
        Location randomFreeLocation = field.getRandomFreeLocation();
        Fox fox = new Fox(true, field, randomFreeLocation);
        animals.add(fox);
        field.place(fox, randomFreeLocation);
        view.showStatus(step, field);
    }
    
    /**
     * Add multiple traps to the simulation
     * @param count The number of trap to added to the simulation
     */
    public void addTraps(int count) {
    	if(count > 0 && count <= field.getLocationsLeft()){
    	for(int counter = 0; counter < count; counter++) {
    		addTrap();
    	}}
    }
    
    /**
     * Add a trap to the simulation
     */
    public void addTrap() {
        Location randomFreeLocation = field.getRandomFreeLocation();
        Trap trap = new Trap(field, randomFreeLocation);
        objects.add(trap);
        field.place(trap, randomFreeLocation);
        view.showStatus(step, field);
    }
}
