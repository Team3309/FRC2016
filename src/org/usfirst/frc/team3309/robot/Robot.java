package org.usfirst.frc.team3309.robot;

import org.team3309.lib.controllers.generic.PIDController;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.CustomAuto;
import org.usfirst.frc.team3309.auto.TimedOutException;
import org.usfirst.frc.team3309.auto.modes.NoMoveAuto;
import org.usfirst.frc.team3309.auto.modes.TwoBallAutoFromSpy;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.driverstation.XboxController;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.Intake;
import org.usfirst.frc.team3309.subsystems.Shooter;
import org.usfirst.frc.team3309.vision.Vision;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	XboxController driverController = Controls.driverController;
	XboxController operatorController = Controls.operatorController;

	private SendableChooser mainAutoChooser = new SendableChooser();
	private SendableChooser defenseAutoChooser = new SendableChooser();
	private SendableChooser startingPositionAutoChooser = new SendableChooser();

	//private CANTalon test = new CANTalon(0);
	//private PIDPositionController pidController = new PIDPositionController(.00001, 0, 0);

	// Runs when Robot is turned on
	public void robotInit() {
		Sensors.navX = new AHRS(SerialPort.Port.kMXP);
		// Set up new Autos in sendable Chooser
		mainAutoChooser.addDefault("No Move", new NoMoveAuto());
		mainAutoChooser.addObject("Two Ball From Spy", new TwoBallAutoFromSpy());
		mainAutoChooser.addObject("Custom Auto", new CustomAuto());
		SmartDashboard.putData("Auto Modes", mainAutoChooser);
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
																	// autos
																	// have
																	// special
																	// privileges
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
		// System.out.println("JSON ARRAYS: " +
		// Vision.getInstance().getGoals());
		/*double encoderIn360 = ((double) test.getPulseWidthPosition()) * (360.0 / 4096.0);
		double posTest = test.getPulseWidthPosition();
		SmartDashboard.putNumber("Abs Pos (scaled): ", encoderIn360);
		SmartDashboard.putNumber("POSITION:", posTest);
		InputState state = new InputState();
		state.setError(270 - encoderIn360);
		double toMotor = pidController.getOutputSignal(state).getMotor();
		SmartDashboard.putNumber("POWER", toMotor);
		pidController.sendToSmartDash();
		this.test.set(toMotor);*/

		// System.out.println("ANALONG: " + test.getAnalogInPosition());
		// Update the subsystems
		Drive.getInstance().update();
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
