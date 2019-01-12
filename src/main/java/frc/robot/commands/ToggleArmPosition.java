package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Arm;

public class ToggleArmPosition extends InstantCommand {
    @Override
    public void execute() {
        DoubleSolenoid.Value newValue = Arm.getInstance().getState() == DoubleSolenoid.Value.kForward ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward;
        Arm.getInstance().setState(newValue);
    }
}