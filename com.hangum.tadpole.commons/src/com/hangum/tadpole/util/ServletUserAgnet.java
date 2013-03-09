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
package com.hangum.tadpole.util;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * 		servlet.getRequest("User-Agent")를 정보를 가지고 사용자 브라우저의 종류와 os정보를 사용합니다.
 * </pre>
 * 
 * @author hangum
 *
 */
public class ServletUserAgnet {
	/** tadpole가 지원하는 시스템만 */
	public enum OS_SIMPLE_TYPE {
        MACOSX,
        WINDOWS,
        LINUX,
        UNKNOWN
    }    
	/** 알아 낼수 있는 전체 os */
    public enum OS_TYPE {
        MACOSX,
        WIN95,
        WIN98,
        WINNT,
        WIN2K,
        WINXP,
        WINVISTA,
        WIN7,
        WIN8,
        LINUX,
        IOS,
        ANDROID,
        JAVA_ME,
        UNKNOWN
    }
    /**알아낼수있는 전체 브라우저 */
    public enum BROWSER_TYPE {
        IE,
        FIREFOX,
        CHROME,
        OPERA,
        SAFARI,
        UNKNOWN
    }

//    private  OS_TYPE 		OS_Type 		= OS_TYPE.UNKNOWN;
    private  OS_SIMPLE_TYPE OS_Simple_Type 	= OS_SIMPLE_TYPE.UNKNOWN;
	private  BROWSER_TYPE 	Browser_Type 	= BROWSER_TYPE.UNKNOWN;
    private  int 			majorVersion	= 0;
    private  String 		fullVersion		= null;
    
    /**
     * 시스템의 정보를 가져온다.
     * 
     * @param req
     */
    public void detect(HttpServletRequest req) {
    	String userAgent = req.getHeader("User-Agent");
    	
//    	OS_Type = detectedOS(userAgent);
    	OS_Simple_Type = detectedOSSimple(userAgent);
    	detectBrowser(userAgent);
    }
    
    private OS_SIMPLE_TYPE detectedOSSimple(String userAgent) {
    	if (null == userAgent) {
            return OS_SIMPLE_TYPE.UNKNOWN;
        } else if(userAgent.contains("Mac OS X")) {
            return OS_SIMPLE_TYPE.MACOSX;
        } else if (userAgent.contains("Windows")) {
            return OS_SIMPLE_TYPE.WINDOWS;
        } else if (userAgent.contains("Linux")) {
            return OS_SIMPLE_TYPE.LINUX;
        }
        return OS_SIMPLE_TYPE.UNKNOWN;
    }

//    private OS_TYPE detectedOS(String userAgent) {
//        if (null == userAgent) {
//            return OS_TYPE.UNKNOWN;
//        } else if(userAgent.contains("Android")) {
//            return OS_TYPE.ANDROID;
//        } else if(userAgent.contains("J2ME")) {
//            return OS_TYPE.JAVA_ME;
//        } else if(userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("iPad")) {
//            return OS_TYPE.IOS;
//        } else if(userAgent.contains("Mac OS X")) {
//            return OS_TYPE.MACOSX;
//        } else if (userAgent.contains("Windows NT 5.0")) {
//            return OS_TYPE.WIN2K;
//        } else if (userAgent.contains("Windows NT 5.1") || userAgent.contains("Windows NT 5.2") || userAgent.contains("Windows XP")) {
//            return OS_TYPE.WINXP;
//        } else if (userAgent.contains("Windows NT 6.0")) {
//            return OS_TYPE.WINVISTA;
//        } else if (userAgent.contains("Windows NT 6.1")) {
//            return OS_TYPE.WIN7;
//        } else if (userAgent.contains("Windows NT 6.2")) {
//            return OS_TYPE.WIN8;
//        } else if (userAgent.contains("Windows NT")) {
//            return OS_TYPE.WINNT;
//        } else if (userAgent.contains("Linux")) {
//            return OS_TYPE.LINUX;
//        }
//        return OS_TYPE.UNKNOWN;
//    }

    public void detectBrowser(String userAgentStr) {
        if (null != userAgentStr) {
            
        	try {
                if(userAgentStr.contains("Chrome/")) {
                    Browser_Type = BROWSER_TYPE.CHROME;
                    fullVersion = userAgentStr.substring(userAgentStr.indexOf("Chrome/")+7);
                    fullVersion = fullVersion.substring(0, fullVersion.indexOf(" ")).trim();
                    majorVersion = Integer.parseInt(fullVersion.substring(0, fullVersion.indexOf(".")));
                } else if (userAgentStr.contains("Safari/")) {
                    Browser_Type = BROWSER_TYPE.SAFARI;
                    fullVersion = userAgentStr.substring(userAgentStr.indexOf("Version/")+8);
                    fullVersion = fullVersion.substring(0, (fullVersion.indexOf(" ") > 0 ? fullVersion.indexOf(" ") : fullVersion.length())).trim();
                    majorVersion = Integer.parseInt(fullVersion.substring(0, fullVersion.indexOf(".")));
                } else if (userAgentStr.contains("Opera ")) {
                    Browser_Type = BROWSER_TYPE.OPERA;
                    fullVersion = userAgentStr.substring(userAgentStr.indexOf("Opera ")+6);
                    fullVersion = fullVersion.substring(0, (fullVersion.indexOf(" ") > 0 ? fullVersion.indexOf(" ") : fullVersion.length())).trim();
                    majorVersion = Integer.parseInt(fullVersion.substring(0, fullVersion.indexOf(".")));
                } else if (userAgentStr.contains("Firefox/")) {
                    Browser_Type = BROWSER_TYPE.FIREFOX;
                    fullVersion = userAgentStr.substring(userAgentStr.indexOf("Firefox/")+8);
                    fullVersion = fullVersion.substring(0, (fullVersion.indexOf(" ") > 0 ? fullVersion.indexOf(" ") : fullVersion.length())).trim();
                    majorVersion = Integer.parseInt(fullVersion.substring(0, fullVersion.indexOf(".")));
                }  else if (userAgentStr.contains("MSIE ")) {
                    Browser_Type = BROWSER_TYPE.IE;
                    fullVersion = userAgentStr.substring(userAgentStr.indexOf("MSIE ")+5);
                    fullVersion = fullVersion.substring(0, fullVersion.indexOf(";")).trim();
                    majorVersion = Integer.parseInt(fullVersion.substring(0, fullVersion.indexOf(".")));
                }
            } catch (NumberFormatException nfe) {
                fullVersion = null;
                majorVersion = 0;
            }
        }
    }

//    public OS_TYPE getOsType() {
//        return OS_Type;
//    }
    
    public OS_SIMPLE_TYPE getOSSimpleType() {
    	return OS_Simple_Type;
    }

    public BROWSER_TYPE getBrowserType() {
        return Browser_Type;
    }
    
    public String getFullVersion() {
        return fullVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

}

