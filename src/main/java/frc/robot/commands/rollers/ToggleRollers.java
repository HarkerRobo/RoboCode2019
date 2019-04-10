package frc.robot.commands.rollers;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.intake.SpinIntakeVelocity;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeDirection;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import frc.robot.util.ConditionalCommand;

/**
 * Toggles the rollers.
 * 
 * @author Angela Jia
 * @since 4/10/19
 */
public class ToggleRollers extends CommandGroup {

    private boolean toggle; 

    public ToggleRollers() {
        toggle = false;
        addParallel(new ConditionalCommand(() -> toggle, 
                                            new SpinIntakeVelocity(IntakeDirection.IN, Intake.DEFAULT_INTAKE_VELOCITY), 
                                            new SpinIntakeVelocity(IntakeDirection.IN, 0)));
        addParallel(new ConditionalCommand(() -> toggle, 
                    new SpinRollersIndefinite(Rollers.getInstance().getRecommendedRollersInput(), RollerDirection.IN),
                    new SpinRollersIndefinite(Rollers.getInstance().getRecommendedRollersInput(), RollerDirection.IN)));
    }

    public void initialize() {
        toggle = !toggle;
    }

    public boolean getToggle() {
        return toggle;
    }
}