import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Analyser {

	private ArrayList<ArrayList<String>> logs;
	
	public Analyser() {
		logs = new ArrayList<ArrayList<String>>();
	}
	
	public void loadLogs() {
		try {
			File log = new File("Logs.dat");
			Scanner scan = new Scanner(log);
		   	   try {
		   		ArrayList<String> records = null;
		   		while(scan.hasNext())
		   		{
		   			String line = scan.next();
					if(line.equalsIgnoreCase("[start]"))
		   			{
						records = new ArrayList<String>();
		   			}
		   			else if (line.equalsIgnoreCase("[finish]"))
		   			{
		   				logs.add(records);
		   			}
		   			else
		   			{
		   				records.add(line);
		   			}
		   		}
		   	   }
		   	   catch(NullPointerException e){
		   		   System.out.println("Error writing logs \n"+e);
		   	   }
		}
		catch(IOException e) {
		    System.out.println("Error writing logs \n"+e);
		}
	}

	public void checkSuccessor() {
		String ultimate = null;
		for(int i=0;i<logs.size();i++)
		{
			ArrayList<String> records = logs.get(i);
			ultimate = records.get(records.size());
			System.out.println(ultimate);
		}
	}
	
	public int getLogCount() {
		return this.logs.size();
	}
	
	public int getWorthyLogCount() {
		int count = 0;
		for(int i=0;i<logs.size();i++)
		{
			ArrayList<String> records = logs.get(i);
			if(records.size() > 300){count++;}
		}
		return count;
	}
}
