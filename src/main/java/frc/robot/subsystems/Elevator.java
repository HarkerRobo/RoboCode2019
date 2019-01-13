package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.commands.elevator.MoveElevatorManual;
import harkerrobolib.wrappers.HSTalon;

/**
 * Represents the elevator on the robot.
 * 
 * @author Angela Jia
 * @since 1/10/19
 */
public class Elevator extends Subsystem {

    private static Elevator el; 
    private HSTalon elTalon;
    private VictorSPX victorOne;
    private VictorSPX victorTwo;
    
    private static final int PEAK_CURRENT_LIMIT = 0;
    private static final int CONT_CURRENT_LIMIT = 0;
    private static final int CONT_CURRENT_TIME = 0;
    private static final boolean INVERTED_MASTER = false;
    private static final boolean INVERTED_VICT_ONE = false;
    private static final boolean INVERTED_VICT_TWO = false;

    public static final int REVERSE_SOFT_LIMIT= 0;
    public static final int POSITION_PID = 0;
    public static final int MAX_SPEED = 0;
    public static final int SLOW_DOWN_PERCENT = 0;
    public static final int FFGRAV = 0;
    public static final int MAX_OUTPUT_FACTOR = 1;
    public static final double MIN_LESS_OUTPUT_FACTOR = 0;
    public static final double MIN_MORE_OUTPUT_FACTOR = -0.5;

    private Elevator() {
        elTalon = new HSTalon(CAN_IDs.EL_MASTER);
        victorOne = new VictorSPX(CAN_IDs.EL_VICTOR_ONE);
        victorTwo = new VictorSPX(CAN_IDs.EL_VICTOR_TWO);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new MoveElevatorManual());
    }

    public static Elevator getInstance() {
        if(el == null)
            el = new Elevator();
        return el;
    }

    public void talonInit() {
        victorOne.follow(elTalon);
        victorTwo.follow(elTalon);
        victorOne.setNeutralMode(NeutralMode.Brake);
        victorTwo.setNeutralMode(NeutralMode.Brake);
        elTalon.setNeutralMode(NeutralMode.Brake);
        elTalon.configContinuousCurrentLimit(CONT_CURRENT_LIMIT);
        elTalon.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        elTalon.configPeakCurrentDuration(CONT_CURRENT_TIME);
        elTalon.setInverted(INVERTED_MASTER);
        victorOne.setInverted(INVERTED_VICT_ONE);
        victorTwo.setInverted(INVERTED_VICT_TWO);
    }

    public void moveElevatorVelocity(double speed) {
        elTalon.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, FFGRAV);
    }
    public HSTalon getMaster() {
        return elTalon;
    }

    public VictorSPX getVictorOne() {
        return victorOne;
    }

    public VictorSPX getVictorTwo() {
        return victorTwo;
    }
}