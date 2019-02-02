package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.RobotMap.CAN_IDs;
import frc.robot.commands.drivetrain.DriveWithPercentManual;
import frc.robot.util.Pair;
import harkerrobolib.subsystems.HSDrivetrain;
import harkerrobolib.wrappers.HSPigeon;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents the drivetrain on the robot.
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * @author Rohan Rashingkar
 * @author Anirudh Kotamraju
 * @author Angela Jia
 * @author Arnav Gupta
 * @author Dawson Chen
 * @since 1/7/19
 */
public class Drivetrain extends HSDrivetrain {
    private static Drivetrain instance;

    private final static boolean LEFT_MASTER_INVERTED = !false;
    private final static boolean RIGHT_MASTER_INVERTED = !true;
    private final static boolean LEFT_FOLLOWER_INVERTED = !false;
    private final static boolean RIGHT_FOLLOWER_INVERTED = !true;

    private final static double MAX_FORWARD_VELOCITY = 10;
    private final static double MAX_TURN_VELOCITY = 4;

    private static int TALON_PEAK_LIMIT = 20;
    private static int TALON_PEAK_TIME = 750;
    private static int TALON_CONTINUOUS_LIMIT = 15;

    public static final int ALLOWABLE_ERROR = 0;
    public static final int POSITION_SLOT_INDEX = 0;
    public static final int ANGLE_SLOT_INDEX = 1;
    public static final int VELOCITY_SLOT_INDEX = 2;
    public static final boolean LEFT_PIGEON_PHASE = true;
    public static final boolean RIGHT_PIGEON_PHASE = true;
    public static final boolean RIGHT_POSITION_PHASE = true;
	public static final boolean LEFT_POSITION_PHASE = true;


    /**
     * Creates new instance of Drivetrain.
     */
    private Drivetrain() { 
        super(new HSTalon(CAN_IDs.DT_LEFT_MASTER), 
                new HSTalon(CAN_IDs.DT_RIGHT_MASTER),
                new VictorSPX (   CAN_IDs.DT_LEFT_FOLLOWER),
                new VictorSPX (   CAN_IDs.DT_RIGHT_FOLLOWER),
                new HSPigeon(CAN_IDs.PIGEON));
    }

    /**
     * Intializes the default command of the drivetrain.
     */
    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand(new AlignWithLimelight(198, 0, 4));
        setDefaultCommand(new DriveWithPercentManual());
    }
    /**
     * A method to initialize the Talons for the start of the match.
     */
    public void talonInit() {
        resetMasters();
        resetTalonInverts();
        setNeutralMode(NeutralMode.Brake);
        setCurrentLimit(TALON_PEAK_LIMIT, TALON_PEAK_TIME, TALON_CONTINUOUS_LIMIT); 
    }

    public void resetTalonInverts() {
        invertTalons(LEFT_MASTER_INVERTED, RIGHT_MASTER_INVERTED, LEFT_FOLLOWER_INVERTED, RIGHT_FOLLOWER_INVERTED);
    }

    public void arcadeDrivePercentOutput(double speed, double turn) {
        arcadeDrivePercentOutput(getTalonOutputs(speed, turn));
    }

    public void arcadeDriveVelocity(double forwardPercent, double turnPercent) {
        double forwardSpeed = forwardPercent * MAX_FORWARD_VELOCITY;
        double turnSpeed = turnPercent * MAX_TURN_VELOCITY;
        
        getLeftMaster().set(ControlMode.Velocity, forwardSpeed + turnSpeed);
        getRightMaster().set(ControlMode.Velocity, forwardSpeed - turnSpeed);
    }

    /**
     * Sets the output of the talons from a pair. The first and second values
     * in the pair correspond to the outputs of the left and right talons, respectively.
     * Both values in the pair should be in the range [-1, 1].
     * 
     * @param outputs a pair repsresenting the outputs of the left and right talons
     */
    public void arcadeDrivePercentOutput(Pair<Double, Double> outputs) {
        getLeftMaster().set(ControlMode.PercentOutput, outputs.getFirst());
        getRightMaster().set(ControlMode.PercentOutput, outputs.getSecond());
    }


    public boolean isProximitySensorTriggered () {
        return getLeftMaster().getSensorCollection().isFwdLimitSwitchClosed();
    }
    
    /**
     * Gets outputs for the left and right talons in order to travel at a given
     * speed and turn weighted to specific degree.
     * 
     * @param speed the speed to travel at [-1, 1]
     * @param turn the amount to turn [-1, 1]
     */
    public Pair<Double, Double> getTalonOutputs(double speed, double turn) {
        return new Pair<Double, Double>(speed + turn, speed - turn);
    }

    /**
     * Returns an instance of Drivetrain.
     * @return an instance of Drivetrain
     */    
    public static Drivetrain getInstance() {
        if(instance == null) {
            instance = new Drivetrain();
        }

        return instance;
    }

}