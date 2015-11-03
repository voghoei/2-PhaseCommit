import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

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

	// Log file

	// Input coordinate file
	static FileInputStream fstreamCoordinateConfigFile = null;
	static FileInputStream fstreamCoordinateFile = null;
	static BufferedReader brCoordinator = null;
	static BufferedReader brCoordinatorConfig = null;
	
	//Buffers
	static Queue<String> hqin = new LinkedList<String>();
	static Queue<String> cqin = new LinkedList<String>();
	static Queue<String> qout = new LinkedList<String>();

	public static void main(String[] args) throws IOException {
		
		socketOpening();		

		hotelIn = new Coordinator_Buffer_In_Hotel(hsoc,hqin);
		concertIn = new Coordinator_Buffer_In_Concert(csoc,cqin);
		outs = new Coordinator_Buffer_Out(hsoc, csoc,qout);
		
		booking();
	}

	public static void booking() {
		String reservation;
		try {
			fstreamCoordinateFile = new FileInputStream(
					brCoordinatorConfig.readLine());
			brCoordinator = new BufferedReader(new InputStreamReader(
					fstreamCoordinateFile));
			
			while ((reservation = brCoordinator.readLine()) != null) {
				qout.add(reservation);
//				outs.hdout.writeUTF(reservation);
//				outs.cdout.writeUTF(reservation);
				String CommandConcert = concertIn.cdin.readUTF();
				System.out.println("Command "+ CommandConcert);
				
     			String CommandHotel = hotelIn.hdin.readUTF();
				System.out.println("Command "+ CommandHotel);
				
//				if (CommandHotel.split(" ")[1].compareTo(reservation.split(" ")[0]) == 0){
//					
//				}
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
