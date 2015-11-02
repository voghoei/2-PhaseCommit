import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Concert_Buffer_In extends Thread {
	Socket csoc;
	DataInputStream din;
	String inputCommand ;	
	Queue<String> qinlocal;
	
	Concert_Buffer_In(Socket csoc,Queue<String> qin) {
		try {
			this.csoc = csoc;
			this.qinlocal = qin;
			din = new DataInputStream(csoc.getInputStream());
			System.out.println("Concert Buffer In Connected ...");
			start();
			

		} catch (IOException e) {
			System.out.println("Concert Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void finalize() throws IOException {
		din.close();
		csoc.close();

	}

	public void run() {
		try {

			System.out.println("Concert Buffer In thread:  "+ Thread.currentThread().getId());
			while(true){
				inputCommand = din.readUTF();				
	            System.out.println("&&&&&&&&  read: "+inputCommand);
	            qinlocal.add(inputCommand);
	            Thread.sleep(1000);
	            
			}
			

		} catch (Exception ex) {
			System.out.println("\tConnection Reset ");
            System.out.println("Waiting for Connection ...");
		}
	}

}
