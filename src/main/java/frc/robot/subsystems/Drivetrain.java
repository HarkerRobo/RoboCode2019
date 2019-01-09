package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import harkerrobolib.subsystems.HSDrivetrain;
import harkerrobolib.wrappers.HSTalon;
import frc.robot.RobotMap;

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
    public static final int POSITION_INDEX = 0;
    public static final double POSITION_LEFT_KP = 0;
    public static final double POSITION_LEFT_KI = 0;
    public static final double POSITION_LEFT_KD = 0;
    public static final double POSITION_RIGHT_KP = 0;
    public static final double POSITION_RIGHT_KI = 0;
    public static final double POSITION_RIGHT_KD = 0;
    public static final double POSITION_KF = 0;


    /**
     * Creates new instance of Drivetrain
     */
    private Drivetrain() { 
        super(new HSTalon(RobotMap.CAN_IDS.LEFT_MASTER), new HSTalon(RobotMap.CAN_IDS.RIGHT_MASTER));
        //Update IDs
    }

    /**
     * 
     */
    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand();
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
        Drivetrain.getInstance().getLeftMaster().configAllowableClosedloopError(Drivetrain.POSITION_INDEX, Drivetrain.ALLOWABLE_ERROR);
        Drivetrain.getInstance().getLeftMaster().config_kP(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_LEFT_KP);
        Drivetrain.getInstance().getLeftMaster().config_kI(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_LEFT_KI);
        Drivetrain.getInstance().getLeftMaster().config_kD(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_LEFT_KD);  
        Drivetrain.getInstance().getLeftMaster().config_kF(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_KF);
        Drivetrain.getInstance().getRightMaster().configAllowableClosedloopError(Drivetrain.POSITION_INDEX, Drivetrain.ALLOWABLE_ERROR);
        Drivetrain.getInstance().getRightMaster().config_kP(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_LEFT_KP);
        Drivetrain.getInstance().getRightMaster().config_kI(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_LEFT_KI); 
        Drivetrain.getInstance().getRightMaster().config_kD(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_RIGHT_KD);  
        Drivetrain.getInstance().getRightMaster().config_kF(Drivetrain.POSITION_INDEX, Drivetrain.POSITION_KF);  


    }

    public void arcadeDrivePercentOutput(double speed, double turn) {
        getLeftMaster().set(ControlMode.PercentOutput, speed + turn);
        getRightMaster().set(ControlMode.PercentOutput, speed - turn);
    }
}