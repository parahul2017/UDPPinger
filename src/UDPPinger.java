import java.io.*;
import java.net.*;
import java.util.regex.*;

public class UDPPinger {

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Pattern pattern = Pattern.compile("www..*.com");
		Matcher matcher = pattern.matcher(""); 
		while(matcher.matches()==false) {
			System.out.println("Enter a valid hostname in this format: www.name.com"); // Hostname to be pinged is requested.
			String hostname = br.readLine();
			pattern= Pattern.compile("www..*.com", pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(hostname); //Pattern matching to validate the format of the hostname.
			if(matcher.matches()==false) {
				System.out.println("Incorrect hostname format. Please enter the hostname in www.abc.com format.");
			}
			else {
				try{
					int choice = 0;
					while(choice!=3) {
						System.out.println("Choose from the options: 1. Stop after specified time. 2. Stop after specified number of pings. 3. Exit"); // The user is given a choice to perform either of the two special options.
						choice = Integer.parseInt(br.readLine());
						switch(choice){
						case 1:
							System.out.println("Enter duration in seconds for which the program should ping the host.");
							int stoppage_time= Integer.parseInt(br.readLine());
							ping_host(hostname, stoppage_time, 0); // Stoppage time provided.
							break;
						case 2:
							System.out.println("Enter the number of pings after which the program should terminate.");
							int pings = Integer.parseInt(br.readLine());
							ping_host(hostname, 0, pings); // Limited number of pings.
							break;
						case 3:
							break;
						default:
							System.out.println("Incorrect input received. Please choose from the menu.");
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static void ping_host(String hostname, int stoppage_time, int pings) throws Exception{
		int port_number = 80;
		DatagramSocket ds = new DatagramSocket(); // UDP socket opened.
		InetAddress ip = InetAddress.getByName(hostname); //Hostname resolved to corresponding IP Address.
		if (stoppage_time!=0){
			long time_pre = System.currentTimeMillis(); //Track of time before beginning to send the datagrams.
			for (int i=0; System.currentTimeMillis() <= time_pre + stoppage_time*1000; i++){
				// for loop runs for the specified time duration.
				String data = "Ping" + i;
				DatagramPacket req = new DatagramPacket(data.getBytes(), data.length(), ip, port_number); // Request that carries the given message.
				long time_curr = System.currentTimeMillis(); //Track of time at the start of each ping.
				ds.send(req);
				DatagramPacket res = new DatagramPacket(new byte[1024], 1024); // Response received from the host.
				ds.setSoTimeout(1000);
				try {
					Data_output(req); //Output to be printed.
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try{
					ds.receive(res);
					
				}catch (IOException E){
					
				}
				long time_post = System.currentTimeMillis(); // Track of time after each ping is completed.
				System.out.println(" took " + (time_post - time_curr) + "ms.");
				//System.out.println();
				
				Thread.sleep(1000);
				//time_prev = time_curr;
			}
		}
		if (pings!=0){
			for (int i=0; i<pings; i++){
				// for loop runs for the specified number of pings only. Rest of the comments will remain the same as above.
				String data = "Ping" + i;
				DatagramPacket req = new DatagramPacket(data.getBytes(), data.length(), ip, port_number);
				long time_curr = System.currentTimeMillis();
				ds.send(req);
				DatagramPacket res = new DatagramPacket(new byte[1024], 1024);
				ds.setSoTimeout(1000);
				try {
					Data_output(req);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try{
					ds.receive(res);
					
				}catch (IOException E){
					
				}
				long time_post = System.currentTimeMillis();
				System.out.print(" took " + (time_post - time_curr) + "ms.");
				System.out.println();
				
				Thread.sleep(1000);
				//time_prev = time_curr;
			}
		}
		
	}
	
	private static void Data_output(DatagramPacket req) throws Exception{
		byte[] store = req.getData(); // Packet as received from the host.
		InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(store));
		BufferedReader br = new BufferedReader(in);
		String read_input = br.readLine();
		System.out.print("Response from " + req.getAddress().getHostAddress() + ": " + new String(read_input));
	}

}

