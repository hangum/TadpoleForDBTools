/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.google.analytics;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;

import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;

/**
 * google analytics caller
 * 
 * UA-53250504-1

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-53250504-1', 'auto');
  ga('send', 'pageview');
</script>

 * 
 * @author hangum
 
 */
public class AnalyticCaller {

	/**
	 * google analytic
	 * 
	 * @param event
	 */
	public static void track(String event) {
		track("action", event);
	}
	
	/**
	 * google analytic
	 * 
	 * @param action
	 * @param event
	 */
	public static void track(String action, String event) {
		if(!ApplicationArgumentUtils.isGAOFF()) {
			JavaScriptExecutor executor = RWT.getClient().getService(JavaScriptExecutor.class);
	
			String strJs = "ga('send', 'pageview', {'" + action + "':'" + event + "'});";
			
			executor.execute(strJs);
		}
	}
}
