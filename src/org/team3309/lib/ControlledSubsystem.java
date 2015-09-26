package org.team3309.lib;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.BlankController;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class ControlledSubsystem extends KragerSubsystem {
	protected Controller mController;
	public ControlledSubsystem(String name) {
		super(name);
		mController = new BlankController();
	}

	public abstract void update();
}
