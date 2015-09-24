package org.team3309.lib;

import org.usfirst.frc.team3309.subsystems.controllers.Controller;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class ControlledSubsystem extends KragerSubsystem {
	private Controller mController = new BlankController();
	public ControlledSubsystem(String name) {
		super(name);
	}

	public abstract void update();
}
