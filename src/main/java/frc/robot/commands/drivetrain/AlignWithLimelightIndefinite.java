package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import frc.robot.util.PIDOutputGetter;
import frc.robot.util.PIDSourceCustomGet;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Drives towards a target using the Limelight camera.
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @since 1/12/19
 */
public class AlignWithLimelightIndefinite extends IndefiniteCommand {
    private Limelight limelight;

    public static final double TURN_KP = .07; //0.09
    public static final double TURN_KI = 0.001;
    public static final double TURN_KD = 0.3;
    public static final double TURN_KF = 0;
    
    public static final double FORWARD_KF = 0;
    public static final double FORWARD_KP = 0.045;
    public static final double FORWARD_KI = 0;//0.001;
    public static final double FOWARD_KD = 0.16;    

    public static final double TURN_ALLOWABLE_ERROR = 0.054;
    public static final double FORWARD_ALLOWABLE_ERROR = 0.05; 

    public PIDOutputGetter turnOutput;
    public PIDOutputGetter forwardOutput;

    private PIDController turnController;
    private PIDController forwardController;

    private double thorSetpoint;
    private double txSetpoint;

    private static boolean LEFT_MASTER_INVERTED = true;
    private static boolean RIGHT_MASTER_INVERTED = false;
    private static boolean LEFT_FOLLOWER_INVERTED = true;
    private static boolean RIGHT_FOLLOWER_INVERTED = false;

    public AlignWithLimelightIndefinite(double thorSetpoint, double txSetpoint) {
        requires(Drivetrain.getInstance());
        this.txSetpoint = txSetpoint;
        this.thorSetpoint = Limelight.THOR_LINEARIZATION_FUNCTION.apply(thorSetpoint);
        limelight = Limelight.getInstance();
        turnOutput = new PIDOutputGetter();
        forwardOutput = new PIDOutputGetter();
    }

    @Override
    public void initialize() {
        turnController = new PIDController(
            TURN_KP, TURN_KI, TURN_KD, TURN_KF, 
            new PIDSourceCustomGet(() -> limelight.getTx(), PIDSourceType.kDisplacement), 
            turnOutput
        );
        
         forwardController = new PIDController(
             FORWARD_KP, FORWARD_KI, FOWARD_KD, FORWARD_KF, 
             new PIDSourceCustomGet(() -> limelight.getThor(), Limelight.THOR_LINEARIZATION_FUNCTION, 
                                     PIDSourceType.kDisplacement),
             forwardOutput
         );
        
        turnController.enable();
        forwardController.enable();

        turnController.setSetpoint(txSetpoint);
        forwardController.setSetpoint(thorSetpoint);

        Drivetrain.getInstance().invertTalons(LEFT_MASTER_INVERTED, RIGHT_MASTER_INVERTED, LEFT_FOLLOWER_INVERTED, RIGHT_FOLLOWER_INVERTED);
    }

    public void execute () {
        double forwardOutputVal = Math.abs(forwardController.getError()) < FORWARD_ALLOWABLE_ERROR ? 0 : forwardOutput.getOutput();
        double turnOutputVal = Math.abs(turnController.getError()) < TURN_ALLOWABLE_ERROR ? 0 : turnOutput.getOutput();

        SmartDashboard.putNumber("Turn Error", turnController.getError());
        SmartDashboard.putNumber("Forward Error", forwardController.getError());

        Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, forwardOutputVal - turnOutputVal);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, forwardOutputVal + turnOutputVal);
    }
}