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
package org.eclipse.rap.internal.design.example.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.internal.provisional.action.CoolBarManager2;
import org.eclipse.jface.internal.provisional.action.IToolBarContributionItem;
import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.internal.design.example.builder.CoolbarLayerBuilder;
import org.eclipse.rap.internal.design.example.builder.DummyBuilder;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.ui.interactiondesign.layout.ElementBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.menus.CommandContributionItem;


public class CoolBarManager extends CoolBarManager2 {

  private static final String HEADER_TOOLBAR_VARIANT = "header-toolbar"; //$NON-NLS-1$
  private static final String HEADER_OVERFLOW_VARIANT = "header-overflow"; //$NON-NLS-1$
  private static final String ACTIVE = "toolbarOverflowActive"; //$NON-NLS-1$
  private static final String INACTIVE = "toolbarOverflowInactive"; //$NON-NLS-1$
  private static final int WAVE_SPACING = 20;

  private Composite overflowParent;
  private Image preservedWave;
  private ToolBar toolbar;
  private final List overflowItems = new ArrayList();
  private Button overflowOpenButton;
  private Button overflowCloseButton;
  private Image newWave;
  private Composite overflowLayer;
  private final ElementBuilder dummyBuilder;
  private ToolBar overflowToolbar;
  private ScrolledComposite overflowToolbarParent;

  private final FocusListener focusListener = new FocusAdapter() {
    @Override
    public void focusLost( FocusEvent event ) {
      // close the overflow if the toolbar focus is lost
      closeOverflow();
      toggleImages();
    }
  };

  /*
   * Class for accessing a pull down item's menu to set a custom variant.
   */
  private class StylingSelectionAdapter extends SelectionAdapter {
    private final String variant;

    public StylingSelectionAdapter( final String variant ) {
      this.variant = variant;
    }

    private void styleMenuItems( final Menu menu ) {
      MenuItem[] items = menu.getItems();
      if( items != null && items.length > 0  && variant != null ) {
        for( int i = 0; i < items.length; i++ ) {
          items[ i ].setData( RWT.CUSTOM_VARIANT, variant );
        }
      }
    }

    @Override
    public void widgetSelected( final SelectionEvent e ) {
      Widget widget = e.widget;
      if( widget != null
          && widget instanceof ToolItem
          && !widget.isDisposed() )
      {
        if( widget.getData( RWT.CUSTOM_VARIANT ) != null ) {
          IContributionItem item = ( IContributionItem ) widget.getData();
          if( item instanceof CommandContributionItem ) {
            CommandContributionItem commandItem
              = ( CommandContributionItem ) item;
            MenuManager manager = commandItem.getMenuManager();
            if( manager != null ) {
              Menu menu = manager.getMenu();
              if( menu != null ) {
                menu.setData( RWT.CUSTOM_VARIANT, variant );
                styleMenuItems( menu );
              }
            }
          } else if( item instanceof ActionContributionItem ) {
            ActionContributionItem actionItem = ( ActionContributionItem ) item;
            IAction action = actionItem.getAction();
            IMenuCreator menuCreator = action.getMenuCreator();
            if( menuCreator != null ) {
              Menu menu = menuCreator.getMenu( toolbar );
              if( menu != null ) {
                menu.setData( RWT.CUSTOM_VARIANT, variant );
                styleMenuItems( menu );
              }
            }
          }
        }
      }
    }
  }

  public CoolBarManager() {
    dummyBuilder
      = new DummyBuilder( null, ILayoutSetConstants.SET_ID_COOLBAR );
  }

  @Override
  public Control createControl2( final Composite parent ) {
    toolbar = new ToolBar( parent, SWT.NONE );
    toolbar.setData( RWT.CUSTOM_VARIANT, HEADER_TOOLBAR_VARIANT );
    toolbar.getParent().getParent().addControlListener( new ControlAdapter() {
      @Override
      public void controlResized( final ControlEvent e ) {
        // close the overflow and update the ToolBar if the browser has resized
        closeOverflow();
        update( true );
      }
    } );
    return toolbar;
  }

  @Override
  public Control getControl2() {
    return toolbar;
  }

