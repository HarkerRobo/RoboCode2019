package frc.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Wraps Limelight functionality into a more easy-to-use class.
 * 
 * @author Finn Frankis
 * @since 1/12/19
 */
public class Limelight {
    private static Limelight instance;

    public static final String LIMELIGHT_TABLE_KEY = "limelight";
    public static final NetworkTable table = NetworkTableInstance.getDefault().getTable(LIMELIGHT_TABLE_KEY);
    
    public static final String TV_KEY = "tv";
    public static final String TX_KEY = "tx";
    public static final String TY_KEY = "ty";
    public static final String TA_KEY = "ta";
    public static final String MODE_KEY = "camMode";
    public static final String SNAP_KEY = "snapshot";


    public static final int VISION_MODE = 0;
    public static final int DRIVER_MODE = 1;

    public static final int NO_SNAPSHOT = 0;
    public static final int SNAPSHOT = 1;

    private PIDSourceType sourceType;

    /**
     * Setup Limelight with default settings
     */
    private Limelight()
    {
        table.getEntry(MODE_KEY).setNumber(VISION_MODE);
        table.getEntry(SNAP_KEY).setNumber(NO_SNAPSHOT);
    }

    public static boolean isTargetVisible() {
        return Math.abs(table.getEntry(TV_KEY).getDouble(0.0) - 1.0) < 1e-5;
    }

    public static double getTx() {
        return table.getEntry(TX_KEY).getDouble(0.0);
    }

    public static double getTy() {
        return table.getEntry(TY_KEY).getDouble(0.0);
    }

    public static double getTa() {
        return table.getEntry(TA_KEY).getDouble(0.0);
    }

    public static Limelight getInstance() {
        if(instance == null) {
            instance = new Limelight();
        }
        return instance;
    }
}