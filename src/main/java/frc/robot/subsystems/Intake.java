package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;
import harkerrobolib.util.Constants;

/**
 * Intakes the cargo into the robot.
 * 
 * @author Finn Frankis
 * @author Anirudh Kotamraju
 * @author Angela Jia
 * @since 1/11/19
 */
public class Intake extends Subsystem {
   public enum IntakeDirection {
      IN(1), OUT(-1), STOP(0);
      private int sign;

      private IntakeDirection(int sign) {
         this.sign = sign;
      }

      public int getSign() {
         return sign;
      }
   }

   private static Intake instance;
   private VictorSPX intakeVictor;
   private CANSparkMax intakeSpark;
   private boolean isSpark;

   private final static int STALL_LIMIT = 40; // current limit (amps) when the robot is stopped
   private final static int FREE_LIMIT = 30; // current limit (amps) when the robot is moving freely

   private final static boolean SPARK_INVERTED;
   private final static boolean VICTOR_INVERTED;
   private final static NeutralMode VICTOR_NEUTRAL_MODE = NeutralMode.Brake;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         SPARK_INVERTED = true;
         VICTOR_INVERTED = true;
      } else {
         SPARK_INVERTED = true;
         VICTOR_INVERTED = true;
      }

   }
   public final static double DEFAULT_INTAKE_MAGNITUDE = 0.8;

   public void setControllerOutput(double magnitude, IntakeDirection direction) {
      setControllerOutput(magnitude * direction.getSign());
   }

   public void setControllerOutput(double percentOutput) {
      // getController().set(percentOutput);
      if (isSpark) {
         intakeSpark.set(percentOutput);
      } else {
         intakeVictor.set(ControlMode.PercentOutput, percentOutput);
      }
   }

   private Intake() {
    //   if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
    //      System.out.println("Spark: " + CAN_IDs.BALL_INTAKE_MASTER_SPARK);
    //      intakeSpark = new CANSparkMax(CAN_IDs.BALL_INTAKE_MASTER_SPARK, MotorType.kBrushless);
    //   } else {
    //      intakeVictor = new VictorSPX(CAN_IDs.BALL_INTAKE_MASTER_VICTOR);
    //   }
      isSpark = RobotMap.ROBOT_TYPE == RobotType.COMP;
      // intakeVictor = new
      // VictorSPX(CAN_IDs.BALL_INTAKE_MASTER);//CANSparkMax(CAN_IDs.BALL_INTAKE_MASTER,
      // MotorType.kBrushless);
   }

   public void controllerInit() {
      if (isSpark) {
         intakeSpark.setInverted(SPARK_INVERTED);
         intakeSpark.setSmartCurrentLimit(STALL_LIMIT, FREE_LIMIT);
         intakeSpark.setCANTimeout(Constants.DEFAULT_TIMEOUT);
      } else {
         intakeVictor.setInverted(VICTOR_INVERTED);
         intakeVictor.setNeutralMode(VICTOR_NEUTRAL_MODE);
      }
   }

   public static Intake getInstance() {
      if (instance == null) {
         instance = new Intake();
      }
      return instance;
   }

   @Override
   protected void initDefaultCommand() {
      // setDefaultCommand(new SpinIntakeManual());
   }

   // public double getEncoderPosition () {
   // return Intake.getInstance().getController().getEncoder().getPosition();
   // }

   // public double getEncoderVelocity () {
   // return Intake.getInstance().getController().getEncoder().getVelocity();
   // }
}