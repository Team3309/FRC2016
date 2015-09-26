package org.usfirst.frc.team3309.subsystems.controllers.generic;

import org.usfirst.frc.team3309.subsystems.controllers.Controller;
import org.usfirst.frc.team3309.subsystems.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.subsystems.controllers.statesandsignals.OutputSignal;

public class BlankController extends Controller{
	int NUMBER_OF_VALUES_TO_RETURN = 1;
	
	public BlankController(int numberOfValuesToReturn) {
		this.NUMBER_OF_VALUES_TO_RETURN = numberOfValuesToReturn;
	}
	
	@Override
	public OutputSignal getOutputState(InputState inputState) {
		return new OutputSignal();
	}

	@Override
	public boolean isCompleted() {
		return false;
	}

}
