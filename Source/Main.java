import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Job implements Comparable<Object> {
	int id;
	int startTime;
	int finishTime;
	
	Job(int id, int start, int finish) {
		this.id = id;
		this.startTime = start;
		this.finishTime = finish;
	}

	@Override
	public int compareTo(Object arg0) {
       int compareage = ((Job)arg0).finishTime;
        /* For Ascending order*/
        return this.finishTime - compareage;
	}
}

public class Main {

	public static void main(String[] args) {
		
		// get args
		if(args.length == 2) {
			System.out.println("File path: " + args[1]);
			System.out.println("Processing file...");
			
			String filePath = args[1];
			
			// read file
			List<Job> jobs = readFile(filePath);			

			if(jobs != null && jobs.size() > 0) {
				System.out.println("Reading complete. \nScheduling began...");
				
				// runtime analysis start
				double startTime = System.nanoTime(); 
				
				// sort by finish time
				Collections.sort(jobs);

				// schedule
				List<Job> selected = schedule(jobs);
				
				// out
				writeListToFile(selected);
				
				// runtime analysis end
				double estimatedTime = (System.nanoTime() - startTime) / 1000000;
				
				// program completed
				System.out.println("Algorithm took " + estimatedTime + " milliseconds to complete.");
				System.out.println("Results are located at 'output.txt' in working directory.");
				
			} else {
				System.err.println("Dataset error: Need at least 1 job to schedule.");
			}
		} else {
			System.err.println("Please specify arguments like following: schedule.jar -i \"/path/to/file/filename.txt\"");
		}
	}

	static List<Job> schedule(List<Job> jobs) {
		
		List<Job> selected = new ArrayList<Job>();
		
		for(int i = 0; i < jobs.size(); i++) {
			if(i == 0) { selected.add(jobs.get(i)); }
			else {
				if(selected.get(selected.size() - 1).finishTime <= jobs.get(i).startTime) {
					selected.add(jobs.get(i));
				}
			}
		}
		
		return selected;
	}

	static void printJobs(List<Job> jobs) {
		System.out.println("ID\tStart\tFinish");
		for (Job job : jobs) {
			System.out.println(job.id + "\t" + job.startTime + "\t" + job.finishTime);
		}
	}
	
	static void writeListToFile(List<Job> output) {
		
		File file = new File("output.txt");
		
		// create file if not exists
		if(!file.exists()) {
			try {
				file.createNewFile();
			
			} catch (IOException e) {
				System.err.println("Error writing file: " + e.getMessage());
			}
		}
		
		FileWriter writer;
		
		try {
			writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write("ID\tS\tF\n----------");
			bufferedWriter.newLine();
			
			for (Job job : output) {
				bufferedWriter.write(job.id + "\t" + job.startTime + "\t" + job.finishTime);
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
			
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
		}
	}
	
	static List<Job> readFile(String path) {
		
		List<Job> jobs = new ArrayList<Job>();
		
		String line = "";
		
		try {
			// reader
			FileReader fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			// read lines one by one until the last line
			while((line = bufferedReader.readLine()) != null) {
				
				// split current line by tab character
				String row[] = line.split("\\t");
				
				if(row.length == 3) {
					Job tempJob = new Job(Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2]));
					jobs.add(tempJob);
				}
			}
			
			bufferedReader.close();
			
			return jobs;
			
		} catch(Exception e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
		
		return null;
	}

}
