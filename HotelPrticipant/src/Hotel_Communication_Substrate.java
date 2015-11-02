import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Hotel_Communication_Substrate extends Thread {
	static Socket hSoc;
	static ServerSocket hotelSoc;
	static int[] roomAvailable;

	public static void main(String[] args) {

		try {
			hotelConfig();
			hotelSoc = new ServerSocket(5218);
			System.out.println("Hotel Socket on Port Number 5218");
			System.out.println("Waiting for Connection ...");
			while (true) {
				hSoc = hotelSoc.accept();

				// hotelSoc = new Socket("localhost", 5218);
				Hotel_Buffer_In in = new Hotel_Buffer_In(hSoc);
				Hotel_Buffer_Out out = new Hotel_Buffer_Out(hSoc);
			}

		} catch (IOException e) {
			System.out.println("exp: Hotel Communication_Substrate ");
			e.printStackTrace();
		}

	}
	
	public static void hotelConfig() {
		try {			
			BufferedReader brConcert = new BufferedReader(new InputStreamReader(new FileInputStream("hotel-configuration-file.txt")));
			roomAvailable = new int[10];
			brConcert.readLine();
			for (int i=0 ; i<10; i++){
				roomAvailable[i]= Integer.parseInt(brConcert.readLine().split(" ")[1]);
			}
						
		} catch (IOException e) {
			System.out.println("exp: Hotel :hotelConfig ");
			e.printStackTrace();
		}

	}

}
