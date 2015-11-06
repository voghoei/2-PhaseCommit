import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hotel_Communication_Substrate {
	static Socket hSoc;
	static ServerSocket hotelSoc;
	static ConcurrentLinkedQueue<String> qin;
	static ConcurrentLinkedQueue<String> qout;
	static int status;
	

	public static void main(String[] args) {
		try {

			qin = new ConcurrentLinkedQueue<String>();
			qout = new ConcurrentLinkedQueue<String>();
			hotelSoc = new ServerSocket(5218);
			System.out.println("Concert Socket on Port Number 5218");
			System.out.println("Waiting for Connection ...");
			hSoc = hotelSoc.accept();
			Hotel_Buffer_In in = new Hotel_Buffer_In(hSoc, qin,status);
			Hotel_Buffer_Out out = new Hotel_Buffer_Out(hSoc, qout,status);
			HotelOperation opt = new HotelOperation(qin, qout,status);
			
			

		} catch (IOException e) {
			System.out.println("exp: Hotel Communication_Substrate ");
			e.printStackTrace();
		} 		
	}	
}
