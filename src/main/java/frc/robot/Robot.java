/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.												*/
/* Open Source Software - may be modified and shared by FRC teams. The code	 */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.																															 */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
	@Override
	public void robotInit() {

	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		
	}

	@Override
	public void robotPeriodic() {
		NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("limelight");
		NetworkTableEntry tx = networkTable.getEntry("tx");
		NetworkTableEntry ty = networkTable.getEntry("ty");
		NetworkTableEntry ta = networkTable.getEntry("ta");
	  
		System.out.println("tx: " + tx.getDouble(0.0) + "; ty: " + ty.getDouble(0.0) + "; ta: " + ta.getDouble(0.0));
	}

	@Override
	public void testPeriodic() {

	}
}
