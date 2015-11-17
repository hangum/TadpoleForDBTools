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
package com.hangum.tadpole.commons.util;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.tadpole.commons.libs.core.define.SystemDefine;

/**
 * application lock
 * 
 * referecne code : http://fresh2l.com/en/blog/2011/01/08/howto-run-only-single-java-application-instance
 * 
 * @author hangum
 *
 */
public class ApplicationLock {
	private static final Logger logger = Logger.getLogger(ApplicationLock.class);
	
	/** hidden constructor */
	private ApplicationLock() {
	}

	/** Lock file */
	File lock_file = null;

	FileLock lock = null;

	FileChannel lock_channel = null;

	FileOutputStream lock_stream = null;

	/**
	 * Creates class lock instance
	 * 
	 * @param key
	 *            Unique application key
	 */
	private ApplicationLock(String key) throws Exception {
		try {
			String tmp_dir = SystemDefine.getConfigureRoot() + "configuration/tadpole/temp/";
			File tmpDir = new File(tmp_dir);
			if(!tmpDir.isDirectory()) {
				tmpDir.mkdirs();
			}
			
			if(logger.isDebugEnabled()) {
				logger.debug("Application temp dir is " + tmp_dir);
			}
	
			// Acquire MD5
			try {
				java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
				md.reset();
				String hash_text = new java.math.BigInteger(1, md.digest(key.getBytes())).toString(16);
				
				while (hash_text.length() < 32) {
					hash_text = "0" + hash_text;
				}
				lock_file = new File(tmp_dir + hash_text + ".app_lock");
			} catch (Exception ex) {
				logger.error("lock_file", ex);
			}
	
			// MD5 acquire fail
			if (lock_file == null) {
				lock_file = new File(tmp_dir + key + ".app_lock");
			}
	
			lock_stream = new FileOutputStream(lock_file);
	
			String f_content = "Tadpole DB Hub Application \r\nLocked by key: " + key 	+ "\r\n";
			lock_stream.write(f_content.getBytes());
	
			lock_channel = lock_stream.getChannel();
	
			lock = lock_channel.tryLock();
	
			if (lock == null) {
				throw new Exception("Can't create Lock");
			}
		} catch(Exception e) {
			logger.error("last error", e);
			throw e;
		}
	}

	/**
	 * Remove Lock. Now another application instance can gain lock.
	 * 
	 * @throws Throwable
	 */
	private void release() throws Throwable {
		if (lock != null) {
			lock.release();
		}

		if (lock_channel != null) {
			lock_channel.close();
		}

		if (lock_stream != null) {
			lock_stream.close();
		}

		if (lock_file != null) {
			lock_file.delete();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.release();
		super.finalize();
	}

	private static ApplicationLock instance;

	/**
	 * Set Lock based on input key Method can be run only one time per
	 * application. Second calls will be ignored.
	 * 
	 * @param key
	 *            Lock key
	 */
	public static boolean setLock(String key) {
		if (instance != null) {
			return true;
		}

		try {
			instance = new ApplicationLock(key);
		} catch (Exception ex) {
			instance = null;
			logger.error("set lock", ex);
			return false;
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				ApplicationLock.releaseLock();
			}
		});
		return true;
	}

	/**
	 * Try to release Lock. After releasing you can not user AppLock again in
	 * application.
	 */
	public static void releaseLock() {
		if (instance == null) {
			return;
		}
		try {
			instance.release();
		} catch (Throwable ex) {
			logger.error("release lock", ex);
		}
	}
}
