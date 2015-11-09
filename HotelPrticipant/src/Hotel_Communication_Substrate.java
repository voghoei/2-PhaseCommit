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
	static AtomicInteger status;

	public static void main(String[] args) throws InterruptedException {
		try {
			status = new AtomicInteger(1);
			qin = new ConcurrentLinkedQueue<String>();
			qout = new ConcurrentLinkedQueue<String>();
			hotelSoc = new ServerSocket(5218);
			System.out.println("Concert Socket on Port Number 5218");
			System.out.println("Waiting for Connection ...");
			hSoc = hotelSoc.accept();
			Hotel_Buffer_In in = new Hotel_Buffer_In(hSoc, qin, status);
			Hotel_Buffer_Out out = new Hotel_Buffer_Out(hSoc, qout, status);
			HotelOperation opt = new HotelOperation(qin, qout, status);

			while (true) {
				System.out.println("Write the F/R for fail or recovery ....");
				bufferRead = new BufferedReader(new InputStreamReader(System.in));
				interuptMessage = bufferRead.readLine();

				if (interuptMessage.equalsIgnoreCase("F")) {

					if (status.get() == 1) {
						status.set(0);
						Thread.sleep(200);
						opt.interrupt();
					}
				}
				if (interuptMessage.equalsIgnoreCase("R")) {
					if (status.get() == 0) {
						status.set(2);
						Thread.sleep(200);
						opt.interrupt();
					}
					Thread.sleep(200);
				}
			}

		} catch (IOException e) {
			System.out.println("exp: Hotel Communication_Substrate ");
			e.printStackTrace();
		}
	}
}
