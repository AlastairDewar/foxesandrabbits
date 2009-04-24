import java.util.ArrayList;
import java.util.Random;

public class Pond extends Objects {

	private final int MAX_FIELD_PERCENT = 20;
	private ArrayList<Location> locations;
	
	public Pond(Field field) {
		super(field, getArea());
		// TODO Auto-generated constructor stub
	}

	private ArrayList<Location> getArea() {
		 Random rand = Randomizer.getRandom();
		 ArrayList<Location> locations = new ArrayList<Location>();
		 int startingWidth = 0;
		 int startingHeight = 0;
		 boolean foundStartingPoint = false;
		 while(!foundStartingPoint) {
			 startingWidth = rand.nextInt(this.getField().getWidth());
			 startingHeight = rand.nextInt(this.getField().getDepth());
			 if(this.getField().getObjectAt(startingWidth, startingHeight) == null)
			 {
				 foundStartingPoint = true;
			 }
		 }
		 Location startLocation = new Location(startingWidth, startingHeight);
		 int totalLocations = this.getField().getDepth() * this.getField().getWidth();
		 totalLocations = totalLocations * (this.MAX_FIELD_PERCENT / 100);
		 int maxLocations = rand.nextInt(totalLocations);
		 while(locations.size() <= maxLocations)
		 {
			 Location current = this.getField().randomAdjacentLocation(startLocation);
			 locations.add(current);
		 }
		 return locations;
	}
	
	@Override
	public void react(Animal animal) {
		// TODO Auto-generated method stub
		// Do nothing
	}

}
