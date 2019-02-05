/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.												*/
/* Open Source Software - may be modified and shared by FRC teams. The code	 */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.																															 */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Wrist;
import frc.robot.util.Limelight;
import harkerrobolib.util.Conversions;
import harkerrobolib.wrappers.HSTalon;

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
    private static Arm arm;
    private static Elevator elevator;
    private static Rollers rollers;
    private static Wrist wrist;
    private static Intake ballIntake;
    private static HatchLatcher hatchLatcher;
    private static Limelight limelight;
    private static OI oi;

    private static double startTime;
    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        drivetrain = Drivetrain.getInstance();
        //arm = Arm.getInstance();
        //elevator = Elevator.getInstance();
        //rollers = Rollers.getInstance();
        //wrist = Wrist.getInstance();
        //hatchLatcher = HatchLatcher.getInstance();
        oi = OI.getInstance();        
        //limelight = Limelight.getInstance();        
       
        drivetrain.talonInit();
        // drivetrain.getPigeon().setFusedHeading(0);
        Conversions.setWheelDiameter(Drivetrain.WHEEL_DIAMETER);
    }

    /**
     * This function is run once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
         startTime = Timer.getFPGATimestamp();
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
        startTime = Timer.getFPGATimestamp();
    }

    /**
     * This function is called periodically during teleoperated mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Current", Drivetrain.getInstance().getLeftMaster().getOutputCurrent());
    }

    /**
     * This function is called periodically during all robot modes (disabled, test,
     * autonomous, teleop).
     */
    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("joystick data Y", oi.getDriverGamepad().getLeftY());
        SmartDashboard.putNumber("joystick data X", oi.getDriverGamepad().getLeftX());
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
    public static Drivetrain getDrivetrain() {
        return drivetrain;
    }

    /**
     * Gets the instance of the elevator on the robot.
     * @return the elevator
     */
    public static Elevator getElevator() {
        return elevator;
    }
    
    /**
     * Gets the instance of the wrist on the robot.
     * @return the wrist
     */
    public static Wrist getWrist() {
        return wrist;
    }

    /**
     * Gets the instance of the rollers on the robot.
     * @return the rollers
     */
    public static Rollers getRollers() {
        return rollers;
    }

    /**
     * Gets the instance of the arm on the robot.
     * @return the arm
     */
    public static Arm getArm() {
        return arm;
    }

     /**
     * Gets the instance of the arm on the robot.
     * @return the arm
     */
    public static Intake getBallIntake() {
        return ballIntake;
    }

    /**
     * Gets the current time elapsed (in milliseconds) since the robot was last enabled, in either
     * autonomous or teleop.
     * @return the time elapsed
     */
    public static int getTime() {
        return (int) ((Timer.getFPGATimestamp() - startTime) * 1000);
    }
}
