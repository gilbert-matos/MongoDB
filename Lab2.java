import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

public class Lab2 {
	public static void main(String[] args) throws IOException {
		//speciesDB();
		DBquery();
	}

	@SuppressWarnings("resource")
	public static void speciesDB() throws IOException {

		String bootstrapUrl = "tcp://dbclass.cs.unca.edu:6666";
		StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));

		StoreClient<String, List<Map<String, String>>> client = factory.getStoreClient("directory");

		// creating initial k-v pair
		System.out.println("Creating initial Key and Value");
		String key = "gmatos:voldemortkey1";
		int counter = 2;

		// import csv file
		File csvFile = new File("/Users/gill/eclipse-workspace/CSCI343/VoldemortProject/src/species.csv");
		BufferedReader br = new BufferedReader(new FileReader(csvFile));
		String s = "";
		List<Map<String, String>> value = new ArrayList<Map<String, String>>();
		Map<String, String> entry = null;
		while ((s = br.readLine()) != null) {
			// add to database
			entry = new HashMap<String, String>();
			entry.put("name", s);
			entry.put("key", "gmatos:voldemortkey" + counter++);
			value.add(entry);

		}
		// change last entry
		entry.replace("key", null);

		// put initial value
		System.out.println("Putting Initial value");
		client.put(key, value);

		// get the value
		System.out.println("Getting the value");
		Versioned<List<Map<String, String>>> versioned = client.get(key);
		System.out.println("Number of items: " + String.valueOf(versioned.getValue().size()));

		// recurse this throughout the database
		for (int i = 0; i < Integer.valueOf(versioned.getValue().size()); i++) {
			entry = versioned.getValue().get(i);
			System.out.println(
					"Entry's Animal: " + String.valueOf(entry.get("name")) );
			System.out.println("Entry points to voldemort key: " + String.valueOf(entry.get("key")));
		}
	}
	// hint: full tree, all the same size

	@SuppressWarnings("resource")
	public static void DBquery() {
		Scanner scan = new Scanner(System.in);
		String bootstrapUrl = "tcp://dbclass.cs.unca.edu:6666";
		StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));

		StoreClient<String, List<Map<String, String>>> client = factory.getStoreClient("directory");

		String key = "gmatos:voldemortkey1";
		Versioned<List<Map<String, String>>> versioned = client.get(key);
		int size = Integer.valueOf(versioned.getValue().size());
		
		System.out.println("Number of items in directory: " + size);
		// ask the user for the directory key or path to retrieve
		System.out.println("What directory are you looking for?");
		String input = scan.nextLine();
		Map<String, String> entry = new HashMap<String, String>();
		int answer = 0;
		
		// query directory
		for (int i = 0; i < Integer.valueOf(versioned.getValue().size()); i++) {
			entry = versioned.getValue().get(i);
			String entryKey = String.valueOf(entry.get("name"));
			
			// if entry equals animal print
			if (entryKey.contains(input)) {
				answer++;
				System.out.println("Animal FOUND:");
				System.out.println("Animal's name: " + String.valueOf(entry.get("name")) );
				System.out.println("Entry points to voldemort key: " + String.valueOf(entry.get("key")));
				System.out.println("");
			}
			
		}
		System.out.println(answer + " items found!");
	}
}
