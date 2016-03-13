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

	// PWM x = new PWM(0);
	private SendableChooser mainAutoChooser = new SendableChooser();
	private SendableChooser defenseAutoChooser = new SendableChooser();
	private SendableChooser startingPositionAutoChooser = new SendableChooser();
	private PowerDistributionPanel pdp;

	// private GearTooth test = new GearTooth(0);
	// private PIDPositionController pidController = new
	// PIDPositionController(.00001, 0, 0);

	// Runs when Robot is turned on
	public void robotInit() {
		System.out.println("INNIT");
		Sensors.init();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		System.out.println("ONCE");
		// Set up new Autos in sendable Chooser
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
		SmartDashboard.putNumber("ANGLE I AM TURNING ( ADDED TO OTHER)", 0);
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

	}

	// This function is called periodically during autonomous
	public void autonomousPeriodic() {
		// System.out.println("AUTO PERIODIC");
		// Sensors.printNavX();
		// if (Vision.getInstance().getGoals().size() > 0)
		// System.out.println("JSON ARRAYS: " +
		// Vision.getInstance().getGoals());

		Drive.getInstance().update();
		Drive.getInstance().sendToSmartDash();
		Shooter.getInstance().update();
		Shooter.getInstance().sendToSmartDash();
		Intake.getInstance().update();
		Intake.getInstance().sendToSmartDash();
	}

	// Init to Tele
	public void teleopInit() {
		Drive.getInstance().toTeleop();
		Shooter.getInstance().mFeedyWheel.autoAssigned = false;

		Vision.getInstance().setLight(0);
		Compressor compressor = new Compressor();
		compressor.setClosedLoopControl(true);
		compressor.start();
		pdp = new PowerDistributionPanel();

	}

	// This function is called periodically during operator control
	public void teleopPeriodic() {

		if (Vision.getInstance().getGoals().size() > 0)
			System.out.println("Y: " + Vision.getInstance().getGoals().get(0).y);

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
		// Update the subsystems
		// UPDATES
		System.out.println("RUNNING");
		Drive.getInstance().update();
		System.out.println("Drie");
		Drive.getInstance().sendToSmartDash();
		Shooter.getInstance().update();
		System.out.println("shhote");
		Shooter.getInstance().sendToSmartDash();
		Intake.getInstance().update();
		System.out.println("Intaek");
		Intake.getInstance().sendToSmartDash();
		/*
		 * if (DriverStation.getInstance().getMatchTime() < 1)
		 * Drive.getInstance().setHighGear(false);
		 */

		// SmartDashboard.putNumber("0", pdp.getCurrent(0));

		// SmartDashboard.putNumber("1", pdp.getCurrent(1));
		/*
		 * SmartDashboard.putNumbe r("2", pdp.getCurrent(2));
		 * SmartDashboard.putNumber("3", pdp.getCurrent(3));
		 * SmartDashboard.putNumber("4", pdp.getCurrent(4));
		 * SmartDashboard.putNumber("5", pdp.getCurrent(5));
		 * SmartDashboard.putNumber("6", pdp.getCurrent(6));
		 * SmartDashboard.putNumber("7", pdp.getCurrent(7));
		 * SmartDashboard.putNumber("8", pdp.getCurrent(8));
		 * SmartDashboard.putNumber("9", pdp.getCurrent(9));
		 * SmartDashboard.putNumber("10", pdp.getCurrent(10));
		 * SmartDashboard.putNumber("11", pdp.getCurrent(11));
		 * SmartDashboard.putNumber("12", pdp.getCurrent(12));
		 * SmartDashboard.putNumber("13", pdp.getCurrent(13));
		 * SmartDashboard.putNumber("14", pdp.getCurrent(14));
		 * SmartDashboard.putNumber("15", pdp.getCurrent(15));
		 */

		// MANUALS
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
