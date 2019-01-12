package frc.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
/**
 * Moves the rollers for a certain time
 */
public class MoveRollersTimed extends TimedCommand
{
    private double magnitude;
    private RollerDirection direction;

    public MoveRollersTimed(double magnitude, double time, RollerDirection direction) {
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