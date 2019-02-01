package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.RobotMap.CAN_IDs;
import frc.robot.commands.drivetrain.AlignWithLimelight;
import frc.robot.commands.drivetrain.DriveToPosition;
import frc.robot.commands.drivetrain.DriveWithVelocityDual;
import frc.robot.commands.drivetrain.DriveWithVelocityManual;
import harkerrobolib.subsystems.HSDrivetrain;
import harkerrobolib.util.Gains;
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

    private static boolean LEFT_MASTER_INVERTED = !false;
    private static boolean RIGHT_MASTER_INVERTED = !true;
    private static boolean LEFT_FOLLOWER_INVERTED = !false;
    private static boolean RIGHT_FOLLOWER_INVERTED = !true;

    private static int TALON_PEAK_LIMIT = 20;
    private static int TALON_PEAK_TIME = 750;
    private static int TALON_CONTINUOUS_LIMIT = 15;

    public static final int ALLOWABLE_ERROR = 0;
    public static final int POSITION_SLOT_INDEX = 0;
    public static final int ANGLE_SLOT_INDEX = 1;
    public static final boolean LEFT_PIGEON_PHASE = true;
    public static final boolean RIGHT_PIGEON_PHASE = true;


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
     * 
     */
    @Override
    protected void initDefaultCommand() {
//        setDefaultCommand(new AlignWithLimelight(198, 0, 4));
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
        getLeftMaster().set(ControlMode.PercentOutput, speed + turn);
        getRightMaster().set(ControlMode.PercentOutput, speed - turn);
    }

    public boolean isProximitySensorTriggered () {
        return getLeftMaster().getSensorCollection().isFwdLimitSwitchClosed();
    }
    
}