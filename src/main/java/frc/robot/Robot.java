/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.												*/
/* Open Source Software - may be modified and shared by FRC teams. The code	 */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.																															 */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.commands.drivetrain.SetLimelightLEDMode;
import frc.robot.commands.drivetrain.SetLimelightLEDMode.LEDMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode.ViewMode;
import frc.robot.commands.elevator.MoveElevatorManual;
import frc.robot.commands.groups.SetScoringPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.wrist.MoveWristManual;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Wrist;
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
   private static Arm arm;
   private static Elevator elevator;
   private static Rollers rollers;
   private static Wrist wrist;
   private static Intake intake;
   //private static Climber climber;
   private static HatchLatcher hatchLatcher;
   private static Limelight limelight;
   private static OI oi;
   private static double startTime;
   private static TalonSRX talon;
   private static PrintWriter pw;
   private static SetScoringPosition currentSetScoringCommand;

   private static final String LOG_FILE_PREFIX = "/home/lvuser/logs/";
   private static String logFileDir = "";
   private static String logFileName = "";

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
    //    arm = Arm.getInstance();
    //    elevator = Elevator.getInstance();
    //    intake = Intake.getInstance();
    //    rollers = Rollers.getInstance();
    //    wrist = Wrist.getInstance();
       //climber = Climber.getInstance();
       //hatchLatcher = HatchLatcher.getInstance();
       oi = OI.getInstance();
       //limelight = Limelight.getInstance();
       drivetrain.talonInit();
    //    elevator.talonInit();
    //    wrist.talonInit();
    //    rollers.talonInit();
    //    intake.controllerInit();
      // climber.talonInit();
      Conversions.setWheelDiameter(Drivetrain.WHEEL_DIAMETER);

      // talon = new TalonSRX(CAN_IDs.WRIST_MASTER);
      // sparkMax = new CANSparkMax(CAN_IDs.BALL_INTAKE_MASTER, MotorType.kBrushless);
      // talon.configFactoryDefault();

      // elevator.getMasterTalon().get
      //System.out.println("var vals " + Location.CARGO_INTAKE.getHasVariableValues());
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

   /**
    * Gets the instance of the drivetrain on the robot.
    * 
    * @return the drivetrain
    */
   public static Drivetrain getDrivetrain() {
      return drivetrain;
   }

//    /**
//     * Gets the instance of the elevator on the robot.
//     * 
//     * @return the elevator
//     */
//    public static Elevator getElevator() {
//       return elevator;
//    }

//    /**
//     * Gets the instance of the wrist on the robot.
//     * 
//     * @return the wrist
//     */
//    public static Wrist getWrist() {
//       return wrist;
//    }

//    /**
//     * Gets the instance of the rollers on the robot.
//     * 
//     * @return the rollers
//     */
//    public static Rollers getRollers() {
//       return rollers;
//    }

//    /**
//     * Gets the instance of the arm on the robot.
//     * 
//     * @return the arm
//     */
//    public static Arm getArm() {
//       return arm;
//    }

//    /**
//     * Gets the instance of the arm on the robot.
//     * 
//     * @return the arm
//     */
//    public static Intake getIntake() {
//       return intake;
//    }

   /**
    * Gets the current time elapsed (in milliseconds) since the robot was last
    * enabled, in either autonomous or teleop.
    * 
    * @return the time elapsed
    */
   public static int getTime() {
      return (int) ((Timer.getFPGATimestamp() - startTime) * 1000);
   }

   @Override
   public void disabledInit() {
      //log("Disabled initialized.");
      //resetPrintWriter();
      // drivetrain.setNeutralMode(RobotMap.Global.DISABLED_NEUTRAL_MODE);

      // elevator.getMasterTalon().set(ControlMode.Disabled, 0.0);
      // wrist.getMasterTalon().set(ControlMode.Disabled, 0.0);

      // ((MoveWristManual)
      // Wrist.getInstance().getDefaultCommand()).disableClosedLoop();
      // ((MoveElevatorManual)
      // Elevator.getInstance().getDefaultCommand()).disableClosedLoop();
   }

   public static void log(String message) {
      String prefix = 150 - DriverStation.getInstance().getMatchTime() + "";
      System.out.println(prefix + ": " + message);
      if (pw != null) {
         pw.print(prefix + ": " + message + "\r\n");
      }
   }

   /**
    * Gets the log file name and its corresponding directory.
    * 
    * @return a pair with the first element as the file's directory (relative to
    *         LOG_FILE_PREFIX) and the second element as the file's name
    */
   private Pair<String, String> getLogFileNameAndDirectory() {
      String directory = "matches/" + DriverStation.getInstance().getEventName() + "/";
      String name = DriverStation.getInstance().getEventName() + DriverStation.getInstance().getMatchType() + "Match"
            + DriverStation.getInstance().getMatchNumber();
      name = name.replaceAll(" ", "");

      if (name.indexOf("None") >= 0) {
         String date = new SimpleDateFormat("y-M-d_H-m-s").format(new Date());
         name = date;
         date = date.substring(0, date.lastIndexOf("-"));
         directory = date.substring(0, date.indexOf("_")) + "/" + date.substring(date.indexOf("_") + 1, date.lastIndexOf("-")) + ":00/";
      }

      name += ".txt";
      return new Pair<String, String>(directory, name);
   }

   private String getLogFileName() {
      return "";
   }

   private void setupPrintWriter() {
      if (pw == null) {
         if (logFileName.equals("") || logFileDir.equals("")) {
            logFileName = getLogFileNameAndDirectory().getSecond();
            logFileDir = getLogFileNameAndDirectory().getFirst();
         }

         try {
            String fileDir = LOG_FILE_PREFIX + logFileDir;
            String fileLoc = fileDir + logFileName;

            System.out.println("DIR " + fileDir + " LOC " + fileLoc);
            File desiredDirectory = new File(fileDir);
            while (desiredDirectory.mkdirs()); // to make all nested directories

            new File(fileLoc).createNewFile();
            pw = new PrintWriter(fileLoc);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   private void resetPrintWriter() {
      if (pw != null) {
         pw.close();
      }
      pw = null;
      logFileName = "";
      logFileDir = "";
   }

   public static SetScoringPosition getSetScoringCommand() {
      return currentSetScoringCommand;
   }

   public static void setScoringCommand(SetScoringPosition scoringCommand) {
      currentSetScoringCommand = scoringCommand;
   }
}
