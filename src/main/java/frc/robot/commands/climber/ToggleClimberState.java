package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Climber;

/**
 * Toggles climber state.
 * 
 * @author Angela Jia
 * @since 4/12/19
 */
public class ToggleClimberState extends InstantCommand {

    public ToggleClimberState() {
        requires(Climber.getInstance());
    }

    @Override
    public void initialize() {
        Robot.log("Toggled climber state " + (Climber.getInstance().getState() == DoubleSolenoid.Value.kForward ? "free" : "rigid"));
        DoubleSolenoid.Value value = Climber.getInstance().getState() == DoubleSolenoid.Value.kForward ? DoubleSolenoid.Value.kReverse
            : DoubleSolenoid.Value.kForward;
        Climber.getInstance().setClimberArmState(value);
    }
}