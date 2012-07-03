package com.hangum.db.browser.rap.core.editors.intro;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * 기본 intro
 * 
 * @author hangum
 *
 */
public class IntroEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(IntroEditor.class);
	public static final String ID = "com.hangum.db.browser.rap.core.editor.intor";

	public IntroEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		setSite(site);
		setInput(input);
		
		IntroEditorInput iei = (IntroEditorInput)input;
		setPartName(iei.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		Browser browser = new Browser(composite, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		if(RWT.getLocale().toString().startsWith("ko")) {
			browser.setUrl("https://sites.google.com/site/tadpolefordb/home");//http://hangumkj.blogspot.com/2011/05/db-browser.html");
		} else {
			browser.setUrl("https://sites.google.com/site/tadpolefordbtoolsen/");//http://hangumkj.blogspot.com/2011/05/db-browser.html");
		}
		
//		Composite composite_1 = new Composite(parent, SWT.NONE);
//		composite_1.setLayout(new GridLayout(1, false));
//		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Label lblTestMysql = new Label(composite_1, SWT.NONE);
//		lblTestMysql.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
//		lblTestMysql.setText("Test MySQL : jdbc:mysql://14.63.212.152:13306/tester, tester/1234");
		
//		text = new Text(composite, SWT.BORDER);
//		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		text.setText("");

	}

	@Override
	public void setFocus() {
	}
}
