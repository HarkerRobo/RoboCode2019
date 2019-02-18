package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Moves the wrist from one side of the elevator to the other safely.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since 2/8/19
 */
public class PassthroughLow extends Command
{   
    private int desiredWristPos;
    private Robot.Side currentSide;
    private Robot.Side desiredSide;
    private CommandGroupWrapper commandGroup;

    public PassthroughLow(Robot.Side currentSide, int desiredWristPos) {
        this.desiredWristPos = desiredWristPos;
        this.desiredSide = Wrist.getInstance().getSide(desiredWristPos);
        this.currentSide = currentSide;
    }

    @Override
    public void initialize() {
        System.out.println("starting a passthrough low " + " from " + currentSide + " to " + desiredSide);
        commandGroup = new CommandGroupWrapper();
        System.out.println ("starting low passthrough");
        if (desiredSide == Robot.Side.FRONT) {commandGroup.sequential(new SetArmPosition(ArmDirection.DOWN)); System.out.println("ac");}
        if (currentSide != desiredSide) {
            if ((currentSide == Robot.Side.FRONT || currentSide == Robot.Side.AMBIGUOUS) && 
                    Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)) {
                    commandGroup.sequential (new PassthroughHigh (currentSide, Wrist.MAX_BACKWARD_POSITION));
            System.out.println("aa");
            }
            commandGroup.sequential(new MoveElevatorMotionMagic(Elevator.SAFE_LOW_PASSTHROUGH_POSITION));
            System.out.println("ab");
        }
        commandGroup.sequential(new MoveWristMotionMagic(desiredWristPos));
        System.out.println("ad");

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

    public void end () {
        System.out.println("PASSTHROUGH OVER LOW");
    }
}