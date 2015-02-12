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
	protected static final String[] ThreeMinuteChartLabel = new String[] { "180", "", "160", "", "140", "", "120", "", "100", "", "80", "", "60", "", "40", "", "20", "", "0" };

	public AbstractTadpoleChart(Composite parent, int style, String strGroupTitle) {
		super(parent, style);
	}

	@Override
	protected void checkSubclass() {
	}

}
