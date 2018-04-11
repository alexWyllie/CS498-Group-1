// Author: Alex Wyllie
// Reads from filepath.

package io.jenkins.plugins.sample;

import java.io.*;
import java.util.ArrayList;

public class ReadFromFile {
	
	static String readAllLinesFromFileAsString(String filePath) { //Read the entire file, returns text as single string
		String line = "";
		String output = "";
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader buf = new BufferedReader(file);
			while ((line = buf.readLine()) != null) {
				output += line + "\n";
			}
			buf.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Cannot find file " + filePath + "\n");
		}
		catch (IOException e) {
			System.out.println("Error reading file " + filePath + "\n");
		}
		return output;
	}
	
	static String[] readAllLinesFromFile(String filePath) { //Reads the entire file, returns an array containing each line
		String line = "";
		ArrayList<String> output = new ArrayList<String>();
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader buf = new BufferedReader(file);
			while ((line = buf.readLine()) != null) {
				output.add(line);
			}
			buf.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Cannot find file " + filePath + "\n");
		}
		catch (IOException e) {
			System.out.println("Error reading file " + filePath + "\n");
		}
		String[] outputArray = new String[output.size()];
		outputArray = output.toArray(outputArray);
		return outputArray;
	}
	
	static String readLineFromFile(String filePath, int lineNum) { //Reads a single line from file
		String line = "";
		int i = 0;
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader buf = new BufferedReader(file);
			while ((line = buf.readLine()) != null) {
				i++;
				if (i == lineNum) {
					break;
				}
			}
			buf.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Cannot find file " + filePath + "\n");
		}
		catch (IOException e) {
			System.out.println("Error reading file " + filePath + "\n");
		}
		return line;
	}
	
	static String[] readLinesFromFile(String filePath, int lineNum, int numLinesToRead) { //Reads numLinesToRead lines starting at lineNum, returns an array containing lines
		String line = "";
		ArrayList<String> output = new ArrayList<String>();
		int i = 0;
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader buf = new BufferedReader(file);
			while ((line = buf.readLine()) != null && i < lineNum) {
				i++;
			}
			i = 0;
			while ((line = buf.readLine()) != null && i < numLinesToRead) {
				output.add(line);
				i++;
			}
			buf.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Cannot find file " + filePath + "\n");
		}
		catch (IOException e) {
			System.out.println("Error reading file " + filePath + "\n");
		}
		String[] outputArray = new String[output.size()];
		outputArray = output.toArray(outputArray);
		return outputArray;
	}
	
	static String readLinesFromFileAsString(String filePath, int lineNum, int numLinesToRead) { //Reads numLinesToRead lines starting at lineNum, returns a single string
		String output = "";
		String line = "";
		int i = 0;
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader buf = new BufferedReader(file);
			while ((line = buf.readLine()) != null && i < lineNum) {
				i++;
			}
			i = 0;
			while ((line = buf.readLine()) != null && i < numLinesToRead) {
				output += line + "\n";
				i++;
			}
			buf.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Cannot find file " + filePath + "\n");
		}
		catch (IOException e) {
			System.out.println("Error reading file " + filePath + "\n");
		}
		return output;
	}
}

