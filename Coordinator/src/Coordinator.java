import java.net.*;
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

	public static void main(String[] args) throws IOException {
		socketOpening();		

		hotelIn = new Coordinator_Buffer_In_Hotel(hsoc);
		concertIn = new Coordinator_Buffer_In_Concert(csoc);
		outs = new Coordinator_Buffer_Out(hsoc, csoc);
		
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

				outs.hdout.writeUTF(reservation);
				outs.cdout.writeUTF(reservation);
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
			cAddress = hotelAddress.split(" ")[0];
			cPort = hotelAddress.split(" ")[1];

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
