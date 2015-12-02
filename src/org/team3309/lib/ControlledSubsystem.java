package org.team3309.lib;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.BlankController;
import org.team3309.lib.controllers.statesandsignals.InputState;

/**
 * Subsystem that contains a Controller
 * 
 * @author TheMkrage
 * 
 */
public abstract class ControlledSubsystem extends KragerSubsystem {
	/**
	 * Controller of Subsystem
	 */
	protected Controller mController;

	public ControlledSubsystem(String name) {
		super(name);
		mController = new BlankController();
	}

	/*
	 * @see org.team3309.lib.KragerSubsystem#update()
	 */
	public abstract void update();

	/**
	 * returns Input State
	 * 
	 * @return The current state the subsystem is in, this is then sent to the
	 *         Controller object.
	 */
	public abstract InputState getInputState();

	public void setController(Controller mController) {
		this.mController = mController;
	}
	
	public boolean isOnTarget() {
		return mController.isCompleted();
	}

	public abstract void sendToSmartDash();
}
