package com.hangum.db.util;

import java.net.InetAddress;
import java.net.Socket;

/**
 * IP, PORT에 ping test 합니다.
 * 
 * @author hangum
 *
 */
public class PingTest implements Runnable {

	public static final int HOST_NOT_FOUND = 0;
	private static final int LOOKING_FOR_HOST = HOST_NOT_FOUND;
	public static final int CANNOT_CONNECT = 1;
	private static final int CONNECTING_TO_HOST = CANNOT_CONNECT;
	public static final int SUCCESS = 2;

	private String hostname;
	private int port;
	private int status;

	private PingTest(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	public void run() {
		try {
			status = LOOKING_FOR_HOST;
			InetAddress addr = InetAddress.getByName(hostname);
			status = CONNECTING_TO_HOST;
			Socket s = new Socket(addr, port);
			status = SUCCESS;
			s.close();
		} catch (Exception e) {
		}
	}

	public static int ping(String hostname, int port, long maxWait) {
		PingTest ping = new PingTest(hostname, port);
		try {
			Thread t = new Thread(ping);
			t.setDaemon(true);
			t.start();
			t.join(maxWait);
		} catch (InterruptedException ie) {
		}
		return ping.status;
	}
}
