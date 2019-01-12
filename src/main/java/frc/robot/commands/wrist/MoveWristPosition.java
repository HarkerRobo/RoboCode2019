package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.ControlMode;


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

    public MoveWristPosition (double position) {
        this.position = position;                
    }            
    
    @Override
    public void initialize() {
        Wrist.getInstance().getMasterTalon().selectProfileSlot (Wrist.POSITION_SLOT, Global.PID_PRIMARY);
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);
        Wrist.getInstance().getMasterTalon().setSensorPhase(Wrist.SENSOR_PHASE);        
    }
    
    @Override
    public void execute() {
        Wrist.getInstance().getMasterTalon().set(ControlMode.Position, position);
    }        
        
    @Override
    protected boolean isFinished() {
        return Math.abs(Wrist.getInstance().getMasterTalon().getClosedLoopError(Wrist.POSITION_SLOT)) < Wrist.ALLOWABLE_ERROR;
    }  
}