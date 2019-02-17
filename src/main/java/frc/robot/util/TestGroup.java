package frc.robot.util;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class TestGroup extends CommandGroup {
    public TestGroup () {
        addSequential(new WaitCommand(2));
    }
}