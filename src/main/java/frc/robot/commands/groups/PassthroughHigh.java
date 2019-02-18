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
import harkerrobolib.commands.PrintCommand;

/**
 * Moves the wrist from one side of the elevator to the other safely.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since 2/8/19
 */
public class PassthroughHigh extends Command
{    
    private int desiredWristPos;
    private Robot.Side currentSide;
    private Robot.Side desiredSide;
    private CommandGroupWrapper commandGroup;

    public PassthroughHigh(Robot.Side currentSide, int desiredWristPos) {
        this.desiredWristPos = desiredWristPos;
        this.desiredSide = Wrist.getInstance().getSide(desiredWristPos);
        this.currentSide = currentSide;
    }

    @Override
    public void initialize() {
        System.out.println("starting a passthrough high from " + currentSide + " to " + desiredSide);
        commandGroup = new CommandGroupWrapper();

        if ((currentSide == Robot.Side.FRONT || currentSide == Robot.Side.AMBIGUOUS) 
            && Elevator.getInstance().isBelow(Elevator.RAIL_POSITION)) {
                System.out.println("we are below rail pos and currently front/ambig");
            commandGroup.sequential(new PassthroughLow(currentSide, Wrist.MAX_BACKWARD_POSITION));
        }
        commandGroup.sequential(new PrintCommand("printcommand after passthrough low!"));
        System.out.println("adding a elevatormotionmagic/movewristmotionmagic");
        commandGroup.sequential(new MoveElevatorMotionMagic(Elevator.SAFE_HIGH_PASSTHROUGH_POSITION))
                    .sequential(new MoveWristMotionMagic(desiredWristPos));
        
        commandGroup.sequential(new PrintCommand("printcommand command done"));
        commandGroup.start();
    }

    @Override
    public boolean isFinished() {
        return commandGroup.isCompleted();       
    } 
    
    @Override
    public void interrupted()
    {
        System.out.println("Interrupted");
        commandGroup.cancel(); 
    }

    public void end () {
        System.out.println("PASSTHROUGH OVER HIGH");
    }
}