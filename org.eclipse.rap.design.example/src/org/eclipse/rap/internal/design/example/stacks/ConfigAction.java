/*******************************************************************************
 * Copyright (c) 2009, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.design.example.stacks;

import org.eclipse.rap.ui.interactiondesign.ConfigurationAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.presentations.IStackPresentationSite;


public class ConfigAction extends ConfigurationAction{

  public ConfigAction() {
  }
  
  
  public void run() {
    IStackPresentationSite site = getSite();
    IWorkbench workbench = PlatformUI.getWorkbench();
    Shell shell = workbench.getActiveWorkbenchWindow().getShell();
    ConfigurationDialog dialog 
      = new ConfigurationDialog( shell,
                                 SWT.ON_TOP | SWT.CLOSE | SWT.BORDER 
                                 | SWT.APPLICATION_MODAL, 
                                 site,
                                 this );
    dialog.open();
  }
}
