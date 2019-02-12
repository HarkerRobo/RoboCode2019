package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot.Side;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Goes to max height and then passes through.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since February 11, 2019
 */
public class PassthroughToHigh extends Command {
    
    private Side desiredSide;
    private CommandGroupWrapper commandGroup;
    private Side currentSide;

    public PassthroughToHigh(Side currentSide, Side desiredSide) {
        this.desiredSide = desiredSide;
        this.currentSide = currentSide;
    }

    @Override
    public void initialize() {
        if(desiredSide == Side.FRONT) {
            if(currentSide == Side.BACK) { //current side is back
                if (!Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
                    commandGroup.sequential(new Passthrough())
                                .sequential(new MoveElevatorMotionMagic(Elevator.HIGH_SCORING_POSITION));
                }
            }
            else {
                commandGroup.sequential(new MoveElevatorMotionMagic(Elevator.HIGH_SCORING_POSITION));
            }
        } else { //desired side is back
            if (currentSide == Side.FRONT) {
                commandGroup.sequential(new MoveElevatorMotionMagic(Elevator.HIGH_SCORING_POSITION));
            }
            else {
                if (!Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
                    commandGroup.sequential(new Passthrough())
                                .sequential(new MoveElevatorMotionMagic(Elevator.HIGH_SCORING_POSITION)); 
                }                                                                
            }                                                            
        }
        commandGroup.sequential(new MoveWristPosition(desiredSide == Side.FRONT ? Wrist.ANGLE_SCORING_FRONT : Wrist.ANGLE_SCORING_BACK));
        commandGroup.start();
    }

    @Override
    protected boolean isFinished() {
        return commandGroup.isCompleted();
    }
}