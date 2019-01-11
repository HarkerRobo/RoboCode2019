package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap.CAN_IDs;
import harkerrobolib.wrappers.HSTalon;

public class BallIntake extends Subsystem{
    private static BallIntake instance;
    private static boolean MASTER_INVERTED= false;
    private HSTalon biTalon;

    private BallIntake(){
        biTalon = new HSTalon(CAN_IDs.BI_MASTER);
    }

    @Override    
    protected void initDefaultCommand() {

    }

    public void setNeutralMode(){

    }
    
    public static BallIntake getInstance(){
        if(instance==null){
            instance = new BallIntake();
        }
        return instance;
    }







}