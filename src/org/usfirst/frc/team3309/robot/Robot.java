package org.usfirst.frc.team3309.robot;

import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.CustomAuto;
import org.usfirst.frc.team3309.auto.modes.HighSpeedCrossAutoIntakeDownMode;
import org.usfirst.frc.team3309.auto.modes.HighSpeedCrossAutoIntakeUpMode;
import org.usfirst.frc.team3309.auto.modes.LowBarAutoMode;
import org.usfirst.frc.team3309.auto.modes.LowBarShootAutoMode;
import org.usfirst.frc.team3309.auto.modes.LowSpeedCrossAutoIntakeUp;
import org.usfirst.frc.team3309.auto.modes.NoMoveAuto;
import org.usfirst.frc.team3309.auto.modes.TurnInPlaceLAAutoMode;
import org.usfirst.frc.team3309.auto.modes.TurnToAngleAutoMode;
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
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.Intake;
import org.usfirst.frc.team3309.subsystems.Shooter;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	XboxController driverController = Controls.driverController;
	XboxController operatorController = Controls.operatorController;

	private SendableChooser mainAutoChooser = new SendableChooser();
	private SendableChooser defenseAutoChooser = new SendableChooser();
	private SendableChooser startingPositionAutoChooser = new SendableChooser();
	private PowerDistributionPanel pdp = new PowerDistributionPanel();

	// Runs when Robot is turned on
	public void robotInit() {
		System.out.println("INNIT");
		Sensors.init();
		pdp = new PowerDistributionPanel();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		System.out.println("ONCE");
		// Set up new Autos in Sendable Chooser
		mainAutoChooser.addDefault("No Move", new NoMoveAuto());
		mainAutoChooser.addObject("Two Ball From Spy", new TwoBallAutoFromSpy());
		mainAutoChooser.addObject("Custom Auto", new CustomAuto());
		mainAutoChooser.addObject("Angle", new TurnToAngleAutoMode());
		mainAutoChooser.addObject("Low Bar", new LowBarAutoMode());
		mainAutoChooser.addObject("Low Bar Shoot", new LowBarShootAutoMode());
		mainAutoChooser.addObject("Go Forward Intake Up", new HighSpeedCrossAutoIntakeUpMode());
		mainAutoChooser.addObject("Go LOW inteke Up", new LowSpeedCrossAutoIntakeUp());
		mainAutoChooser.addObject("Turn in Place", new TurnInPlaceLAAutoMode());
		mainAutoChooser.addObject("Go Forward Intake Down", new HighSpeedCrossAutoIntakeDownMode());
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
		Intake.getInstance();
		Shooter.getInstance();
		Drive.getInstance();
		Vision.getInstance().start();
	}

	// When first put into disabled mode
	public void disabledInit() {
		Vision.getInstance().setLight(0);
		Drive.getInstance().setHighGear(false);
	}

	// Called repeatedly in disabled mode
	public void disabledPeriodic() {
	}

	// Init to Auto
	public void autonomousInit() {
		Sensors.resetDrive();
		Vision.getInstance().setLight(.2);
		// Find out what to run based off of mainAutoChooser and act accordingly
		if (mainAutoChooser.getSelected() instanceof CustomAuto) { // Custom
			CustomAuto auto = (CustomAuto) mainAutoChooser.getSelected();
			auto.setDefense((Operation) defenseAutoChooser.getSelected());
			auto.setStartingPosition((int) startingPositionAutoChooser.getSelected());
			(new Thread(auto)).start();
		} else {
			(new Thread((AutoRoutine) mainAutoChooser.getSelected())).start();
		}
		Drive.getInstance().initAuto();
		Shooter.getInstance().initAuto();
		Intake.getInstance().initAuto();
	}

	// This function is called periodically during autonomous
	public void autonomousPeriodic() {
		Drive.getInstance().updateAuto();
		Drive.getInstance().sendToSmartDash();
		Shooter.getInstance().updateAuto();
		Shooter.getInstance().sendToSmartDash();
		Intake.getInstance().updateAuto();
		Intake.getInstance().sendToSmartDash();
	}

	// Init to Tele
	public void teleopInit() {
		Drive.getInstance().initTeleop();
		Shooter.getInstance().initTeleop();
		Intake.getInstance().initTeleop();
		Vision.getInstance().setLight(0);
		Compressor compressor = new Compressor();
		compressor.setClosedLoopControl(true);
		compressor.start();
	}

	// This function is called periodically during operator control
	public void teleopPeriodic() {

		if (Vision.getInstance().getGoals().size() > 0)
			System.out.println("Y: " + Vision.getInstance().getGoals().get(0).y);
		// Update the subsystems
		Drive.getInstance().updateTeleop();
		Drive.getInstance().sendToSmartDash();
		Shooter.getInstance().updateTeleop();
		Shooter.getInstance().sendToSmartDash();
		Intake.getInstance().updateTeleop();
		Intake.getInstance().sendToSmartDash();
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
