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
package com.hangum.tadpole.commons.exception.dialog;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		IBM Corporation - initial API and implementation 
 * 		Sebastian Davids <sdavids@gmx.de> - Fix for bug 19346 - Dialog font should
 * 			be activated and used by other components.
 *******************************************************************************/

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;

/**
 * A dialog to display one or more errors to the user, as contained in an
 * <code>IStatus</code> object. If an error contains additional detailed
 * information then a Details button is automatically supplied, which shows or
 * hides an error details viewer when pressed by the user.
 * 
 * @see org.eclipse.core.runtime.IStatus
 */
public class ExceptionDetailsErrorDialog extends IconAndMessageDialog {
    /**
     * Static to prevent opening of error dialogs for automated testing.
     */
    public static boolean AUTOMATED_MODE = false;

    /**
     * Reserve room for this many list items.
     */
    private static final int LIST_ITEM_COUNT = 7;

    /**
     * The nesting indent.
     */
    private static final String NESTING_INDENT = "  "; //$NON-NLS-1$

    /**
     * The Details button.
     */
    private Button detailsButton;

    /**
     * The title of the dialog.
     */
    private String title;

    /**
     * The SWT text control that displays the error details.
     */
    private Text text;

    /**
     * Indicates whether the error details viewer is currently created.
     */
    private boolean listCreated = false;

    /**
     * Filter mask for determining which status items to display.
     */
    private int displayMask = 0xFFFF;

    /**
     * The main status object.
     */
    private IStatus status;

//    /**
//     * The current clipboard. To be disposed when closing the dialog.
//     */
//    private Clipboard clipboard;

	private boolean shouldIncludeTopLevelErrorInDetails = false;

    /**
     * Creates an error dialog. Note that the dialog will have no visual
     * representation (no widgets) until it is told to open.
     * <p>
     * Normally one should use <code>openError</code> to create and open one
     * of these. This constructor is useful only if the error object being
     * displayed contains child items <it>and </it> you need to specify a mask
     * which will be used to filter the displaying of these children.
     * </p>
     * 
     * @param parentShell
     *            the shell under which to create this dialog
     * @param dialogTitle
     *            the title to use for this dialog, or <code>null</code> to
     *            indicate that the default title should be used
     * @param message
     *            the message to show in this dialog, or <code>null</code> to
     *            indicate that the error's message should be shown as the
     *            primary message
     * @param status
     *            the error to show to the user
     * @param displayMask
     *            the mask to use to filter the displaying of child items, as
     *            per <code>IStatus.matches</code>
     * @see org.eclipse.core.runtime.IStatus#matches(int)
     */
    public ExceptionDetailsErrorDialog(Shell parentShell, String dialogTitle, String message,
            IStatus status, int displayMask) {
        super(parentShell);
        this.title = dialogTitle == null ? JFaceResources
                .getString("Problem_Occurred") :
                dialogTitle;
        this.message = message == null ? status.getMessage()
                : JFaceResources
                        .format(
                        		"Reason", new Object[] { message, status.getMessage() });
        this.status = status;
        this.displayMask = displayMask;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /*
     * (non-Javadoc) Method declared on Dialog. Handles the pressing of the Ok
     * or Details button in this dialog. If the Ok button was pressed then close
     * this dialog. If the Details button was pressed then toggle the displaying
     * of the error details area. Note that the Details button will only be
     * visible if the error being displayed specifies child details.
     */
    protected void buttonPressed(int id) {
        if (id == IDialogConstants.DETAILS_ID) {
            // was the details button pressed?
            toggleDetailsArea();
        } else {
            super.buttonPressed(id);
        }
    }

    /*
     * (non-Javadoc) Method declared in Window.
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(title);
        shell.setImage(GlobalImageUtils.getTadpoleIcon());
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Details buttons
        createButton(parent, IDialogConstants.OK_ID, Messages.get().Confirm, true);
        createDetailsButton(parent);
    }

    /**
     * Create the details button if it should be included.
     * @param parent the parent composite
     * @since 3.2
     */
	protected void createDetailsButton(Composite parent) {
		if (shouldShowDetailsButton()) {
            detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, Messages.get().ExceptionDetailsErrorDialog_3, false);
        }
	}

