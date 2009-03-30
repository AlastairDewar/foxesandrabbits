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
    public Objects(Field field, Location location)
    {
        triggered = false;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this object act - that is: make it do
     * whatever it wants/needs to do.
     * @param newObjects A list to add newly created objects to.
     */
    abstract public void act(Animal animal);

    /**
     * Check whether the object is triggered or not.
     * @return true if the object has been triggered.
     */
    public boolean isTriggered()
    {
        return triggered;
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
