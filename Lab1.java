import java.util.Scanner;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

public class Lab1 {

	public static void main(String[] args) {
		stringStore();
	}

	public static void stringStore() {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String key = "gmatos";
		
        String bootstrapUrl = "tcp://dbclass.cs.unca.edu:6666";
        StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));

        StoreClient<String, String> client = factory.getStoreClient("test");
        
        // add values
        System.out.println("How many more values would you like to add?");
        int total = scan.nextInt(); 
        System.out.println("Add values");
        //first iteration
        if(client.get(key) == null && total == 1) {
        	//putting in values
        	String vals = scan.next(); 
        	client.put(key,vals);
        }
        //other iterations
        else if(total >= 1) {
        	for(int i = 1; i <= total ;i++){
        	//putting in values
        	String vals = scan.next(); 
            client.put(key,client.get(key).getValue().concat(", " + vals));
        	}
        }
        
        //print values
        System.out.println("Final Versioned Object: " + String.valueOf(client.get(key)));
        System.out.println("ALL Values: [" + String.valueOf(client.get(key).getValue())+ "]");
      
        //delete key
        //client.delete(key);
        
    }

}
