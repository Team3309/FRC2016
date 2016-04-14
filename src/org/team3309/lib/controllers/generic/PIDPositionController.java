package org.team3309.lib.controllers.generic;

import org.team3309.lib.ControlledSubsystem;

/**
 * Used to maintain a position of an error. Returns just the direct PID Value.
 * 
 * @author TheMkrage
 *
 */
public class PIDPositionController extends PIDController {
	public PIDPositionController(ControlledSubsystem system, boolean hasSeparateThread, double kP, double kI, double kD) {
		super(system, hasSeparateThread, kP, kI, kD);
	}

	public PIDPositionController(ControlledSubsystem system, boolean hasSeparateThread, double kP, double kI, double kD, double kILimit) {
		super(system, hasSeparateThread, kP, kI, kD, kILimit);
	}
	
	public PIDPositionController(ControlledSubsystem system, double kP, double kI, double kD) {
		super(system, kP, kI, kD);
	}

	public PIDPositionController(ControlledSubsystem system, double kP, double kI, double kD, double kILimit) {
		super(system, kP, kI, kD, kILimit);
	}
}
