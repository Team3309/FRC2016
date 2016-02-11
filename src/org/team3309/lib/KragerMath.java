package org.team3309.lib;

/**
 * Class containing static methods of various mathmatical methods
 * 
 * @author TheMkrage
 * 
 */
public class KragerMath {

	/**
	 * Sin of degrees
	 * @param a degrees
	 * @return
	 */
	public static double sinDeg(double a) {
		return Math.sin(a * (180 / Math.PI));
	}

	/**
	 * Cos of degrees
	 * @param a degrees
	 * @return
	 */
	public static double cosDeg(double a) {
		return Math.cos(a * (180 / Math.PI));
	}

	/**
	 * Tan of Degrees
	 * @param a degrees
	 * @return
	 */
	public static double tanDeg(double a) {
		return Math.tan(a * (180 / Math.PI));
	}
	
	public static double threshold(double input) {
		if (Math.abs(input) < .1) {
			return 0;
		}
		return input;
	}
}
