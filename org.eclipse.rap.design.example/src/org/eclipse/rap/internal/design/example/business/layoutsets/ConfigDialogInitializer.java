/*******************************************************************************
 * Copyright (c) 2010, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.design.example.business.layoutsets;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.rwt.graphics.Graphics;
import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;


public class ConfigDialogInitializer implements ILayoutSetInitializer { 
  
  public void initializeLayoutSet( final LayoutSet layoutSet ) {
    layoutSet.addColor( ILayoutSetConstants.CONFIG_BLACK, 
                        Graphics.getColor( 0, 0, 0 ) );
    layoutSet.addColor( ILayoutSetConstants.CONFIG_WHITE, 
                        Graphics.getColor( 255, 255, 255 ) );
    String path = ILayoutSetConstants.IMAGE_PATH_BUSINESS;
    layoutSet.addImagePath( ILayoutSetConstants.CONFIG_DIALOG_CLOSE, 
                            path + "close.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.CONFIG_DIALOG_ICON, 
                            path + "conf_dialog_icon.png" ); //$NON-NLS-1$
  }
}
