package org.usfirst.frc.team3309.auto.operations.intakepivot;

import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.subsystems.intake.IntakePivot;

public class MoveIntakePivotToHigh extends Operation {

	private boolean isHigh = false;
	private double encoder = 0;

	public MoveIntakePivotToHigh(double encoder, boolean isHigh) {
		super(encoder);
		this.encoder = encoder;
		this.isHigh = isHigh;
	}

	@Override
	public void perform() throws InterruptedException, TimedOutException {
		if (!isHigh) {
			IntakePivot.getInstance().toIntakePosition();
		} else {
			IntakePivot.getInstance().toUpPosition();
		}

	}

}
