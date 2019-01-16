package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPusher;

/**
 * Sets Hatch Intake Direction
 * 
 * @author Chirag Kaushik
 * @since January 14 2019
 */
public class SetHatchPusherDirection extends InstantCommand {
    private HatchPusher.PushDirection direction;

    public SetHatchPusherDirection(HatchPusher.PushDirection direction) {
        this.direction = direction;
    }

    @Override
    public void initialize() {
        HatchPusher.getInstance().setSolenoidState(direction.getValue());        
    }
}               