  @Override
  public void update( final boolean force ) {
    if( ( isDirty() || force ) && getControl2() != null ) {
      refresh();
      boolean changed = false;

      /*
       * Make a list of items including only those items that are
       * visible. Separators are being removed. Because we use only one Toolbar
       * all ToolBarContributionItems will be extracted in their IContribution
       * Items.
       */
      final IContributionItem[] items = getItems();
      final List visibleItems = new ArrayList( items.length );
      for( int i = 0; i < items.length; i++ ) {
        final IContributionItem item = items[i];
        if( item.isVisible() ) {
          if( item instanceof IToolBarContributionItem ) {
            IToolBarContributionItem toolbarItem
              = ( IToolBarContributionItem ) item;
            IToolBarManager toolBarManager = toolbarItem.getToolBarManager();
            IContributionItem[] toolbarItems = toolBarManager.getItems();
            for( int j = 0; j < toolbarItems.length; j++ ) {
              final IContributionItem toolItem = toolbarItems[ j ];
              if( toolItem.isVisible() && !toolItem.isSeparator() ) {
                visibleItems.add( toolItem );
              }
            }
          }
        }
      }

      /*
       * Make a list of ToolItem widgets in the tool bar for which there
       * is no current visible contribution item. These are the widgets
       * to be disposed. Dynamic items are also removed.
       */
      ToolItem[] toolItems = toolbar.getItems();
      final ArrayList toolItemsToRemove = new ArrayList( toolItems.length );
      for( int i = 0; i < toolItems.length; i++ ) {
          final Object data = toolItems[i].getData();
          if( ( data == null )
                  || ( !visibleItems.contains( data ) )
                  || ( ( data instanceof IContributionItem )
                      && ( ( IContributionItem ) data ).isDynamic() ) ) {
              toolItemsToRemove.add( toolItems[i] );
          }
      }

      // Dispose of any items in the list to be removed.
      for( int i = toolItemsToRemove.size() - 1; i >= 0; i-- ) {
        ToolItem toolItem = ( ToolItem ) toolItemsToRemove.get(i);
        if( !toolItem.isDisposed() ) {
          Control control = toolItem.getControl();
          if( control != null ) {
            toolItem.setControl( null );
            control.dispose();
          }
          toolItem.dispose();
        }
      }

      // Add any new items by telling them to fill.
      toolItems = toolbar.getItems();
      IContributionItem sourceItem;
      IContributionItem destinationItem;
      int sourceIndex = 0;
      int destinationIndex = 0;
      final Iterator visibleItemItr = visibleItems.iterator();
      while( visibleItemItr.hasNext() ) {
        sourceItem = ( IContributionItem ) visibleItemItr.next();

        // Retrieve the corresponding contribution item from SWT's
        // data.
        if( sourceIndex < toolItems.length ) {
          destinationItem
            = ( IContributionItem ) toolItems[ sourceIndex ].getData();
        } else {
          destinationItem = null;
        }

        // The items match if they are equal or both separators.
        if( destinationItem != null ) {
          if( sourceItem.equals( destinationItem ) ) {
            sourceIndex++;
            destinationIndex++;
            sourceItem.update();
            continue;
          } else if( ( destinationItem.isSeparator() )
                  && ( sourceItem.isSeparator() ) ) {
            toolItems[ sourceIndex ].setData( sourceItem );
            sourceIndex++;
            destinationIndex++;
            sourceItem.update();
            continue;
          }
        }

        // Otherwise, a new item has to be added.
        final int start = toolbar.getItemCount();
        sourceItem.fill( toolbar, destinationIndex );
        final int newItems = toolbar.getItemCount() - start;
        // add the selection listener for the styling
        StylingSelectionAdapter listener
          = new StylingSelectionAdapter( HEADER_TOOLBAR_VARIANT );
        for( int i = 0; i < newItems; i++ ) {
          ToolItem item = toolbar.getItem( destinationIndex++ );
          item.setData( sourceItem );
          item.addSelectionListener( listener );
        }
        changed = true;
      }

      // Remove any old widgets not accounted for.
      for( int i = toolItems.length - 1; i >= sourceIndex; i-- ) {
        final ToolItem item = toolItems[ i ];
        if( !item.isDisposed() ) {
          Control control = item.getControl();
          if( control != null ) {
            item.setControl( null );
            control.dispose();
          }
          item.dispose();
          changed = true;
        }
      }

      // Update wrap indices. only needed by a coolbar
      //updateWrapIndices();

      // Update the sizes.
      for( int i = 0; i < items.length; i++ ) {
        IContributionItem item = items[ i ];
        item.update( SIZE );
      }

      if (changed) {
          updateToolbarTabOrder();
      }

      // We are no longer dirty.
      setDirty( false );
      styleToolItems();
      toolbar.pack();
      toolbar.layout( true, true );
      manageOverflow();
    }
  }

