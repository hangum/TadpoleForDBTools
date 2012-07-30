package com.hangum.db.browser.rap.core.editors.main;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TextWidgetText implements IEntryPoint {
	private Text text_1;

	/**
	 * @wbp.parser.entryPoint
	 */
	public int createUI() { 
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
		
		final Text text = new Text(shell, style);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.keyCode);
				
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					text.insert(new Character(SWT.TAB).toString());
				}
			}
		});
//		text.addTraverseListener(new TraverseListener() {
//			public void keyTraversed(TraverseEvent e) {
//				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
//					e.doit = false;
//					e.detail = SWT.TRAVERSE_NONE;
//				}
//			}
//		});
		text.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					text_1.insert(new Character(SWT.TAB).toString());
				}
			}
		});
//		text_1.addTraverseListener(new TraverseListener() {
//			public void keyTraversed(TraverseEvent e) {
//				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
//					e.doit = false;
//					e.detail = SWT.TRAVERSE_NONE;
//				}
//			}
//		});
		text_1.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		shell.setSize(404, 380);
		shell.open();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
		
		return 0;
	}
}
