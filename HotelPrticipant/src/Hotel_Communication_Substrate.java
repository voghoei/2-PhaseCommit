import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Hotel_Communication_Substrate extends Thread {
	static Socket hSoc;
	static ServerSocket hotelSoc;

	public static void main(String[] args) {

		try {
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

}
