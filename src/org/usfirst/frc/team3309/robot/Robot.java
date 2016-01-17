package org.usfirst.frc.team3309.robot;

import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.driverstation.XboxController;
import org.usfirst.frc.team3309.subsystems.Shooter;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	XboxController driverController = Controls.driverController;
	XboxController operatorController = Controls.operatorController;

	// Runs when Robot is turned on
	public void robotInit() {
		//Sensors.navX = new AHRS(I2C.Port.kMXP);
	}

	// When first put into disabled mode
	public void disabledInit() {
	}

	// Called repeatedly in disabled mode
	public void disabledPeriodic() {
	}

	// Init to Auto
	public void autonomousInit() {

	}

	// This function is called periodically during autonomous
	public void autonomousPeriodic() {
	}

	// Init to Tele
	public void teleopInit() {
		//Sensors.shooterCounter.
	}

	// This function is called periodically during operator control
	public void teleopPeriodic() {
		Compressor c = new Compressor(0);
		
		//System.out.println("Sensors: " + Sensors.shooterCounter.get());
		/* Display 6-axis Processed Angle Data                                      */
        /*SmartDashboard.putBoolean(  "IMU_Connected",        Sensors.navX.isConnected());
        SmartDashboard.putBoolean(  "IMU_IsCalibrating",    Sensors.navX.isCalibrating());
        SmartDashboard.putNumber(   "IMU_Yaw",              Sensors.navX.getYaw());
        SmartDashboard.putNumber(   "IMU_Pitch",            Sensors.navX.getPitch());
        SmartDashboard.putNumber(   "IMU_Roll",             Sensors.navX.getRoll());
		// Update the subsystems*/
		//Drive.getInstance().update();
		Shooter.getInstance().update();
	}
}
