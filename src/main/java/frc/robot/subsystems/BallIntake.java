package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.commands.MoveIntakeBallManual;
import harkerrobolib.wrappers.HSTalon;

/**
 * Intakes the ball into the robot
 * 
 * @author Anirudh Kotamraju
 * @since 1/11/2019
 */
public class BallIntake extends Subsystem {
    private static BallIntake instance;
    private HSTalon intakeTalon;

    private final static int PEAK_LIMIT = 20;
    private final static int PEAK_TIME = 1;
    private final static int CONTINUOUS_CURRENT = 15;

    private final static boolean MASTER_INVERTED = false;
    private final static NeutralMode NEUTRAL_MODE = NeutralMode.Brake;

    public HSTalon getTalon() {
        return intakeTalon;
    }

    public void setTalonOutput(double percentOutput) {
        getTalon().set(ControlMode.PercentOutput, percentOutput);
    }

    private BallIntake() {
        intakeTalon = new HSTalon(CAN_IDs.BALL_INTAKE_MASTER);
        initTalons();
    }

    private void initTalons() {
        intakeTalon.setInverted(MASTER_INVERTED);
        intakeTalon.setNeutralMode(NEUTRAL_MODE);
    }

    public static BallIntake getInstance() {
        if(instance == null){
            instance = new BallIntake();
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
        setDefaultCommand(new MoveIntakeBallManual());
    }

    
}