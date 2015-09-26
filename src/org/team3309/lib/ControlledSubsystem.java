package org.team3309.lib;

import org.usfirst.frc.team3309.subsystems.controllers.Controller;
import org.usfirst.frc.team3309.subsystems.controllers.generic.BlankController;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class ControlledSubsystem extends KragerSubsystem {
	protected Controller mController;
	public ControlledSubsystem(String name) {
		super(name);
		mController = new BlankController(1);
	}

	public abstract void update();
}
