import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Coordinator_Communication_Substrate {

	public static void main(String[] args) throws IOException {

		ServerSocket concertSoc = new ServerSocket(5217);
		System.out.println("Concert Socket on Port Number 5217");
		ServerSocket hotelSoc = new ServerSocket(5218);
		System.out.println("Hotel Socket on Port Number 5218");
		System.out.println("Waiting for Connection ...");
		Socket hsoc;
		Socket csoc;

		while (true) {
			if ((hsoc = hotelSoc.accept()) != null
					&& (csoc = concertSoc.accept()) != null) {
				Coordinator_Buffer_In_Hotel hotelIn = new Coordinator_Buffer_In_Hotel(
						hsoc);
				Coordinator_Buffer_In_Concert concertIn = new Coordinator_Buffer_In_Concert(
						csoc);
				Coordinator_Buffer_Out outs = new Coordinator_Buffer_Out(hsoc,
						csoc);
			}

		}

	}

}
