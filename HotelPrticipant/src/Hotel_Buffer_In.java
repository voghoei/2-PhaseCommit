import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hotel_Buffer_In extends Thread {
	Socket hsoc;
	DataInputStream din;
	ConcurrentLinkedQueue<String> qinlocal;
	int statusLocal;

	Hotel_Buffer_In(Socket hsoc, ConcurrentLinkedQueue<String> qin, int status) {
		try {
			this.statusLocal = status;
			this.hsoc = hsoc;
			this.qinlocal = qin;
			din = new DataInputStream(hsoc.getInputStream());
			System.out.println("Hotel Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Hotel Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String msg;
			System.out.println("Hotel Buffer In thread:  " + Thread.currentThread().getId());
			while (true) {
				if (statusLocal == 1) {
					msg = din.readUTF();
					System.out.println("while Hotel in, Normal mode : " + msg);
					qinlocal.add(msg);
				} else {
					msg = din.readUTF();
					System.out.println("while Hotel in, Discarde Message " + msg);
				}

			}

		} catch (Exception ex) {
			System.out.println("\tConnection Reset ");
			System.out.println("Waiting for Connection ...");
		}
	}

	protected void finalize() throws IOException {
		din.close();
		hsoc.close();
	}

}
