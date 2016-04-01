package org.usfirst.frc.team3309.robot;

public class Constants {
	public static boolean IS_HOOD_SENSOR_REVERSED_PRACTICE = true;
	public static boolean IS_HOOD_SENSOR_REVERSED_COMP = true;
	public static double HOOD_BOTTOM_VALUE_COMP = -84;
	public static double HOOD_BOTTOM_VALUE_PRACTICE = -165.0;
	public static double PIVOT_TOP_VALUE_COMP = 0;//
	public static double PIVOT_TOP_VALUE_PRACTICE = -102;

	public static boolean isComp = true;

	public static double valueForCompOrPractice(double compValue, double practiceValue) {
		if (isComp) {
			return compValue;
		} else {
			return practiceValue;
		}
	}

	public static boolean valueForCompOrPractice(boolean compValue, boolean practiceValue) {
		if (isComp) {
			return compValue;
		} else {
			return practiceValue;
		}
	}

	public static boolean isHoodSensorReversed() {
		return valueForCompOrPractice(IS_HOOD_SENSOR_REVERSED_COMP, IS_HOOD_SENSOR_REVERSED_PRACTICE);
	}

	public static double getHoodBottomValue() {
		return valueForCompOrPractice(HOOD_BOTTOM_VALUE_COMP, HOOD_BOTTOM_VALUE_PRACTICE);
	}

	public static double getPivotTopValue() {
		return valueForCompOrPractice(PIVOT_TOP_VALUE_COMP, PIVOT_TOP_VALUE_PRACTICE);
	}
}
