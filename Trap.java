/**
 * A simple model of a trap.
 * Traps eat rabbits and foxes.
 * 
 * @author Alastair F Dewar
 * @version 2009.03.27
 */
public class Trap extends Objects
{
    // Characteristics shared by all traps (static fields).
    
	/* None */
    
    // Individual characteristics (instance fields).
	
    // Whether or not the trap is snared.
    private boolean snared;

    /**
     * Create a trap.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Trap(Field field, Location location)
    {
        super(field, location, false);
    }
    
    /**
     * This is what the trap does most of the time: it just 
     * waits for itself to be snared.
     * @param newTraps A list to add newly added traps to.
     */
    public void react(Animal animal)
    {
        if(isTriggered()) {
            this.snared = true;
            animal.setDead();
            this.destroy();
        }
    }
    
    /**
     * This is what the trap does most of the time: it just 
     * waits for itself to be snared.
     * @return Whether or not the trap has been snared.
     */
    public boolean isSnared()
    {
    	return snared;
    }

}
