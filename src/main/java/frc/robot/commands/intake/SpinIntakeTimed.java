package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeDirection;

/**
 * Intakes the ball into the robot.
 * 
 * @author Anirudh Kotamraju
 * @since 1/11/19
 */
public class SpinIntakeTimed extends TimedCommand {
    private double magnitude;
    private IntakeDirection direction;

    /**
     * Creates a new command that runs for a specific time.
     * @param time the time (in seconds) that the command should run for
     */
    public SpinIntakeTimed(double magnitude, double time, IntakeDirection direction) {
        super(time);
        requires(Intake.getInstance());
        this.magnitude = magnitude;
        this.direction = direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(){
        Intake.getInstance().setControllerOutput(magnitude,direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end(){
        Intake.getInstance().setControllerOutput(0,IntakeDirection.STOP); 
    }
}

