import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Concert_Buffer_Out extends Thread {
	Socket csoc;
	DataOutputStream dout;
	ConcurrentLinkedQueue<String> qoutlocal;
	AtomicInteger statusLocal;

	Concert_Buffer_Out(Socket csoc, ConcurrentLinkedQueue<String> qout, AtomicInteger status) {
		try {
			this.statusLocal = status;
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
			String msg;
			System.out.println("Concert Buffer Out thread :  " + Thread.currentThread().getId());
			while (true) {				
				if (qoutlocal.size() > 0) {					
					if (statusLocal.get() == 1) {
						msg = qoutlocal.poll();
						dout.writeUTF(msg);
						System.out.println("Buffer Out, message: " + msg);
					} else {
						qoutlocal.clear();
						System.out.println("Buffer Out, qout clean. ");
					}
				}
				sleep(1000);
			}
		} catch (Exception ex) {
			System.out.println("exp: Concert Buffer Out thread ");
		}
	}
}
