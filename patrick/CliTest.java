package patrick;

import common.messages.*;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class CliTest {
    public static final int PORT = 44000;
    public static void main (String [] args){
	try {
	    System.out.println("CliTest.java");
	    // create TCP socket channel
	    SocketChannel channel = SocketChannel.open();
	    System.out.println("CliTest.java: channel created");
	    channel.connect(new InetSocketAddress("localhost",PORT));
	    System.out.println("CliTest.java: channel socket connected");
	    System.out.println();
	    
	    // create  & send a login message
	    byte [] bytes = LoginMessage.getLoginMessage("user1","password1");
	    System.out.println("CliTest.java: login message created");
	    System.out.println(LoginMessage.getMessageString(bytes));
	    System.out.printf("CliTest.java: login message length in bytes: %d\n", bytes.length);
	    ByteBuffer buf = ByteBuffer.allocate(4096);
	    buf.put(bytes);
	    buf.flip();
	    //System.out.printf("CliTest.java: buf.remaining before channel.write(): %d\n", buf.remaining()); 
	    int numwritten = channel.write(buf);
	    System.out.printf("CliTest.java: login mesage written: number of bytes written: %d\n", numwritten);
	    
	    // read reply message
	    buf.clear();
	    int numread = channel.read(buf);
	    bytes = new byte[numread];
	    buf.flip();
	    buf.get(bytes);
		if (LoginMessage.isLoginSuccessMessage(bytes)){
	        System.out.printf("CliTest.java: first message read: Success Message: number of bytes read: %d\n", numread);
		    	// get remote port number from success message
	        int port = LoginMessage.getPort(bytes);
	        System.out.printf("Port Number: %d\n", port);
	        byte playerid = LoginMessage.getPlayerId(bytes);
	        System.out.printf("Player id: %d\n", playerid);
	        String mcastString = LoginMessage.getMulticastAddress(bytes);
	        System.out.printf("Multicast Address: %s\n", mcastString);
	        // create datagram channel & connect to rem port
	        DatagramChannel dchannel = DatagramChannel.open();
	        dchannel.socket().bind(new InetSocketAddress(0));
            dchannel.socket().connect(new InetSocketAddress(channel.socket().getInetAddress(),port));
	        // get localport of datagram socket
	        int localport = dchannel.socket().getLocalPort();
	        System.out.printf("UDP local port: %d\n", localport);
	        // send success message to send port number to server
	        bytes = LoginMessage.getLoginSuccessMessage(playerid,null,localport);
	        buf.clear();
	        buf.put(bytes);
	        buf.flip();
	        channel.write(buf);
	        
	        DeathMessage dm = new DeathMessage((byte) 1, (byte) 1);
	        bytes = dm.getByteMessage();
	        buf.clear();
	        buf.put(bytes);
	        buf.flip();
	        channel.write(buf);	        
	 	}
	    else{
	        System.out.printf("CliTest.java: first message read: NOT Success Message: number of bytes read: %d\n", numread);
	    }
	    channel.close();
	}
	catch (Exception e){
	    e.printStackTrace();
	}
    }
}
