package org.team3309.lib.controllers;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * The basis of any Controller. A Controller can be made to calculate values
 * that can be then applied in a ControllerSubsystem.
 * 
 * @author TheMkrage
 * 
 */
public abstract class Controller {
	/**
	 * Tells if Controller is enabled or not.
	 */
	public boolean mEnabled = false;

	/**
	 * Resets the Controller. For example, reseting the integral term back to
	 * zero in a PID Loop
	 */
	public abstract void reset();

	public abstract OutputSignal getOutputSignal(InputState inputState);

	public abstract boolean isCompleted();
}
