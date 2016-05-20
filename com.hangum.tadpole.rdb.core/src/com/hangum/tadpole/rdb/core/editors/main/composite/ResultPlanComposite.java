/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     billy.goo - add dialog to view detail record
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.plandetail.GeneralPlanComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.PlanTailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.swtdesigner.ResourceManager;

/**
 * plan 을 보여주기 위한 
 * 
 */
public class ResultPlanComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultPlanComposite.class);
	
	private ResultMainComposite resultMainComposite;
	private Button btnAddVertical;
	
	private SashForm sashFormResult;
	private GeneralPlanComposite compositeQueryPlan;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ResultPlanComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		
		Composite compHead = new Composite(this, SWT.NONE);
		compHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(7, false);
		gl_composite.verticalSpacing = 2;
		gl_composite.horizontalSpacing = 2;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 2;
		compHead.setLayout(gl_composite);
		
		btnAddVertical = new Button(compHead, SWT.NONE);
		btnAddVertical.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(SWT.VERTICAL == sashFormResult.getOrientation()) {
					sashFormResult.setOrientation(SWT.HORIZONTAL);	
					btnAddVertical.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/layouts_split_vertical.png"));										
				} else {
					sashFormResult.setOrientation(SWT.VERTICAL);
					btnAddVertical.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/layouts_split_horizontal.png"));
				}
				
				layout();
			}
		});
		btnAddVertical.setToolTipText(Messages.get().ChangeRotation);
		btnAddVertical.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/layouts_split_horizontal.png"));
		
		sashFormResult = new SashForm(this, SWT.HORIZONTAL);
		sashFormResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		compositeQueryPlan = new GeneralPlanComposite(sashFormResult, SWT.BORDER);
		compositeQueryPlan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 2;
		gl_compositeResult.horizontalSpacing = 2;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 2;
		compositeQueryPlan.setLayout(gl_compositeResult);
	}

	public void setRDBResultComposite(ResultMainComposite resultMainComposite) {
		this.resultMainComposite = resultMainComposite;
	}

	public void setQueryPlanData(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
	
		if(!compositeQueryPlan.getCompositeTail().getBtnPinSelection()) {
			compositeQueryPlan.setQueryPlanData(reqQuery, rsDAO);
		} else {
			compositeQueryPlan = new GeneralPlanComposite(sashFormResult, SWT.BORDER);
			compositeQueryPlan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
			GridLayout gl_compositeResult = new GridLayout(1, false);
			gl_compositeResult.verticalSpacing = 2;
			gl_compositeResult.horizontalSpacing = 2;
			gl_compositeResult.marginHeight = 0;
			gl_compositeResult.marginWidth = 2;
			compositeQueryPlan.setLayout(gl_compositeResult);
			
			compositeQueryPlan.setQueryPlanData(reqQuery, rsDAO);
		}
		
		resultSashLayout();
	}
	
	/**
	 * refresh sash layout form
	 * 
	 */
	private void resultSashLayout() {
		Map<Integer, Integer> mapWidths = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mapHeight = new HashMap<Integer, Integer>();
//		int intParentWidth = sashFormResult.getBounds().width;
//		int intParentHeight = sashFormResult.getBounds().height;
		int intTmpCount = 0;
		
		try {
			List<GeneralPlanComposite> listDisComp = new ArrayList<>();
			Control[] childControls = sashFormResult.getChildren();
			for (int i=0; i<childControls.length; i++) {
				Control control = childControls[i];
				if(control instanceof GeneralPlanComposite) {
					GeneralPlanComposite resultComposite = (GeneralPlanComposite)control;
					PlanTailComposite tailComposite = resultComposite.getCompositeTail();
					if(!tailComposite.getBtnPinSelection()) {
						listDisComp.add(resultComposite);
					} else {
						mapWidths.put(intTmpCount, resultComposite.getBounds().width);
						mapHeight.put(intTmpCount, resultComposite.getBounds().height);
						intTmpCount++;
					}
				}
			}
			
			// 삭제한다.
			int intDispCount = listDisComp.size()-1;
			for(int i=0; i<intDispCount; i++) {
				listDisComp.get(i).dispose();
			}
			
			int weights[] = new int[mapWidths.size()+1];
			if(mapWidths.size() != 0) {
				for (int i=0; i<mapWidths.size(); i++) {
					float intCompositeWeights = 0f;
					if(sashFormResult.getOrientation() == SWT.HORIZONTAL) {
						intCompositeWeights = mapWidths.get(i) * 100;
					} else {
						intCompositeWeights = mapHeight.get(i) * 100;
					}
					weights[i] = (int)intCompositeWeights;
					intTmpCount += weights[i];
					// 처음 위젯이 생성 되었을 경우무조건 100이므로 반만 위젲을 준다. 
					if(weights[i] == 100) {
						weights[i] = 50;
						intTmpCount = 50;
					// 100 이 넘어가면 마지막 위젲에서 30로 만큼 위젲을 차지한다.
					} else if(intTmpCount >= 100) { 
						weights[i] = 30;
					}
				}
				weights[mapWidths.size()] = 100 - intTmpCount;
			} else {
				weights[0] = 100;
			}
			sashFormResult.setWeights(weights);
		} catch(Exception e) {
			logger.error("calc weights of result composite");
		}
		sashFormResult.layout();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
