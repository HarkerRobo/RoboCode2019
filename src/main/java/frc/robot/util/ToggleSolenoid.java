package frc.robot.util;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ToggleSolenoid extends InstantCommand {

    private DoubleSolenoid solenoid;
    public ToggleSolenoid(DoubleSolenoid solenoid) {
        this.solenoid = solenoid;
    }

    public void initialize() {
        solenoid.set(PneumaticsUtil.switchSolenoidValue(solenoid.get()));
    }
}