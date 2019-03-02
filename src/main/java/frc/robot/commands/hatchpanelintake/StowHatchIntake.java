package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.subsystems.HatchLatcher.FlowerDirection;
import harkerrobolib.auto.CommandGroupWrapper;

/**
 * 
 */
public class StowHatchIntake extends CommandGroupWrapper {
    public StowHatchIntake() {
        FlowerDirection oldFlowerDirection = HatchLatcher.getInstance().getFlowerState();
        sequential(new SetFlowerManual(FlowerDirection.CLOSED));
        sequential(new ConditionalCommand(new WaitCommand(HatchLatcher.FLOWER_ACTUATION_TIME)) {
           @Override 
           public boolean condition() {
               return oldFlowerDirection == FlowerDirection.OPEN;
           }
        });
        sequential(new SetExtenderState(ExtenderDirection.IN));
    }
}