package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.commands.intake.SpinIntakeManual;
import harkerrobolib.wrappers.HSTalon;

/**
 * Intakes the cargo into the robot.
 * 
 * @author Anirudh Kotamraju
 * @since 1/11/2019
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
    private HSTalon intakeTalon;

    private final static int PEAK_LIMIT = 20;
    private final static int PEAK_TIME = 1;
    private final static int CONTINUOUS_CURRENT = 15;

    private final static boolean MASTER_INVERTED = false;
    private final static NeutralMode NEUTRAL_MODE = NeutralMode.Brake;

    public HSTalon getTalon() {
        return intakeTalon;
    }

    public void setTalonOutput(double magnitude, IntakeDirection direction) {
        getTalon().set(ControlMode.PercentOutput, magnitude * direction.getSign());
    }

    public void setTalonOutput(double percentOutput) {
        getTalon().set(ControlMode.PercentOutput, percentOutput);
    }

    private Intake() {
        intakeTalon = new HSTalon(CAN_IDs.BALL_INTAKE_MASTER);
        initTalons();
    }

    private void initTalons() {
        intakeTalon.setInverted(MASTER_INVERTED);
        intakeTalon.setNeutralMode(NEUTRAL_MODE);
    }

    public static Intake getInstance() {
        if(instance == null){
            instance = new Intake();
        }
        return instance;
    }

    public void setCurrentLimits() {
        intakeTalon.configPeakCurrentDuration(PEAK_TIME);
        intakeTalon.configPeakCurrentLimit(PEAK_LIMIT);
        intakeTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT);
    }
    
    @Override    
    protected void initDefaultCommand() {
        setDefaultCommand(new SpinIntakeManual());
    }

    
}