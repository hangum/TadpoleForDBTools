/*******************************************************************************
 * Copyright (c) 2008, 2012 EclipseSource and others.
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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class ViewToolBarManager extends ToolBarManager2 {

  private static final String STYLING_VARIANT = "viewToolbar"; //$NON-NLS-1$
  private ItemData[] itemsData;

  @Override
  public ToolBar createControl( final Composite parent ) {
    ToolBar toolBar = getControl();
    if( !toolBarExist() && parent != null ) {
      toolBar = super.createControl( parent );
      toolBar.setData( RWT.CUSTOM_VARIANT, STYLING_VARIANT );
    }
    return toolBar;
  }

  @Override
  public void update( final boolean force ) {
    if( isDirty() || force ) {
      if( toolBarExist() ) {
        ToolBar toolBar = getControl();
        // clean contains all active items without double separators
        IContributionItem[] items = getItems();
        ArrayList clean = new ArrayList(items.length);
        IContributionItem separator = null;
        for( int i = 0; i < items.length; ++i ) {
          IContributionItem ci = items[ i ];
          if( ci.isSeparator() ) {
            separator = ci;
          } else {
            if( separator != null ) {
              if( clean.size() > 0 ) {
                clean.add( separator );
              }
              separator = null;
            }
            clean.add( ci );
          }
        }
        // determine obsolete items (removed or non active)
        ToolItem[] mi = toolBar.getItems();
        ArrayList toRemove = new ArrayList( mi.length );
        for( int i = 0; i < mi.length; i++ ) {
          // there may be null items in a toolbar
          if( mi[ i ] == null ) {
            continue;
          }
          Object data = mi[ i ].getData();
          if( data == null
              || !clean.contains( data )
              || ( data instanceof IContributionItem && ( ( IContributionItem ) data)
                  .isDynamic() ) ) {
            toRemove.add( mi[ i ] );
          }
        }
        // Turn redraw off if the number of items to be added
        // is above a certain threshold, to minimize flicker,
        // otherwise the toolbar can be seen to redraw after each item.
        // Do this before any modifications are made.
        // We assume each contribution item will contribute at least one
        // toolbar item.
        boolean useRedraw = clean.size() - ( mi.length - toRemove.size() ) >= 3;
        try {
          if( useRedraw ) {
            toolBar.setRedraw( false );
          }
          // remove obsolete items
          for (int i = toRemove.size(); --i >= 0;) {
            ToolItem item = ( ToolItem )toRemove.get( i );
            if( !item.isDisposed() ) {
              Control ctrl = item.getControl();
              if( ctrl != null ) {
                item.setControl( null );
                ctrl.dispose();
              }
              item.dispose();
            }
          }
          // add new items
          IContributionItem src, dest;
          mi = toolBar.getItems();
          int srcIx = 0;
          int destIx = 0;
          for( Iterator e = clean.iterator(); e.hasNext(); ) {
            src = ( IContributionItem )e.next();
            // get corresponding item in SWT widget
            if( srcIx < mi.length ) {
              dest = ( IContributionItem )mi[ srcIx ].getData();
            } else {
              dest = null;
            }
            if( dest != null && src.equals( dest ) ) {
              srcIx++;
              destIx++;
              continue;
            }
            if( dest != null && dest.isSeparator() && src.isSeparator() ) {
              mi[ srcIx ].setData( src );
              srcIx++;
              destIx++;
              continue;
            }
            int start = toolBar.getItemCount();
            src.fill( toolBar, destIx );
            int newItems = toolBar.getItemCount() - start;
            for( int i = 0; i < newItems; i++ ) {
              ToolItem item = toolBar.getItem( destIx++ );
              item.setData( src );
              item.setData( RWT.CUSTOM_VARIANT, STYLING_VARIANT );
            }
          }
          // remove any old tool items not accounted for
          for( int i = mi.length; --i >= srcIx; ) {
            ToolItem item = mi[ i ];
            if( !item.isDisposed() ) {
              Control ctrl = item.getControl();
              if( ctrl != null ) {
                item.setControl( null );
                ctrl.dispose();
              }
              item.dispose();
            }
          }
          setDirty(false);
          updateItemsData();
          disposeInvisibleItems();
        } finally {
          // turn redraw back on if we turned it off above
          if( useRedraw ) {
            toolBar.setRedraw( true );
          }
        }
      }
    }
  }

  public ItemData[] getItemsData() {
    return itemsData;
  }

  private boolean toolBarExist() {
    ToolBar toolBar = getControl();
    return toolBar != null && !toolBar.isDisposed();
  }

  private void updateItemsData() {
    ToolBar toolBar = getControl();
    ToolItem[] items = toolBar.getItems();
    List<ItemData> data = new ArrayList<ItemData>();
    for( int i = 0; i < items.length; i++ ) {
      IContributionItem contributionItem = ( IContributionItem )items[ i ].getData();
      if( !contributionItem.isGroupMarker() && !contributionItem.isSeparator() ) {
        ItemData itemData = new ItemData( contributionItem.getId(),
                                          items[ i ].getText(),
                                          items[ i ].getToolTipText(),
                                          items[ i ].getImage() );
        data.add( itemData );
      }
    }
    itemsData = data.toArray( new ItemData[ 0 ] );
  }

  private void disposeInvisibleItems() {
    ToolBar toolBar = getControl();
    ToolItem[] items = toolBar.getItems();
    for( int i = 0; i < items.length; i++ ) {
      ToolItem item = items[ i ];
      if( !item.isDisposed() ) {
        Object data = item.getData();
        if( data instanceof IContributionItem ) {
          IContributionItem itemData = ( IContributionItem )data;
          if( !itemData.isVisible() ) {
            Control ctrl = item.getControl();
            if( ctrl != null ) {
              item.setControl( null );
              ctrl.dispose();
            }
            item.dispose();
          }
        }
      }
    }
  }
}
