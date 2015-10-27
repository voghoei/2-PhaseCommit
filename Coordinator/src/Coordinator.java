import java.net.*;
import java.io.*;

public class Coordinator {
	static ServerSocket concertSoc;
	static ServerSocket hotelSoc;
	static Socket hsoc;
	static Socket csoc;
	static Coordinator_Buffer_In_Hotel hotelIn;
	static Coordinator_Buffer_In_Concert concertIn;
	static Coordinator_Buffer_Out outs;

	public static void main(String[] args) throws IOException {
		Communication_Substrate();
	}

	public static void Communication_Substrate() throws IOException {
		concertSoc = new ServerSocket(5217);
		System.out.println("Concert Socket on Port Number 5217");
		hotelSoc = new ServerSocket(5218);
		System.out.println("Hotel Socket on Port Number 5218");
		System.out.println("Waiting for Connection ...");

		while (true) {
			if ((hsoc = hotelSoc.accept()) != null
					&& (csoc = concertSoc.accept()) != null) {
				hotelIn = new Coordinator_Buffer_In_Hotel(hsoc);
				concertIn = new Coordinator_Buffer_In_Concert(csoc);
				outs = new Coordinator_Buffer_Out(hsoc, csoc);
			}
		}
	}

}
