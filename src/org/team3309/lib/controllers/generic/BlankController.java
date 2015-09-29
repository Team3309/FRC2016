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

	@Override
	public void reset() {
		
	}

	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		return new OutputSignal(); // Returns only zeros for everything
	}

	@Override
	public boolean isCompleted() {
		return false; // Never ends
	}
}
