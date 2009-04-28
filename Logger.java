import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;

public class Logger {

	private ArrayList<String> logs;
	private Simulator simulator;
	
	public Logger (Simulator sim) {
		this.simulator = sim;
		logs = new ArrayList<String>();
		write("[start]");
		//write("[size="+sim.getField().getDepth()+","+sim.getField().getWidth()+"]");
		addRecord(simulator.getView().stats.getPopulationDetails(simulator.getField()));
	}
	
	public void addRecord(String record) {
		String[] results = record.split(": ");
		String newRecord = null, foxRecord = null, rabbitRecord = null, trapRecord = null;
        for(int i=0; i<=3; i++){
            if(results[i].contains("Fox"))
            {
            	String count = results[i+1];
            	String[] count2 = count.split(" ");
            	count = count2[0];
            	foxRecord = "fox:"+Integer.parseInt(count);
            }
            if(results[i].contains("Rabbit"))
            {
            	String count = results[i+1];
            	String[] count2 = count.split(" ");
            	count = count2[0];
            	rabbitRecord = "rabbit:"+Integer.parseInt(count);
            }
            if(results[i].contains("Trap"))
            {
            	String count = results[i+1];
            	String[] count2 = count.split(" ");
            	count = count2[0];
            	trapRecord = "trap:"+Integer.parseInt(count);
            }
        }
        newRecord = foxRecord+"+"+rabbitRecord+"+"+trapRecord;
		write(newRecord);
	}
	
	private void write(String data) {
		logs.add(data);
	}
	
	public void finish() {
		write("[finish]");
		try {
			FileWriter log = new FileWriter("Logs.dat", true);
		   	   try {
		   		Iterator<String> it = logs.iterator();
		   		while(it.hasNext()){
			    log.write(it.next()+"\n");}
			    log.close();
		   	   }
		   	   catch(NullPointerException e){
		   		   System.out.println("Error writing logs \n"+e);
		   	   }
		}
		catch(IOException e) {
		    System.out.println("Error writing logs \n"+e);
		}
	}
	
}
