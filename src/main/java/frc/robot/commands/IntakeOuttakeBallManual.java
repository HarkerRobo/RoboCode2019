package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.subsystems.Rollers;
import harkerrobolib.util.MathUtil;

public class IntakeOuttakeBallManual extends Command{

	@Override
	protected boolean isFinished() {
		return false;
	}
	public void initialize(){

	}
	public boolean hasOperatorControllerInput(){
		if(MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getRightY(),OI.OPERATOR_DEADBAND)!=0){
			return true;
		}
		return false;
	}

	public void execute(){
		if (hasOperatorControllerInput()) {
			double rightY = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getRightY(),OI.OPERATOR_DEADBAND);
			Rollers.getInstance().getTopTalon().set(ControlMode.PercentOutput, rightY);
			Rollers.getInstance().getBottomTalon().set(ControlMode.PercentOutput, rightY*-1);
		}
		
		else{
			double rightTrigger = OI.getInstance().getDriverGamepad().getRightTrigger();
			Rollers.getInstance().getTopTalon().set(ControlMode.PercentOutput, rightTrigger);
			Rollers.getInstance().getBottomTalon().set(ControlMode.PercentOutput, rightTrigger*-1);
		}
	}
  
}