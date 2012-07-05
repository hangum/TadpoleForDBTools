package com.hangum.db.browser.rap.dialog.about;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.db.browser.rap.Messages;
import com.swtdesigner.ResourceManager;

public class AboutDialog extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AboutDialog(Shell parentShell) {
		super(parentShell);
		// Tadpole for DB Tools - 뒷다리 나오기 프로젝트 (2012/04/01)
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 2;
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		Label lblNewLabelImage = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabelImage = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabelImage.heightHint = 199;
		gd_lblNewLabelImage.widthHint = 150;
		gd_lblNewLabelImage.minimumHeight = 184;
		gd_lblNewLabelImage.minimumWidth = 300;
		lblNewLabelImage.setLayoutData(gd_lblNewLabelImage);
//		lblNewLabel_1.setText(Messages.AboutDialog_lblNewLabel_1_text);
		lblNewLabelImage.setImage(ResourceManager.getPluginImage("com.hangum.db.browser.start", "resources/icons/TadpoleForDBTools.png"));
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		안녕하세요. 사용해 주셔서 감사합니다.
//		  버그나 질문은 메일(<a href=\"mailto:adi.tadpole@gmail.com\">tadpole</a>), 
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setText(Messages.AboutAction_3);
		
		Label lblNewLabel0 = new Label(composite_1, SWT.NONE);
		lblNewLabel0.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel0.setText("MAIL : <a href=\"mailto:adi.tadpole@gmail.com\" target=\"_blank\">adi.tadpole@gmail.com</a>");//Messages.AboutAction_4);

		Label lblNewLabel2 = new Label(composite_1, SWT.NONE);
		lblNewLabel2.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel2.setText("HOME : <a href=\"https://sites.google.com/site/tadpolefordb\" target=\"_blank\">https://sites.google.com/site/tadpolefordb</a>");//Messages.AboutAction_4);
		
		Label lblNewLabel3 = new Label(composite_1, SWT.NONE);
		lblNewLabel3.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel3.setText("ISSUES : <a href=\"http://code.google.com/p/tadpole-for-db-tools/issues/list\" target=\"_blank\">http://code.google.com/p/tadpole-for-db-tools/issues/list</a>");//Messages.AboutAction_4);
		
		Label lblNewLabel4 = new Label(composite_1, SWT.NONE);
		lblNewLabel4.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		lblNewLabel4.setText(Messages.AboutAction_5);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(635, 308);
	}
}
