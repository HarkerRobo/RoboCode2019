package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public MoveArmPosition extends Command {
    private double position;

    public MoveArmPosition(double position) {
        this.position = position;
    }

    @Override
    public void initialize() {
        Arm.getInstance().setArmPosition(position);
    }

    @Override
    public boolean isFinished() {
       // Arm.getInstance().
    }

    @Override
    public void end() {
        Arm.getInstance().armMotionPercentOutput(0.0);
    }

    @Override
    public void interrupted() {
        Arm.getInstance().armMotionPercentOutput(0.0);
    }
}