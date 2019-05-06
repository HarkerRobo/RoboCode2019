package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Climber.ClimberState;

/**
 * Sets climber state to the specified state.
 * 
 * @author Angela Jia
 * @since 4/12/19
 */
public class SetClimberState extends InstantCommand {

    private ClimberState state;

    public SetClimberState(ClimberState state) {
        requires(Climber.getInstance());
        this.state = state;
    }

    @Override
    public void initialize() {
        Robot.log("Set climber state to " + (state.getState() == Value.kForward ? "free" : "rigid"));
        Climber.getInstance().setClimberArmState(state.getState());
    }
}