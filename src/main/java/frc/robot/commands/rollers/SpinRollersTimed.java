package frc.robot.commands.rollers;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
/**
 * Moves the rollers for a certain time.
 * 
 * @since 1/12/19
 */
public class SpinRollersTimed extends TimedCommand
{
    private double magnitude;
    private RollerDirection direction;

    public SpinRollersTimed(double magnitude, double time, RollerDirection direction) {
        super(time);
        requires(Rollers.getInstance());
        this.magnitude = magnitude; 
        this.direction = direction;       
    }

    @Override
    public void execute()
    {
        Rollers.getInstance().moveRollers(magnitude, direction);
    }

    @Override
    public void end()
    {
        Rollers.getInstance().stopRollers();                        
    }

    @Override
    public void interrupted()
    {
        end();
    }

}