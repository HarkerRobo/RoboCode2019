/**
 * Moves the balls to the cargo space
 * @author Shahzeb Lakhani
 * @version 1/10/19
 */
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import harkerrobolib.wrappers.HSTalon;
public class Rollers extends Subsystem {

    private final boolean TOP_INVERTED =false;
    private final boolean BOTTOM_INVERTED = false;
    private final int CONTINUOUS_CURRENT_LIMIT = 0;
    private final int PEAK_CURRENT_LIMIT = 0;
    private final int PEAK_TIME = 0;

    private static Rollers r;
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
		
    }
    
    /**
     * Gets the talon instance
     */
    public static Rollers getInstance() {
        if(r == null)
            r = new Rollers();
        return r;
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
    

}