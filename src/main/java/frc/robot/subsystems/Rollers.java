package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import harkerrobolib.wrappers.HSTalon;

/**
 * Moves the balls to the cargo space
 * @author Shahzeb Lakhani
 * @author Chirag Kaushik
 * @since 1/10/19
 */
public class Rollers extends Subsystem {
    public enum RollerDirection {
        IN(1), OUT(-1);

        private int sign;

        private RollerDirection(int sign) {
            this.sign = sign;
        }

        public int getSign() {
            return sign;
        }
    }

    private static final boolean TOP_INVERTED = false;
    private static final boolean BOTTOM_INVERTED = false;
    private static final int CONTINUOUS_CURRENT_LIMIT = 0;
    private static final int PEAK_CURRENT_LIMIT = 0;
    private static final int PEAK_TIME = 0;

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
		//setDefaultCommand();
    }
    
    /**
     * Initialises the Talons
     */
    public void talonInit(){
        rTalonTop.setNeutralMode(NeutralMode.Coast);
        rTalonBottom.setNeutralMode(NeutralMode.Coast);
        rTalonTop.setInverted(TOP_INVERTED);
        rTalonBottom.setInverted(BOTTOM_INVERTED);
        rTalonTop.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);
        rTalonBottom.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);
        rTalonTop.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        rTalonBottom.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
        rTalonTop.configPeakCurrentDuration(PEAK_TIME);
        rTalonBottom.configPeakCurrentDuration(PEAK_TIME);
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

    public void moveRollers(double magnitude, RollerDirection direction) {
        rTalonTop.set(ControlMode.PercentOutput, magnitude * direction.getSign());
        rTalonBottom.set(ControlMode.PercentOutput, -1 * magnitude * direction.getSign());
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