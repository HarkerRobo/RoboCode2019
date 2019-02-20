package frc.robot.commands.wrist;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.Global;
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
    private Supplier<Integer> setpointLambda;
    
    public static final double KF = 2.6;
    public static final double KP = 1.1;
	public static final double KI = 0.0025;
    public static final double KD = 100;
    public static final int IZONE = 150;
    public static final int ACCELERATION = 325;
    public static final int CRUISE_VELOCITY = 300;

    public MoveWristMotionMagic (double angle) {
        requires (Wrist.getInstance());
        this.position = Wrist.getInstance().convertDegreesToEncoder(angle);              
    }            

    public MoveWristMotionMagic (Supplier<Integer> setpointLambda) {
        super(0);
        this.setpointLambda = setpointLambda;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        if (setpointLambda != null) {this.position = Wrist.getInstance().convertDegreesToEncoder(setpointLambda.get());}
        Wrist.getInstance().setupMotionMagic();
        System.out.println("entering wrist motion " + position);
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
        return Math.abs(position - Wrist.getInstance().getMasterTalon().getSelectedSensorPosition()) <= Wrist.ALLOWABLE_ERROR;
    }

    @Override
    public void end () {
        System.out.println("wrist motion magic end");
    }

    @Override
    public void interrupted() {
        end();
    }
}