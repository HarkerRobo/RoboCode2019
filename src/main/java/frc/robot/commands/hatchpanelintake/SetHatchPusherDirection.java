package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPusher;

/**
 * Sets the hatch's intake to a specified direction.
 * 
 * @author Chirag Kaushik
 * @since  1/14/19
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
