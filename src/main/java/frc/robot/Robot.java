/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.												*/
/* Open Source Software - may be modified and shared by FRC teams. The code	 */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.																															 */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.drivetrain.SetLimelightLEDMode;
import frc.robot.commands.drivetrain.SetLimelightLEDMode.LEDMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode.ViewMode;
import frc.robot.subsystems.Drivetrain;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.Global;
import frc.robot.commands.drivetrain.FollowPath;
import frc.robot.commands.drivetrain.GenerateAndFollowPath;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import frc.robot.util.Pair;
import frc.robot.util.Paths;
import harkerrobolib.auto.CommandGroupWrapper;
import harkerrobolib.util.Conversions;
import jaci.pathfinder.Waypoint;

/**
 * Represents the core of the code, where the highest-level robot functions are
 * called.
 *
 * @author Finn Frankis
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Angela Jia
 * @author Dawson Chen
 * @author Shahzeb Lakhani
 * @since 1/6/19
 */
public class Robot extends TimedRobot {

   private static Drivetrain drivetrain;
   private static Limelight limelight;
   private static OI oi;
   private static double startTime;

   // private CANSparkMax talon;

   public enum Side {
      FRONT, BACK, AMBIGUOUS;
   }

   /**
    * This function is run when the robot is first started up and should be used
    * for any initialization code.
    */
   @Override
   public void robotInit() {
      System.out.println("robotinit");
       drivetrain = Drivetrain.getInstance();
       oi = OI.getInstance();
       //limelight = Limelight.getInstance();
       drivetrain.talonInit();
      Conversions.setWheelDiameter(Drivetrain.WHEEL_DIAMETER);
   }

   /**
     * This function is run once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
         double dt = 0.05;

         // Waypoint[] points =
         // {
         //     new Waypoint(0, 0, 0),
         //     new Waypoint(3, 0, 0)
         // };
         // double cruiseVelocity = 5;

         // new GenerateAndFollowPath(points, dt, cruiseVelocity).start();
         new FollowPath(Paths.straightLineLeft, Paths.straightLineRight, dt).start();
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
   }

   /**
    * This function is called periodically during test mode.
    */
   @Override
   public void testPeriodic() {
   }

   @Override
   public void disabledInit() {
   }

   /**
    * Gets the instance of the drivetrain on the robot.
    * 
    * @return the drivetrain
    */
   public static Drivetrain getDrivetrain() {
      return drivetrain;
   }

   /**
    * Gets the current time elapsed (in milliseconds) since the robot was last
    * enabled, in either autonomous or teleop.
    * 
    * @return the time elapsed
    */
   public static int getTime() {
      return (int) ((Timer.getFPGATimestamp() - startTime) * 1000);
   }

   public static void log(String message) {
      // String prefix = 150 - DriverStation.getInstance().getMatchTime() + "";
      // System.out.println(prefix + ": " + message);
      // if (pw != null) {
      //    pw.print(prefix + ": " + message + "\r\n");
      // }
   }
}

