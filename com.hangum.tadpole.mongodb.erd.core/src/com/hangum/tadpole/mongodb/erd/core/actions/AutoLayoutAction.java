/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.erd.core.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.erd.core.Messages;
import com.hangum.tadpole.mongodb.erd.core.part.RelationEditPart;
import com.hangum.tadpole.mongodb.erd.core.part.TableEditPart;
import com.hangum.tadpole.mongodb.erd.stanalone.Activator;
import com.hangum.tadpole.mongodb.model.Relation;
import com.hangum.tadpole.mongodb.model.Table;

/**
 * autolayout action
 * 
 * @author hangum
 *
 */
public class AutoLayoutAction extends SelectionAction {
	public static final String ID = "com.hangum.tadpole.mongodb.erd.core.actions.AutoLayoutAction"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(AutoLayoutAction.class);
	private GraphicalViewer viewer;

	public AutoLayoutAction(IWorkbenchPart part, GraphicalViewer graphicalViewer) {
		super(part);
		setLazyEnablementCalculation(false);
		
		this.viewer = graphicalViewer;
	}

	@Override
	protected void init() {
		setText(Messages.get().AutoLayoutAction_0);
		setToolTipText(Messages.get().AutoLayoutAction_0);
		setId(ID);
		setEnabled(true);
	}
	
	@Override
	protected boolean calculateEnabled() {
		return true;
	}
	
	public GraphicalViewer getViewer() {
		return viewer;
	}
	
	@Override
	public void run() {
		try {
			CompoundCommand commands = new CompoundCommand();
			List models = getViewer().getContents().getChildren();
			NodeList graphNodes = new NodeList();
			EdgeList graphEdges = new EdgeList();
			
			// nodes
			for(int i=0;i<models.size();i++){
				Object obj = models.get(i);
				if(obj instanceof TableEditPart){
					TableEditPart editPart = (TableEditPart) obj;
					Table model = (Table) editPart.getModel();
					EntityNode node = new EntityNode();
					node.model = model;
					node.width = editPart.getFigure().getSize().width;
					node.height = editPart.getFigure().getSize().height + 40;
					graphNodes.add(node);
				}
			}
			
			
			// edge
			for (int i = 0; i < models.size(); i++) {
				Object obj = models.get(i);
				if(obj instanceof TableEditPart){
					TableEditPart tableEditpart = (TableEditPart) obj;
					
					List outgoing = tableEditpart.getSourceConnections();
					for (int j = 0; j < outgoing.size(); j++) {
						RelationEditPart conn = (RelationEditPart) outgoing.get(j);
						EntityNode source = (EntityNode) getNode(graphNodes, (Table)conn.getSource().getModel());
						EntityNode target = (EntityNode) getNode(graphNodes, (Table)conn.getTarget().getModel());
						
						if(source != null && target != null){
							ConnectionEdge edge = new ConnectionEdge(source, target);
							Relation relation = (Relation)conn.getModel();
							edge.model = relation.getSource();
							graphEdges.add(edge);
						}
					}
				}
			}
	
			DirectedGraph graph = new DirectedGraph();
			graph.nodes = graphNodes;
			graph.edges = graphEdges;
			new DirectedGraphLayout().visit(graph);
			for (int i = 0; i < graph.nodes.size(); i++) {
				EntityNode node = (EntityNode) graph.nodes.getNode(i);
				commands.add(new LayoutCommand(node.model, node.x, node.y));
			}
	
			getViewer().getEditDomain().getCommandStack().execute(commands);
		} catch(Exception e) {
			logger.error(Messages.get().AutoLayoutAction_2, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.get().Error, Messages.get().AutoLayoutAction_2, errStatus); //$NON-NLS-1$

		}
	}
	
	private static EntityNode getNode(NodeList list, Table model){
		for(int i=0;i<list.size();i++){
			EntityNode node = (EntityNode) list.get(i);
			if(node.model == model){
				return node;
			}
		}
		return null;
	}
	
	private class EntityNode extends Node {
		private Table model;
	}
//
	private class ConnectionEdge extends Edge {
		private Table model;
		public ConnectionEdge(EntityNode source, EntityNode target){
			super(source, target);
		}
	}

	/**
	 * Command to relocate the entity model.
	 * This command is executed as a part of CompoundCommand.
	 */
	private class LayoutCommand extends Command {

		private Table target;
		private int x;
		private int y;
		private int oldX;
		private int oldY;

		public LayoutCommand(Table target, int x, int y){
			this.target = target;
			this.x = x;
			this.y = y;
			this.oldX = target.getConstraints().x;
			this.oldY = target.getConstraints().y;
		}

		public void execute() {
			this.target.setConstraints(new Rectangle(this.x, this.y, -1, -1));
		}

		public void undo() {
			this.target.setConstraints(new Rectangle(this.oldX, this.oldY, -1, -1));
		}
	}

}
