import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;


public class Hotel_Buffer_Out extends Thread {
	Socket hsoc;
	DataOutputStream dout;
	Queue<String> qoutlocal;
	
	Hotel_Buffer_Out(Socket hsoc,Queue<String> qout) {
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
			System.out.println("Hotel Buffer Out thread :  "+ Thread.currentThread().getId());
			while(true){
				if (!qoutlocal.isEmpty()){
					dout.writeUTF(qoutlocal.poll());
					System.out.println("is not empty .....");
					Thread.sleep(1000);
				}
				//else
					//System.out.println("Concert Buffer Out thread: qout empty ");
			}

		} catch (Exception ex) {
			// Logger.getLogger(Transferfile.class.getName()).log(Level.SEVERE,
			// null, ex);
			System.out.println("exp: Hotel Buffer Out thread ");
			
		}
	}

}
