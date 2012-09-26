/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
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
	public static final int CANNOT_CONNECT = 1;
	public static final int SUCCESS = 2;

	private String hostname;
	private int port;
	private int status;

	/**
	 * 테스트 할 정보를 넣습니다.
	 * 
	 * @param hostname
	 * @param port
	 */
	private PingTest(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	/**
	 * run test
	 */
	public void run() {
		try {
			status = HOST_NOT_FOUND;
			InetAddress addr = InetAddress.getByName(hostname);
			
			status = CANNOT_CONNECT;
			Socket s = new Socket(addr, port);
			
			status = SUCCESS;
			s.close();
		} catch (Exception e) {
		}
		
	}

	/**
	 * hostname, port에 maxWait초 만틈 ping테스트를 시도합니다.
	 * 
	 * @param hostname
	 * @param port
	 * @param maxWait
	 * @return
	 */
	public static int ping(String hostname, int port, long maxWait) {
		
		PingTest ping = new PingTest(hostname, port);
		
		try {
			Thread t = new Thread(ping);
			t.setDaemon(true);
			t.start();
			t.join(maxWait);
			
		} catch (InterruptedException ie) {
			//ignor exception
		}
		
		return ping.status;
	}
}
