///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.commons.start;
//
//import org.apache.log4j.Logger;
//
//import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
//import com.hangum.tadpole.commons.libs.core.utils.PingTest;
//
///**
// * standalone 모드로 시스템이 시작할 때 디폴트 브라우저를 오픈하여 주도록 합니다. 
// * 
// * @author hangum
// *
// */
//public class TadpoleOpenBrowser  implements Runnable {
//	private static final Logger logger = Logger.getLogger(TadpoleOpenBrowser.class);
//	
//	@Override
//	public void run() {
//		
//		String s1 = System.getProperty("os.name").toLowerCase();
//		Runtime runtime = Runtime.getRuntime();
//		try {
//			// TODO 시스템 속도가 느려서 워크벤치가 정상적으로 동작하지 못했을 경우에 대비하여 기다립니다.
//			try { Thread.sleep(1000); } catch(Exception e) {};
//			int stats = PingTest.ping("127.0.0.1", Integer.parseInt(PublicTadpoleDefine.TADPOLE_DEFAULT_PORT), 5000);
//			if(stats != PingTest.SUCCESS) {
//				try { Thread.sleep(3000); } catch(Exception e) {};
//			}
//			
//			if(logger.isDebugEnabled()) logger.debug("start open browser");
//				
//			// window
//			if (s1.indexOf("windows") >= 0) {
//				runtime.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", PublicTadpoleDefine.getTadpoleUrl() });
//			
//			// mac
//			} else if (s1.indexOf("mac") >= 0) {
//				Runtime.getRuntime().exec(new String[] { "open", PublicTadpoleDefine.getTadpoleUrl() });
//			
//			// linux
//			} else {
//				
//				// linux
//				String as[] = { "python -m webbrowser", "firefox", "mozilla-firefox", "mozilla", "konqueror", "netscape", "opera" };
//				boolean isSuccess = false;
//				
//				int i = 0;
//				do {
//					
//					if (i >= as.length)
//						break;
//					try {
//						runtime.exec( ( as[i] + " " + PublicTadpoleDefine.getTadpoleUrl() ).split(" ") );
//						isSuccess = true;
//						break;
//					} catch (Exception exception) {
//						i++;
//					}
//					
//				} while (true);
//				
//				if (!isSuccess) logger.info("standlone open url exception ");
//				
//			}
//		} catch (Exception e) {
//			logger.error("standalone start open URL", e);
//		}
//		
//		if(logger.isDebugEnabled()) logger.debug("end open browser");
//	} // end run
//	
//}	//  end class TadpoleOpenBrowser 
