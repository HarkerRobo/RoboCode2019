package frc.robot.commands.groups;

import frc.robot.Robot;
import frc.robot.Robot.Side;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.hatchpanelintake.SetExtenderState;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.util.ConditionalCommand;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.SequentialCommandGroup;
import harkerrobolib.commands.CallMethodCommand;

/**
 * Brings all necessary subsystems inside the frame perimeter of the robot
 * in order to play defense legally.
 * 
 * @author Chirag Kaushik
 * @author Jatin Kohli
 * @since February 12, 2019
 */
public class StowHatchAndCargoIntake extends SequentialCommandGroup {

    public StowHatchAndCargoIntake () {
        super(new CallMethodCommand(() -> Robot.log("Entering defense mode.")), 
                new SetExtenderState(ExtenderDirection.IN),
                new ConditionalCommand(() -> (Wrist.getInstance().getCurrentSide() == Side.FRONT &&
                                                !Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)), 
                    new SetScoringPosition(Location.PARALLEL_FRONT), 
                    new SetScoringPosition(Location.PARALLEL_BACK)),
              new MoveWristMotionMagic(Wrist.MID_POSITION),
              new SetArmPosition(ArmDirection.UP));
    }
} 

// sequential(new CommandGroupWrapper()
        //     .parallel(new LoadOrScoreHatch(ScoreState.LOAD))
        //     .parallel(new CommandGroupWrapper().sequential(new ConditionalCommand(new PassthroughLow(Robot.Side.FRONT, Wrist.SAFE_BACKWARD_POSITION)) {
        //         @Override
        //         public boolean condition() {
        //             return Wrist.getInstance().getCurrentSide() == Robot.Side.FRONT && 
        //                    Elevator.getInstance().isAbove(Elevator.BALL_INTAKING_HEIGHT) &&
        //                    Arm.getInstance().getDirection() == ArmDirection.DOWN;
        //         }
        //         })
        //         .sequential(new SetArmPosition(ArmDirection.UP))
        //     )
        // );
        // sequential(new MoveWristPosition(Wrist.MID_POSITION));