  /*
   * This method manages the items which can not be shown in the coolbar because
   * it is to small. So an overflow will be shown including these items.
   */
  private void manageOverflow() {
    int coolbarWidth = toolbar.getParent().getSize().x - WAVE_SPACING;
    int childrenLength = toolbar.getItemCount() - 1;
    overflowItems.clear();
    for( int i = childrenLength; i >= 0; i-- ) {
      int childrenSize = getChildrenSize( toolbar );
      if( childrenSize > coolbarWidth ) {
        ToolItem toolItem = toolbar.getItem( i );
        IContributionItem item = ( IContributionItem ) toolItem.getData();
        addOverflowItem( item );
        activateOverflowOpenButton();
        Control control = toolItem.getControl();
        toolItem.setControl( null );
        if( control != null ) {
          control.dispose();
        }
        toolItem.dispose();
      }
    }
    // check if the overflow button should be activated or not
    checkOverflowActivation();
  }

  private void checkOverflowActivation() {
    // If every item has a representation in the toolbar, the overflow button
    // should be invisible
    if( overflowItems.size() > 0 ) {
      activateOverflowOpenButton();
    } else {
      deactivateOverflowButton();
    }
  }

  private void addOverflowItem( final IContributionItem item ) {
    // add the contrib item to the overflow items if it's not allready in
    if( !overflowItems.contains( item ) ) {
      overflowItems.add( item );
    }
  }

  private void deactivateOverflowButton() {
    if( overflowOpenButton != null ) {
      overflowOpenButton.setVisible( false );
    }
  }

  /*
   * This method calculates the size of all children of the coolbar. This is
   * necessary to compare the correct sizes for the overflow.
   */
  private int getChildrenSize( final ToolBar toolbar ) {
    int result = 0;
    FormData spacing
      = dummyBuilder.getPosition( ILayoutSetConstants.COOLBAR_SPACING );
    if( spacing != null ) {
      ToolItem[] items = toolbar.getItems();
      for( int i = 0; i < items.length; i++ ) {

        result += items[ i ].getWidth() + spacing.width;
      }
    }
    return result;
  }

  /*
   * Creates and activates the overflow button
   */
  private void activateOverflowOpenButton() {
    if( overflowParent != null && overflowOpenButton == null ) {
      overflowOpenButton = new Button( overflowParent, SWT.PUSH );
      overflowOpenButton.setData( RWT.CUSTOM_VARIANT, INACTIVE );
      overflowOpenButton.setLayoutData( getOverflowButtonLayoutData() );

      overflowOpenButton.addSelectionListener( new SelectionAdapter() {
        @Override
        public void widgetSelected( final SelectionEvent e ) {
          // open the overflow and toggle the chefron icon
          createOverflowLayer();
          toggleImages();
        }
      } );
    }
    overflowOpenButton.setVisible( true );
    // create the close button
    if( overflowCloseButton == null ) {
      overflowCloseButton = new Button( overflowParent, SWT.PUSH );
      overflowCloseButton.setData( RWT.CUSTOM_VARIANT, ACTIVE );
      overflowCloseButton.setLayoutData( getOverflowButtonLayoutData() );
      overflowCloseButton.addSelectionListener( new SelectionAdapter() {
        @Override
        public void widgetSelected( final SelectionEvent e ) {
          closeOverflow();
          toggleImages();
        };
      } );
    }
    overflowCloseButton.setVisible( false );
  }

