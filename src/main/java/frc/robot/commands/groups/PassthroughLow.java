// package frc.robot.commands.groups;

// import edu.wpi.first.wpilibj.command.CommandGroup;
// import frc.robot.Robot;
// import frc.robot.commands.arm.SetArmPosition;
// import frc.robot.commands.elevator.MoveElevatorMotionMagic;
// import frc.robot.commands.wrist.MoveWristMotionMagic;
// import frc.robot.subsystems.Arm.ArmDirection;
// import frc.robot.subsystems.Elevator;
// import frc.robot.subsystems.Wrist;
// import frc.robot.util.ConditionalCommand;
// import harkerrobolib.auto.SequentialCommandGroup;

// /**
//  * Moves the wrist from one side of the elevator to the other safely.
//  * 
//  * @author Finn Frankis
//  * @author Angela Jia
//  * @author Chirag Kaushik
//  * @since 2/8/19
//  */
// public class PassthroughLow extends CommandGroup
// { 
//     public PassthroughLow(Robot.Side currentSide, int desiredWristPos) {
//         Robot.Side desiredSide = Wrist.getInstance().getSide(desiredWristPos);

//         addSequential(new ConditionalCommand(() -> desiredSide == Robot.Side.FRONT, new SetArmPosition(ArmDirection.DOWN)));
//         addSequential(new ConditionalCommand(() -> currentSide != desiredSide, new SequentialCommandGroup(
//             new ConditionalCommand(
//                 () -> (currentSide == Robot.Side.FRONT || currentSide == Robot.Side.AMBIGUOUS) && 
//                     Elevator.getInstance().isAbove(Elevator.RAIL_POSITION), 
//                 new PassthroughHigh (currentSide, Wrist.MAX_BACKWARD_POSITION)), 
//             new MoveElevatorMotionMagic(Elevator.SAFE_LOW_PASSTHROUGH_POSITION))));
//         addSequential(new MoveWristMotionMagic(desiredWristPos));
//     }

//     public void end () {
//         System.out.println("PASSTHROUGH OVER LOW");
//     }
// }