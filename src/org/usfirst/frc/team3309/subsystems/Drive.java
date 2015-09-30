package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Drive Train
 * 
 * @author TheMkrage
 *
 */
public class Drive extends ControlledSubsystem {
	private static Drive instance;

	public static Drive getInstance() {
		if (instance == null)
			instance = new Drive("Drive");
		return instance;
	}

	private Drive(String name) {
		super(name);
	}

	// Sets controller based on what state the remotes and game are in
	private void updateController() {

	}

	@Override
	public void update() {
		updateController();
		OutputSignal output = mController.getOutputSignal(getInputState());
	}

	@Override
	public InputState getInputState() {
		InputState input = new InputState();
		input.setAngularPos(0);
		input.setAngularVel(0);
		input.setLeftPos(0);
		input.setLeftVel(0);
		input.setRightPos(0);
		input.setRightVel(0);
		return input;
	}

}
