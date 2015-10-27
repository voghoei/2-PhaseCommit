import java.io.IOException;
import java.net.Socket;



public class Hotel_Communication_Substrate extends Thread {
	static Socket hotelSoc; 
	
	public static void main(String[] args) throws IOException {
		hotelSoc = new Socket("localhost", 5218);
		Hotel_Buffer_In in = new Hotel_Buffer_In(hotelSoc);
		Hotel_Buffer_Out out = new Hotel_Buffer_Out(hotelSoc);
	}

}
