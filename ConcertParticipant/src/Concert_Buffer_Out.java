import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Concert_Buffer_Out extends Thread {
	Socket csoc;
	DataOutputStream dout;
	ConcurrentLinkedQueue<String> qoutlocal;

	 Concert_Buffer_Out(Socket csoc, ConcurrentLinkedQueue<String> qout) {
		try {
			this.csoc = csoc;
			this.qoutlocal = qout;
			dout = new DataOutputStream(csoc.getOutputStream());
			System.out.println("Concert Buffer Out Connected ...");
			start();

		} catch (IOException e) {
			System.out.println("Concert Buffer Out Connection faild ...");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void finalize() throws IOException {
		dout.close();
		csoc.close();
	}

	public void run() {
		try {
			System.out.println("Concert Buffer Out thread :  "
					+ Thread.currentThread().getId());
			while (true) {
				if (qoutlocal.size() > 0) {
					//System.out.println("is not empty .....");
					String msg = qoutlocal.poll();
					dout.writeUTF(msg);
					System.out.println("is not empty ....." + msg);
//					Thread.sleep(1000);
				}
//				 else
//				 System.out.println("Concert Buffer Out thread: qout empty ");
			}

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
			System.out.println("exp: Concert Buffer Out thread ");
		}
	}
}
