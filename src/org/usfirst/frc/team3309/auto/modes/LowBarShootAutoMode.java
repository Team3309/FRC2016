package org.usfirst.frc.team3309.auto.modes;

import org.team3309.lib.controllers.generic.BlankController;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.subsystems.Drive;

public class LowBarShootAutoMode extends AutoRoutine {

	@Override
	public void routine() throws TimedOutException, InterruptedException {
		LowBarAutoMode x = new LowBarAutoMode();
		x.start();
		
	}

}
