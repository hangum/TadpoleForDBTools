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
package org.eclipse.rap.internal.design.example;


public interface ILayoutSetConstants {
  /*
   *  Image paths
   */
  public static final String IMAGE_PATH_FANCY = "img/fancy/"; //$NON-NLS-1$
  public static final String IMAGE_PATH_BUSINESS = "img/business/"; //$NON-NLS-1$
  
  /*
   *  LayoutSet Ids
   */
  public static final String SET_ID_CONFIG_DIALOG 
    = "org.eclipse.rap.design.example.layoutset.confdialog";    //$NON-NLS-1$
  public static final String SET_ID_COOLBAR 
    = "org.eclipse.rap.design.example.layoutset.coolbar";   //$NON-NLS-1$
  public static final String SET_ID_OVERFLOW 
  = "org.eclipse.rap.design.example.layoutset.coolbaroverflow"; //$NON-NLS-1$
  public static final String SET_ID_FOOTER 
    = "org.eclipse.rap.design.example.layoutset.footer";  //$NON-NLS-1$
  public static final String SET_ID_HEADER 
    = "org.eclipse.rap.design.example.layoutset.header";  //$NON-NLS-1$
  public static final String SET_ID_LOGO 
    = "org.eclipse.rap.design.example.layoutset.logo"; //$NON-NLS-1$
  public static final String SET_ID_MENUBAR 
    = "org.eclipse.rap.design.example.layoutset.menubar"; //$NON-NLS-1$
  public static final String SET_ID_PERSP 
    = "org.eclipse.rap.design.example.layoutset.perspective"; //$NON-NLS-1$
  public static final String SET_ID_STACKPRESENTATION 
    = "org.eclipse.rap.design.example.layoutset.stack";   //$NON-NLS-1$

  /*
   * LayoutSet content
   */
  
  // ConfigDialogInitializer
  public static final String CONFIG_WHITE = "CONFIG_WHITE"; //$NON-NLS-1$
  public static final String CONFIG_BLACK = "CONFIG_BLACK"; //$NON-NLS-1$
  public static final String CONFIG_DIALOG_CLOSE = "dialog.close"; //$NON-NLS-1$
  public static final String CONFIG_DIALOG_ICON = "dialog.conf.icon"; //$NON-NLS-1$
  
  // CoolbarInitializer
  public static final String COOLBAR_OVERFLOW_INACTIVE 
    = "coolbar.overflow.inactive"; //$NON-NLS-1$
  public static final String COOLBAR_OVERFLOW_ACTIVE 
    = "coolbar.overflow.active"; //$NON-NLS-1$
  public static final String COOLBAR_BUTTON_BG = "coolbar.button.bg"; //$NON-NLS-1$
  public static final String COOLBAR_OVERFLOW_COLOR = "coolbar.overflow.color"; //$NON-NLS-1$
  public static final String COOLBAR_ARROW = "coolbar.arrow"; //$NON-NLS-1$
  public static final String COOLBAR_BUTTON_POS = "coolbar.layer.button.pos"; //$NON-NLS-1$
  public static final String COOLBAR_SPACING = "colbar.layer.spacing"; //$NON-NLS-1$
  
  // CoolbarOverflowInitializer
  public static final String OVERFLOW_BG = "coolbar.layer.bg"; //$NON-NLS-1$
  public static final String OVERFLOW_RIGHT = "coolbar.layer.right"; //$NON-NLS-1$
  public static final String OVERFLOW_LEFT = "coolbar.layer.left"; //$NON-NLS-1$
  public static final String OVERFLOW_WAVE = "coolbar.layer.wave"; //$NON-NLS-1$
  public static final String OVERFLOW_ARROW = "coolbar.layer.arrow"; //$NON-NLS-1$
  public static final String OVERFLOW_POS = "coolbar.layer.pos"; //$NON-NLS-1$
    
  // FooterInitializer
  public static final String FOOTER_LEFT = "footer.left"; //$NON-NLS-1$
  public static final String FOOTER_BG = "footer"; //$NON-NLS-1$
  public static final String FOOTER_RIGHT = "footer.right"; //$NON-NLS-1$
  
  // HeaderInitializer
  public static final String HEADER_LEFT = "header.left"; //$NON-NLS-1$
  public static final String HEADER_LEFT_BG = "header.left.bg"; //$NON-NLS-1$
  public static final String HEADER_WAVE = "header.wave"; //$NON-NLS-1$
  public static final String HEADER_RIGHT_BG = "header.right.bg"; //$NON-NLS-1$
  public static final String HEADER_RIGHT = "header.right";  //$NON-NLS-1$
  
  // LogoInitializer
  public static final String LOGO = "header.logo"; //$NON-NLS-1$
  public static final String LOGO_POSITION = "header.logo.position"; //$NON-NLS-1$
  
  // MenuBarInitializer
  public static final String MENUBAR_ARROW = "menubar.arrow"; //$NON-NLS-1$
  public static final String MENUBAR_BG = "menubar.bg"; //$NON-NLS-1$
  
