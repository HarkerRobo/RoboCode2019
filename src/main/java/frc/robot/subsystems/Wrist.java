package frc.robot.subsystems;

import java.awt.Color;
import java.util.function.Supplier;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot.Side;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.commands.wrist.MoveWristManual;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.commands.wrist.MoveWristPosition;
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

    public static final int CONTINUOUS_CURRENT_LIMIT = 7;
    public static final int PEAK_CURRENT_LIMIT = 10;
    public static final int PEAK_TIME = 500;
    
    public static final int SCORING_POSITION_FRONT_HATCH = 0;
    public static final int SCORING_POSITION_FRONT_CARGO = 0;
    public static final int SCORING_POSITION_BACK_HATCH = 0;
    public static final int SCORING_POSITION_BACK_CARGO = 0;

    public static final double ARBITRARY_FF = 0.017;

    public static final int ANGLE_INTAKE = 180;
    public static final int HATCH_INTAKING_POSITION = 0;
    public static final int CARGO_INTAKING_POSITION = 180;

    public static final int ALLOWABLE_ERROR = 400;
    public static final int MAX_FORWARD_POSITION = 0;
    public static final int MAX_BACKWARD_POSITION = 10000; // TUNE
    public static final int MID_POSITION = (MAX_FORWARD_POSITION + MAX_BACKWARD_POSITION)/2;
    public static final int SAFE_FORWARD_POSITION = 0;
    public static final int SAFE_BACKWARD_POSITION = 10000;
    public static final int RANGE_OF_MOTION = Math.abs(MAX_FORWARD_POSITION - MAX_BACKWARD_POSITION);

    /**
     * The percentage distance from either the front or the back after which precautionary measures must be taken to limit max operable speed.
     */
    public static final double SLOW_DOWN_PERCENT_TO_ENDPOINT = 0.75;

    /*
     * The physical distance (encoder units) from either the front or the back after which precautionary measures must be taken to limit max operable speed.
     */
    public static final double SLOW_DOWN_DISTANCE_FROM_ENDPOINT = Wrist.RANGE_OF_MOTION * (1 - Wrist.SLOW_DOWN_PERCENT_TO_ENDPOINT);

    public static final double MAX_PERCENT_OUTPUT = 1.0;
    public static final double MIN_PERCENT_OUTPUT = 0.0;

    public static final int MAX_SPEED = 100; // TUNE

    public static final int POSITION_SLOT = 0;
    public static final int MOTION_MAGIC_SLOT = 1;

    public static final boolean SENSOR_PHASE = true;
    public static Supplier<Double> feedForwardLambda = () -> (ARBITRARY_FF * Math.cos(Wrist.getInstance().convertEncoderToRadians(Wrist.getInstance().getMasterTalon().getSelectedSensorPosition())));

    private Wrist () {
        wristMaster = new HSTalon(CAN_IDs.WRIST_MASTER);
        wristFollower = new VictorSPX(CAN_IDs.WRIST_FOLLOWER);
    }

	@Override
	protected void initDefaultCommand() {
       // setDefaultCommand(new MoveWristManual());
    }
    
    public void talonInit () {
        wristFollower.follow(wristMaster);

        wristMaster.setNeutralMode(NeutralMode.Brake);
        wristFollower.setNeutralMode (NeutralMode.Brake);

        wristMaster.setInverted(MASTER_INVERTED);
        wristFollower.setInverted(FOLLOWER_INVERTED);

        wristMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

        wristMaster.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);
        wristMaster.configPeakCurrentDuration(PEAK_TIME);
        wristMaster.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        wristMaster.enableCurrentLimit(true);

        wristMaster.setSensorPhase(SENSOR_PHASE);
        // wristMaster.setSelectedSensorPosition(0);
        System.out.println("taloninit");
    }

    public HSTalon getMasterTalon () {
        return wristMaster;
    }

    public VictorSPX getFollowerTalon () {
        return wristFollower;
    }

    public void setWristPercentOutput (double value, WristDirection direction) {
        // wristMaster.set(ControlMode.PercentOutput, value * direction.getSign(), 
        //                 DemandType.ArbitraryFeedForward, feedForwardLambda.get() 
        //                 );
    }

    public void setWrist (ControlMode mode, double value) {
        // wristMaster.set(mode, value, DemandType.ArbitraryFeedForward, feedForwardLambda.get());
    }
    /**
     * Sets the output color of the LED.
     */
    public void setLEDOutput(Color color) {
        canifier.setLEDOutput(color.getRed() / 255.0, Global.RED_CHANNEL);
        canifier.setLEDOutput(color.getGreen() / 255.0, Global.GREEN_CHANNEL);
        canifier.setLEDOutput(color.getBlue() / 255.0, Global.BLUE_CHANNEL);
    }

    public static Wrist getInstance() {
        if (wr == null) 
            wr = new Wrist();
        return wr;
    }

    /**
     * Determines if the current position is 
     * behind the specified position
     * 
     * @param position position that the current position is compared with
     */
    public boolean isFurtherBack (int position) {
        return !isFurtherForward(position);
    }

    public  boolean isFurtherBack (int currentPosition, int positionToCompare) {
        return !isFurtherForward(currentPosition, positionToCompare);
    }

    /**
     * Determines if the current position is 
     * in front of the specified position.
     * 
     * @param position position that the current position is compared in relation to
     */
    public boolean isFurtherForward (int position) {
        return getMasterTalon().getSelectedSensorPosition() < position;
    }

    public boolean isFurtherForward(int comparedPosition, int comparisonPosition) {
        return comparisonPosition < comparedPosition;
    }

    public boolean isFurtherBackward (int position) {
        return getMasterTalon().getSelectedSensorPosition() > position;
    }

    public boolean isFurtherBackward (int comparedPosition, int comparisonPosition) {
        return comparisonPosition > comparedPosition;
    }

    public boolean isForward(int position) {
        return position <= MID_POSITION;
    }

    public boolean isBackward(int position) {
        return position > MID_POSITION;
    }

    public boolean isAt (int position) {
        return Math.abs(getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY) - position) == ALLOWABLE_ERROR;
    }

    public void setupPositionPID () {
        Wrist.getInstance().getMasterTalon().selectProfileSlot (Wrist.POSITION_SLOT, Global.PID_PRIMARY);
        Wrist.getInstance().getMasterTalon().setSensorPhase(Wrist.SENSOR_PHASE);     

        Wrist.getInstance().getMasterTalon().config_kP(Wrist.POSITION_SLOT, MoveWristPosition.KP);
        Wrist.getInstance().getMasterTalon().config_kI(Wrist.POSITION_SLOT, MoveWristPosition.KI);
        Wrist.getInstance().getMasterTalon().config_kD(Wrist.POSITION_SLOT, MoveWristPosition.KD);
        Wrist.getInstance().getMasterTalon().config_kF(Wrist.POSITION_SLOT, MoveWristPosition.KF);
        Wrist.getInstance().getMasterTalon().config_IntegralZone(Wrist.POSITION_SLOT, MoveWristPosition.IZONE);
    }

    public void setupMotionMagic() {
        Wrist.getInstance().getMasterTalon().selectProfileSlot (Wrist.MAX_BACKWARD_POSITION, Global.PID_PRIMARY);
        Wrist.getInstance().getMasterTalon().setSensorPhase(Wrist.SENSOR_PHASE);     

        Wrist.getInstance().getMasterTalon().config_kP(Wrist.MOTION_MAGIC_SLOT, MoveWristMotionMagic.KP);
        Wrist.getInstance().getMasterTalon().config_kI(Wrist.MOTION_MAGIC_SLOT, MoveWristMotionMagic.KI);
        Wrist.getInstance().getMasterTalon().config_kD(Wrist.MOTION_MAGIC_SLOT, MoveWristMotionMagic.KD);
        Wrist.getInstance().getMasterTalon().config_kF(Wrist.MOTION_MAGIC_SLOT, MoveWristMotionMagic.KF);
        Wrist.getInstance().getMasterTalon().configMotionCruiseVelocity(Wrist.MOTION_MAGIC_SLOT, MoveWristMotionMagic.CRUISE_VELOCITY);
        Wrist.getInstance().getMasterTalon().configMotionAcceleration(Wrist.MOTION_MAGIC_SLOT, MoveWristMotionMagic.ACCELERATION);
    }

    /**
     * Determines if the current position is 
     * in the ambiguous region.
     */
    public boolean isAmbiguous() {
        return !isFurtherForward(SAFE_FORWARD_POSITION) && !isFurtherBack(SAFE_BACKWARD_POSITION);
    }

    public boolean isSafelyForward() {
        return isFurtherForward(SAFE_FORWARD_POSITION);
    }

    public boolean isSafelyForward(int position) {
        return isFurtherForward(position, SAFE_FORWARD_POSITION);
    }

    public boolean isSafelyBackward(int position) {
        return isFurtherBack(position, SAFE_BACKWARD_POSITION);
    }

    public boolean isSafelyBackward() {
        return isFurtherBack(SAFE_BACKWARD_POSITION);
    }

    public boolean isForward() {
        return isFurtherForward(MID_POSITION);
    }

    public boolean isBackward() {
        return isFurtherBack(MID_POSITION);
    }

    public boolean mustPassThrough(int desiredWristPosition) {
        return Wrist.getInstance().isAmbiguous() || 
                                        (Wrist.getInstance().isSafelyForward(desiredWristPosition) && Wrist.getInstance().isSafelyBackward() || 
                                            Wrist.getInstance().isSafelyBackward(desiredWristPosition) && Wrist.getInstance().isSafelyForward());
    }

    public Side getCurrentSide() {
        if (isAmbiguous()) {return Side.AMBIGUOUS;}
        return isSafelyForward() ? Side.FRONT : Side.BACK;
    }

    public Side getSide (int position) {
        return isSafelyForward(position) ? Side.FRONT : Side.BACK;
    }

    public int convertAngleToEncoder(double angle) {
        return (int)(angle * 4096 / 360);
    }

    public double convertEncoderToRadians(int encoder) {
        return encoder / 4096.0 * 2 * Math.PI;
    }
}   