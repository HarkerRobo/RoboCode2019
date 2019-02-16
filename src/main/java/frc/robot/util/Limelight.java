package frc.robot.util;

import java.util.function.Function;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Wraps Limelight functionality into a more easy-to-use class.
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * @since 1/12/19
 */
public class Limelight {
    private static Limelight instance;

    public static final String LIMELIGHT_TABLE_KEY = "limelight";
    public final NetworkTable table = NetworkTableInstance.getDefault().getTable(LIMELIGHT_TABLE_KEY);
    
    public static final String TV_KEY = "tv";
    public static final String TX_KEY = "tx";
    public static final String TY_KEY = "ty";
    public static final String TA_KEY = "ta";
    public static final String TS_KEY = "ts";
    public static final String TL_KEY = "tl";
    public static final String TSHORT_KEY = "tshort";
    public static final String TLONG_KEY = "tlong";
    public static final String THOR_KEY = "thor";
    public static final String TVERT_KEY = "tvert";
    public static final String MODE_KEY = "camMode";
    public static final String SNAP_KEY = "snapshot";
    public static final String CORNERX_KEY = "tcornx";
    public static final String CORNERY_KEY = "tcorny";
    public static final String CAMTRAN_KEY = "camtran";

    public static final int VISION_MODE = 0;
    public static final int DRIVER_MODE = 1;

    public static final int NO_SNAPSHOT = 0;
    public static final int SNAPSHOT = 1;
 
    public static double THOR_EXPONENT = 0.62291;
    public static double THOR_COEFF = 1000;
    public static double TX_SETPOINT = 0;
    public static Function<Double, Double> THOR_LINEARIZATION_FUNCTION = (thor) -> (THOR_COEFF / Math.pow(thor, THOR_EXPONENT));

    /**
     * Setup Limelight with default settings
     */
    private Limelight()
    {
        table.getEntry(MODE_KEY).setNumber(VISION_MODE);
        table.getEntry(SNAP_KEY).setNumber(NO_SNAPSHOT);
    }

    /**
     * Determines whether a target has been latched onto.
     * 
     * @return true if a target is visible; otherwise,
     *         false
     */
    public boolean isTargetVisible() {
        return Math.abs(table.getEntry(TV_KEY).getDouble(0.0) - 1.0) < 1e-5;
    }

    /**
     * Determines the horizontal angular distance from the crosshair to the center of the bounding box representing the target.
     * 
     * @return the horizontal angular distance to the target, in degrees
     */
    public double getTx() {
        return table.getEntry(TX_KEY).getDouble(0.0);
    }

    /**
     * Toggles the limelight between driver and vision mode
     */
    public void toggleCamMode()
    {
        if (table.getEntry(MODE_KEY).getDouble(0) == VISION_MODE)
            table.getEntry(MODE_KEY).setNumber(DRIVER_MODE);
        else
            table.getEntry(MODE_KEY).setNumber(VISION_MODE);
    }

    /**
     * Determines the vertical angular distance from the crosshair to the center of the bounding box representing the target.
     * 
     * @return the vertical, angular distance to the target, in degrees
     */
    public double getTy() {
        return table.getEntry(TY_KEY).getDouble(0.0);
    }

    /**
     * Determines the angular skew of the bounding box representing the target.
     * 
     * @return the angular skew, in degrees [-90, 0]
     */
    public double getTs() {
        return table.getEntry(TS_KEY).getDouble(0.0);
    }

    /**
     * Determines the area of the target.
     * 
     * @return the area, as a percent of the total screen
     */
    public double getTa() {
        return table.getEntry(TA_KEY).getDouble(0.0);
    }

    /**
     * Determines the latency of the camera feed, or a measure of how long the feed takes to process.
     * 
     * @return the latency, in milliseconds 
     */
    public double getTl() {
        return table.getEntry(TL_KEY).getDouble(0.0);
    }

    /**
     * Determines the sidelength of the shortest side of the fitted bounding box.
     * The fitted bounding box is a rectangular convex hull surrounding the selected pixels,
     * while the rough bounding box is a rectangle around the fitted bounding box, put is not rotated
     * @return the shortest sidelength, in pixels
     */
    public double getTshort() {
        return table.getEntry(TSHORT_KEY).getDouble(0.0);
    }

