//package com.hangum.tadpole.application.start.sessions;
//
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
//
///**
// * Tadpole Session Listener
// * 
// * @author hangum
// * @deprecated
// */
//public class TadpoleSessionListener implements HttpSessionListener {
//	public TadpoleSessionListener() {
//		super();
//	}
//
//	@Override
//	public void sessionCreated(HttpSessionEvent se) {
//		System.out.println("===================== SESSION CREATED ===========================");
//		System.out.println("===== session id is " + se.getSession().getId() );
//	}
//
//	@Override
//	public void sessionDestroyed(HttpSessionEvent se) {
//		System.out.println("===================== SESSION destoryed ===========================");
//		System.out.println("===== session id is " + se.getSession().getId() );
//	}
//
//}
