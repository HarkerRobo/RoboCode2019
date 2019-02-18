package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.WaitCommand;
import harkerrobolib.auto.CommandGroupWrapper;
import harkerrobolib.commands.PrintCommand;

public class TestCommand extends Command {
    private CommandGroupWrapper wrapper;

    public TestCommand() {
        wrapper = new CommandGroupWrapper();
    }

    @Override
    public void initialize() {
        wrapper.sequential(new PrintCommand("first command"));
        if(Math.random() > 0.5) {
            wrapper.sequential(new PrintCommand("waiting for 1 sec"))
                   .sequential(new WaitCommand(1));
        }
        wrapper.sequential(new PrintCommand("Done waiting"));
        wrapper.sequential(new WaitCommand(1));
        if(Math.random() > 0.5) {
            wrapper.sequential(new PrintCommand("something else"));
        }
        wrapper.sequential(new PrintCommand("Last command"));
        wrapper.start();
    }

    @Override
    public void end() {
        System.out.println("DONE");
    }

    public boolean isFinished() {
        return wrapper.isCompleted();
    }

    public void interrupted() {
        System.out.println("Interrupted");
        end();
    }
}