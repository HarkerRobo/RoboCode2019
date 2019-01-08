package frc.robot.commands;

import harkerrobolib.commands.IndefiniteCommand;

/**
 * 
 * @version 1/7/19
 */
public class DriveWithVelocity extends IndefiniteCommand { 
    private double velocity;

    public DriveWithVelocity(double velocity) {
        this.velocity = velocity;
    }

    protected void end(){
        
    }
    
    protected void execute(){

    }
}



