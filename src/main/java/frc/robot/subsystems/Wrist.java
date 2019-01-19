package frc.robot.subsystems;

import java.awt.Color;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.commands.wrist.MoveWristManual;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents the wrist on the robot.
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * @since 1/10/19
 */
public class Wrist extends Subsystem {
    public enum WristDirection {
        TO_BACK (1), TO_FRONT (-1);

        private final int direction;
        private WristDirection (int direction) {
            this.direction = direction;
        }

        public int getSign () {return direction;}
    }

    private static Wrist wr;
    
    private HSTalon wristMaster;
    private VictorSPX wristFollower;

    private CANifier canifier;

    private static final boolean MASTER_INVERTED = false;
    private static final boolean FOLLOWER_INVERTED = false;

    private static final int CONTINUOUS_CURRENT_LIMIT = 0;
    private static final int PEAK_CURRENT_LIMIT = 0;
    private static final int PEAK_TIME = 500;
    public static final double WRIST_ANGLE_SCORING = 0.0;
    public static final double WRIST_ANGLE_INTAKE = 180.0;
    public static final int ALLOWABLE_ERROR = 400;
    public static final int MAX_FORWARD_POSITION = 0;
    public static final int MAX_BACKWARD_POSITION = 10000; // TUNE
    public static final int RANGE_OF_MOTION = Math.abs(MAX_FORWARD_POSITION - MAX_BACKWARD_POSITION);
    /**
     * The percentage distance from either the front or the back after which precautionary measures must be taken to limit max operable speed.
     */
    public static final double SLOW_DOWN_PERCENT_TO_ENDPOINT = 0.75;

    /**
     * The physical distance (encoder units) from either the front or the back after which precautionary measures must be taken to limit max operable speed.
     */
    public static final double SLOW_DOWN_DISTANCE_FROM_ENDPOINT = Wrist.RANGE_OF_MOTION * (1 - Wrist.SLOW_DOWN_PERCENT_TO_ENDPOINT);

    public static final double MAX_PERCENT_OUTPUT = 1.0;
    public static final double MIN_PERCENT_OUTPUT = 0.0;

    public static final int MAX_SPEED = 100; // TUNE

    public static final int POSITION_SLOT = 0;

    public static final boolean SENSOR_PHASE = false;

    //default color of the LED
    private static final Color DEFAULT_COLOR = Color.RED;

    //enabled color of the LED
    private static final Color ENABLED_COLOR = Color.GREEN;

    //Position Constants
    public static final double KP = 0.0;
    public static final double KI = 0.0;
    public static final double KD = 0.0;
    public static final double KF = 0.0;

    private Wrist () {
        wristMaster = new HSTalon(CAN_IDs.WRIST_MASTER);
        wristFollower = new VictorSPX(CAN_IDs.WRIST_FOLLOWER);
    }

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new MoveWristManual());
    }
    
    public void talonInit () {
        wristMaster.setNeutralMode(NeutralMode.Brake);
        wristFollower.setNeutralMode (NeutralMode.Brake);

        wristMaster.setInverted(MASTER_INVERTED);
        wristFollower.setInverted(FOLLOWER_INVERTED);

        wristMaster.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);
        wristMaster.configPeakCurrentDuration(PEAK_TIME);
        wristMaster.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);

        wristMaster.config_kP(Wrist.POSITION_SLOT, KP);
        wristMaster.config_kI(Wrist.POSITION_SLOT, KI);
        wristMaster.config_kD(Wrist.POSITION_SLOT, KD);
        wristMaster.config_kF(Wrist.POSITION_SLOT, KF);
    }

    public HSTalon getMasterTalon () {
        return wristMaster;
    }

    public VictorSPX getFollowerTalon () {
        return wristFollower;
    }

    public void setWrist (double percent, WristDirection direction) {
        wristMaster.set(ControlMode.PercentOutput, percent * direction.getSign());
    }

    /**
     * Sets the output color of the LED.
     */
    public void setLEDOutput(Color color) {
        canifier.setLEDOutput(color.getRed() / 255.0, Global.RED_CHANNEL);
        canifier.setLEDOutput(color.getGreen() / 255.0, Global.GREEN_CHANNEL);
        canifier.setLEDOutput(color.getBlue() / 255.0, Global.BLUE_CHANNEL);
    }

    public void updateLEDIfProximitySensorTriggered() {
        if(Drivetrain.getInstance().isProximitySensorTriggered())
            setLEDOutput(ENABLED_COLOR);
        else
            setLEDOutput(DEFAULT_COLOR);
    }

    public static Wrist getInstance() {
        if (wr == null) {wr = new Wrist();}
        return wr;
    }

}