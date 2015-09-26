package org.team3309.lib.controllers;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public abstract class Controller {
	public boolean mEnabled = false;
	public abstract void reset();
	public abstract OutputSignal getOutputState(InputState inputState);
	public abstract boolean isCompleted();
}
