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
package com.hangum.db.browser.rap.core.editors.main;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
 
/**
 * This class is used to ptovide an example of showing
 * completion feature in a text field.
 * @author Debadatta Mishra(PIKU)
 *
 */
public class AutoCompletionTextField implements IEntryPoint {

	public int createUI() {
		String[] args = new String[0];
		main(args);
		
		return 0;
	}

    /**
     * A label for for display of message.
     */
    private static Label label = null;
    /**
     * Object of type {@link Text} to display a text field
     */
    private static Text text = null;
    /**
     * A String array of default proposals for autocompletion
     */
    private static String[] defaultProposals = new String[] { "Assistance 1","Assistance 2", "Assistance 3" , "Assistance 4" , "Assistance 5"};
    /**
     * A String for key press
     */
    private static String KEY_PRESS = "Ctrl+Space";
 
    /**
     * Method used to create a label.
     * 
     * @author Debadatta Mishra (PIKU)
     * @param shell of type {@link Shell}
     */
    private static void createLabel( Shell shell )
    {
        label = new Label( shell , SWT.NONE);
        label.setText("Enter some text in the text field");
        //Alignment of label in the shell
        FormData label1LData = new FormData();
        label1LData.width = 162;
        label1LData.height = 15;
        label1LData.left =  new FormAttachment(0, 1000, 12);
        label1LData.top =  new FormAttachment(0, 1000, 12);
        label.setLayoutData(label1LData);
    }
 
    /**
     * Method used to display an array of String data for
     * autocompletion. You can have your own method like
     * this to get the autocompletion data. This method
     * can be customized to get the data from database
     * and you can display as autocompletion array.
     * 
     * @param text of type String
     * @return an array of String data
     * @author Debadatta Mishra (PIKU)
     */
    private static String[] getAllProposals( String text )
    {
        String[] proposals = new String[5];
        if( text == null || text.length() == 0 )
            proposals = defaultProposals;
        else
        {
            for( int i = 0 ; i < 5 ; i++ )
                proposals[i] = text+i;
        }
        return proposals;
    }
 
    /**
     * This method is used to provide the implementaion
     * of eclipse autocompletion feature. User has to press
     * "CTRL+Space" to see the autocompletion effect.
     * 
     * @param text of type {@link Text}
     * @param value of type String
     * @author Debadatta Mishra (PIKU)
     */
    private static void setAutoCompletion( Text text , String value )
    {
        try
        {
            ContentProposalAdapter adapter = null;
            String[] defaultProposals = getAllProposals(value);
            SimpleContentProposalProvider scp = new SimpleContentProposalProvider( defaultProposals );
            scp.setProposals(defaultProposals);
            KeyStroke ks = KeyStroke.getInstance(KEY_PRESS);
            adapter = new ContentProposalAdapter(text, new TextContentAdapter(),
                    scp,ks,null);
            adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
 
    /**
     * Method used to create a text field.
     * 
     * @author Debadatta Mishra (PIKU)
     * @param shell of type {@link Shell}
     * @author Debadatta Mishra (PIKU)
     */
    private static void createText( Shell shell )
    {
        text = new Text(shell,SWT.BORDER);
        //Alignment of Text field in the shell
        FormData text1LData = new FormData();
        text1LData.width = 223;
        text1LData.height = 34;
        text1LData.left =  new FormAttachment(0, 1000, 236);
        text1LData.top =  new FormAttachment(0, 1000, 12);
        text.setLayoutData(text1LData);
        //Method for autocompletion
        setAutoCompletion(text, null);
 
        text.addKeyListener( new KeyAdapter()
        {
            public void keyReleased(KeyEvent ke) 
            {
                //Method for autocompletion
                setAutoCompletion(text, text.getText());
            }
        }
        );
    }
 
    /**
     * Main method to execute the test
     * 
     * @author Debadatta Mishra (PIKU)
     * @param args of type {@link String}
     */
    public static void main(String[] args) 
    {
        final Display display = new Display ();
        final Shell shell = new Shell (display, SWT.CLOSE);
        shell.setText("A text field with autocompletion support, press CTRL+Space to see the effect");
        shell.setLayout(new FormLayout());
        shell.setSize(600, 200);
 
        createLabel(shell);
        createText(shell);
 
        shell.open ();
       
    }
 
}
