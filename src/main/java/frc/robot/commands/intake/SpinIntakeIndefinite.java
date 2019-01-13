package frc.robot.commands.intake;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Spins the intake indefinitely.
 * 
 * @author Chirag Kaushik
 * @since  1/12/19
 */
public class SpinIntakeIndefinite extends IndefiniteCommand {
    private IntakeDirection direction;
    private double magnitude;

    public SpinIntakeIndefinite(double magnitude, IntakeDirection direction) {
        requires(Intake.getInstance());
        this.magnitude = magnitude;
        this.direction = direction;
    }    

    @Override
    public void execute() {
        Intake.getInstance().setTalonOutput(magnitude, direction);
    }

    @Override
    public void end() {
        Intake.getInstance().setTalonOutput(0.0);
    }

    @Override
    public void interrupted() {
        Intake.getInstance().setTalonOutput(0.0);
    }
}