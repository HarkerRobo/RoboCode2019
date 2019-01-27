/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.												*/
/* Open Source Software - may be modified and shared by FRC teams. The code	 */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.																															 */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.drivetrain.AlignWithLimelight;
import frc.robot.commands.drivetrain.AlignWithLimelightIndefinite;
import frc.robot.commands.drivetrain.DriveWithVelocityTimed;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchPusher;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Wrist;
import frc.robot.util.Limelight;
import harkerrobolib.auto.SequentialCommandGroup;

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

    private Drivetrain drivetrain;    
    private Arm arm;
    private Elevator elevator;
    private Rollers rollers;
    private Wrist wrist;
    private Intake ballIntake;
    private HatchPusher hatchPusher;
    private Limelight limelight;
    private OI oi;

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
        //hatchPusher = HatchPusher.getInstance();
        oi = OI.getInstance();        
        limelight = Limelight.getInstance();        
                
        drivetrain.talonInit();
    }

    /**
     * This function is run once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
         new SequentialCommandGroup(
             new AlignWithLimelight(198, 0, 4)
            //  new DriveWithVelocityTimed(2, -0.3)
         ).start();
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
        // System.out.printf(
        //     "tx0: %f ty0: %f ta0: %f ts0: %f", 
        //     limelight.getRawContourTx(0),
        //     limelight.getRawContourTy(0),
        //     limelight.getRawContourTa(0),
        //     limelight.getRawContourTs(0)
        // );
        // System.out.println("fused heading: " + drivetrain.getPigeon().getFusedHeading());
        System.out.println("Thor: " + limelight.getThor());
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
    public Intake getBallIntake() {
        return ballIntake;
    }
}
