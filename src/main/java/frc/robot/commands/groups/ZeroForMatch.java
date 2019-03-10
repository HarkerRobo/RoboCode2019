// package frc.robot.commands.groups;

// import frc.robot.commands.elevator.ZeroElevator;
// import frc.robot.commands.wrist.ZeroWrist;
// import harkerrobolib.auto.SequentialCommandGroup;

// /**
//  * Zeroes both the wrist and the elevator then puts the wrist into the stowed defense mode.
//  * 
//  * @author Finn Frankis
//  * @since 3/8/19
//  */
// public class ZeroForMatch extends SequentialCommandGroup {
//     public ZeroForMatch() {
//         super(
//             new ZeroElevator(),
//             new ZeroWrist(),
//             new StowHatchAndCargoIntake()
//         );
//     }
// }