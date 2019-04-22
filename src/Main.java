import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main{
	private static int hashSpace;
	private static int numNodes;
	private static int numKeys;
	private static int[] hashedNodes;
	private static int[] hashedKeys;
	
	public static void main(String[] args){
		//readValuesFromConsole();	//read input from console instead
		
		File input = chooseFile();
		try {
			//Initialize input values
			String line;
			String hold[];
			FileReader reader = new FileReader(input);
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			line = bufferedReader.readLine();
			hashSpace = Integer.parseInt(line);
			
			line = bufferedReader.readLine();
			numNodes = Integer.parseInt(line);
			
			line = bufferedReader.readLine();
			numKeys = Integer.parseInt(line);
			
			line = bufferedReader.readLine();
			hold = line.split(",");
			hashedNodes = new int[hold.length];
			for(int i = 0; i < hold.length; i++) {
				hashedNodes[i] = Integer.parseInt(hold[i]);
			}
			
			line = bufferedReader.readLine();
			hold = line.split(",");
			hashedKeys = new int[hold.length];
			for(int i = 0; i < hold.length; i++) {
				hashedKeys[i] = Integer.parseInt(hold[i]);
			}
			bufferedReader.close();	//close the reader
			
			//Output
			String fileName = "EHA35output.txt";
			FileWriter writer = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			//Finger Tables
			for(int i = 0; i < numKeys; i++) {
				int currentKey = hashedKeys[i];
				int hashedKey = currentKey % (hashSpace + 1);	//Double check that the key is indeed hashed
				
				int keyLocation = findKeyLocation(hashedKey);
				//printFingerTable(keyLocation);	//outputs the results to console instead
				outputFingerTable(keyLocation, bufferedWriter);
			}
			bufferedWriter.close();	//close the writer

		}
		catch (IOException | NullPointerException e) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			String errorMessage = "An error in file selection occurred.";
			JOptionPane.showMessageDialog(frame, errorMessage, "File Selection Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private static File chooseFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Choose the input file");
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		else {
			return null;
		}
	}
	
	
	//Asks users to write input using the console (used for testing)
	private static void readValuesFromConsole() {
		Scanner in = new Scanner(System.in);
		String hold[];
		
		//Reading the input
		System.out.print("Hash Space Size: ");
		hashSpace = Integer.parseInt(in.nextLine());
		
		System.out.print("Number of Nodes: ");
		numNodes = Integer.parseInt(in.nextLine());
		
		System.out.print("Number of Keys: ");
		numKeys = Integer.parseInt(in.nextLine());
		
		System.out.print("Hashed Nodes: ");
		hold = in.nextLine().split(",");
		hashedNodes = new int[hold.length];
		for(int i = 0; i < hold.length; i++) {
			hashedNodes[i] = Integer.parseInt(hold[i]);
		}

		System.out.print("Hashed Keys: ");
		hold = in.nextLine().split(",");
		hashedKeys = new int[hold.length];
		for(int i = 0; i < hold.length; i++) {
			hashedKeys[i] = Integer.parseInt(hold[i]);
		}
		
		in.close();	//close the scanner
	}
	
	private static int findKeyLocation(int key) {
		int node = key;	//allows for testing with print line below
		while(true) {
			if (arrayContainsVal(node, hashedNodes)) {
				//System.out.println("Key " + key + " is matched to node: " + node);
				return node;
			}
			node++;
			if (node > hashSpace) {
				node = 0;
			}
			//System.out.println("crash??");
		}
	}
	
	//Helper function for findKeyLocation()
	private static boolean arrayContainsVal(int val, int[] array) {
		for(int i = 0; i < array.length; i++) {
			if(val == array[i]) {
				return true;
			}
		}
		return false;
	}
	
	//Outputs output in console (used for testing)
	private static void printFingerTable(int node) {
		System.out.println(node);
		
		int numFingers = logBase2(hashSpace + 1);
		for(int i = 0; i < numFingers; i++) {
			int nextPossibleSucc = (node + (int)Math.pow(2, i)) % (hashSpace + 1);
			int succ = findKeyLocation(nextPossibleSucc);
			System.out.println(i + " " + nextPossibleSucc + " "  + succ);
		}
	}
	
	private static void outputFingerTable(int node, BufferedWriter bufferedWriter) {
		try {
			String nodeString = String.valueOf(node);	//there's a bug when writing the node as an int
			bufferedWriter.write(nodeString);
			bufferedWriter.newLine();
			int numFingers = logBase2(hashSpace + 1);
			for(int i = 0; i < numFingers; i++) {
				int nextPossibleSucc = (node + (int)Math.pow(2, i)) % (hashSpace + 1);
				int succ = findKeyLocation(nextPossibleSucc);
				bufferedWriter.write(i + " " + nextPossibleSucc + " " + succ);
				bufferedWriter.newLine();
			}
		}
		catch (IOException e) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			String errorMessage = "An error occurred when writing to file.";
			JOptionPane.showMessageDialog(frame, errorMessage, "Output Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Helper function for printFingerTable() and outputFingerTable()
	private static int logBase2(int number) {
		double logNumber = Math.log((double) number);
		double logBase = Math.log(2);
		
		int retVal = (int) (logNumber/logBase);
		//System.out.println("logBase2 of " + number + " is " + retVal);
		return retVal;
	}
}
