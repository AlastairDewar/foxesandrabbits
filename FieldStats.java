import java.util.HashMap;
import java.util.Set;

/**
 * This class collects and provides some statistical data on the state 
 * of a field. It is flexible: it will create and maintain a counter 
 * for any class of object that is found within the field.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2008.03.30
 */
public class FieldStats
{
    // Counters for each type of entity (fox, rabbit, etc.) in the simulation.s
	public HashMap<Class, Counter> counters;
    // Whether the counters are currently up to date.
    public boolean countsValid;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats()
    {
        // Set up a collection for counters for each type of animal that
        // we might find
        counters = new HashMap<Class, Counter>();
        countsValid = true;
    }

    /**
     * Get details of what is in the field.
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Field field)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
    /**
     * Invalidate the current set of statistics; reset all 
     * counts to zero.
     */
    public void reset()
    {
        countsValid = false;
        for(Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of animal.
     * @param anyClass The class of animal to increment.
     */
    public void incrementCount(Class anyClass)
    {
        Counter count = counters.get(anyClass);
        if(count == null) {
            // We do not have a counter for this object/animal yet.
            // Create one.
            count = new Counter(anyClass.getName());
            counters.put(anyClass, count);
        }
        count.increment();
    }

    /**
     * Indicate that an animal count has been completed.
     */
    public void countFinished()
    {
        countsValid = true;
        
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @param field The field were checking
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        // How many counts are non-zero.
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            if(info.getCount() > 0 && key.getSuperclass().getName().equals("Animal")) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }
    
    /**
     * Generate counts of the number of animals/objects.
     * These are not kept up to date as foxes, rabbits and traps
     * are placed in the field, but only when a request
     * is made for the information.
     * @param field The field to generate the stats for.
     */
    private void generateCounts(Field field)
    {
        reset();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object object = field.getObjectAt(row, col);
                if(object != null) {
                    incrementCount(object.getClass());
                }
            }
        }
        countsValid = true;
    }
    
    /**
     * Return all keys which belong to the animal class
     * @return Set<Class> all keys which are animals
     */
    private Set<Class> getAnimalKeys(){
    	Set<Class> animalKeys = null;
    	/*Set<Class> animalKeys = counters.keySet();
    	for(Class key : animalKeys) {
    		if(!key.getSuperclass().getName().equals("Animal"))
    		{
    			animalKeys.remove(key);
    			//System.out.println(key.getSuperclass().getName());
    		}
    	}*/
    	return animalKeys;
    }
}
