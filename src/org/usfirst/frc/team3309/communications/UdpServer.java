package org.usfirst.frc.team3309.communications;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class UdpServer extends Thread {

	
	HashMap<String,String> pairs = new HashMap<String,String>();
	int port = 2225;
	MulticastSocket socket;
	InetAddress group;
	
	public UdpServer()
	{
		try {
			socket = new MulticastSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Could not create socket...");
		} 
		
		try {
			group = InetAddress.getByName("10.41.41.255");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not find broadcast address...");
		}
		//
		//start();
	}
	
	public void put(String key, String value)
	{
		//System.out.println("Sent packet- " + key + " - " + value);
		try {
			byte[] buf = new String(key+"~"+value).trim().getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("error sending data");
			e.printStackTrace();
		}
	}
	
	public void put(String key, int value)
	{
		put(key, String.valueOf(value));
	}

	public String get(String key)
	
	{
		return pairs.get(key);
	}
	
	public void run()
	{
		try
		{
			socket.joinGroup(group);
			while ( true )
			{
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
			    socket.receive(packet);
			    
			    String data = new String(packet.getData());
			    String[] keyvalue = data.split("~");
			    if ( keyvalue.length == 2 )
			    	pairs.put(keyvalue[0], keyvalue[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server error!");
		}
	}
	}