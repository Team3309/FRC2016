package org.team3309.lib.controllers.drive;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;
import org.usfirst.frc.team3309.robot.Sensors;

public class DriveWhileOnADefenseController extends Controller {

	private final double THRESHOLD = 8;
	private Controller controllerToRun;
	private boolean hasBeganGoingOverDefense = false;
	private boolean hasGoneOverDefense = false;
	private double countsOfAfter = 0;

	public DriveWhileOnADefenseController(ControlledSubsystem system, Controller x) {
		super(system);
		this.controllerToRun = x;
	}

	@Override
	public void reset() {

	}

	@Override
	public OutputSignal getOutputSignal() {
		return super.getOutputSignal();
	}

	@Override
	public void update(InputState inputState) {
		// IF roll is greater than thresh, start keeping track
		if (Math.abs(Sensors.getRoll()) > THRESHOLD) {
			if (hasBeganGoingOverDefense) {
				// Do nothing
			} else {
				hasBeganGoingOverDefense = true;
			}
		} else {
			if (hasBeganGoingOverDefense) {
				countsOfAfter++;
			}
		}

		if (countsOfAfter > 12) {
			hasGoneOverDefense = true;
		}
		controllerToRun.update(inputState);
		this.lastOutputState = controllerToRun.getOutputSignal();
	}

	@Override
	public boolean isCompleted() {
		return hasGoneOverDefense;
	}

}
