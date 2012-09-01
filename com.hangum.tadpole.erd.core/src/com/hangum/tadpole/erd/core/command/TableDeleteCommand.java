/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.erd.core.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import com.hangum.tadpole.model.DB;
import com.hangum.tadpole.model.Relation;
import com.hangum.tadpole.model.RelationKind;
import com.hangum.tadpole.model.Table;

/**
 * Table model delete command
 * 
 * @author hangum
 *
 */
public class TableDeleteCommand extends Command {
	private DB db;
	private Table table;

	// relation delete
	private List<Relation> relations;
	private Map<Relation, Table> relationSource;
	private Map<Relation, RelationKind> relationKindSource;
	private Map<Relation, Table> relationTarget;
	private Map<Relation, RelationKind> relationKindTarget;
	
	@Override
	public void execute() {
		detachRelatin();
		table.setDb(null);
	}
	
	/**
	 * relation detach
	 */
	private void detachRelatin() {
		relations = new ArrayList<Relation>();
		relationSource = new HashMap<Relation, Table>();
		relationKindSource = new HashMap<Relation, RelationKind>();
		relationTarget = new HashMap<Relation, Table>();
		relationKindTarget = new HashMap<Relation, RelationKind>();
		
		relations.addAll(table.getIncomingLinks());
		relations.addAll(table.getOutgoingLinks());
		for (Relation relation : relations) {
			relationSource.put(relation, relation.getSource());
			relationKindSource.put(relation, relation.getSource_kind());
			relationTarget.put(relation, relation.getTarget());
			relationKindTarget.put(relation, relation.getTarget_kind());
			
			relation.setSource(null);
			relation.setSource_kind(null);
			relation.setTarget(null);
			relation.setTarget_kind(null);
		}

	}

	@Override
	public void undo() {
		reattachRelation();
		table.setDb(db);
	}
	
	/**
	 * relation reattach
	 */
	private void reattachRelation() {
		for (Relation relation : relations) {
			relation.setSource(relationSource.get(relation));
			relation.setSource_kind(relationKindSource.get(relation));
			relation.setTarget(relationTarget.get(relation));
			relation.setTarget_kind(relationKindTarget.get(relation));
		}
	}

	public void setTable(Table table) {
		this.table = table;
		this.db = table.getDb();
	}
}
