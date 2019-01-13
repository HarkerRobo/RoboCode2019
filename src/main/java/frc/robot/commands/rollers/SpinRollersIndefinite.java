package frc.robot.commands.rollers;

import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Spin the rollers at a certain magnitude and direction while button is pressed.
 * 
 * @author Chirag Kaushik
 * @since 1/12/19
 */
public class SpinRollersIndefinite extends IndefiniteCommand {
    private RollerDirection direction;
    private double magnitude;

    public SpinRollersIndefinite(double magnitude, RollerDirection direction) {
        requires(Rollers.getInstance());
        this.direction = direction;
        this.magnitude = magnitude;
    }

    @Override
    public void execute() {
        Rollers.getInstance().moveRollers(magnitude, direction);
    }    

    @Override
    public void end() {
        Rollers.getInstance().stopRollers();
    }

    @Override
    public void interrupted() {
        Rollers.getInstance().stopRollers();
    }
}