    /**
     * This implementation of the <code>Dialog</code> framework method creates
     * and lays out a composite and calls <code>createMessageArea</code> and
     * <code>createCustomArea</code> to populate it. Subclasses should
     * override <code>createCustomArea</code> to add contents below the
     * message.
     */
    protected Control createDialogArea(Composite parent) {
        createMessageArea(parent);
        // create a composite with standard margins and spacing
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData childData = new GridData(GridData.FILL_BOTH);
        childData.horizontalSpan = 2;
        composite.setLayoutData(childData);
        composite.setFont(parent.getFont());
        
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
        return composite;
    }

    /*
     * @see IconAndMessageDialog#createDialogAndButtonArea(Composite)
     */
    protected void createDialogAndButtonArea(Composite parent) {
        super.createDialogAndButtonArea(parent);
        if (this.dialogArea instanceof Composite) {
            //Create a label if there are no children to force a smaller layout
            Composite dialogComposite = (Composite) dialogArea;
            if (dialogComposite.getChildren().length == 0) {
				new Label(dialogComposite, SWT.NULL);
			}
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IconAndMessageDialog#getImage()
     */
    protected Image getImage() {
        if (status != null) {
            if (status.getSeverity() == IStatus.WARNING) {
				return getWarningImage();
			}
            if (status.getSeverity() == IStatus.INFO) {
				return getInfoImage();
			}
        }
        //If it was not a warning or an error then return the error image
        return getErrorImage();
    }

    /**
     * Create this dialog's drop-down list component.
     * 
     * @param parent
     *            the parent composite
     * @return the drop-down list component
     */
    protected Text createDropDownList(Composite parent) {
        // create the list
        text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
        // fill the list
        populateList(text);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
        data.heightHint = 300;//text.getItemHeight() * LIST_ITEM_COUNT;
        data.horizontalSpan = 2;
        text.setLayoutData(data);
        text.setFont(parent.getFont());
//        Menu copyMenu = new Menu(text);
//        MenuItem copyItem = new MenuItem(copyMenu, SWT.NONE);
//        copyItem.addSelectionListener(new SelectionListener() {
//            /*
//             * @see SelectionListener.widgetSelected (SelectionEvent)
//             */
//            public void widgetSelected(SelectionEvent e) {
//                copyToClipboard();
//            }
//
//            /*
//             * @see SelectionListener.widgetDefaultSelected(SelectionEvent)
//             */
//            public void widgetDefaultSelected(SelectionEvent e) {
//                copyToClipboard();
//            }
//        });
//        copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
//        text.setMenu(copyMenu);
        listCreated = true;
        return text;
    }

    /*
     * (non-Javadoc) Method declared on Window.
     */
    /**
     * Extends <code>Window.open()</code>. Opens an error dialog to display
     * the error. If you specified a mask to filter the displaying of these
     * children, the error dialog will only be displayed if there is at least
     * one child status matching the mask.
     */
    public int open() {
        if (!AUTOMATED_MODE && shouldDisplay(status, displayMask)) {
            return super.open();
        }
        setReturnCode(OK);
        return OK;
    }

    /**
     * Opens an error dialog to display the given error. Use this method if the
     * error object being displayed does not contain child items, or if you wish
     * to display all such items without filtering.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param dialogTitle
     *            the title to use for this dialog, or <code>null</code> to
     *            indicate that the default title should be used
     * @param message
     *            the message to show in this dialog, or <code>null</code> to
     *            indicate that the error's message should be shown as the
     *            primary message
     * @param status
     *            the error to show to the user
     * @return the code of the button that was pressed that resulted in this
     *         dialog closing. This will be <code>Dialog.OK</code> if the OK
     *         button was pressed, or <code>Dialog.CANCEL</code> if this
     *         dialog's close window decoration or the ESC key was used.
     */
    public static int openError(Shell parent, String dialogTitle,
            String message, IStatus status) {
        return openError(parent, dialogTitle, message, status, IStatus.OK
                | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
    }

    /**
     * Opens an error dialog to display the given error. Use this method if the
     * error object being displayed contains child items <it>and </it> you wish
     * to specify a mask which will be used to filter the displaying of these
     * children. The error dialog will only be displayed if there is at least
     * one child status matching the mask.
     * 
     * @param parentShell
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param title
     *            the title to use for this dialog, or <code>null</code> to
     *            indicate that the default title should be used
     * @param message
     *            the message to show in this dialog, or <code>null</code> to
     *            indicate that the error's message should be shown as the
     *            primary message
     * @param status
     *            the error to show to the user
     * @param displayMask
     *            the mask to use to filter the displaying of child items, as
     *            per <code>IStatus.matches</code>
     * @return the code of the button that was pressed that resulted in this
     *         dialog closing. This will be <code>Dialog.OK</code> if the OK
     *         button was pressed, or <code>Dialog.CANCEL</code> if this
     *         dialog's close window decoration or the ESC key was used.
     * @see org.eclipse.core.runtime.IStatus#matches(int)
     */
    public static int openError(Shell parentShell, String title,
            String message, IStatus status, int displayMask) {
        ExceptionDetailsErrorDialog dialog = new ExceptionDetailsErrorDialog(parentShell, title, message,
                status, displayMask);
        return dialog.open();
    }

    /**
     * Populates the list using this error dialog's status object. This walks
     * the child static of the status object and displays them in a list. The
     * format for each entry is status_path : status_message If the status's
     * path was null then it (and the colon) are omitted.
     * @param listToPopulate The list to fill.
     */
    private void populateList(Text listToPopulate) {
        populateList(listToPopulate, status, 0, shouldIncludeTopLevelErrorInDetails);
    }

    /**
     * Populate the list with the messages from the given status. Traverse the
     * children of the status deeply and also traverse CoreExceptions that appear
     * in the status.
     * @param listToPopulate the list to populate
     * @param buildingStatus the status being displayed
     * @param nesting the nesting level (increases one level for each level of children)
     * @param includeStatus whether to include the buildingStatus in the display or
     * just its children
     */
    private void populateList(Text listToPopulate, IStatus buildingStatus,
            int nesting, boolean includeStatus) {
        
        if (!buildingStatus.matches(displayMask)) {
            return;
        }

        Throwable t = buildingStatus.getException();
        boolean isCoreException= t instanceof CoreException;
        boolean incrementNesting= false;
        
       	if (includeStatus) {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < nesting; i++) {
	            sb.append(NESTING_INDENT);
	        }
	        String message = buildingStatus.getMessage();
            sb.append(message);
	        listToPopulate.append(sb.toString());
	        incrementNesting= true;
       	}
        	
        if (!isCoreException && t != null) {
        	// Include low-level exception message
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < nesting; i++) {
	            sb.append(NESTING_INDENT);
	        }
	        
	        StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw );
			
	        String message = sw.getBuffer().toString();
	        if (message == null) {
				message = t.toString();
			}
	        	
	        sb.append(message);
	        listToPopulate.append(sb.toString());
	        incrementNesting= true;
        }
        
        if (incrementNesting) {
			nesting++;
		}
        
        // Look for a nested core exception
        if (isCoreException) {
            CoreException ce = (CoreException)t;
            IStatus eStatus = ce.getStatus();
            // Only print the exception message if it is not contained in the parent message
            if (message == null || message.indexOf(eStatus.getMessage()) == -1) {
                populateList(listToPopulate, eStatus, nesting, true);
            }
        }

        
        // Look for child status
        IStatus[] children = buildingStatus.getChildren();
        for (int i = 0; i < children.length; i++) {
            populateList(listToPopulate, children[i], nesting, true);
        }
    }

