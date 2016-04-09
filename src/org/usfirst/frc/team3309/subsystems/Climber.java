package org.usfirst.frc.team3309.subsystems;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class Climber extends ControlledSubsystem {
	private static Climber instance;

	private Solenoid latches = new Solenoid(RobotMap.CLIMBER_LATCHES);
	private DoubleSolenoid leftCarriage = new DoubleSolenoid(RobotMap.CLIMBER_LEFT_UP, RobotMap.CLIMBER_LEFT_DOWN);
	private DoubleSolenoid rightCarriage = new DoubleSolenoid(RobotMap.CLIMBER_RIGHT_UP, RobotMap.CLIMBER_RIGHT_DOWN);

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
		latches.set(true);
		rightCarriage.set(Value.kReverse);
		leftCarriage.set(Value.kReverse);
		
	}

	@Override
	public void initTeleop() {
		latches.set(true);
		rightCarriage.set(Value.kReverse);
		leftCarriage.set(Value.kReverse);
	}

	@Override
	public void initAuto() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTeleop() {
		// System.out.println(Controls.operatorController.getPOV());
		if (Controls.operatorController.getPOV() == 90) { // Go Up
			latches.set(false);
			KragerTimer.delayMS(1000);
			rightCarriage.set(Value.kForward);
			leftCarriage.set(Value.kForward);
		} else if (Controls.operatorController.getPOV() == 180) { // Neutral
			latches.set(true);
			rightCarriage.set(Value.kOff);
			leftCarriage.set(Value.kOff);
		} else if (Controls.operatorController.getPOV() == 270) {
			latches.set(true);
			KragerTimer.delayMS(1000);
			rightCarriage.set(Value.kReverse);
			leftCarriage.set(Value.kReverse);
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