package org.team3309.lib;

/**
 * Class containing static methods of various mathmatical methods
 * 
 * @author TheMkrage
 * 
 */
public class KragerMath {

	/**
	 * returns the sin of degrees
	 * @param a how many degrees
	 * @return
	 */
	public static double sinDeg(double a) {
		return Math.sin(a * (180 / Math.PI));
	}

	/**
	 * returns the cos of degrees
	 * @param a how many degrees
	 * @return
	 */
	public static double cosDeg(double a) {
		return Math.cos(a * (180 / Math.PI));
	}

	/**
	 * returns the tan of degrees
	 * @param a how many degrees
	 * @return
	 */
	public static double tanDeg(double a) {
		return Math.tan(a * (180 / Math.PI));
	}
}
