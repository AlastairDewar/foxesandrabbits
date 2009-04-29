import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The depth and width of the field.
    private int depth, width;
    // Storage for the animals and objects.
    private Object[][] field;

    /**
     * Represent a field of the given dimensions.
     * @param newDepth The depth of the field.
     * @param newWidth The width of the field.
     */
    public Field(int newDepth, int newWidth)
    {
        depth = newDepth;
        width = newWidth;
        field = new Object[depth][width];
    }
    
    /**
     * Empty the field.
     */
    public void clear()
    {
        for(int row = 0; row < depth; row++) {
            for(int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }
    
    /**
     * Clear the given location.
     * @param location The location to clear.
     */
    public void clear(Location location)
    {
        field[location.getRow()][location.getCol()] = null;
    }
    
    /**
     * Place an animal/object at the given location.
     * If there is already an animal/object at the location it will
     * be lost.
     * @param object The animal/object to be placed.
     * @param row Row coordinate of the location.
     * @param col Column coordinate of the location.
     */
    public void place(Object object, int row, int col)
    {
        place(object, new Location(row, col));
    }
    
    /**
     * Place an object across several locations
     * @param object The object your placing in the field
     * @param newLocations The locations the object is present on
     */
    public void place(Object object, ArrayList<Location> newLocations)
    {
    	for(int counter = 0; counter < newLocations.size(); counter++)
    	{
    		Location location = newLocations.get(counter); 
	        place(object, location);
        }
    }
    
    /**
     * Place an animal/object at the given location.index
     * If there is already an animal/object at the location it will
     * be lost.
     * @param object The animal/object to be placed.
     * @param location Where to place the animal/objects.
     */
    public void place(Object object, Location location)
    {
        field[location.getRow()][location.getCol()] = object;
    }
    
    /**
     * Return the animal/object at the given location, if any.
     * @param location Where in the field.
     * @return The animal/object at the given location, or null if there is none.
     */
    public Object getObjectAt(Location location)
    {
        return getObjectAt(location.getRow(), location.getCol());
    }
    
    /**
     * Return the animal/object at the given location, if any.
     * @param row The desired row.
     * @param col The desired column.
     * @return The animal/object at the given location, or null if there is none.
     */
    public Object getObjectAt(int row, int col)
    {
        return field[row][col];
    }
    
    /**
     * Generate a random location that is adjacent to the
     * given location, or is the same location.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location)
    {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }
    
    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<Location>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
        	// TODO Change to make it check if its visible, not its name
            if(getObjectAt(next) == null || getObjectAt(next).toString().contains("Trap")) {
                free.add(next);
            }
        }
        return free;
    }
    
    /**
     * Try to find a free location that is adjacent to the
     * given location. If there is none, return null.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location)
    {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if(free.size() > 0) {
            return free.get(rand.nextInt(free.size()));
        }
        else {
            return null;
        }
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location)
    {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<Location>();
        if(location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
    
    /**
     * Return a random free location in the field
     * @return Location a random free location
     */
    public Location getRandomFreeLocation() 
    {
    	Location newLocation = null;
    	if(getLocationsLeft() >= 1){
        	Location location = null;
        	Randomizer random = new Randomizer();
    	while(newLocation == null){
    	int startingPointDepth = random.getRandom().nextInt(depth);
    	int startingPointWidth = random.getRandom().nextInt(width);
    	location = new Location(startingPointDepth, startingPointWidth);
    	if(getObjectAt(location) == null){
    		newLocation = location;}
    	else if(freeAdjacentLocation(location) != null){
    		newLocation = freeAdjacentLocation(location);}
    	else{
    		newLocation = null;
    	}}}
    	return newLocation;
    }
    
    /**
     * Return how many free locations are left in the field
     * @return int The number of free locations left in the field
     */
    public int getLocationsLeft()
    {
    	int locationsLeft = 0;
        for(int row = 0; row < getDepth(); row++) {
            for(int col = 0; col < getWidth(); col++) {
            	if(getObjectAt(row, col) == null){locationsLeft++;}
            }
        }
    	return locationsLeft;
    }
    
}
