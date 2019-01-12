/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.												*/
/* Open Source Software - may be modified and shared by FRC teams. The code	 */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.																															 */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.BallIntake;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Wrist;

/**
 * Represents the core of the code, where the highest-level robot functions are
 * called.
 * 
 * @author Finn Frankis
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Angela Jia
 * 
 * @since 1/6/19
 */
public class Robot extends TimedRobot {

  private Drivetrain drivetrain;  
  private Arm arm;
  private Elevator elevator;
  private Rollers rollers;
  private Wrist wrist;
  private BallIntake ballIntake;
  private OI oi;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    drivetrain = Drivetrain.getInstance();
    arm = Arm.getInstance();
    elevator = Elevator.getInstance();
    rollers = Rollers.getInstance();
    wrist = Wrist.getInstance();
    oi = OI.getInstance();        
        
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during all robot modes (disabled, test,
   * autonomous, teleop).
   */
  @Override
  public void robotPeriodic() {
    NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = networkTable.getEntry("tx");
    NetworkTableEntry ty = networkTable.getEntry("ty");
    NetworkTableEntry ta = networkTable.getEntry("ta");
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    System.out.println(String.format("tx: %.2f ty: %.2f ta: %.2f", x, y, area));
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  /**
   * Gets the instance of the drivetrain on the robot.
   * @return the drivetrain
   */
  public Drivetrain getDrivetrain() {
    return drivetrain;
  }

    /**
   * Gets the instance of the elevator on the robot.
   * @return the elevator
   */
  public Elevator getElevator() {
    return elevator;
  }
  
  /**
   * Gets the instance of the wrist on the robot.
   * @return the wrist
   */
  public Wrist getWrist() {
    return wrist;
  }

  /**
   * Gets the instance of the rollers on the robot.
   * @return the rollers
   */
  public Rollers getRollers() {
    return rollers;
  }

  /**
   * Gets the instance of the arm on the robot.
   * @return the arm
   */
  public Arm getArm() {
    return arm;
  }

   /**
   * Gets the instance of the arm on the robot.
   * @return the arm
   */
  public BallIntake getBallIntake() {
    return ballIntake;
  }
}