    /**
     * Determines the sidelength of the longest side of the fitted bounding box.
     * The fitted bounding box is a rectangular convex hull surrounding the selected pixels,
     * while the rough bounding box is a rectangle around the fitted bounding box, put is not rotated
     * @return the longest sidelength, in pixels
     */
    public double getTlong() {
        return table.getEntry(TLONG_KEY).getDouble(0.0);
    }

    /**
     * Determines the horizontal sidelength of the rough bounding box.
     * The fitted bounding box is a rectangular convex hull surrounding the selected pixels,
     * while the rough bounding box is a rectangle around the fitted bounding box, put is not rotated
     * @return the horizontal sidelength, in pixels [0, 320]
     */    
    public double getThor() {
        return table.getEntry(THOR_KEY).getDouble(0.0);
    }

    /**
     * Determines the vertical sidelength of the rough bounding box.
     * The fitted bounding box is a rectangular convex hull surrounding the selected pixels,
     * while the rough bounding box is a rectangle around the fitted bounding box, put is not rotated
     * @return the vertical sidelength, in pixels [0, 240]
     */
    public double getTvert() {
        return table.getEntry(TVERT_KEY).getDouble(0.0);
    }

    /**
     * Returns the values of the Limelight's compute 3D localization calculations.
     * 
     * @return The compute 3D values, in the specified order (x,y,z,pitch,yaw,roll).
     *         These values place the target as the origin and represent the camera's
     *         orientation relative to the target.
     */
    public double[] getCamtranData()
    {
        double[] nullArr = null;
        return table.getEntry(CAMTRAN_KEY).getDoubleArray(nullArr);
    }

    /**
     * Returns vertical distance from the target
     * 
     * @return The camera's vertical distance from the target.
     */
    public double getCamtranX()
    {
        return getCamtranData()[0];
    }

    /**
     * Returns height difference between the the target
     * 
     * @return The camera's height relative to the target's height (automatically 0).
     */
    public double getCamtranY()
    {
        return getCamtranData()[1];
    }

    /**
     * Returns horizontal distance from the target
     * 
     * @return The camera's horizontal distance from the target.
     */
    public double getCamtranZ()
    {
        return getCamtranData()[2];
    }

    /**
     * Returns camera's pitch (forward/backward tilt) relative to the target
     * 
     * @return The camera's pitch relative to the target (automatically 0).
     */
    public double getCamtranPitch()
    {
        return getCamtranData()[3];
    }

    /**
     * Returns camera's yaw (left/right rotation) relative to the target
     * 
     * @return The camera's pitch relative to the target (automatically 0).
     */
    public double getCamtranYaw()
    {
        return getCamtranData()[4];
    }

    /**
     * Returns camera's yaw (left/right tilt) relative to the target
     * 
     * @return The camera's pitch relative to the target (automatically 0).
     */
    public double getCamtranRoll()
    {
        return getCamtranData()[5];
    }

    public double getRawContourTx(int contourId) {
        return table.getEntry(TX_KEY + contourId).getDouble(0.0);
    }

    public double getRawContourTy(int contourId) {
        return table.getEntry(TY_KEY + contourId).getDouble(0.0);
    }

    public double getRawContourTa(int contourId) {
        return table.getEntry(TA_KEY + contourId).getDouble(0.0);
    }

    public double getRawContourTs(int contourId) {
        return table.getEntry(TS_KEY + contourId).getDouble(0.0);
    }

    public double[] getCornersX() {
        double[] nullArr = null;
        return table.getEntry(CORNERX_KEY).getDoubleArray(nullArr);
    }

    public double[] getCornersY() {
        double[] nullArr = null;
        return table.getEntry(CORNERY_KEY).getDoubleArray(nullArr);
    }

    public double getLeftArea () {
        return getRawContourTx(0) <  getRawContourTx(1) ? getRawContourTa(0) : getRawContourTa(1);
    }

    public double getRightArea() {
        return getRawContourTx(0) >  getRawContourTx(1) ? getRawContourTa(0) : getRawContourTa(1);
    }

    /**
     * Gets the singleton instance of the limelight class.
     * 
     * @return the instance of the limelight class
     */
    public static Limelight getInstance() {
        if(instance == null) {
            instance = new Limelight();
        }
        return instance;
    }
}