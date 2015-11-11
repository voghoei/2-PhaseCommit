import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Hotel_Communication_Substrate {
	static Socket hSoc;
	static ServerSocket hotelSoc;
	static ConcurrentLinkedQueue<String> qin;
	static ConcurrentLinkedQueue<String> qout;
	static BufferedReader bufferRead;
	static String interuptMessage;

	static Hotel_Buffer_In in;
	static Hotel_Buffer_Out out;
	static HotelOperation opt;

	static AtomicInteger status;

	public static void main(String[] args) throws InterruptedException {
		try {

			status = new AtomicInteger(1);
			status.set(1);
			qin = new ConcurrentLinkedQueue<String>();
			qout = new ConcurrentLinkedQueue<String>();
			hotelSoc = new ServerSocket(5218);
			System.out.println("Hotel Socket on Port Number 5218");
			System.out.println("Waiting for Connection ...");
			hSoc = hotelSoc.accept();
			in = new Hotel_Buffer_In(hSoc, qin, status);
			out = new Hotel_Buffer_Out(hSoc, qout, status);
			opt = new HotelOperation(qin, qout, status);
			//
			// //test
			// status.set(0);
			// Thread.sleep(20);
			// opt.interrupt();
			// //end Test

			InteruptHandeling();

		} catch (IOException e) {
			System.out.println("exp: Hotel Communication_Substrate ");
			e.printStackTrace();
		}
	}

	public static void InteruptHandeling() throws IOException, InterruptedException {
		while (true) {
			bufferRead = new BufferedReader(new InputStreamReader(System.in));
			interuptMessage = bufferRead.readLine();
			if (interuptMessage.equalsIgnoreCase("F")) {
				if (status.get() == 1) {
					status.set(0);
					opt.interrupt();					
				}
			}
			if (interuptMessage.equalsIgnoreCase("R")) {
				if (status.get() == 0) {
					status.set(2);
					opt.interrupt();
				}
			}
		}

	}
}
