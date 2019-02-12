package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.commands.intake.SpinIntakeIndefinite;
import frc.robot.commands.intake.SpinIntakeManual;
import harkerrobolib.util.Constants;

/**
 * Intakes the cargo into the robot.
 * 
 * @author Finn Frankis
 * @author Anirudh Kotamraju
 * @since 1/11/19
 */
public class Intake extends Subsystem {
    public enum IntakeDirection {
        IN(1),OUT(-1),STOP(0);
        private int sign;
        private IntakeDirection(int sign) {
            this.sign = sign;
        }
        public int getSign() {
            return sign;
        }
    }

    private static Intake instance;
    private CANSparkMax intakeSparkMax;

    private final static int STALL_LIMIT = 20; // current limit (amps) when the robot is stopped
    private final static int FREE_LIMIT = 15; // current limit (amps) when the robot is moving freely

    private final static boolean CONTROLLER_INVERTED = false;

    public final static double DEFAULT_INTAKE_MAGNITUDE = 1.0;

    public CANSparkMax getController() {
        return intakeSparkMax;
    }

    public void setControllerOutput(double magnitude, IntakeDirection direction) {
        setControllerOutput (magnitude * direction.getSign());
    }

    public void setControllerOutput(double percentOutput) {
        getController().set(percentOutput);
    }

    private Intake() {
        intakeSparkMax = new CANSparkMax(CAN_IDs.BALL_INTAKE_MASTER, MotorType.kBrushless);
    }

    public void controllerInit() {
        intakeSparkMax.setInverted(CONTROLLER_INVERTED);
        intakeSparkMax.setSmartCurrentLimit(STALL_LIMIT, FREE_LIMIT);
        intakeSparkMax.setCANTimeout(Constants.DEFAULT_TIMEOUT);
    }

    public static Intake getInstance() {
        if(instance == null){
            instance = new Intake();
        }
        return instance;
    }

    @Override    
    protected void initDefaultCommand() {
        setDefaultCommand(new SpinIntakeManual());
    }

    public double getEncoderPosition () {
        return Intake.getInstance().getController().getEncoder().getPosition();
    }

    public double getEncoderVelocity () {
        return Intake.getInstance().getController().getEncoder().getVelocity();
    }
}