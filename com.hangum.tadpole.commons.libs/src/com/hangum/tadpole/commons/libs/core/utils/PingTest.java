/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.utils;

import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * IP, PORT에 ping test 합니다.
 * 
 * @author hangum
 *
 */
public class PingTest implements Runnable {
	private static final Logger logger = Logger.getLogger(PingTest.class);
	
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
			logger.error("pint test faile", ie);
		}
		
		return ping.status;
	}
}
