package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Wrist;

/**
 * Moves the wrist to a given position.
 * 
 * @author Finn Frankis
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * 
 * @since 1/12/19
 */
public class MoveWristMotionMagic extends Command {
    private double position;
    
    public static final double KF = 0.0;
    public static final double KP = 0.5;
	public static final double KI = 0.0;
    public static final double KD = 1;
    public static final int ACCELERATION = 5;
    public static final int CRUISE_VELOCITY = 200;

    public MoveWristMotionMagic (double position) {
        requires (Wrist.getInstance());
        this.position = position;                
    }            
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        System.out.println("entering wrist motion");
        Wrist.getInstance().setupMotionMagic();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Wrist.getInstance().setWrist(ControlMode.MotionMagic, position);
    }        
        
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished() {
        return true;//Math.abs(Wrist.getInstance().getMasterTalon().getClosedLoopError(Wrist.POSITION_SLOT)) < Wrist.ALLOWABLE_ERROR;
    }
}