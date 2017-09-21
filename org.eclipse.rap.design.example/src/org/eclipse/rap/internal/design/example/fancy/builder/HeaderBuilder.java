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
package org.eclipse.rap.internal.design.example.fancy.builder;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.internal.design.example.builder.DummyBuilder;
import org.eclipse.rap.ui.interactiondesign.layout.ElementBuilder;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


public class HeaderBuilder extends ElementBuilder {
  
  private Image leftBg;
  private Image logo;
  private Image rightBg;
  private Image wave;
  private FormData fdLogo;
  private Control toolBar;
  private Composite leftArea;
  private Composite waveArea;
  private Composite buttonBg;

  public HeaderBuilder( Composite parent, String layoutSetId ) {
    super( parent, layoutSetId );
    initLayoutData();
  }

  private void initLayoutData() {
    LayoutSet set = getLayoutSet();
    // images
    leftBg = createImage( set.getImagePath( ILayoutSetConstants.HEADER_LEFT_BG ) );    
    rightBg = createImage( set.getImagePath( ILayoutSetConstants.HEADER_RIGHT_BG ) );
    wave = createImage( set.getImagePath( ILayoutSetConstants.HEADER_WAVE ) );
    
    // logo
    ElementBuilder builder 
      = new DummyBuilder( null, ILayoutSetConstants.SET_ID_LOGO );
    logo = builder.getImage( ILayoutSetConstants.LOGO );
    // positions
    LayoutSet layoutSet = ( LayoutSet ) builder.getAdapter( LayoutSet.class );
    fdLogo = layoutSet.getPosition( ILayoutSetConstants.LOGO_POSITION );
  }

  public void addControl( Control control, Object layoutData ) {
    toolBar = control;
    toolBar.setLayoutData( layoutData );
  }

  public void addControl( Control control, String positionId ) {
  }

  public void addImage( Image image, Object layoutData ) {
  }

  public void addImage( Image image, String positionId ) {
  }

  public void build() {
    getParent().setLayout( new FormLayout() );
    getParent().setBackgroundMode( SWT.INHERIT_FORCE );
    getParent().setBackgroundImage( leftBg );
    
    leftArea = new Composite( getParent(), SWT.NONE );
    leftArea.setLayout( new FormLayout() );
//    leftArea.setBackgroundImage( leftBg );
    FormData fdLeftArea = new FormData();
    leftArea.setLayoutData( fdLeftArea );
    fdLeftArea.left = new FormAttachment( 0 );
    fdLeftArea.top = new FormAttachment( 0, 0 );    
    fdLeftArea.height = leftBg.getBounds().height;
    
    
    waveArea = new Composite( getParent(), SWT.NONE );
    waveArea.setLayout( new FormLayout() );
    waveArea.setBackgroundImage( wave );
    final FormData fdWaveArea = new FormData();
    waveArea.setLayoutData( fdWaveArea );
    fdWaveArea.left = new FormAttachment( leftArea );
    fdWaveArea.top = new FormAttachment( 0, 0 );
 
 
    
    final Composite logoArea = new Composite( getParent(), SWT.NONE );
    logoArea.setLayout( new FormLayout() );
    logoArea.setBackgroundImage( rightBg );
    FormData fdLogoArea = new FormData();
    logoArea.setLayoutData( fdLogoArea );
    fdLogoArea.right = new FormAttachment( 100, 0 );
    fdLogoArea.top = new FormAttachment( 0, 0 );
    fdLogoArea.height = rightBg.getBounds().height;
    fdLogoArea.width = rightBg.getBounds().width;
    
    Label logoLabel = new Label( logoArea, SWT.NONE );
    logoLabel.setImage( logo ); 
    logoLabel.setLayoutData( fdLogo );
    fdLogo.height = logo.getBounds().height;
    fdLogo.width = logo.getBounds().width;    
    fdLeftArea.right = new FormAttachment( logoArea, -80 );
    
    buttonBg = new Composite( leftArea, SWT.NONE );
    FormData fdButtonBg = new FormData();
    buttonBg.setLayoutData( fdButtonBg );
    fdButtonBg.height = 30;
    fdButtonBg.width = 30;
    buttonBg.moveAbove( leftArea );
    buttonBg.moveAbove( logoArea );
  }

  public void dispose() {
  }

  public Control getControl() {
    return leftArea;
  }

  public Point getSize() {
    return leftArea.getSize();
  }
  
  public Object getAdapter( Class adapter ) {
    Object result = null;
    if( adapter == Composite.class ) {
      result = waveArea;
    }
    return result;
  }
}
