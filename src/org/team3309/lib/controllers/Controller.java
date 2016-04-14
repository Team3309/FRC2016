package org.team3309.lib.controllers;

import org.team3309.lib.ControlledSubsystem;
import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * The basis of any Controller. A Controller can be made to calculate values
 * that can be then applied in a ControllerSubsystem.
 * 
 * @author TheMkrage
 * 
 */
public abstract class Controller implements Runnable {

	private String name = "Default";
	protected OutputSignal lastOutputState = new OutputSignal();
	private final double LOOP_TIME = 10;
	private ControlledSubsystem subsystem;
	private boolean hasOwnThread = false;

	public Controller(ControlledSubsystem subsystem, boolean hasOwnThread) {
		this.hasOwnThread = hasOwnThread;
		this.subsystem = subsystem;
		if (hasOwnThread)
			this.start();
	}

	public Controller(ControlledSubsystem subsystem) {
		this.subsystem = subsystem;
		if (hasOwnThread)
			this.start();
	}

	/**
	 * Resets the Controller. For example, reseting the integral term back to
	 * zero in a PID Loop
	 */
	public abstract void reset();

	/**
	 * Should be ran one time each loop, tells the subsystem what to do based
	 * off of the controller.
	 * 
	 * @return The signal sent to the ControlledSubsystem
	 */
	public OutputSignal getOutputSignal() {
		return lastOutputState;
	}

	/**
	 * 
	 * @param inputState
	 *            The state of the ControlledSubsystem
	 */
	public abstract void update(InputState inputState);

	/**
	 * Tells if the controller is done executed its specified task.
	 * 
	 * @return boolean telling if controller is done or not
	 */
	public abstract boolean isCompleted();

	/**
	 * Sends info of controller to smartdash for looks on data and tuning
	 */
	public void sendToSmartDash() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void print(String print) {
		System.out.println(this.getName() + " " + print);
	}

	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
			double startTime = System.currentTimeMillis();
			if (DriverStation.getInstance().isEnabled()) {
				update(subsystem.getInputState());
			}
			// Loop Speed
			double changeInTime = System.currentTimeMillis();
			double timeItTook = changeInTime - startTime;
			long overhead = (long) (LOOP_TIME - (timeItTook));
			 System.out.println(this.getName() + " IT TOOK: " + timeItTook);
			try {
				if (overhead > 2) {
					// System.out.println("Time for Loop: " + overhead);
					KragerTimer.delayMS(overhead);
				} else {
					KragerTimer.delayMS(5);
					System.out.println(subsystem.getName() + " Loop Speed too fast!!! " + overhead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