  /*
   * Change the images, this includes the chefron icon and the wave image
   */
  private void toggleImages() {
    Image wave = null;
    if( overflowOpenButton.isVisible() ) {
      // The button was inactive so active it
      overflowOpenButton.setVisible( false );
      overflowCloseButton.setVisible( true );
      wave = newWave;
      overflowLayer.getParent().setVisible( true );
      overflowLayer.setFocus();
    } else {
      overflowCloseButton.setVisible( false );
      overflowOpenButton.setVisible( true );
      overflowLayer.getParent().setVisible( false );
      wave = preservedWave;
    }
    overflowParent.setBackgroundImage( wave );
  }

  private FormData getOverflowButtonLayoutData() {
    String imageId = ILayoutSetConstants.COOLBAR_OVERFLOW_ACTIVE;
    Image image = dummyBuilder.getImage( imageId );
    FormData fdOverFlowButton
      = dummyBuilder.getPosition( ILayoutSetConstants.COOLBAR_BUTTON_POS );
    if( image != null ) {
      fdOverFlowButton.width = image.getBounds().width;
      fdOverFlowButton.height = image.getBounds().height;
    }
    return fdOverFlowButton;
  }

  private void createOverflowLayer() {
    ElementBuilder layerBuilder
    = new CoolbarLayerBuilder( overflowParent.getParent(),
                               ILayoutSetConstants.SET_ID_OVERFLOW );
    if( overflowLayer == null ) {
      layerBuilder.build();
      overflowLayer = ( Composite ) layerBuilder.getControl();
      overflowLayer.addFocusListener( focusListener );
      newWave = layerBuilder.getImage( ILayoutSetConstants.OVERFLOW_WAVE );
    }

    Object adapter = layerBuilder.getAdapter( CoolBarManager.class );
    if( adapter != null ) {
      // position the layer
      FormData fdLayer = ( FormData ) overflowLayer.getParent().getLayoutData();
      Display display = overflowLayer.getDisplay();
      Point location = display.map( overflowOpenButton, null, 20, 0 );
      fdLayer.left = new FormAttachment( 0, location.x);
      fdLayer.top = new FormAttachment( 0, 37 );
      overflowParent.getParent().getParent().layout( true );
    } else {
      FormData fdParent = ( FormData ) overflowParent.getLayoutData();
      FormData fdLayer = ( FormData ) overflowLayer.getParent().getLayoutData();
      fdLayer.left = fdParent.left;
    }

    // fill the vertical overflow toolbar with the overflow items
    fillOverflowToolbar();

    overflowParent.getParent().layout( true );
    overflowLayer.getParent().moveAbove( null );
    overflowLayer.getParent().moveBelow( overflowParent );
  }

  private void closeOverflow( ) {
    if( overflowLayer != null && preservedWave != null ) {
      boolean opened = overflowLayer.getParent().isVisible();
      if( opened ) {
        overflowLayer.getParent().setVisible( false );
        overflowParent.setBackgroundImage( preservedWave );
        overflowOpenButton.setData( RWT.CUSTOM_VARIANT, INACTIVE );
        clearOverflowToolbar();
      }
    }
  }

  /*
   * Dispose all Items in the overflow
   */
  private void clearOverflowToolbar() {
    if( overflowToolbar != null ) {
      ToolItem[] items = overflowToolbar.getItems();
      for( int i = 0; i < items.length; i++ ) {
        ToolItem toolItem = items[ i ];
        if( toolItem != null && !toolItem.isDisposed() ) {
          toolItem.setData( null );
          toolItem.dispose();
        }
      }
    }
  }

