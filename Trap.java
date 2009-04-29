/**
 * A simple model of a trap.
 * Traps snare rabbits and foxes.
 * 
 * @author alastair
 */
public class Trap extends Objects
{
    // Whether or not the trap is snared.
    private boolean snared;

    /**
     * Create a trap.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Trap(Field field, Location location)
    {
        super(field, location, false);
        snared = false;
    }
    
    /**
     * This is what the trap does most of the time: it just 
     * waits for itself to be snared.
     * @param animal The poor animal to fall into the trap
     */
    public void react(Animal animal)
    {
        if(isTriggered()) {
            snared = true;
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
