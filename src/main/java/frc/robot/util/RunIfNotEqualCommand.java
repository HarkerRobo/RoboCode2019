package frc.robot.util;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command that runs a specific command if it is not equal to another command.
 * 
 * @author Chirag Kaushik
 * @since March 9, 2019
 */
public class RunIfNotEqualCommand extends ConditionalCommand {

    public RunIfNotEqualCommand(Supplier<Command> commandToRun, Supplier<Command> commandToCompare) {
        super(() -> (!commandToRun.get().equals(commandToCompare.get())), commandToRun.get());
    }
}