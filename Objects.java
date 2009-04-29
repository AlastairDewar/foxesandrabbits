import java.util.ArrayList;

/**
 * A class representing shared characteristics of objects.
 * @author alastair
 */
public abstract class Objects
{
    // Whether the object has been triggered or not.
    private boolean triggered;
    // Whether the object is visible to animals or not.
    private boolean visibleToAnimals;
    // The objects's field.
    private Field field;
    // The objects's position in the field.
    private Location location;
    private ArrayList<Location> locations;
    
    /**
     * Create a new object at location in field.
     * @param newField The field currently occupied.
     * @param location The location within the field.
     * @param animalVisibility Wether the object is visible to animals
     */
    public Objects(Field newField, Location location, boolean animalVisibility)
    {
        triggered = false;
        visibleToAnimals = animalVisibility;
        field = newField;
        locations = null;
        setLocation(location);
    }
 
    /**
     * Create a new object at location in field.
     * @param newField The field currently occupied.
     * @param locations An arraylist of the locations occcupied by the object
     * @param animalVisibility Wether the object is visible to animals
     */
    public Objects(Field newField, ArrayList<Location> locations, boolean animalVisibility)
    {
    	triggered = false;
    	field = newField;
    	visibleToAnimals = animalVisibility;
    	location = null;
    	setLocation(locations);
    }

	/**
     * Make this object react to an animal - that is: make it do
     * whatever it wants/needs to do.
     */
    abstract public void react(Animal animal);

    /**
     * Check whether the object is triggered or not.
     * @return true if the object has been triggered.
     */
    public boolean isTriggered()
    {
        return triggered;
    }
    
    /**
     * Check whether the object is visible to animals or not
     * @return whether the object is visible to animals or not.
     */
    public boolean isVisibleToAnimals()
    {
        return visibleToAnimals;
    }

    /**
     * Indicate that the object is to be destroyed.
     * It is removed from the field.
     */
    public void destroy()
    {
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the object's location.
     * @return The object's location.
     */
    public Location getLocation()
    {
        return location;
    }
   
    /**
     * Return the object's location.
     * @return The object's location.
     */
    public ArrayList<Location> getLocations()
    {
        return locations;
    }
    
    /**
     * Return the object's field.
     * @return The object's field.
     */
    public Field getField()
    {
        return field;
    }
    
    /**
     * Place the object at the new location in the given field.
     * @param newLocation The object's new location.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Place the object at the new locations in the given field.
     * @param newLocations The object's new locations.
     */
    public void setLocation(ArrayList<Location> newLocations)
    {
    	for(int counter = 0; counter < newLocations.size(); counter++)
    	{
    		Location location = newLocations.get(counter); 
	        if(location != null) {
	            field.clear(location);
	        }
        }
        locations = newLocations;
        field.place(this, newLocations);
    }
    
    /**
     * Trigger the object
     */
    public void trigger()
    {
    	triggered = true;
    }
}
