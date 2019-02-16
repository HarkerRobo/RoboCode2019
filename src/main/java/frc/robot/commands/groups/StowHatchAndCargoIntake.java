package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.robot.Robot;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.groups.Passthrough.PassthroughType;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch.ScoreState;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * Brings all necessary subsystems inside the frame perimeter of the robot
 * in order to play defense legally.
 * 
 * @author Chirag Kaushik
 * @since February 12, 2019
 */
public class StowHatchAndCargoIntake extends CommandGroupWrapper {

    public StowHatchAndCargoIntake () {
        sequential(new CommandGroupWrapper()
            .parallel(new LoadOrScoreHatch(ScoreState.LOAD))
            .parallel(new CommandGroupWrapper().sequential(new ConditionalCommand(new Passthrough(PassthroughType.LOW, Robot.Side.FRONT, Wrist.SAFE_BACKWARD_POSITION)) {
                @Override
                public boolean condition() {
                    return Wrist.getInstance().getCurrentSide() == Robot.Side.FRONT && 
                           Elevator.getInstance().isAbove(Elevator.BALL_INTAKING_HEIGHT) &&
                           Arm.getInstance().getDirection() == ArmDirection.DOWN;
                }
                })
                .sequential(new SetArmPosition(ArmDirection.UP))
            )
        );
        sequential(new MoveWristPosition(Wrist.MID_POSITION));
    }
} 