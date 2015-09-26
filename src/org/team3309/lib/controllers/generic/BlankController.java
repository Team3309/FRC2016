package org.team3309.lib.controllers.generic;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public class BlankController extends Controller{
	
	public BlankController() {
		
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public OutputSignal getOutputSignal(InputState inputState) {
		return new OutputSignal();
	}

	@Override
	public boolean isCompleted() {
		return false;
	}
}