  /*
   * Take all overflow items and fill the vertical overflow toolbar.
   */
  private void fillOverflowToolbar() {
    if( overflowToolbar == null ) {
      // scrolled toolbar parent
      overflowToolbarParent
        = new ScrolledComposite( overflowLayer, SWT.V_SCROLL );
      DummyBuilder builder
        = new DummyBuilder( null, ILayoutSetConstants.SET_ID_OVERFLOW );
      FormData pos = builder.getPosition( ILayoutSetConstants.OVERFLOW_POS );
      overflowToolbarParent.setLayoutData( pos );
      // parent for the toolbar
      Composite parent = new Composite( overflowToolbarParent, SWT.NONE );
      parent.setLayout( new FillLayout() );
      // toolbar
      overflowToolbar = new ToolBar( parent, SWT.VERTICAL );
      overflowToolbar.setBackgroundMode( SWT.INHERIT_FORCE );
      overflowToolbar.setData( RWT.CUSTOM_VARIANT,
                               HEADER_OVERFLOW_VARIANT );
      overflowLayer.getParent().addFocusListener( focusListener );
      // configure the ScrolledComposite
      overflowToolbarParent.setContent( parent );
      overflowToolbarParent.setExpandVertical( true );
      overflowToolbarParent.setExpandHorizontal( true );
      overflowToolbarParent.setOrigin( 0, 0 );
      overflowToolbarParent.setAlwaysShowScrollBars( false );
    }
    // clear the old overflow if items exist
    clearOverflowToolbar();
    // fill the toolbar
    int maxWidth = 0;
    for( int i = 0; i < overflowItems.size(); i++ ) {
      IContributionItem item = ( IContributionItem ) overflowItems.get( i );
      item.fill( overflowToolbar, i );
      final ToolItem toolItem = overflowToolbar.getItem( i );
      // add a selection listener for the styling
      StylingSelectionAdapter listener
        = new StylingSelectionAdapter( HEADER_OVERFLOW_VARIANT );
      toolItem.addSelectionListener( listener );
      toolItem.setData( RWT.CUSTOM_VARIANT, HEADER_OVERFLOW_VARIANT );
      if( toolItem.getWidth() > maxWidth ) {
        maxWidth = toolItem.getWidth();
      }
    }
    // layout the controls
    overflowLayer.getParent().layout( true, true );
    overflowLayer.getParent().pack( true );
    overflowToolbarParent.setMinSize( maxWidth, overflowItems.size() * 25 );
    // bring the scroll position back to it's origin every time the overflow
    // has opened
    overflowToolbarParent.setOrigin( 0, 0 );
    overflowToolbarParent.layout();
    overflowToolbarParent.setFocus();
  }

  private void styleToolItems() {
    if( toolbar != null ) {
      ToolItem[] items = toolbar.getItems();
      for( int i = 0; i < items.length; i++ ) {
        ToolItem toolItem = items[ i ];
        final IContributionItem item = ( IContributionItem ) toolItem.getData();
        if( toolItem.getText() == "" ) { //$NON-NLS-1$
          modifyModeForceText( item );
        }
        toolItem.setData( RWT.CUSTOM_VARIANT, HEADER_TOOLBAR_VARIANT );
      }
    }
  }


  /*
   * This method changes the modes from ActionContributionItems and
   * CommandContributionItems to display the text within a ToolItem.
   */
  private void modifyModeForceText( final IContributionItem item ) {
    if( item instanceof ActionContributionItem ) {
      ActionContributionItem actionItem = ( ActionContributionItem ) item;
      actionItem.setMode( ActionContributionItem.MODE_FORCE_TEXT );
    } else if( item instanceof CommandContributionItem ) {
      CommandContributionItem commandItem = ( CommandContributionItem ) item;
      commandItem.setMode( CommandContributionItem.MODE_FORCE_TEXT );
    }
  }

  private void updateToolbarTabOrder() {
    if( toolbar != null ) {
      ToolItem[] items = toolbar.getItems();
      if( items != null ) {
        ArrayList children = new ArrayList( items.length );
        for( int i = 0; i < items.length; i++ ) {
          if( ( items[ i ].getControl() != null )
             && ( !items[ i ].getControl().isDisposed() ) )
          {
            children.add( items[ i ].getControl() );
          }
        }
        // Convert array
        Control[] childrenArray = new Control[ 0 ];
        childrenArray = ( Control[] ) children.toArray( childrenArray );
        if( childrenArray != null ) {
          toolbar.setTabList( childrenArray );
        }
      }
    }
  }

  /*
   * Method to set the parent for the overflow. This method is called within
   * the WindowComposers.
   */
  public void setOverflowParent( final Composite overflowParent ) {
    this.overflowParent = overflowParent;
    preservedWave = overflowParent.getBackgroundImage();
  }

}
