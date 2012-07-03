package com.hangum.db.util.download;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;

import com.hangum.db.define.Define;

/**
 * 쿼리결과, 히스토리 다운로드 서비스
 * 
 * @author hangum
 *
 */
public class DownloadServiceHandler implements IServiceHandler {
	public static final String ID = "com.hangum.db.browser.rap.core.editors.main.internals.DownloadServiceHandler";
	private static final Logger logger = Logger.getLogger(DownloadServiceHandler.class);
	private String name;
	private String content;

	@Override
	public void service() throws IOException, ServletException {
		HttpServletResponse resp = RWT.getResponse();
		makeHtmlFile(resp);
	}

	private void makeHtmlFile(HttpServletResponse resp) {
		OutputStream os = null;
	    try {
	      byte[] editorContentBytes = getContent().getBytes();
	      String contentType = "text/html";
	      
	      // Set response headers
	      resp.setContentType( contentType );
	      resp.setContentLength( editorContentBytes.length );
	      resp.setHeader( "Content-Disposition", "attachment; filename="
	                                             + "\""
	                                             + getName()
	                                             + "\""
	                                             + Define.SQL_DILIMITER );
	      resp.flushBuffer();
	      // Copy documentation to responce's output stream.
	      os = resp.getOutputStream();
	      os.write( editorContentBytes );
	      os.flush();
	    } catch( Exception e ) {
	      throw new IllegalArgumentException( "Download failed. Exception: " + e.getLocalizedMessage() );
	    } finally {
	      try {
	        if( os != null ) {
	          os.close();
	        }
	      } catch( IOException e ) {
	        // Ignore
	      }
	    }
	}

	public String getId() {
		return ID + hashCode();
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
