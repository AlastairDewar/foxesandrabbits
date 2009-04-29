import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kolling
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // Whether the animal is diseased.
    private boolean diseased;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // The animal's gender
    private char gender;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param newField The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field newField, Location location)
    {
        alive = true;
        diseased = false;
        field = newField;
        setLocation(location);
        Randomizer random = new Randomizer();
        if(random.getRandom().nextBoolean() == true)
        	{gender = 'M';}else{gender = 'F';}
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to add newly born animals to.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Check whether the animal is diseased or not.
     * @return true if the animal is diseased.
     */
    public boolean isDiseased()
    {
    	return diseased;
    }
    
    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    public void setDiseased()
    {
        diseased = true;
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    public Field getField()
    {
        return field;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
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
     * Return the animals gender
     * @return The animals gender
     */
    public char getGender() {
    	return gender;
    }
    
    /**
     * Return whether or not the animal is diseased
     * @return whether or not the animal is diseased
     */
    public boolean diseased() {
    	return diseased;
    }
    
    /**
     * Set the whether or not the animal is diseased
     * @param newDisease Whether or not the animal is diseased
     */
    public void setDiseased(boolean newDisease) {
    	diseased = newDisease;
    }
}
