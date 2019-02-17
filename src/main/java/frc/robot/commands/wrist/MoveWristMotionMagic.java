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
    
    public static final double KF = 2.6;
    public static final double KP = 1.1;
	public static final double KI = 0.0025;
    public static final double KD = 100;
    public static final int IZONE = 150;
    public static final int ACCELERATION = 450;
    public static final int CRUISE_VELOCITY = 400;

    public MoveWristMotionMagic (double angle) {
        requires (Wrist.getInstance());
        this.position = Wrist.getInstance().convertDegreesToEncoder(angle);              
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
        SmartDashboard.putNumber("Wrist Error", Wrist.getInstance().getMasterTalon().getClosedLoopError());
        Wrist.getInstance().setWrist(ControlMode.MotionMagic, position);
    }        
        
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished() {
        return true;//Math.abs(Wrist.getInstance().getMasterTalon().getClosedLoopError(Wrist.POSITION_SLOT)) < Wrist.ALLOWABLE_ERROR;
    }

    @Override
    public void end () {
        System.out.println("wrist motion magic end");
        Wrist.getInstance().setWrist(ControlMode.Disabled, 0);
    }

    @Override
    public void interrupted() {
        end();
    }
}