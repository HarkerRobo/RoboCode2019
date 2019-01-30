package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.FlowerDirection;

public class SetFlowerManual extends InstantCommand {
    private FlowerDirection direction;
    public SetFlowerManual(FlowerDirection direction){
        requires(HatchLatcher.getInstance());
        this.direction = direction;
    }
    public void initialize(){
        HatchLatcher.getInstance().setFlowerState(direction);        
    }
}