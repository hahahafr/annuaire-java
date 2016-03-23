package gestetu05;

import java.io.BufferedReader;
import java.io.IOException;


public class Reception implements Runnable {

	private BufferedReader in;
	private String message = null, login = "utilisateur";
	
	public Reception(BufferedReader in,String log){
		
		this.in = in;
                login=log;
	}
	
	public void run() {
		
	        try {

	    		while(true){
			message = in.readLine();
			System.out.println(message);
	    		}
			
		    } catch (IOException e) {
				
				e.printStackTrace();
			}
	}

}
