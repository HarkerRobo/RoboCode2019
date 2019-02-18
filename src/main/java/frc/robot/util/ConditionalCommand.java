package frc.robot.util;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Represents a conditional command that uses a lambda to produce conditional values.
 * 
 * @author Chirag Kaushik
 * @since February 12, 2019
 */
public class ConditionalCommand extends edu.wpi.first.wpilibj.command.ConditionalCommand {
    private BooleanSupplier condition;

    public ConditionalCommand(BooleanSupplier condition, Command trueCommand) {
        super(trueCommand);
        this.condition = condition;
    }

    public ConditionalCommand(BooleanSupplier condition, Command trueCommand, Command falseCommand) {
        super(trueCommand, falseCommand);
        this.condition = condition;
    }

    @Override
    public boolean condition() {
        return condition.getAsBoolean();
    }
}