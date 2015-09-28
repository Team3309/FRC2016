package org.team3309.lib.controllers.generic;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * Controller that returns only zero values. Used when a subsystem is disabled,
 * but still requires a controller.
 * 
 * @author TheMkrage
 * 
 */
public class BlankController extends Controller {

	/*
	 * (non-Javadoc)
	 * @see org.team3309.lib.controllers.Controller#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc) 
	 * @see org.team3309.lib.controllers.Controller#getOutputSignal(org.team3309.lib.controllers.statesandsignals.InputState)
	 */
	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		return new OutputSignal();
	}

	/*
	 * (non-Javadoc)
	 * @see org.team3309.lib.controllers.Controller#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return false; // Never ends
	}
}
