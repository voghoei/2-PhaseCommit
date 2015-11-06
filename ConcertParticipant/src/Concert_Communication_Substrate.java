import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Concert_Communication_Substrate {
	static Socket cSoc;
	static ServerSocket concertSoc;
	static ConcurrentLinkedQueue<String> qin;
	static ConcurrentLinkedQueue<String> qout;
	static int status;
	

	public static void main(String[] args) {
		try {

			qin = new ConcurrentLinkedQueue<String>();
			qout = new ConcurrentLinkedQueue<String>();
			concertSoc = new ServerSocket(5217);
			System.out.println("Concert Socket on Port Number 5217");
			System.out.println("Waiting for Connection ...");
			cSoc = concertSoc.accept();
			Concert_Buffer_In in = new Concert_Buffer_In(cSoc, qin,status);
			Concert_Buffer_Out out = new Concert_Buffer_Out(cSoc, qout,status);
			ConcertOperation opt = new ConcertOperation(qin, qout,status);
			Thread.ge
			
			

		} catch (IOException e) {
			System.out.println("exp: Concert Communication_Substrate ");
			e.printStackTrace();
		} 		
	}	
}
