package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.RobotMap.CAN_IDS;
import frc.robot.commands.DriveWithVelocityManual;
import harkerrobolib.subsystems.HSDrivetrain;
import harkerrobolib.util.Gains;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents the Drivetrain on the robot.
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * @author Rohan Rashingkar
 * @author Anirudh Kotamraju
 * @author Angela Jia
 * @version 1/7/19
 */
public class Drivetrain extends HSDrivetrain {
    private static Drivetrain instance;

    private static boolean LEFT_MASTER_INVERTED = false;
    private static boolean RIGHT_MASTER_INVERTED = false;

    private static int TALON_PEAK_LIMIT = 20;
    private static int TALON_PEAK_TIME = 750;
    private static int TALON_CONTINUOUS_LIMIT = 15;

    public static final int ALLOWABLE_ERROR = 0;
    public static final int POSITION_SLOT_INDEX = 0;
    public static final double POSITION_LEFT_KP = 0;
    public static final double POSITION_LEFT_KI = 0;
    public static final double POSITION_LEFT_KD = 0;
    public static final double POSITION_RIGHT_KP = 0;
    public static final double POSITION_RIGHT_KI = 0;
    public static final double POSITION_RIGHT_KD = 0;


    /**
     * Creates new instance of Drivetrain
     */
    private Drivetrain() { 
        super(new HSTalon(CAN_IDS.DT_LEFT_MASTER), 
                new HSTalon(CAN_IDS.DT_RIGHT_MASTER), 
                new VictorSPX (CAN_IDS.DT_LEFT_FOLLOWER),
                new VictorSPX (CAN_IDS.DT_RIGHT_FOLLOWER));
        //Update IDs
    }

    /**
     * 
     */
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveWithVelocityManual());
    }

    /**
     *  Returns an instance of Drivetrain.
     * @return an instance of Drivetrain
     */    
    public static Drivetrain getInstance() {
        if(instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    /**
     * A method to initialize the Talons for the start of the match
     */
    public void talonInit() {
        invertTalons(LEFT_MASTER_INVERTED, RIGHT_MASTER_INVERTED);
        setNeutralMode(NeutralMode.Brake);
        setCurrentLimit(TALON_PEAK_LIMIT, TALON_PEAK_TIME, TALON_CONTINUOUS_LIMIT); 
        configClosedLoopConstants(Drivetrain.POSITION_SLOT_INDEX, 
                                    new Gains()
                                                .kP(Drivetrain.POSITION_LEFT_KP)
                                                .kI(Drivetrain.POSITION_LEFT_KI)
                                                .kD(Drivetrain.POSITION_LEFT_KD), 
                                    new Gains()
                                                .kP(Drivetrain.POSITION_RIGHT_KP)
                                                .kI(Drivetrain.POSITION_RIGHT_KI)
                                                .kD(Drivetrain.POSITION_RIGHT_KD)); // kF will be set to zero if not specified
    }

    public void arcadeDrivePercentOutput(double speed, double turn) {
        getLeftMaster().set(ControlMode.PercentOutput, speed + turn);
        getRightMaster().set(ControlMode.PercentOutput, speed - turn);
    }
}