import java.io.IOException;
import java.net.Socket;

public class Concert_Communication_Substrate {
	static Socket concertSoc;

	public static void main(String[] args) throws IOException {
		concertSoc = new Socket("localhost", 5217);
		Concert_Buffer_In in = new Concert_Buffer_In(concertSoc);
		Concert_Buffer_Out out = new Concert_Buffer_Out(concertSoc);

	}

}
