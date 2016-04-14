package org.team3309.lib.controllers;

import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * The basis of any Controller. A Controller can be made to calculate values
 * that can be then applied in a ControllerSubsystem.
 * 
 * @author TheMkrage
 * 
 */
public abstract class Controller {

	private String name = "Default";
<<<<<<< HEAD
	protected OutputSignal lastOutputState = new OutputSignal();
	private double LOOP_TIME = 15;
	
	public double getLOOP_TIME() {
		return LOOP_TIME;
	}

	public void setLOOP_TIME(double lOOP_TIME) {
		LOOP_TIME = lOOP_TIME;
	}

	private ControlledSubsystem subsystem;
	private boolean hasOwnThread = true;
	public Thread thread;

	public Controller(ControlledSubsystem subsystem, boolean hasOwnThread) {
		this.hasOwnThread = hasOwnThread;
		this.subsystem = subsystem;
		if (hasOwnThread)
			this.start();
		this.update(subsystem.getInputState());
	}

	public Controller(ControlledSubsystem subsystem) {
		this.subsystem = subsystem;
		if (hasOwnThread)
			this.start();
		this.update(subsystem.getInputState());
	}
=======
>>>>>>> parent of 887d793... restructured for separate loops

	/**
	 * Resets the Controller. For example, reseting the integral term back to
	 * zero in a PID Loop
	 */
	public abstract void reset();

	/**
	 * Should be ran one time each loop, tells the subsystem what to do based
	 * off of the controller.
	 * 
	 * @param inputState
	 *            The state of the ControlledSubsystem
	 * @return The signal sent to the ControlledSubsystem
	 */
	public abstract OutputSignal getOutputSignal(InputState inputState);

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
<<<<<<< HEAD

	private void start() {
		try {
			System.out.println("STARTING: " + this.getName() + " of Sub " + subsystem.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		thread = new Thread(this);
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
			// System.out.println(subsystem.getName() + " IT TOOK: " +
			// timeItTook + " + " + this.getName());
			try {
				if (overhead > 0) {
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
=======
>>>>>>> parent of 887d793... restructured for separate loops
}
