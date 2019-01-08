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
                                                          
    }

    public void arcadeDrivePercentOutput(double speed, double turn) {
        getLeftMaster().set(ControlMode.PercentOutput, speed + turn);
        getRightMaster().set(ControlMode.PercentOutput, speed - turn);
    }
}