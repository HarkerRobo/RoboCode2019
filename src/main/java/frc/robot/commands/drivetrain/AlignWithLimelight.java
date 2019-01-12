package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import frc.robot.util.PIDOutputGetter;

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

    public static final double TURN_KP = 0.0;
    public static final double TURN_KI = 0.0;
    public static final double TURN_KD = 0.0;
    
    public static final double FOWARD_KP = 0.0;
    public static final double FORWARD_KI = 0.0;
    public static final double FOWARD_KD = 0.0;    

    public static final double TX_SETPOINT = 0.0;
    public static final double TA_SETPOINT = 0.4;    
    
    public static final double TURN_ALLOWABLE_ERROR = 0.054;
    public static final double FORWARD_ALLOWABLE_ERROR = 0.01;

    public PIDOutputGetter turnOutput;
    public PIDOutputGetter forwardOutput;

    private PIDController turnController;
    private PIDController forwardController;


    public AlignWithLimelight() {
        requires(Drivetrain.getInstance());
        limelight = Limelight.getInstance();
        turnOutput = new PIDOutputGetter();
        forwardOutput = new PIDOutputGetter();
    }

    @Override
    public void initialize() {
        turnController = new PIDController(TURN_KP, TURN_KI, TURN_KD, new PIDSource() {
            private PIDSourceType sourceType;            
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                sourceType = pidSource;
            }
        
            @Override
            public PIDSourceType getPIDSourceType() {
                return sourceType;
            }
            
            @Override
            public double pidGet() {
                return Limelight.getTx();
            }    
        }, turnOutput);
        
        forwardController = new PIDController(FOWARD_KP, FORWARD_KI, FOWARD_KD, new PIDSource() {
            private PIDSourceType sourceType;
            
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
                sourceType = pidSource;
            }
        
            @Override
            public PIDSourceType getPIDSourceType() {
                return sourceType;
            }
            
            @Override
            public double pidGet() {
                return Limelight.getTa();
            }                            
        }, forwardOutput);

        turnController.setSetpoint(TX_SETPOINT);
        forwardController.setSetpoint(TA_SETPOINT);
        
        turnController.enable();
        forwardController.enable();
    }

    public void execute () {
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, forwardOutput.getOutput() + turnOutput.getOutput());
        Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, forwardOutput.getOutput() - turnOutput.getOutput());
    }

    @Override
    public void end() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
        turnController.disable();
        forwardController.disable();
    }

    @Override
    public void interrupted() {
        end();
    }

    @Override
    public boolean isFinished() {
        return forwardController.getError() <= FORWARD_ALLOWABLE_ERROR && turnController.getError() <= TURN_ALLOWABLE_ERROR;       
    }
}