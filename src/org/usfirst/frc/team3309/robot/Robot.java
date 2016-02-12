package org.usfirst.frc.team3309.robot;

import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.CustomAuto;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.modes.NoMoveAuto;
import org.usfirst.frc.team3309.auto.modes.TwoBallAutoFromSpy;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossChevelDeFrise;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossDrawBridge;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossLowBar;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossMoat;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossPortcullis;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossRamparts;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossRockWall;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossRoughTerrain;
import org.usfirst.frc.team3309.auto.operations.defenses.CrossSallyPort;
import org.usfirst.frc.team3309.auto.operations.defenses.Operation;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.driverstation.XboxController;
import org.usfirst.frc.team3309.subsystems.Shooter;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	XboxController driverController = Controls.driverController;
	XboxController operatorController = Controls.operatorController;

	PWM x = new PWM(0);
	private SendableChooser mainAutoChooser = new SendableChooser();
	private SendableChooser defenseAutoChooser = new SendableChooser();
	private SendableChooser startingPositionAutoChooser = new SendableChooser();

	// private GearTooth test = new GearTooth(0);
	// private PIDPositionController pidController = new
	// PIDPositionController(.00001, 0, 0);

	// Runs when Robot is turned on
	public void robotInit() {
		Sensors.navX = new AHRS(SerialPort.Port.kMXP);
		// Set up new Autos in sendable Chooser
		mainAutoChooser.addDefault("No Move", new NoMoveAuto());
		mainAutoChooser.addObject("Two Ball From Spy", new TwoBallAutoFromSpy());
		mainAutoChooser.addObject("Custom Auto", new CustomAuto());
		SmartDashboard.putData("Auto", mainAutoChooser);
		startingPositionAutoChooser.addDefault("1", 1);
		startingPositionAutoChooser.addObject("2", 2);
		startingPositionAutoChooser.addObject("3", 3);
		startingPositionAutoChooser.addObject("4", 4);
		startingPositionAutoChooser.addObject("5", 5);
		SmartDashboard.putData("Starting Position", startingPositionAutoChooser);
		defenseAutoChooser.addDefault("Low Bar", new CrossLowBar());
		defenseAutoChooser.addObject("Cheval De Frise", new CrossChevelDeFrise());
		defenseAutoChooser.addObject("Draw Bridge", new CrossDrawBridge());
		defenseAutoChooser.addObject("Moat", new CrossMoat());
		defenseAutoChooser.addObject("Portcullis", new CrossPortcullis());
		defenseAutoChooser.addObject("Ramparts", new CrossRamparts());
		defenseAutoChooser.addObject("Rock Wall", new CrossRockWall());
		defenseAutoChooser.addObject("Rough Terrain", new CrossRoughTerrain());
		defenseAutoChooser.addObject("Sally Port", new CrossSallyPort());
		SmartDashboard.putData("Defense", defenseAutoChooser);
	}

	// When first put into disabled mode
	public void disabledInit() {
	}

	// Called repeatedly in disabled mode
	public void disabledPeriodic() {
	}

	// Init to Auto
	public void autonomousInit() {
		// Find out what to run based off of mainAutoChooser and act accordingly
		if (mainAutoChooser.getSelected() instanceof CustomAuto) { // Custom
			CustomAuto auto = (CustomAuto) mainAutoChooser.getSelected();
			auto.setDefense((Operation) defenseAutoChooser.getSelected());
			auto.setStartingPosition((int) startingPositionAutoChooser.getSelected());
			try {
				auto.start();
			} catch (TimedOutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				((AutoRoutine) mainAutoChooser.getSelected()).start();
			} catch (TimedOutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// This function is called periodically during autonomous
	public void autonomousPeriodic() {
	}

	// Init to Tele
	public void teleopInit() {
		// Vision.getInstance().start();

		// Sensors.shooterCounter.
	}

	// This function is called periodically during operator control
	public void teleopPeriodic() {
		//x.
		// System.out.println("JSON ARRAYS: " +
		// Vision.getInstance().getGoals());
		/*
		 * double encoderIn360 = ((double) test.getPulseWidthPosition()) *
		 * (360.0 / 4096.0); double posTest = test.getPulseWidthPosition();
		 * SmartDashboard.putNumber("Abs Pos (scaled): ", encoderIn360);
		 * SmartDashboard.putNumber("POSITION:", posTest); InputState state =
		 * new InputState(); state.setError(270 - encoderIn360); double toMotor
		 * = pidController.getOutputSignal(state).getMotor();
		 * SmartDashboard.putNumber("POWER", toMotor);
		 * pidController.sendToSmartDash(); this.test.set(toMotor);
		 */

		// System.out.println("ANALONG: " + test.getAnalogInPosition());
		// Update the subsystems
		// Drive.getInstance().update();
		Shooter.getInstance().update();
		// Intake.getInstance().update();
		// Shooter.getInstance().update();
		// Shooter.getInstance().sendToSmartDash();
		Sensors.printNavX();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
