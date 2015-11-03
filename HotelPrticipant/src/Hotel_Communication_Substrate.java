import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Hotel_Communication_Substrate {
	static Socket hSoc;
	static ServerSocket hotelSoc;
	static Queue<String> qin = new LinkedList<String>();
	static Queue<String> qout = new LinkedList<String>();
	

	public static void main(String[] args) {
		try {

			
			hotelSoc = new ServerSocket(5218);
			System.out.println("Hotel Socket on Port Number 5218");
			System.out.println("Waiting for Connection ...");
			hSoc = hotelSoc.accept();
			Hotel_Buffer_In in = new Hotel_Buffer_In(hSoc, qin);
			Hotel_Buffer_Out out = new Hotel_Buffer_Out(hSoc, qout);
			HotelOperation opt = new HotelOperation(qin, qout);
			
			

		} catch (IOException e) {
			System.out.println("exp: Hotel Communication_Substrate ");
			e.printStackTrace();
		} 		
	}	
}
