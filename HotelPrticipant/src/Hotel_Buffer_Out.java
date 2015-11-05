import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hotel_Buffer_Out extends Thread {
	Socket hsoc;
	DataOutputStream dout;
	ConcurrentLinkedQueue<String> qoutlocal;

	Hotel_Buffer_Out(Socket hsoc, ConcurrentLinkedQueue<String> qout) {
		try {
			this.qoutlocal = qout;
			this.hsoc = hsoc;
			dout = new DataOutputStream(hsoc.getOutputStream());
			System.out.println("Hotel Buffer Out Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Hotel Buffer Out Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void finalize() throws IOException {
		dout.close();
		hsoc.close();
	}

	public void run() {
		try {
			System.out.println("Hotel Buffer Out thread :  "
					+ Thread.currentThread().getId());
			while (true) {
				if (qoutlocal.size() > 0) {
					String msg = qoutlocal.poll();
					dout.writeUTF(msg);
					System.out.println("is not empty ....." + msg);
//					Thread.sleep(1000);
				}
				// else
				// System.out.println("Hotel Buffer Out thread: qout empty ");
			}

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
			System.out.println("exp: Hotel Buffer Out thread ");
		}
	}
}
