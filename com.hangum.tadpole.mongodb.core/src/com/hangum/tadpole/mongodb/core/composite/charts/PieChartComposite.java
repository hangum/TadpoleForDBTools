package com.hangum.tadpole.mongodb.core.composite.charts;

import org.eclipse.swt.widgets.Composite;

public class PieChartComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PieChartComposite(Composite parent, int style) {
		super(parent, style);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
