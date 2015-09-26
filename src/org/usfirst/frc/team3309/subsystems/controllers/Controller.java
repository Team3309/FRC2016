package org.usfirst.frc.team3309.subsystems.controllers;

import org.usfirst.frc.team3309.subsystems.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.subsystems.controllers.statesandsignals.OutputSignal;

public abstract class Controller {
	public boolean mEnabled = false;
	public abstract OutputSignal getOutputState(InputState inputState);
	public abstract boolean isCompleted();
}
