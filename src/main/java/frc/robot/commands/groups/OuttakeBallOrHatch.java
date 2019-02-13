package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch.ScoreState;
import frc.robot.commands.rollers.SpinRollersIndefinite;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;


/**
 * Outtakes either a cargo ball or a hatch panel by detecting which game piece
 * the wrist contains. It will outtake a hatch panel only if the wrist contains one,
 * otherwise it will outtake cargo, regardless of whether there is a cargo ball.
 * 
 * @author Anirudh Kotamraju
 * @author Chirag Kaushik
 * @since February 12, 2019
 */
public class OuttakeBallOrHatch extends ConditionalCommand {
    public OuttakeBallOrHatch() {
        super(
            new LoadOrScoreHatch(ScoreState.SCORE), 
            new SpinRollersIndefinite(Rollers.DEFAULT_ROLLER_MAGNITUDE, RollerDirection.OUT));
        requires(HatchLatcher.getInstance());
        requires(Intake.getInstance());  
    }

    @Override
    public boolean condition() {
        return Drivetrain.getInstance().hasHatchPanel();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}