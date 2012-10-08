package com.hangum.tadpole.rdb.core.editors.main;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.define.Define;

public class TextBoundSnippet implements IEntryPoint {

	/**
	 * @wbp.parser.entryPoint
	 */
	public int createUI() { 
		Display display = new Display ();
		Shell shell = new Shell (display);
	
		final Text text = new Text (shell, SWT.BORDER | SWT.V_SCROLL);
		Rectangle clientArea = shell.getClientArea ();
		text.setBounds (clientArea.x + 10, clientArea.y + 10, 1000, 1000);
		for (int i=0; i<16; i++) {
			text.append ("Line " + i + "\n");
		}
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
//				System.out.println("[key list][SWT.CTRL]" + SWT.CTRL + " [SWT.F11]" + SWT.F11);
//				System.out.println("[key code]" + e.keyCode + "[e.stateMask]" + e.stateMask);
				
				// ctrl + f11
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					System.out.println("-----[tab]--------------------------------------------------------------------------");
					text.insert("\t");
					e.doit = false;
				}
				
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// tab key를 먹지 않도록 수정
		text.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = false;
					e.detail = SWT.TRAVERSE_NONE;
				}
			}
		});
		
		
		shell.open ();
		
		// 0부터 시작 하니까는...
		text.setSelection (27);
		System.out.println ("selection=" + text.getSelection ());
		System.out.println ("caret position=" + text.getCaretPosition ());
//		System.out.println ("caret location=" + text.getCaretLocation ());
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
		
		return 0;
	}
	
}
