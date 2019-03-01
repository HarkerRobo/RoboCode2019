package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.Robot.Side;
import frc.robot.RobotMap.CAN_IDs;
import frc.robot.RobotMap.RobotType;
import frc.robot.commands.rollers.SpinRollersManual;
import frc.robot.util.Pair;
import harkerrobolib.wrappers.HSTalon;

/**
 * Moves the cargo to the cargo space.
 * 
 * @author Shahzeb Lakhani
 * @author Chirag Kaushik
 * @since 1/10/19
 */
public class Rollers extends Subsystem {
    public enum RollerDirection {
        IN(-1), OUT(1);

        private int sign;

        private RollerDirection(int sign) {
            this.sign = sign;
        }

        public int getSign() {
            return sign;
        }
    }

    private static final boolean TOP_INVERTED;
    private static final boolean BOTTOM_INVERTED;

    static { 
        if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
            TOP_INVERTED = false;
            BOTTOM_INVERTED = true;
        }
        else {
            TOP_INVERTED = false;
            BOTTOM_INVERTED = false;
        }
    }
    
    private static final int CONTINUOUS_CURRENT_LIMIT = 7;
    private static final int PEAK_CURRENT_LIMIT = 10;
    private static final int PEAK_TIME = 500;
    private static final double ROCKET_HIGH_SCORING_SPEED = 0.8;
    private static final double TOP_SPIN = 0.1;

    public static final double DEFAULT_ROLLER_MAGNITUDE = 0.8;
    public static final double ROLLER_SHOOTING_SPEED = 0.65;
    public static final double HATCH_STOW_SPEED = 0.75;

    private static Rollers instance;
    private HSTalon rTalonTop;
    private HSTalon rTalonBottom;
    
    /**
     * Creates new Talons
     */
    private Rollers() {
        rTalonTop = new HSTalon(CAN_IDs.RO_TOP);
        rTalonBottom = new HSTalon(CAN_IDs.RO_BOTTOM);
    }

    @Override
	protected void initDefaultCommand() {
		setDefaultCommand(new SpinRollersManual());
    }
    
    /**
     * Initialises the Talons
     */
    public void talonInit(){
        rTalonTop.configFactoryDefault();
        rTalonBottom.configFactoryDefault();
        
        rTalonTop.setNeutralMode(NeutralMode.Coast);
        rTalonBottom.setNeutralMode(NeutralMode.Coast);

        rTalonTop.setInverted(TOP_INVERTED);
        rTalonBottom.setInverted(BOTTOM_INVERTED);

        setupCurrentLimits();
    }

    public void setupCurrentLimits () {
        rTalonTop.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);
        rTalonBottom.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);

        rTalonTop.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        rTalonBottom.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);

        rTalonTop.configPeakCurrentDuration(PEAK_TIME);
        rTalonBottom.configPeakCurrentDuration(PEAK_TIME);

        rTalonBottom.enableCurrentLimit(true);
        rTalonTop.enableCurrentLimit(true);
    }
    
    public HSTalon getTopTalon(){
        return rTalonTop;        
    }

    public HSTalon getBottomTalon() {
        return rTalonBottom;
    }

    public void stopRollers() {
        rTalonTop.set(ControlMode.Disabled, 0);
        rTalonBottom.set(ControlMode.Disabled, 0);
    }

    public void moveRollers(double output, RollerDirection direction) {
        rTalonTop.set(ControlMode.PercentOutput, output * direction.getSign());
        rTalonBottom.set(ControlMode.PercentOutput, -1 * output * direction.getSign());
    }

    /**
     * Pair<top output, bottom output>
     */
    public Pair<Double, Double> getRecommendedRollersOutput() {
        double wristAngle = Wrist.getInstance().getCurrentAngleDegrees();
        if((Wrist.getInstance().getCurrentSide() == Side.FRONT ||
            Wrist.getInstance().getCurrentSide() == Side.AMBIGUOUS) && 
            Wrist.getInstance().isFurtherBackward(wristAngle, Wrist.SCORING_POSITION_FRONT_CARGO_2) ||
            (Wrist.getInstance().getCurrentSide() == Side.BACK) &&
            Wrist.getInstance().isFurtherForward(wristAngle, Wrist.SCORING_POSITION_BACK_CARGO_2)) {
            return new Pair<Double, Double> (DEFAULT_ROLLER_MAGNITUDE + TOP_SPIN, DEFAULT_ROLLER_MAGNITUDE - TOP_SPIN);
        }
        //  if(Math.abs(wristAngle - 90)
        //      return ROLLER_SHOOTING_SPEED;
        return new Pair<Double, Double> (DEFAULT_ROLLER_MAGNITUDE, DEFAULT_ROLLER_MAGNITUDE);
    }

    public void moveRollers(double topMagnitude, double bottomMagnitude, RollerDirection direction) {
        rTalonTop.set(ControlMode.PercentOutput, topMagnitude * direction.getSign());
        rTalonBottom.set(ControlMode.PercentOutput, bottomMagnitude * direction.getSign());
    }

     /**
     * Gets the talon instance.
     * 
     * @return talon instance
     */
    public static Rollers getInstance() {
        if(instance == null)
            instance = new Rollers();
        return instance;
    }
}