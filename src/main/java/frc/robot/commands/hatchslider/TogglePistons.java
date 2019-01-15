package frc.robot.commands.hatchslider;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchSlider;

/**
 * Command to toggle whether the Hatch Slider's 
 * solenoids are extended or contracted
 */
public class TogglePistons extends InstantCommand
{
    public TogglePistons()
    {
        requires(HatchSlider.getInstance());
    }

    public void initialize()
    {
        DoubleSolenoid solenoid = HatchSlider.getInstance().getSolenoid();
        if(solenoid.get() == DoubleSolenoid.Value.kForward)
            solenoid.set(DoubleSolenoid.Value.kReverse);
        else 
            solenoid.set(DoubleSolenoid.Value.kForward);
    }
}
