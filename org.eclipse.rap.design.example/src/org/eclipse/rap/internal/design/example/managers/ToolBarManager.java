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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;


public class ToolBarManager extends ToolBarManager2
{
  
  
  public ToolBarManager() {
    super();
  }

  public void addPropertyChangeListener( IPropertyChangeListener listener ) {
  }

  public ToolBar createControl( Composite parent ) {
    return super.createControl( parent );
  }

  public Control createControl2( Composite parent ) {
    return createControl( parent );
  }

  public void dispose() {
    super.dispose();
  }

  public ToolBar getControl() {
    return super.getControl();
  }

  public Control getControl2() {
    return super.getControl2();
  }

  public int getItemCount() {
    return super.getItems().length;
  }

  public void removePropertyChangeListener( IPropertyChangeListener listener ) {
  }

  public void setOverrides( IContributionManagerOverrides newOverrides ) {
    super.setOverrides( newOverrides );
  }

  public void add( IAction action ) {
    super.add( action );
  }

  public void add( IContributionItem item ) {
    super.add( item );
  }

  public void appendToGroup( String groupName, IAction action ) {
    super.appendToGroup( groupName, action );
  }

  public void appendToGroup( String groupName, IContributionItem item ) {
    super.appendToGroup( groupName, item );
  }

  public IContributionItem find( String id ) {
    return super.find( id );
  }

  public IContributionItem[] getItems() {
    return super.getItems();
  }

  public IContributionManagerOverrides getOverrides() {
    return super.getOverrides();
  }

  public void insertAfter( String id, IAction action ) {
    super.insertAfter( id, action );
  }

  public void insertAfter( String id, IContributionItem item ) {
    super.insertAfter( id, item );
  }

  public void insertBefore( String id, IAction action ) {
    super.insertBefore( id, action );
  }

  public void insertBefore( String id, IContributionItem item ) {
    super.insertBefore( id, item );
  }

  public boolean isDirty() {
    return super.isDirty();
  }

  public boolean isEmpty() {
    return super.isEmpty();
  }

  public void markDirty() {
    super.markDirty();
  }

  public void prependToGroup( String groupName, IAction action ) {
    super.prependToGroup( groupName, action );
  }

  public void prependToGroup( String groupName, IContributionItem item ) {
    super.prependToGroup( groupName, item );
  }

  public IContributionItem remove( String id ) {
    return super.remove( id );
  }

  public IContributionItem remove( IContributionItem item ) {
    return super.remove( item );
  }

  public void removeAll() {
    super.removeAll();
  }

  public void update( boolean force ) {
    super.update( force );
  }
}
