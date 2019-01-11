package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

public class IntakeOuttakeBallManual extends Command{

	@Override
	protected boolean isFinished() {
		return false;
	}
	public void initialie(){

	}
	public void execute(){
		double rightTrigger = OI.getInstance().getDriverGamepad().getRightTrigger();
	}
  
}