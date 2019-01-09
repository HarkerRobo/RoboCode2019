package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

/**
 * Turn to specified angle.
 * 
 * @author Angela Jia
 * @version 1/8/19
 */
public class TurnToAngle extends Command {

    public TurnToAngle( ) {
        requires(Drivetrain.getInstance());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

    }
}