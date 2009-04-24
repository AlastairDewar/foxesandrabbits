import java.util.ArrayList;

/**
 * A class representing shared characteristics of objects.
 * 
 * @author Alastair Fraser Dewar
 * @version 2009.03.27
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
    
    /**
     * Create a new object at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Objects(Field field, Location location, boolean visibility)
    {
        this.triggered = false;
        this.visibleToAnimals = visibility;
        this.field = field;
        setLocation(location);
    }
    
    public Objects(Field field, ArrayList<Location> locations)
    {
    	this.visibleToAnimals = true;
    	this.field = field;
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
    
    public void trigger()
    {
    	this.triggered = true;
    }
}
