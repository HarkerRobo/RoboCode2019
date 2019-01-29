package frc.robot.commands.drivetrain;

import harkerrobolib.commands.IndefiniteCommand;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import frc.robot.util.PIDOutputGetter;
import frc.robot.util.PIDSourceCustomGet;
import harkerrobolib.util.MathUtil;

public class DriveWithVelocityDual extends IndefiniteCommand {

    private PIDController turnController;
    private PIDOutputGetter turnOutput;
    
    private double txSetpoint;

    public DriveWithVelocityDual(double txSetpoint) {
        requires(Drivetrain.getInstance());
        this.txSetpoint = txSetpoint;
        turnOutput = new PIDOutputGetter();
    }

    @Override
    public void initialize() {
        Drivetrain.getInstance().setBoth(ControlMode.PercentOutput, 0.0);
        turnController = new PIDController(AlignWithLimelight.TURN_KP, AlignWithLimelight.TURN_KI, 
                                            AlignWithLimelight.TURN_KD, AlignWithLimelight.TURN_KF, 
                                            new PIDSourceCustomGet(() -> Limelight.getInstance().getTx(), PIDSourceType.kDisplacement), 
                                            turnOutput);
        turnController.enable();
        turnController.setSetpoint(txSetpoint);
    }

    @Override
    public void execute() {
        double leftDriverX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
        double leftDriverY = 0;//MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);
        //System.out.println(OI.getInstance().getDriverGamepad().getButtonBumperLeftState());
        if(OI.getInstance().getDriverGamepad().getButtonBumperLeftState()) {
            leftDriverX = -turnOutput.getOutput();
        }

        System.out.println(turnController.getError());
        
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, /*forwardOutputVal*/ - turnOutput.getOutput() /*- angleOutputVal*/);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, /*forwardOutputVal +*/ turnOutput.getOutput() /*+ angleOutputVal*/);
    }       
    
    @Override
    public void end() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
    }

    @Override
    public void interrupted() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0.0);
    }

}