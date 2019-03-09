package frc.robot.util;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command that runs a specific command if it is not equal to another command.
 * 
 * @author Chirag Kaushik
 * @since March 9, 2019
 */
public class RunIfNotEqualCommand extends ConditionalCommand {

    public RunIfNotEqualCommand(Command commandToRun, Command commandToCompare) {
        super(() -> (!commandToRun.equals(commandToCompare)), commandToRun);
    }
}