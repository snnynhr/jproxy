import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {
	public static void main(String args[])
	{
		int port = 65500;
		if(args.length > 0)
			port = Integer.parseInt(args[0]);
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println(server.getLocalPort());
			while (true) {
				Socket connection = server.accept();
				System.out.println(connection.toString());
				try {
					Reader in = new InputStreamReader(
							new BufferedInputStream(
									connection.getInputStream( )
									)
							);
					StringBuffer request = new StringBuffer(4096);
					int c = in.read();
					while (c >=0) {
						//System.out.print((char)(c));
						if (c == '\r' || c == '\n' || c == -1)
						{
							System.out.println(request.toString());
							request = new StringBuffer(4096);
						}
						else
						{
							request.append((char) c);
						}
						c = in.read();
					}

					Writer out = new OutputStreamWriter(connection.getOutputStream());
					out.write("You've connected to this server. Bye-bye now.\r\n");
					out.flush();
					connection.close( );
				}
				catch (IOException ex) {

				}
				finally 
				{
					try 
					{
						if (connection != null) connection.close( );
					}
					catch (IOException ex) {}
				}
			}
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}
}