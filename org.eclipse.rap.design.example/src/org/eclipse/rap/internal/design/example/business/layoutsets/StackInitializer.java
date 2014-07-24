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
package org.eclipse.rap.internal.design.example.business.layoutsets;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.rwt.graphics.Graphics;
import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;


public class StackInitializer implements ILayoutSetInitializer {

  public void initializeLayoutSet( final LayoutSet layoutSet ) {
    String path = ILayoutSetConstants.IMAGE_PATH_BUSINESS;
    layoutSet.addImagePath( ILayoutSetConstants.STACK_CONF_ACTIVE, 
                            path + "stack_tab_conf_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_CONF_INACTIVE, 
                            path + "stack_tab_conf_inactive.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_CONF_BG_ACTIVE, 
                            path + "stack_tab_active_confarea_bg.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TAB_INACTIVE_BG_ACTIVE, 
                            path + "stack_tab_inactive_bg_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_CONF_BG_INACTIVE, 
                            path + "stack_tab_inactive_confarea_bg.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TAB_INACTIVE_RIGHT_ACTIVE, 
                            path + "stack_tab_inactive_right_active.png" ); //$NON-NLS-1$
    String separatorActive 
      = ILayoutSetConstants.STACK_TAB_INACTIVE_SEPARATOR_ACTIVE;
    layoutSet.addImagePath( separatorActive, 
                            path + "stack_tab_inactive_separator_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TAB_OVERFLOW_ACTIVE, 
                            path + "stack_tab_overflow_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_LEFT, 
                            path + "stack_border_left.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_RIGHT, 
                            path + "stack_border_right.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_TOP, 
                            path + "stack_border_top.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_BOTTOM, 
                            path + "stack_border_bottom.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_LEFT_ACTIVE, 
                            path + "stack_border_left.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_RIGHT_AVTIVE, 
                            path + "stack_border_right.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_BORDER_BOTTOM_ACTIVE, 
                            path + "stack_border_bottom.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_INACTIVE_CORNER, 
                            path + "stack_inactive_corner.png" ); //$NON-NLS-1$
    String cornerActive = ILayoutSetConstants.STACK_TAB_INACTIVE_CORNER_ACTIVE;
    layoutSet.addImagePath( cornerActive, 
                            path + "stack_inactive_corner_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_VIEW_TOOLBAR_BG, 
                            path + "viewtoolbar_bg.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_VIEW_MENU_ICON, 
                            path + "viewMenu.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_VIEW_PULLDOWN, 
                            path + "viewPulldown.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TAB_BG_ACTIVE,
                            path + "stack_tab_bg_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TABBAR_LEFT_ACTIVE, 
                            path + "trans.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TABBAR_RIGHT_ACTIVE, 
                            path + "trans.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TABBAR_LEFT_INACTIVE, 
                            path + "trans.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.STACK_TABBAR_RIGHT_INACTIVE, 
                            path + "trans.png" ); //$NON-NLS-1$
    FormData fdConfButton = new FormData();
    fdConfButton.top = new FormAttachment( 0, 1 );
    fdConfButton.right = new FormAttachment( 100, 0 ); 
    layoutSet.addPosition( ILayoutSetConstants.STACK_CONF_POSITION, 
                           fdConfButton );
    FormData fdOverflow = new FormData();
    fdOverflow.top = new FormAttachment( 0, 5 );
    fdOverflow.right = new FormAttachment( 100, -22 );
    layoutSet.addPosition( ILayoutSetConstants.STACK_OVERFLOW_POSITION,
                           fdOverflow );
    layoutSet.addColor( ILayoutSetConstants.STACK_BUTTON_ACTIVE, 
                        Graphics.getColor( 0, 88, 159 ) );
    layoutSet.addColor( ILayoutSetConstants.STACK_BUTTON_INACTIVE, 
                        Graphics.getColor( 148, 148, 148 ) );
    FormData fdTabBg = new FormData();
    fdTabBg.width = 0;
    fdTabBg.height = 0;
    layoutSet.addPosition( ILayoutSetConstants.STACK_TABBG_POS, fdTabBg );
    FormData fdButton = new FormData();
    fdButton.top = new FormAttachment( 0, -2 );
    layoutSet.addPosition( ILayoutSetConstants.STACK_BUTTON_TOP, fdButton );
    FormData fdConfPos = new FormData();
    fdConfPos.right = new FormAttachment( 100 );
    layoutSet.addPosition( ILayoutSetConstants.STACK_CONF_POS, fdConfPos );
  }
}
