package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Moves the wrist from one side of the elevator to the other safely.Elevator
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since 2/8/19
 */
public class Passthrough extends Command
{    
    public enum PassthroughType {
        LOW, HIGH
    }
    private PassthroughType type;

    private int desiredWristPos;
    private Robot.Side currentSide;
    private Robot.Side desiredSide;
    private CommandGroupWrapper commandGroup;

    public Passthrough(PassthroughType type, Robot.Side currentSide, int desiredWristPos) {
        this.desiredWristPos = desiredWristPos;
        this.desiredSide = Wrist.getInstance().getSide(desiredWristPos);
        this.currentSide = currentSide;
        this.type = type;
    }

    @Override
    public void initialize() {
        if (type == PassthroughType.HIGH) {
            if ((currentSide == Robot.Side.FRONT || currentSide == Robot.Side.AMBIGUOUS) 
                && Elevator.getInstance().isBelow(Elevator.RAIL_POSITION)) {
                commandGroup.sequential(new Passthrough(PassthroughType.LOW, currentSide, Wrist.MAX_BACKWARD_POSITION));
            }
            commandGroup.sequential(new MoveElevatorMotionMagic(Elevator.SAFE_HIGH_PASSTHROUGH_POSITION))
                        .sequential(new MoveWristMotionMagic(desiredWristPos));
        }
        else if (type == PassthroughType.LOW) {
            if (currentSide != desiredSide) {
                if ((currentSide == Robot.Side.FRONT || currentSide == Robot.Side.AMBIGUOUS) && 
                     Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
                        commandGroup.sequential (new Passthrough (PassthroughType.HIGH, currentSide, Wrist.MAX_BACKWARD_POSITION));
                }
                commandGroup.sequential(new MoveElevatorMotionMagic(Elevator.SAFE_LOW_PASSTHROUGH_POSITION));
            }
            commandGroup.sequential(new MoveWristMotionMagic(desiredWristPos));
         }

        commandGroup.start();
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
}