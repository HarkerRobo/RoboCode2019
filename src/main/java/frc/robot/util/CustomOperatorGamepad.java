package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import harkerrobolib.wrappers.HSJoystickButton;

/**
 * @author Anirudh Kotamraju
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @author Dawson Chen
 * @author Jatin Kohli
 * @author Angela Jia
 */
public class CustomOperatorGamepad extends Joystick {            
    private static final int FORWARD_THREE_PORT = 0;
    private static final int BACKWARD_THREE_PORT = 1;
    private static final int FORWARD_TWO_PORT = 2;
    private static final int BACKWARD_TWO_PORT = 3;
    private static final int FORWARD_ONE_PORT = 4;
    private static final int BACKWARD_ONE_PORT = 5;

    private static final int ZERO_PORT = 6;
    private static final int OUTTAKE_BALL_OR_HATCH_PORT= 7;
    private static final int INTAKE_HATCH_PORT = 8; 
    private static final int BRING_ALL_IN_PORT = 9;

    private HSJoystickButton forwardOneButton;
    private HSJoystickButton forwardTwoButton;
    private HSJoystickButton forwardThreeButton;
    private HSJoystickButton backwardOneButton;
    private HSJoystickButton backwardTwoButton;
    private HSJoystickButton backwardThreeButton; 

    private HSJoystickButton zeroButton; 
    private HSJoystickButton outtakeButton;
    private HSJoystickButton intakeHatchButton;
    private HSJoystickButton stowButton; 
    
    /**
     * Creates a new custom operator gamepad with a specific port.
     */
    public CustomOperatorGamepad(int port) {
        super(port);
        forwardThreeButton = new HSJoystickButton(this, FORWARD_THREE_PORT);
        backwardThreeButton = new HSJoystickButton(this, BACKWARD_THREE_PORT);
        forwardTwoButton = new HSJoystickButton(this, FORWARD_TWO_PORT);
        backwardTwoButton = new HSJoystickButton(this, BACKWARD_TWO_PORT);           
        forwardOneButton = new HSJoystickButton(this, FORWARD_ONE_PORT);
        backwardOneButton = new HSJoystickButton(this, BACKWARD_ONE_PORT);

        zeroButton = new HSJoystickButton(this, ZERO_PORT);
        outtakeButton = new HSJoystickButton(this, OUTTAKE_BALL_OR_HATCH_PORT); 
        intakeHatchButton  = new HSJoystickButton(this, INTAKE_HATCH_PORT);
        stowButton = new HSJoystickButton(this, BRING_ALL_IN_PORT);
    }



    /**
     * Gets the forward three button.
     * @return the forward three button 
     */
    public HSJoystickButton getForwardThreeButton() {
        return forwardThreeButton;
    }
    
    /**
     * Gets the backward three button.
     * @return the backward three button 
     */
    public HSJoystickButton getBackwardThreeButton() {
        return backwardThreeButton;
    }
    
    /**
     * Gets the forward two button.
     * @return the forward two button 
     */
    public HSJoystickButton getForwardTwoButton() {
        return forwardTwoButton;
    }
    
    /**
     * Gets the backward two button.
     * @return the backward two button 
     */
    public HSJoystickButton getBackwardTwoButton() {
        return backwardTwoButton;
    }
    
    /**
     * Gets the forward one button.
     * @return the forward one button 
     */
    public HSJoystickButton getForwardOneButton() {
        return forwardOneButton;
    }
    
    /**
     * Gets the backward one button.
     * @return the backward one button 
     */
    public HSJoystickButton getBackwardOneButton() {
        return backwardOneButton;
    }

    
    /**
     * Gets the zero button.
     * @return the zero button 
     */
    public HSJoystickButton getZeroButton() {
        return zeroButton;
    }
    /**
     * Gets the outtake ball or hatch button.
     * @return the outtake ball or hatch button 
     */
    public HSJoystickButton getOuttakeButton() {
        return outtakeButton;
    }
    /**
     * Gets the intake and hatch button.
     * @return the intake and hatch button 
     */
    public HSJoystickButton getIntakeHatchButton() {
        return intakeHatchButton;
    }
    /**
     * Gets the stow button.
     * @return the stow button
     */
    public HSJoystickButton getStowButton() {
        return stowButton;
    }


    
    /**
     * Checks if the forward three button is pressed.
     * @return whether the forward three button is pressed
     */
    public boolean getForwardThreePressed() {
        return forwardThreeButton.get();
    }
    
    /**
     * Checks if the backward three button is pressed.
     * @return whether the backward three button is pressed
     */
    public boolean getBackwardThreePressed() {
        return backwardThreeButton.get();
    }
    
    /**
     * Checks if the forward two button is pressed.
     * @return whether the forward two button is pressed
     */
    public boolean getForwardTwoPressed() {
        return forwardTwoButton.get();
    }

    /**
     * Checks if the backward two button is pressed.
     * @return whether the backward two button is pressed
     */
    public boolean getBackwardTwoPressed() {
        return backwardTwoButton.get();
    }

    /**
     * Checks if the forward one button is pressed.
     * @return whether the forward one button is pressed
     */
    public boolean getForwardOnePressed() {
        return forwardOneButton.get();
    }

    /**
     * Checks if the backward one button is pressed.
     * @return whether the backward one button is pressed
     */
    public boolean getBackwardOnePressed() {
        return backwardOneButton.get();
    }

    /**
     * Checks if the zero button is pressed.
     * @return whether the zero button is pressed
     */
    public boolean getZeroPressed(){
        return zeroButton.get();
    }

    /**
     * Checks if the outtake Ball or Hatch button is pressed.
     * @return whether the outtake Ball or Hatch button is pressed
     */

    public boolean getOuttakeBallOrHatchPressed(){
        return outtakeButton.get();
    }

    /**
     * Checks if the intake Hatch button is pressed.
     * @return whether the intake Hatch button is pressed
     */
    public boolean getIntakeHatchPressed(){
        return intakeHatchButton.get();
    }

    /**
     * Checks if the bring all in button is pressed.
     * @return whether the bring all in is pressed
     */
    public boolean getBringAllInPressed(){
        return stowButton.get();
    }

    
}