import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.*;
import java.util.Arrays;

public class Proxy {
	public static void main(String args[])
	{
		int server_port = 65500;
		if(args.length > 0)
			server_port = Integer.parseInt(args[0]);
		try {
			ServerSocket server = new ServerSocket(server_port);
			System.out.println(server.getLocalPort());
			while (true) {
				Socket connection = server.accept();
				System.out.println(connection.toString());
				try {
					Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
					char[] rq = new char[8192];
					int c = in.read(rq);
					connection.close();
					String request = String.valueOf(rq);
					String[] header = request.split("\n");
					String[] http_header = header[0].split(" ");
					String method = http_header[0];
					String url = http_header[1];
					String footer = http_header[2];
					if(method.equals("GET"))
					{
						URL u = new URL(url);
						int port = u.getPort() == -1 ? 80 : u.getPort();
						Socket dest = new Socket(u.getHost(), port);
						Writer out = new OutputStreamWriter(dest.getOutputStream());
						out.write(method + " " + url + " " + footer);
						for(int i = 1; i<header.length; i++)
							out.write(header[i]);
						out.write("\r\n");
						out.flush();
						out.close();
						dest.close();
					}
					else
					{
						System.out.println("DOESN'T Handle request.");
						System.out.println(request);
					}
					Writer out = new OutputStreamWriter(connection.getOutputStream());
					out.write("You've connected to this server. Bye-bye now.\r\n");
					out.flush();
					//connection.close( );
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