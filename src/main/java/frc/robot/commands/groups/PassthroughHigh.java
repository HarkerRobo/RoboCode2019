// package frc.robot.commands.groups;

// import edu.wpi.first.wpilibj.command.CommandGroup;
// import frc.robot.Robot;
// import frc.robot.commands.elevator.MoveElevatorMotionMagic;
// import frc.robot.commands.wrist.MoveWristMotionMagic;
// import frc.robot.subsystems.Elevator;
// import frc.robot.subsystems.Wrist;
// import frc.robot.util.ConditionalCommand;

// /**
//  * Moves the wrist from one side of the elevator to the other safely.
//  * 
//  * @author Finn Frankis
//  * @author Angela Jia
//  * @author Chirag Kaushik
//  * @since 2/8/19
//  */
// public class PassthroughHigh extends CommandGroup
// {   
//     public PassthroughHigh(Robot.Side currentSide, int desiredWristPos) {
//         addSequential(
//             new ConditionalCommand(() -> (currentSide == Robot.Side.FRONT || currentSide == Robot.Side.AMBIGUOUS) 
//                                             && Elevator.getInstance().isBelow(Elevator.RAIL_POSITION), 
//                                     new PassthroughLow(currentSide, Wrist.MAX_BACKWARD_POSITION)));
//         addSequential(new MoveElevatorMotionMagic(Elevator.SAFE_HIGH_PASSTHROUGH_POSITION));
//         addSequential(new MoveWristMotionMagic(desiredWristPos));
//     }

//     public void end () {
//         System.out.println("PASSTHROUGH OVER HIGH");
//     }
// }