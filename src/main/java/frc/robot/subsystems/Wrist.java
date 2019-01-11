package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import harkerrobolib.wrappers.HSTalon;

/**
 * 
 * @author Finn Frankis
 * @version 1/10/19
 */
public class Wrist extends Subsystem {

    private static Wrist wr;

    private HSTalon wristMaster;
    private VictorSPX wristFollower;

    private final boolean MASTER_INVERTED = false;
    private final boolean FOLLOWER_INVERTED = false;

    private final int CONTINUOUS_CURRENT_LIMIT = 0;
    private final int PEAK_CURRENT_LIMIT = 0;
    private final int PEAK_TIME = 500;


    private Wrist () {
        wristMaster = new HSTalon(CAN_IDs.WRIST_MASTER);
        wristFollower = new VictorSPX(CAN_IDs.WRIST_FOLLOWER);
    }

	@Override
	protected void initDefaultCommand() {
		
	}

    public static Wrist getInstance () {
        if (wr == null) {wr = new Wrist();}
        return wr;
    }

    public void talonInit () {
        wristMaster.setNeutralMode(NeutralMode.Brake);
        wristFollower.setNeutralMode (NeutralMode.Brake);

        wristMaster.setInverted(MASTER_INVERTED);
        wristFollower.setInverted(FOLLOWER_INVERTED);

        wristMaster.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT);
        wristMaster.configPeakCurrentDuration(PEAK_TIME);
        wristMaster.configPeakCurrentLimit(PEAK_CURRENT_LIMIT);
    }
}