    /**
     * Returns whether the given status object should be displayed.
     * 
     * @param status
     *            a status object
     * @param mask
     *            a mask as per <code>IStatus.matches</code>
     * @return <code>true</code> if the given status should be displayed, and
     *         <code>false</code> otherwise
     * @see org.eclipse.core.runtime.IStatus#matches(int)
     */
    protected static boolean shouldDisplay(IStatus status, int mask) {
        IStatus[] children = status.getChildren();
        if (children == null || children.length == 0) {
            return status.matches(mask);
        }
        for (int i = 0; i < children.length; i++) {
            if (children[i].matches(mask)) {
				return true;
			}
        }
        return false;
    }

    /**
     * Toggles the unfolding of the details area. This is triggered by the user
     * pressing the details button.
     */
    private void toggleDetailsArea() {
        Point windowSize = getShell().getSize();
        Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (listCreated) {
            text.dispose();
            listCreated = false;
            detailsButton.setText(Messages.get().ExceptionDetailsErrorDialog_3);
        } else {
            text = createDropDownList((Composite) getContents());
            detailsButton.setText(Messages.get().ExceptionDetailsErrorDialog_5);
        }
        Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        getShell()
                .setSize(
                        new Point(windowSize.x, windowSize.y
                                + (newSize.y - oldSize.y)));
    }

