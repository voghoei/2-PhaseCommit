import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hotel_Buffer_Out extends Thread {
	Socket hsoc;
	DataOutputStream dout;
	ConcurrentLinkedQueue<String> qoutlocal;
	int statusLocal;

	Hotel_Buffer_Out(Socket hsoc, ConcurrentLinkedQueue<String> qout, int status) {
		try {
			this.statusLocal = status;
			this.hsoc = hsoc;
			this.qoutlocal = qout;
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
			String msg;
			System.out.println("Hotel Buffer Out thread :  " + Thread.currentThread().getId());
			while (true) {
				if (qoutlocal.size() > 0) {
					if (statusLocal == 1) {
						msg = qoutlocal.poll();
						dout.writeUTF(msg);
						System.out.println("Hotel Buffer Out, message" + msg);
					} else {
						qoutlocal.clear();
						System.out.println("Hotel Buffer Out, qout clean ");
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("exp: Hotel Buffer Out thread ");
		}
	}
}
