package org.usfirst.frc.team3309.robot;

import org.usfirst.frc.team3309.driverstation.Controls;
import org.usfirst.frc.team3309.driverstation.XboxController;
import org.usfirst.frc.team3309.subsystems.Shooter;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	XboxController driverController = Controls.driverController;
	XboxController operatorController = Controls.operatorController;

	// Runs when Robot is turned on
	public void robotInit() {
		Sensors.navX = new AHRS(SerialPort.Port.kMXP);	
		
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
		// Sensors.shooterCounter.
	}

	// This function is called periodically during operator control
	public void teleopPeriodic() {
		Compressor c = new Compressor(0);
  
		// Update the subsystems
		// Drive.getInstance().update();
		Shooter.getInstance().update();
	}
}
