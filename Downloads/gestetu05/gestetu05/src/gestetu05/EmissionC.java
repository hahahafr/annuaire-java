package gestetu05;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class EmissionC implements Runnable {

	private PrintWriter out;
	private String message = null;
	private Scanner sc = null;
        String login;
	
	public EmissionC(PrintWriter out, String log) {
		this.out = out;
                 login=log;
	}

	
	public void run() {
		
		  sc = new Scanner(System.in);
		  
		  while(true){
			    System.out.println("Votre message :");
				message = sc.nextLine();
				out.println(login+":" + message);
			    out.flush();
			  }
	}
}
