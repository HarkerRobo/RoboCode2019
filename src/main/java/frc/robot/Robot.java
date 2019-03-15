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
import edu.wpi.first.wpilibj.command.WaitCommand;
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
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Wrist;
import frc.robot.util.Limelight;
import harkerrobolib.auto.CommandGroupWrapper;
import harkerrobolib.util.Conversions;

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
    private static Climber climber;
    private static HatchLatcher hatchLatcher;
    private static Limelight limelight;
    private static OI oi;
    private static double startTime;
    private static TalonSRX talon;
    private static PrintWriter pw;
    private static SetScoringPosition currentSetScoringCommand;

    private CommandGroupWrapper wrapper;
    private static final String LOG_FILE_PREFIX = "/home/lvuser/logs/";
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
        // arm = Arm.getInstance();
        elevator = Elevator.getInstance();
        intake = Intake.getInstance();
        rollers = Rollers.getInstance();
        wrist = Wrist.getInstance();
        climber = Climber.getInstance();
        // hatchLatcher = HatchLatcher.getInstance();
        // oi = OI.getInstance();        
        //limelight = Limelight.getInstance();        
       
        drivetrain.talonInit();
        elevator.talonInit();
        wrist.talonInit();
        rollers.talonInit();
        intake.controllerInit();
        climber.talonInit();
        Conversions.setWheelDiameter(Drivetrain.WHEEL_DIAMETER);

        // talon = new TalonSRX(CAN_IDs.WRIST_MASTER);
        // sparkMax = new CANSparkMax(CAN_IDs.BALL_INTAKE_MASTER, MotorType.kBrushless);
        // talon.configFactoryDefault();

        // elevator.getMasterTalon().get
        System.out.println("var vals " + Location.CARGO_INTAKE.getHasVariableValues());
        wrapper = new CommandGroupWrapper().sequential(new WaitCommand(1)).sequential(new WaitCommand(2));
    }

    /**
     * This function is run once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
         startTime = Timer.getFPGATimestamp(); 
        setupPrintWriter();
        log("Autonomous initialized.");
        new SetLimelightLEDMode(LEDMode.OFF).start();
        new SetLimelightViewMode(ViewMode.DRIVER).start();

        HatchLatcher.getInstance().setExtenderState(ExtenderDirection.IN);
        new SetArmPosition(ArmDirection.UP).start();
         
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
        wrapper.start();
        startTime = Timer.getFPGATimestamp();
        setupPrintWriter();
        log("Teleop initialized.");
        // Elevator.getInstance().setElevator(ControlMode.Disabled, 0);
        // Wrist.getInstance().setWrist(ControlMode.Disabled, 0);
        // Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        //Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    }

    /**
     * This function is called periodically during teleoperated mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Elevator Position", Elevator.getInstance().getMasterTalon().getSelectedSensorPosition());
        // talon.set(ControlMode.PercentOutput, OI.getInstance().getDriverGamepad().getRightY());
        // System.out.println("completed: " + cgw.isCompleted() + " " + cgw.isRunning());
        //talon.set(ControlMode.PercentOutput, OI.getInstance().getDriverGamepad().getRightX());
        // sparkMax.set(OI.getInstance().getDriverGamepad().getRightX());

        // SmartDashboard.putNumber("el current ", Elevator.getInstance().getFollowerTalon().getOutputCurrent());
        // System.out.println("Elevator position: " + Elevator.getInstance().getMasterTalon().getSelectedSensorPosition() + " Wrist position: " + Wrist.getInstance().getMasterTalon().getSelectedSensorPosition());
        
    }

    /**
     * This function is called periodically during all robot modes (disabled, test,
     * autonomous, teleop).
     */
    @Override
    public void robotPeriodic() {
        // SmartDashboard.putNumber("Left Error", drivetrain.getLeftMaster().getClosedLoopError(Global.PID_PRIMARY));
        // SmartDashboard.putNumber("Right Error", drivetrain.getRightMaster().getClosedLoopError(Global.PID_PRIMARY));
    
        SmartDashboard.putNumber("Wrist Position", Wrist.getInstance().getCurrentAngleDegrees());
        SmartDashboard.putNumber("Elevator Position", Elevator.getInstance().getMasterTalon().getSelectedSensorPosition());
        SmartDashboard.putNumber("LEFT Y", OI.getInstance().getDriverGamepad().getLeftY());
        SmartDashboard.putNumber("el current", Elevator.getInstance().getMasterTalon().getOutputCurrent());
        
        SmartDashboard.putBoolean("Is extended?", HatchLatcher.getInstance().getExtenderState() == ExtenderDirection.OUT);

        /* Necessary for custom dashboard. Do not remove. */
        SmartDashboard.putBoolean("Has hatch?", HatchLatcher.getInstance().hasHatch());
        SmartDashboard.putBoolean("Is scoring on cargo ship?", OI.getInstance().getCargoBayToggleMode());
        SmartDashboard.putBoolean("Has wrist manual control?", OI.getInstance().getWristToggleMode());
        SmartDashboard.putBoolean("Arm up?", Arm.getInstance().getDirection() == ArmDirection.UP);
        SmartDashboard.putNumber("date", System.currentTimeMillis());

        if (pw != null) {
            pw.flush();
        }
        // //System.out.println(limelight.getCamtranData());
        // SmartDashboard.putNumber("right y", OI.getInstance().getDriverGamepad().getRightY());
        // SmartDashboard.putNumber("right x", OI.getInstance().getDriverGamepad().getRightX());
        // SmartDashboard.putNumber("left y", OI.getInstance().getDriverGamepad().getLeftY());
        // SmartDashboard.putNumber("left x", OI.getInstance().getDriverGamepad().getLeftX());

        // System.out.println(OI.getInstance().getCustomOperatorGamepad().getBackwardThreePressed());
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
        // System.out.println("elevator limit " + elevator.getMasterTalon().getSensorCollection().isRevLimitSwitchClosed());
        // System.out.println("elevator limit fwd" + elevator.getMasterTalon().getSensorCollection().isFwdLimitSwitchClosed());
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
    public static Intake getIntake() {
        return intake;
    }

    /**
     * Gets the current time elapsed (in milliseconds) since the robot was last enabled, in either
     * autonomous or teleop.
     * @return the time elapsed
     */
    public static int getTime() {
        return (int) ((Timer.getFPGATimestamp() - startTime) * 1000);
    }
    @Override
    public void disabledInit() {
        log("Disabled initialized.");
        if (pw != null)
            pw.close();
        drivetrain.setNeutralMode(RobotMap.Global.DISABLED_NEUTRAL_MODE);

        elevator.getMasterTalon().set(ControlMode.Disabled, 0.0);
        wrist.getMasterTalon().set(ControlMode.Disabled, 0.0);

        ((MoveWristManual) Wrist.getInstance().getDefaultCommand()).disableClosedLoop();
        ((MoveElevatorManual) Elevator.getInstance().getDefaultCommand()).disableClosedLoop();
    }
    
    public static void log (String message) {
        String prefix = 150 - DriverStation.getInstance().getMatchTime() + "";
        System.out.println(prefix + ": " + message);
        if (pw != null) {
            pw.print(prefix + ": " + message + "\r\n");
        } 
    }

    private String getLogFileName () {
        try {
            System.out.println(new SimpleDateFormat("y-M-d H:m").format(new Date()));
        } catch(Exception e) {e.printStackTrace();}

        String name = DriverStation.getInstance().getEventName() + DriverStation.getInstance().getMatchType() + "Match" + DriverStation.getInstance().getMatchNumber() + ".txt";
        name = name.replaceAll(" ", "");
        return name;
    }

    private void setupPrintWriter () {
        if (logFileName.equals("")) {
            logFileName = getLogFileName();
        }

        if (pw == null) {
            try {
                new File(LOG_FILE_PREFIX + logFileName).createNewFile();
                pw = new PrintWriter(LOG_FILE_PREFIX + logFileName);
             } 
             catch (Exception e) {
                 e.printStackTrace();
             } 
        }
    }


    public static SetScoringPosition getSetScoringCommand() {
        return currentSetScoringCommand;
    }

     public static void setScoringCommand(SetScoringPosition scoringCommand) {
        currentSetScoringCommand = scoringCommand;
    }
}
