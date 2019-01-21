package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
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
 * @since 1/12/19
 */
public class AlignWithLimelight extends Command {
    private Limelight limelight;

    public static final double TURN_KP = 0.1;
    public static final double TURN_KI = 0.0;
    public static final double TURN_KD = 0.0;
    public static final double TURN_KF = 0;
    
    public static final double FORWARD_KF = 0;
    public static final double FOWARD_KP = 1;
    public static final double FORWARD_KI = 0.0;
    public static final double FOWARD_KD = 3;    

    public static final double TURN_ALLOWABLE_ERROR = 0.5; //0.054
    public static final double FORWARD_ALLOWABLE_ERROR = 2; //0.01

    public PIDOutputGetter turnOutput;
    public PIDOutputGetter forwardOutput;

    private PIDController turnController;
    private PIDController forwardController;

    private double thorSetpoint;
    private double txSetpoint;


    public AlignWithLimelight(double thorSetpoint, double txSetpoint) {
        requires(Drivetrain.getInstance());
        this.txSetpoint = txSetpoint;
        this.thorSetpoint = thorSetpoint;
        limelight = Limelight.getInstance();
    }

    @Override
    public void initialize() {
        turnController = new PIDController(
            TURN_KP, TURN_KI, TURN_KD, TURN_KF, 
            new PIDSourceCustomGet(() -> Limelight.getInstance().getTx(), PIDSourceType.kDisplacement), 
            turnOutput
        );
        
        forwardController = new PIDController(
            FOWARD_KP, FORWARD_KI, FOWARD_KD, FORWARD_KF, 
            new PIDSourceCustomGet(() -> Limelight.getInstance().getThor(), Limelight.THOR_LINEARIZATION_FUNCTION, PIDSourceType.kDisplacement),
            forwardOutput
        );
        
        turnController.enable();
        forwardController.enable();

        turnController.setSetpoint(txSetpoint);
        forwardController.setSetpoint(thorSetpoint);
    }

    public void execute () {
        // System.out.println("turn: " + turnOutput.getOutput());
        // System.out.println("forward: " + forwardOutput.getOutput());
        // try {
            
        // } catch (Exception e) {
        //     System.out.println("in execute");
        //     e.printStackTrace();
        // }

        Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, (forwardOutput.getOutput()));// + turnOutput.getOutput()));
        Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, (forwardOutput.getOutput()));// - turnOutput.getOutput()));
    }
    
    @Override
    public void end() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
        turnController.disable();
        forwardController.disable();
        System.out.println("Done!");  
    }

    @Override
    public void interrupted() {
        System.out.println("Interrupted");
        end();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(forwardController.getError() / forwardController.getSetpoint()) <= FORWARD_ALLOWABLE_PERCENT;// && turnController.getError() <= TURN_ALLOWABLE_ERROR;
    }
}