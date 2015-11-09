import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Concert_Communication_Substrate {
	static Socket cSoc;
	static ServerSocket concertSoc;
	static ConcurrentLinkedQueue<String> qin;
	static ConcurrentLinkedQueue<String> qout;
	static BufferedReader bufferRead;
	static String interuptMessage;
	
	static Concert_Buffer_In in;
	static Concert_Buffer_Out out;
	static ConcertOperation opt;
	
	static AtomicInteger status;


	public static void main(String[] args) throws InterruptedException {
		try {
			
			status = new AtomicInteger(1);
			status.set(1);
			System.out.println("status atomic :"+ status.get());
			qin = new ConcurrentLinkedQueue<String>();
			qout = new ConcurrentLinkedQueue<String>();
			concertSoc = new ServerSocket(5217);
			System.out.println("Concert Socket on Port Number 5217");
			System.out.println("Waiting for Connection ...");
			cSoc = concertSoc.accept();
			in = new Concert_Buffer_In(cSoc, qin, status);
			out = new Concert_Buffer_Out(cSoc, qout, status);
			opt = new ConcertOperation(qin, qout, status);
			
//			//test 
//			status.set(0);
//			Thread.sleep(20);
//			opt.interrupt();
//			//end Test
//			
			
			InteruptHandeling();
			
			}catch (IOException e) {
			System.out.println("exp: Concert Communication_Substrate ");
			e.printStackTrace();
		}
	}
	
	public static void InteruptHandeling() throws IOException, InterruptedException{
		while (true) {
			System.out.println("status = "+ status);
			System.out.println("Write the F/R for fail or recovery ....");
			bufferRead = new BufferedReader(new InputStreamReader(System.in));
			interuptMessage = bufferRead.readLine();
			if (interuptMessage.equalsIgnoreCase("F")) {
				if (status.equals(1)) {
					status.set(0);
					opt.interrupt();
					System.out.println("status = "+ status);
				}
				}
			if (interuptMessage.equalsIgnoreCase("R")) {
				if (status.get()==0) {
					status.set(2);
					opt.interrupt();						
				}					
			}
		}
		
	}
}
