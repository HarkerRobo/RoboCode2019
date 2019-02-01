package frc.robot.commands;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap.CAN_IDs;

public class StartCompressor extends InstantCommand{
    private static final boolean START_BOOL = true;
    public void initalize() {
        Compressor compressor = new Compressor(CAN_IDs.PCM);
        
        compressor.setClosedLoopControl(START_BOOL);
        
    }
