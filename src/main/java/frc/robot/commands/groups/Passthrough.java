package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Moves the wrist from one side of the elevator to the other safely.Elevator
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @since 2/8/19
 */
public class Passthrough extends Command
{    
    private int desiredWristPosition;
    private int safeElevatorHeight;
    private CommandGroupWrapper commandGroup;

    public Passthrough () {
        this (Wrist.SAFE_FORWARD_POSITION);
    }
    public Passthrough(int desiredWristPos) {    
        requires(Wrist.getInstance());
        requires(Elevator.getInstance());
        
        this.desiredWristPosition = desiredWristPos;
    }

    @Override
    public void initialize() {
        // Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);
        // Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Global.PID_PRIMARY);

        // boolean needsToPassThrough = Wrist.getInstance().mustPassThrough(desiredWristPosition);
         
        // commandGroup = new CommandGroupWrapper();

        // if(needsToPassThrough && Elevator.getInstance().isBelow(Elevator.RAIL_POSITION)) {
        //     commandGroup.sequential(new MoveElevatorPosition(safeElevatorHeight));
        // }
        // commandGroup.sequential(new MoveWristPosition(desiredWristPosition));    
        // commandGroup.start();           
        // TODO: ALWAYS GO TO BOTTOM
    }

    @Override
    public boolean isFinished() {
        return commandGroup.isCompleted();       
    } 
    
    @Override
    public void interrupted()
    {
        commandGroup.cancel();
    }

    public void clearRequirements()
    {
        super.clearRequirements();
    }
}