import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Concert_Buffer_In extends Thread {
	Socket csoc;
	DataInputStream din;
	ConcurrentLinkedQueue<String> qinlocal;
	int statusLocal;

	Concert_Buffer_In(Socket csoc, ConcurrentLinkedQueue<String> qin, int status) {
		try {
			this.statusLocal = status;
			this.csoc = csoc;
			this.qinlocal = qin;
			din = new DataInputStream(csoc.getInputStream());
			System.out.println("Concert Buffer In Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Concert Buffer In Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String msg;
			System.out.println("Concert Buffer In thread:  " + Thread.currentThread().getId());
			while (true) {
				if (statusLocal == 1) {
					msg = din.readUTF();
					System.out.println("while concert in, Normal mode : " + msg);
					qinlocal.add(msg);
				} else {
					msg = din.readUTF();
					System.out.println("while concert in, Discarde Message " + msg);
				}

			}

		} catch (Exception ex) {
			System.out.println("\tConnection Reset ");
			System.out.println("Waiting for Connection ...");
		}
	}

	protected void finalize() throws IOException {
		din.close();
		csoc.close();
	}

}
