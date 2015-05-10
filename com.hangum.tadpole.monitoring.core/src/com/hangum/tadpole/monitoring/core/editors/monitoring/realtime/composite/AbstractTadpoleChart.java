package com.hangum.tadpole.monitoring.core.editors.monitoring.realtime.composite;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * abstract tadpole chart
 * 
 * @author hangum
 *
 */
public abstract class AbstractTadpoleChart extends Group {
	
	/** 3 minute chart label */
	protected static final String[] ThreeMinuteChartLabel = new String[] { "0", "", "20", "", "40", "", "60", "", "80", "", "100", "", "120", "", "140", "", "160", "", "180" };

	public AbstractTadpoleChart(Composite parent, int style, String strGroupTitle) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {
	}

}
