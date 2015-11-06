import java.net.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

import javafx.util.Pair;

public class Coordinator {
	// Sockes Initialization
	static ServerSocket concertSoc;
	static ServerSocket hotelSoc;
	static Socket hsoc;
	static Socket csoc;
	static String hAddress, cAddress, hPort, cPort;

	// Classes Initialization
	static Coordinator_Buffer_In_Hotel hotelIn;
	static Coordinator_Buffer_In_Concert concertIn;
	static Coordinator_Buffer_Out outs;
	static CoordinatorOperation operation;

	// Log file
	
	//
	static int status;
		

	// Input coordinate file
	static FileInputStream fstreamCoordinateConfigFile = null;
	static BufferedReader brCoordinatorConfig = null;
	
	//Buffers
	static ConcurrentLinkedQueue<String> hqin;
	static ConcurrentLinkedQueue<String> cqin;
	static ConcurrentLinkedQueue<String> qout;
	
	public static void main(String[] args) throws IOException {
		hqin = new ConcurrentLinkedQueue<String>();
		cqin = new ConcurrentLinkedQueue<String>();
		qout = new ConcurrentLinkedQueue<String>();
		
		socketOpening();		

		hotelIn = new Coordinator_Buffer_In_Hotel(hsoc,hqin,status);
		concertIn = new Coordinator_Buffer_In_Concert(csoc,cqin,status);
		outs = new Coordinator_Buffer_Out(hsoc, csoc,qout,status);	
		operation = new  CoordinatorOperation(cqin,hqin,qout, brCoordinatorConfig,status);
	}

	

	public static void socketOpening() {

		try {
			fstreamCoordinateConfigFile = new FileInputStream(
					"coordinator-configuration-file.txt");
			
			brCoordinatorConfig = new BufferedReader(new InputStreamReader(
					fstreamCoordinateConfigFile));

			String concertAdd = brCoordinatorConfig.readLine();
			cAddress = concertAdd.split(" ")[0];
			cPort = concertAdd.split(" ")[1];

			String hotelAddress = brCoordinatorConfig.readLine();
			hAddress = hotelAddress.split(" ")[0];
			hPort = hotelAddress.split(" ")[1];

			csoc = new Socket(cAddress, Integer.parseInt(cPort));
			System.out.println("Concert Socket on Port Number "+cPort);
			
			hsoc = new Socket(hAddress, Integer.parseInt(hPort));			
			System.out.println("Hotel Socket on Port Number "+hPort);			

		} catch (IOException e) {
			System.out.println("exp: Coordinator Communication_Substrate ");
			e.printStackTrace();
		}

	}

}