  // PerspectiveSwitcherInitializer
  public static final String PERSP_CLOSE = "perspective.close"; //$NON-NLS-1$
  public static final String PERSP_LEFT_ACTIVE = "perspective.left.active"; //$NON-NLS-1$
  public static final String PERSP_RIGHT_ACTIVE = "perspective.right.active"; //$NON-NLS-1$
  public static final String PERSP_BG = "perspective.bg"; //$NON-NLS-1$
  public static final String PERSP_BG_ACTIVE = "perspective.bg.active"; //$NON-NLS-1$
  public static final String PERSP_BUTTON_POS = "perspective.button.position"; //$NON-NLS-1$
  
  // StackInitializer
  public static final String STACK_BORDER_TOP = "stack.border.top"; //$NON-NLS-1$
  public static final String STACK_BORDER_BOTTOM = "stack.border.bottom"; //$NON-NLS-1$
  public static final String STACK_BORDER_LEFT = "stack.border.left"; //$NON-NLS-1$
  public static final String STACK_BORDER_RIGHT = "stack.border.right"; //$NON-NLS-1$
  public static final String STACK_BORDER_BOTTOM_ACTIVE 
    = "stack.border.bottom.active"; //$NON-NLS-1$
  public static final String STACK_BORDER_LEFT_ACTIVE 
    = "stack.border.left.active"; //$NON-NLS-1$
  public static final String STACK_BORDER_RIGHT_AVTIVE 
    = "stack.border.right.active"; //$NON-NLS-1$
  public static final String STACK_CONF_ACTIVE = "stack.conf.active"; //$NON-NLS-1$
  public static final String STACK_CONF_INACTIVE = "stack.conf.inactive"; //$NON-NLS-1$
  public static final String STACK_CONF_BG_ACTIVE = "stack.conf.bg.active"; //$NON-NLS-1$
  public static final String STACK_TAB_INACTIVE_BG_ACTIVE 
    = "stack.tab.inactive.bg.act"; //$NON-NLS-1$
  public static final String STACK_TAB_BG_ACTIVE = "stack.tab.bg.active"; //$NON-NLS-1$
  public static final String STACK_CONF_BG_INACTIVE = "stack.conf.bg.inactive"; //$NON-NLS-1$
  public static final String STACK_TAB_INACTIVE_RIGHT_ACTIVE 
    = "stack.tab.inactive.right.active"; //$NON-NLS-1$
  public static final String STACK_TAB_INACTIVE_SEPARATOR_ACTIVE 
    = "stack.tab.inactive.separator.active"; //$NON-NLS-1$
  public static final String STACK_TAB_OVERFLOW_ACTIVE 
    = "stack.tab.overflow.active"; //$NON-NLS-1$
  public static final String STACK_INACTIVE_CORNER = "stack.corner.inactive"; //$NON-NLS-1$
  public static final String STACK_TAB_INACTIVE_CORNER_ACTIVE 
    = "stack.inactive.corner.inactive"; //$NON-NLS-1$
  public static final String STACK_VIEW_TOOLBAR_BG = "stack.viewtoolbar.bg"; //$NON-NLS-1$
  public static final String STACK_VIEW_MENU_ICON = "stack.viewmenu.icon"; //$NON-NLS-1$
  public static final String STACK_VIEW_PULLDOWN = "stack.view.pulldown.arrow"; //$NON-NLS-1$
  public static final String STACK_TABBAR_RIGHT_ACTIVE 
    = "stack.tabbar.right.active"; //$NON-NLS-1$
  public static final String STACK_TABBAR_LEFT_ACTIVE 
  = "stack.tabbar.left.active"; //$NON-NLS-1$
  public static final String STACK_TABBAR_RIGHT_INACTIVE 
    = "stack.tabbar.right.inactive"; //$NON-NLS-1$
  public static final String STACK_TABBAR_LEFT_INACTIVE 
    = "stack.tabbar.left.inactive"; //$NON-NLS-1$
  public static final String STACK_CONF_POSITION = "stack.conf.position"; //$NON-NLS-1$
  public static final String STACK_OVERFLOW_POSITION = "stack.overflow.pos"; //$NON-NLS-1$
  public static final String STACK_BUTTON_ACTIVE = "stack.button.active"; //$NON-NLS-1$
  public static final String STACK_BUTTON_INACTIVE = "stack.button.inactive"; //$NON-NLS-1$
  public static final String STACK_TABBG_POS = "stack.tabbg.pos"; //$NON-NLS-1$
  public static final String STACK_BUTTON_TOP = "stack.button.top"; //$NON-NLS-1$
  public static final String STACK_CONF_POS = "stack.conf.pos"; //$NON-NLS-1$
  public static final String STACK_TOP_STANDALONE_ACTIVE 
    = "stack.top.standalone.active"; //$NON-NLS-1$
  public static final String STACK_TOP_STANDALONE_INACTIVE 
    = "stack.top.standalone.inactive"; //$NON-NLS-1$
 }
