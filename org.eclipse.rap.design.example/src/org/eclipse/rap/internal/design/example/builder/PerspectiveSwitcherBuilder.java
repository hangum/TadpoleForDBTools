/*******************************************************************************
 * Copyright (c) 2009, 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.design.example.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.internal.design.example.Messages;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.ui.interactiondesign.layout.ElementBuilder;
import org.eclipse.rap.ui.internal.preferences.SessionScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.graphics.Graphics;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;


public class PerspectiveSwitcherBuilder extends ElementBuilder {

  private static final String RAP_PERSPECTIVES = "RAP_PERSPECTIVES"; //$NON-NLS-1$

  private final Composite background;
  private final Map perspectiveButtonMap;
  private final Map buttonPerspectiveMap;
  private final List buttonList;
  private final List perspectiveList;
  private Button otherButton;
  private final Image left;
  private final Image right;
  private final Image bg;
  private final Image bgActive;

  private final PerspectiveAdapter perspectiveAdapter = new PerspectiveAdapter() {

    @Override
    public void perspectiveActivated( IWorkbenchPage page, IPerspectiveDescriptor perspective ) {
      addIdToStore( perspective.getId() );

      Button button = createPerspectiveButton( perspective );
      cleanButtons( button );

      background.layout( true );
      Control[] children = { background };
      Composite parent = getParent();
      parent.changed( children );
      parent.layout( true );
      parent.getParent().layout( true );
    }
  };

  public PerspectiveSwitcherBuilder( Composite parent, String subSetId ) {
    super( parent, subSetId );
    background = new Composite( parent, SWT.NONE );
    background.setData( RWT.CUSTOM_VARIANT, "compTrans" ); //$NON-NLS-1$
    RowLayout layout = new RowLayout();
    background.setLayout( layout );
    layout.spacing = 3;
    layout.marginBottom = 0;
    layout.marginTop = 0;

    perspectiveButtonMap = new HashMap();
    buttonPerspectiveMap = new HashMap();
    buttonList = new ArrayList();
    perspectiveList = new ArrayList();
    // images
    left = getImage( ILayoutSetConstants.PERSP_LEFT_ACTIVE );
    right = getImage( ILayoutSetConstants.PERSP_RIGHT_ACTIVE );
    bg = getImage( ILayoutSetConstants.PERSP_BG );
    bgActive = getImage( ILayoutSetConstants.PERSP_BG_ACTIVE );
  }

  @Override
  public void addControl( Control control, Object layoutData ) {
  }

  @Override
  public void addControl( Control control, String positionId ) {
  }

  private void addIdToStore( String id ) {
    if( !perspectiveList.contains( id ) ) {
      perspectiveList.add( id );
    }
    save();
  }

  @Override
  public void addImage( Image image, Object layoutData ) {
  }

  @Override
  public void addImage( Image image, String positionId ) {
  }

  @Override
  public void build() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
    workbenchWindow.addPerspectiveListener( perspectiveAdapter );

    IPerspectiveDescriptor[] descriptors = load();
    for( int i = 0; i < descriptors.length; i++ ) {
      createPerspectiveButton( descriptors[ i ] );
    }

    // Button for the perspective dialog
    Composite otherBg = new Composite( background, SWT.NONE );
    otherBg.setLayout( new FormLayout() );
    otherButton = new Button( otherBg, SWT.PUSH | SWT.FLAT );
    FormData fdOther = new FormData();
    otherButton.setLayoutData( fdOther );
    fdOther.left = new FormAttachment( 0, left.getBounds().width );
    FormData buttonPos
      = getLayoutSet().getPosition( ILayoutSetConstants.PERSP_BUTTON_POS );
    fdOther.top = buttonPos.top;
    otherButton.setData( RWT.CUSTOM_VARIANT, "perspective" ); //$NON-NLS-1$
    otherButton.setText( Messages.get().PerspectiveSwitcherBuilder_Other );
    IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
    final IWorkbenchAction perspectiveAction
      = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create( activeWindow );
    otherButton.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent e ) {
        perspectiveAction.run();
      }
    } );

  }

  /*
   * redesign the buttons
   */
  private void cleanButtons( Button current ) {
    for( int i = 0; i < buttonList.size(); i++ ) {
      Button button = ( Button ) buttonList.get( i );
      Composite parent = button.getParent();
      Control[] children = parent.getChildren();
      if( !button.equals( current ) ) {
        for( int j = 0; j < children.length; j++ ) {
          if( children[ j ] instanceof Label ) {
            children[ j ].setVisible( false );
          }
        }
        parent.setBackgroundImage( bg );
        button.setData( RWT.CUSTOM_VARIANT, "perspective" ); //$NON-NLS-1$
      } else {
        for( int j = 0; j < children.length; j++ ) {
          children[ j ].setVisible( true );
        }
        parent.setBackgroundImage( bgActive );
        button.setData( RWT.CUSTOM_VARIANT, "perspectiveActive" ); //$NON-NLS-1$
      }
      parent.layout( true );
    }
  }

  private void cleanUpButton( IPerspectiveDescriptor perspective, Button button ) {
    buttonList.remove( button );
    perspectiveButtonMap.remove( perspective );
    buttonPerspectiveMap.remove( button );
    button.getParent().dispose();
    background.layout( true );
    Control[] children = { background };
    Composite parent = getParent();
    parent.changed( children );
    parent.layout( true );
    parent.getParent().layout( true );
  }

  private void closePerspective( IPerspectiveDescriptor desc ) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
    IWorkbenchPage activePage = workbenchWindow.getActivePage();
    activePage.closePerspective( desc, true, false );
  }

  private Button createPerspectiveButton( final IPerspectiveDescriptor desc ) {

    Button result = (  Button ) perspectiveButtonMap.get( desc );
    if( result == null && desc != null && desc.getLabel() != null ) {
      Composite buttonBg = new Composite( background, SWT.NONE );
      buttonBg.setBackground( Graphics.getColor( 247, 247, 247 ) );
      buttonBg.setLayout( new FormLayout() );

      Label leftBg = new Label( buttonBg, SWT.NONE );
      leftBg.setImage( left );
      FormData fdLeftBg = new FormData();
      leftBg.setLayoutData( fdLeftBg );
      fdLeftBg.top = new FormAttachment( 0 );
      fdLeftBg.left = new FormAttachment( 0 );
      fdLeftBg.height = left.getBounds().height;
      fdLeftBg.width = left.getBounds().width;

      Label rightBg = new Label( buttonBg, SWT.NONE );
      rightBg.setImage( right );
      FormData fdRightBg = new FormData();
      rightBg.setLayoutData( fdRightBg );
      fdRightBg.top = new FormAttachment( 0 );
      fdRightBg.height = right.getBounds().height;
      fdRightBg.width = right.getBounds().width;

      final Button perspButton = new Button( buttonBg, SWT.PUSH | SWT.FLAT );
      perspButton.setData( RWT.CUSTOM_VARIANT, "perspective" ); //$NON-NLS-1$
      FormData fdButton = new FormData();
      perspButton.setLayoutData( fdButton );
      fdButton.left = new FormAttachment( leftBg );
      FormData buttonPos
        = getLayoutSet().getPosition( ILayoutSetConstants.PERSP_BUTTON_POS );
      fdButton.top = buttonPos.top;
      fdRightBg.left = new FormAttachment( perspButton );

      perspButton.setText( desc.getLabel() );

      perspectiveButtonMap.put( desc, perspButton );
      buttonPerspectiveMap.put( perspButton, desc );
      buttonList.add( perspButton );

      perspButton.addSelectionListener( new SelectionAdapter() {
        @Override
        public void widgetSelected( SelectionEvent e ) {
          cleanButtons( perspButton );
          switchPerspective( desc.getId() );
        }
      } );

      Menu menu = new Menu( perspButton );
      MenuItem item = new MenuItem( menu, SWT.PUSH );
      item.setText( Messages.get().PerspectiveSwitcherBuilder_Close );
      item.setImage( getImage( ILayoutSetConstants.PERSP_CLOSE ) );
      item.addSelectionListener( new SelectionAdapter() {
        @Override
        public void widgetSelected( SelectionEvent e ) {
          removeIdFromStore( desc.getId() );
          Button button = ( Button ) perspectiveButtonMap.get( desc);
          if( button != null ) {
            cleanUpButton( desc, button );
          }
          closePerspective( desc );
          background.layout();
        }
      } );
      perspButton.setMenu( menu );
      if( otherButton != null ) {
        otherButton.getParent().moveBelow( perspButton.getParent() );
      }
      result = perspButton;
    }

    return result;
  }

  private IEclipsePreferences createSessionScope() {
    return new SessionScope().getNode( RAP_PERSPECTIVES );
  }

  @Override
  public void dispose() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
    workbenchWindow.removePerspectiveListener( perspectiveAdapter );
    Composite parent = getParent();
    if( parent != null && !parent.isDisposed() ) {
      parent.dispose();
    }

  }

  @Override
  public Control getControl() {
    return background;
  }

  @Override
  public Point getSize() {
    return null;
  }

  private IPerspectiveDescriptor[] load() {
    Preferences store = createSessionScope();
    String[] keys;
    IPerspectiveDescriptor[] result = null;

    IWorkbench workbench = PlatformUI.getWorkbench();
    IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();

    try {
      keys = store.keys();
      result = new IPerspectiveDescriptor[ keys.length ];
      for( int i = 0; i < keys.length; i++ ) {
        String perspectiveId = keys[ i ];

        int pos = store.getInt( perspectiveId, 0 );
        if( pos <= perspectiveList.size() ) {
          perspectiveList.add( pos, perspectiveId );
        } else {
          perspectiveList.add( perspectiveId );
        }
      }
      for( int i = 0; i < perspectiveList.size(); i++ ) {
        String id = ( String ) perspectiveList.get( i );
        result[ i ] = registry.findPerspectiveWithId( id );
      }

    } catch( BackingStoreException e ) {
      e.printStackTrace();
    }
    return result;
  }

  private void removeIdFromStore( String id ) {
    perspectiveList.remove( id );
    Preferences store = createSessionScope();
    store.remove( id );
    save();
  }

  private void save() {
    Preferences store = createSessionScope();
    try {
      store.clear();
      for( int i = 0; i < perspectiveList.size(); i++ ) {
        String id = ( String ) perspectiveList.get( i );
        store.putInt( id, i );
      }
      store.flush();
    } catch( BackingStoreException e ) {
      e.printStackTrace();
    }
  }

  private void switchPerspective( String perspectiveId) {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
    try {
      workbench.showPerspective( perspectiveId, window );
    } catch( WorkbenchException e ) {
      e.printStackTrace();
    }
  }

}
