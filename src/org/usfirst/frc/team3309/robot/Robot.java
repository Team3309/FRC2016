package org.usfirst.frc.team3309.robot;

import java.util.List;

import org.team3309.lib.KragerTimer;
import org.usfirst.frc.team3309.auto.AutoRoutine;
import org.usfirst.frc.team3309.auto.CustomAuto;
import org.usfirst.frc.team3309.auto.modes.GoForwardStraightAutoMode;
import org.usfirst.frc.team3309.auto.modes.HighSpeedCrossAutoIntakeDownMode;
import org.usfirst.frc.team3309.auto.modes.HighSpeedCrossAutoIntakeUpMode;
import org.usfirst.frc.team3309.auto.modes.LowBarAutoMode;
import org.usfirst.frc.team3309.auto.modes.LowBarShootAutoMode;
import org.usfirst.frc.team3309.auto.modes.LowSpeedCrossAutoIntakeUp;
import org.usfirst.frc.team3309.auto.modes.NoMoveAuto;
import org.usfirst.frc.team3309.auto.modes.SpyBotAutoMode;
import org.usfirst.frc.team3309.auto.modes.TurnInPlaceLAAutoMode;
import org.usfirst.frc.team3309.auto.modes.TurnToAngleAutoMode;
import org.usfirst.frc.team3309.auto.modes.TwoBallAutoFromSpy;
import org.usfirst.frc.team3309.auto.modes.TwoBallAutoMode;
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
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos2ToCenter;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos2ToLeft;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos3ToCenter;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos3ToRight;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos4ToCenter;
import org.usfirst.frc.team3309.auto.operations.goalsfrompos.Pos5ToRight;
import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.driverstation.XboxController;
import org.usfirst.frc.team3309.subsystems.Climber;
import org.usfirst.frc.team3309.subsystems.Drive;
import org.usfirst.frc.team3309.subsystems.Intake;
import org.usfirst.frc.team3309.subsystems.Shooter;
import org.usfirst.frc.team3309.vision.Goal;
import org.usfirst.frc.team3309.vision.IndicatingLights;
import org.usfirst.frc.team3309.vision.Vision;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	XboxController driverController = Controls.driverController;
	XboxController operatorController = Controls.operatorController;

	private SendableChooser mainAutoChooser = new SendableChooser();
	private SendableChooser defenseAutoChooser = new SendableChooser();
	private SendableChooser startingPositionAutoChooser = new SendableChooser();

	private final double LOOP_SPEED_MS = 40;

	// Runs when Robot is turned on
	public void robotInit() {
		System.out.println("Robot INIT");
		Sensors.init();
		System.out.println("Sensors INIT");
		SmartDashboard.putNumber("ANGLE I AM TURNING ( ADDED TO OTHER)", 0);
		try {

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		// Set up new Autos in SendableChooser
		mainAutoChooser.addDefault("No Move", new NoMoveAuto());
		mainAutoChooser.addObject("Go Straight", new GoForwardStraightAutoMode());
		mainAutoChooser.addObject("Two Ball From Spy", new TwoBallAutoFromSpy());
		mainAutoChooser.addObject("Custom Auto", new CustomAuto());
		mainAutoChooser.addObject("Angle", new TurnToAngleAutoMode());
		mainAutoChooser.addObject("Low Bar", new LowBarAutoMode());
		mainAutoChooser.addObject("Low Bar Shoot", new LowBarShootAutoMode());
		mainAutoChooser.addObject("Go Forward Intake Up", new HighSpeedCrossAutoIntakeUpMode());
		mainAutoChooser.addObject("Go LOW inteke Up", new LowSpeedCrossAutoIntakeUp());
		mainAutoChooser.addObject("Turn in Place", new TurnInPlaceLAAutoMode());
		mainAutoChooser.addObject("Spy Bot Auto", new SpyBotAutoMode());
		mainAutoChooser.addObject("TWO BALL", new TwoBallAutoMode());
		mainAutoChooser.addObject("Go Forward Intake Down", new HighSpeedCrossAutoIntakeDownMode());
		SmartDashboard.putData("Auto", mainAutoChooser);
		// Add Starting Positions and Goal To Shoot In in SendableChooser
		startingPositionAutoChooser.addObject("2-Left", new Pos2ToLeft());
		startingPositionAutoChooser.addObject("2-Center", new Pos2ToCenter());
		startingPositionAutoChooser.addObject("3-Center", new Pos3ToCenter());
		startingPositionAutoChooser.addObject("3-Right", new Pos3ToRight());
		startingPositionAutoChooser.addObject("4", new Pos4ToCenter());
		startingPositionAutoChooser.addObject("5", new Pos5ToRight());
		SmartDashboard.putData("Starting Position", startingPositionAutoChooser);
		// Add Defenses in SendableChooser
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
		System.out.println("INTAKE");
		Shooter.getInstance();
		System.out.println("SHOOTER");
		Drive.getInstance();
		System.out.println("DRIVE");
		Vision.getInstance().start();
	}

	private void manageLoopSpeed() {
		// Loop Speed
		double timeItTook = time.get();
		long overhead = (long) (LOOP_SPEED_MS - (1000 * timeItTook));
		try {
			if (overhead > 5) {

				KragerTimer.delayMS(overhead);
			} else {
				KragerTimer.delayMS(5);
				System.out.println("Loop Speed too fast!!! " + overhead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// When first put into disabled mode
	public void disabledInit() {
		Vision.getInstance().setLight(0);
		Drive.getInstance().setHighGear(false);
	}

	// Called repeatedly in disabled mode
	public void disabledPeriodic() {
		Vision.getInstance().setLight(0);
		// ();
	}

	// Init to Auto
	public void autonomousInit() {
		Sensors.resetDrive();
		Vision.getInstance().setLight(Vision.getInstance().BRIGHTNESS);
		// Find out what to run based off of mainAutoChooser and act accordingly
		if (mainAutoChooser.getSelected() instanceof CustomAuto) { // Custom
			CustomAuto auto = (CustomAuto) mainAutoChooser.getSelected();
			auto.setDefense((Operation) defenseAutoChooser.getSelected());
			auto.setStartingPosition((Operation) startingPositionAutoChooser.getSelected());
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
		time.start();
		time.reset();
		Drive.getInstance().updateAuto();
		Drive.getInstance().sendToSmartDash();
		Shooter.getInstance().updateAuto();
		Shooter.getInstance().sendToSmartDash();
		Intake.getInstance().updateAuto();
		Intake.getInstance().sendToSmartDash();
		IndicatingLights.getInstance().update();

		manageLoopSpeed();
	}

	// Init to Tele
	public void teleopInit() {
		Drive.getInstance().initTeleop();
		Shooter.getInstance().initTeleop();
		Intake.getInstance().initTeleop();
		Climber.getInstance().initTeleop();
		Vision.getInstance().setLight(0);
		Compressor compressor = new Compressor();
		compressor.setClosedLoopControl(true);
		compressor.start();
		System.out.println("Teleop INIT");
	}

	Timer time = new Timer();

	// This function is called periodically during operator control
	public void teleopPeriodic() {

		time.start();
		time.reset();

		List<Goal> goals = Vision.getInstance().getGoals();
		if (goals.size() > 0) {
			// System.out.println("Y: " + goals.get(0).y);
			SmartDashboard.putNumber("Y Value", goals.get(0).y);
		}

		// Update the subsystems
		//Drive.getInstance().updateTeleop();
		Drive.getInstance().sendToSmartDash();
		Shooter.getInstance().updateTeleop();
		Shooter.getInstance().sendToSmartDash();
		Intake.getInstance().updateTeleop();
		Intake.getInstance().sendToSmartDash();
		Climber.getInstance().updateTeleop();
		IndicatingLights.getInstance().update();

		manageLoopSpeed();
	}
}
