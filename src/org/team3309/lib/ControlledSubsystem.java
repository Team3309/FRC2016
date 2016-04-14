
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
public abstract class ControlledSubsystem extends KragerSystem {
	/**
	 * Controller of Subsystem For Teleop
	 */
	protected Controller teleopController;
	protected Controller autoController;

	public ControlledSubsystem(String name) {
		super(name);
<<<<<<< HEAD
		System.out.println("Controlled: " + name);
=======
		teleopController = new BlankController();
>>>>>>> parent of 887d793... restructured for separate loops
	}

	/*
	 * @see org.team3309.lib.KragerSubsystem#update()
	 */
	public abstract void updateTeleop();

	public abstract void updateAuto();

	/**
	 * returns Input State
	 * 
	 * @return The current state the subsystem is in, this is then sent to the
	 *         Controller object.
	 */
	public abstract InputState getInputState();

	public void setTeleopController(Controller mController) {
		try {
			if (this.teleopController.thread != null) {
				this.teleopController.thread.stop();
			}
		} catch (Exception e) {

		}
		this.teleopController = mController;
	}

	public void setAutoController(Controller mController) {
		try {
			if (this.autoController.thread != null)
				this.autoController.thread.stop();
		} catch (Exception e) {

		}
		this.autoController = mController;
	}

	/**
	 * Tells if the controller is where it is supposed to be
	 * 
	 * @return
	 */
	public boolean isOnTarget() {
		return teleopController.isCompleted();
	}

	/**
	 * Use this to send controllers to the smartdash for live tuning
	 */
	public abstract void sendToSmartDash();

	/**
	 * Manual Control for no sensors
	 */
	public abstract void manualControl();
}
