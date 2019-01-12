package frc.robot.util;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Wraps Limelight functionality into a more easy-to-use class.
 * 
 * @author Finn Frankis
 * @since 1/12/19
 */
public class Limelight {
    public static final String LIMELIGHT_TABLE_KEY = "limelight";
    public static final String TX_KEY = "tx";
    public static final String TY_KEY = "ty";
    public static final String TZ_KEY = "tz";

    public static double getTx() {
        return NetworkTableInstance.getDefault().getTable(LIMELIGHT_TABLE_KEY).getEntry(TX_KEY).getDouble(0.0);
    }

    public static double getTy() {
        return NetworkTableInstance.getDefault().getTable(LIMELIGHT_TABLE_KEY).getEntry(TY_KEY).getDouble(0.0);
    }

    public static double getTz() {
        return NetworkTableInstance.getDefault().getTable(LIMELIGHT_TABLE_KEY).getEntry(TZ_KEY).getDouble(0.0);
    }
}