    /**
     * Put the details of the status of the error onto the stream.
     * 
     * @param buildingStatus
     * @param buffer
     * @param nesting
     */
    private void populateCopyBuffer(IStatus buildingStatus,
            StringBuffer buffer, int nesting) {
        if (!buildingStatus.matches(displayMask)) {
            return;
        }
        for (int i = 0; i < nesting; i++) {
            buffer.append(NESTING_INDENT);
        }
        buffer.append(buildingStatus.getMessage());
        buffer.append("\n"); //$NON-NLS-1$
        
        // Look for a nested core exception
        Throwable t = buildingStatus.getException();
        if (t instanceof CoreException) {
            CoreException ce = (CoreException)t;
            populateCopyBuffer(ce.getStatus(), buffer, nesting + 1);
        }
        
        IStatus[] children = buildingStatus.getChildren();
        for (int i = 0; i < children.length; i++) {
            populateCopyBuffer(children[i], buffer, nesting + 1);
        }
    }

//    /**
//     * Copy the contents of the statuses to the clipboard.
//     */
//    private void copyToClipboard() {
//        if (clipboard != null) {
//			clipboard.dispose();
//		}
//        StringBuffer statusBuffer = new StringBuffer();
//        populateCopyBuffer(status, statusBuffer, 0);
//        clipboard = new Clipboard(text.getDisplay());
//        clipboard.setContents(new Object[] { statusBuffer.toString() },
//                new Transfer[] { TextTransfer.getInstance() });
//    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#close()
     */
    public boolean close() {
//        if (clipboard != null) {
//			clipboard.dispose();
//		}
        return super.close();
    }
    
    /**
     * Show the details portion of the dialog if it is not already visible.
     * This method will only work when it is invoked after the control of the dialog
     * has been set. In other words, after the <code>createContents</code> method
     * has been invoked and has returned the control for the content area of the dialog.
     * Invoking the method before the content area has been set or after the dialog has been
     * disposed will have no effect.
     * @since 3.1
     */
    protected final void showDetailsArea() {
        if (!listCreated) {
            Control control = getContents();
            if (control != null && ! control.isDisposed()) {
				toggleDetailsArea();
			}
        }
    }
    
    /**
     * Return whether the Details button should be included.
     * This method is invoked once when the dialog is built.
     * By default, the Details button is only included if
     * the status used when creating the dialog was a multi-status
     * or if the status contains an exception.
     * Subclasses may override.
     * @return whether the Details button should be included
     * @since 3.1
     */
    protected boolean shouldShowDetailsButton() {
        return status.isMultiStatus() || status.getException() != null;
    }
    
    /**
     * Set the status displayed by this error dialog to the given status.
     * This only affects the status displayed by the Details list.
     * The message, image and title should be updated by the subclass,
     * if desired.
     * @param status the status to be displayed in the details list
     * @since 3.1
     */
    protected final void setStatus(IStatus status) {
        if (this.status != status) {
	        this.status = status;
        }
        shouldIncludeTopLevelErrorInDetails = true;
        if (listCreated) {
            repopulateList();
        }
    }
    
    /**
     * Repopulate the supplied list widget.
     */
    private void repopulateList() {
        if (text != null && !text.isDisposed()) {
	        text.setText(""); //$NON-NLS-1$
	        populateList(text);
        }
    }
}
