package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;
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
public class MoveWristPosition extends Command {
    private double position;
    
	public static final double KF = 0.0;
	public static final double KD = 0.0;
	public static final double KI = 0.0;
	public static final double KP = 0.0;

    public MoveWristPosition (double position) {
        requires (Wrist.getInstance());
        this.position = position;                
    }            
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        Wrist.getInstance().getMasterTalon().selectProfileSlot (Wrist.POSITION_SLOT, Global.PID_PRIMARY);
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);
        Wrist.getInstance().getMasterTalon().setSensorPhase(Wrist.SENSOR_PHASE);     
        

        Wrist.getInstance().getMasterTalon().config_kP(Wrist.POSITION_SLOT, MoveWristPosition.KP);
        Wrist.getInstance().getMasterTalon().config_kI(Wrist.POSITION_SLOT, MoveWristPosition.KI);
        Wrist.getInstance().getMasterTalon().config_kD(Wrist.POSITION_SLOT, MoveWristPosition.KD);
        Wrist.getInstance().getMasterTalon().config_kF(Wrist.POSITION_SLOT, MoveWristPosition.KF);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        Wrist.getInstance().getMasterTalon().set(ControlMode.Position, position);
    }        
        
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished() {
        return Math.abs(Wrist.getInstance().getMasterTalon().getClosedLoopError(Wrist.POSITION_SLOT)) < Wrist.ALLOWABLE_ERROR;
    }  
}