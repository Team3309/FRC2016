package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;

import edu.wpi.first.wpilibj.Solenoid;

public class Climber extends ControlledSubsystem {
	private static Climber instance;

	private Solenoid pivot = new Solenoid(1);
	private Solenoid leftCarriage = new Solenoid(2);
	private Solenoid rightCarriage = new Solenoid(3);

	/**
	 * Singleton Pattern
	 * 
	 * @return the single instance
	 */
	public static Climber getInstance() {
		if (instance == null)
			instance = new Climber("Climber");
		return instance;
	}

	private Climber(String name) {
		super(name);
	}

	@Override
	public void initTeleop() {

	}

	@Override
	public void initAuto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTeleop() {
		System.out.println(Controls.operatorController.getPOV());
		if (Controls.operatorController.getPOV() == 0) {
			pivot.set(true);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rightCarriage.set(true);
			leftCarriage.set(true);
		}
	}

	@Override
	public void updateAuto() {

	}

	@Override
	public void sendToSmartDash() {

	}

	@Override
	public void manualControl() {

	}

	@Override
	public InputState getInputState() {
		// TODO Auto-generated method stub
		return null;
	}
}