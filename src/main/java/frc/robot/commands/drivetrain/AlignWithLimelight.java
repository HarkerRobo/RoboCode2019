package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import frc.robot.util.PIDOutputGetter;
import frc.robot.util.PIDSourceCustomGet;

/**
 * Drives towards a target using the Limelight camera.
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @author Angela Jia
 * @since 1/12/19
 */
public class AlignWithLimelight extends Command {
    private Limelight limelight;

    private static final double TURN_KP = .07; //0.09
    private static final double TURN_KI = 0.001;
    private static final double TURN_KD = 0.3;
    private static final double TURN_KF = 0;
    
    private static final double FORWARD_KF = 0;
    private static final double FORWARD_KP = 0.045;
    private static final double FORWARD_KI = 0;//0.001;
    private static final double FOWARD_KD = 0.2;   

    public static final double TURN_ALLOWABLE_ERROR = 0.054;
    public static final double FORWARD_ALLOWABLE_ERROR = 0.05;

    private static final double POS_TX_SETPOINT = 1.0; 
    private static final double MAX_ALLOWABLE_TX = 7.5;   

    private static final double THOR_SWITCH_POINT = 50.0;

    private PIDOutputGetter turnOutput;
    private PIDOutputGetter forwardOutput;

    private PIDController turnController;
    private PIDController forwardController;

    private double thorSetpoint;
    private double txSetpoint;

    private static boolean LEFT_MASTER_INVERTED = true;
    private static boolean RIGHT_MASTER_INVERTED = false;
    private static boolean LEFT_FOLLOWER_INVERTED = true;
    private static boolean RIGHT_FOLLOWER_INVERTED = false;

    public AlignWithLimelight(double thorSetpoint, double txSetpoint, double angleSetpoint) {
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
        boolean isTxPositive = limelight.getTx() >= 0;
        boolean hasReachedThorSwitchpoint = limelight.getThor() >= THOR_SWITCH_POINT;   
        boolean isTxWithinAllowableRange = MAX_ALLOWABLE_TX < limelight.getTx();     

        double forwardOutputVal = Math.abs(forwardController.getError()) < FORWARD_ALLOWABLE_ERROR ? 0 : forwardOutput.getOutput();
        double turnOutputVal;

        turnController.setSetpoint(
            isTxPositive || hasReachedThorSwitchpoint ? txSetpoint : POS_TX_SETPOINT
        );

        turnOutputVal = isTxPositive && isTxWithinAllowableRange &&(Math.abs(turnController.getError()) < TURN_ALLOWABLE_ERROR || 
                                    !hasReachedThorSwitchpoint)
                                    ? 0 : turnOutput.getOutput();

        SmartDashboard.putNumber("Turn Error", turnController.getError());
        SmartDashboard.putNumber("Forward Error", forwardController.getError());

        Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, forwardOutputVal - turnOutputVal /*- angleOutputVal*/);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, forwardOutputVal + turnOutputVal /*+ angleOutputVal*/);
    }
    
    @Override
    public void end() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
        turnController.disable();
        forwardController.disable();
        Drivetrain.getInstance().resetTalonInverts();
    }

    @Override
    public void interrupted() {
        System.out.println("Interrupted");
        end();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(forwardController.getError()) <= FORWARD_ALLOWABLE_ERROR && 
                Math.abs(turnController.getError()) <= TURN_ALLOWABLE_ERROR; 
                //Math.abs(angleController.getError()) <= ANGLE_ALLOWABLE_ERROR ;
    }
}