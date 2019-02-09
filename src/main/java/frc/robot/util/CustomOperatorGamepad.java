package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import harkerrobolib.wrappers.HSJoystickButton;

/**
 * Represents a custom operator controller with 6 buttons
 * arranged in a 2x3 grid, as shown below.
 * --------
 * | o  o |
 * | o  o | 
 * | o  o |
 * --------
 * 
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @author Anirudh Kotamraju
 * @author Dawson Chen
 * @author Jatin Kohli
 * @author Angela Jia
 */
public class CustomOperatorGamepad extends Joystick {            
    private static final int TOP_LEFT_PORT = 0;
    private static final int TOP_RIGHT_PORT = 1;
    private static final int MIDDLE_LEFT_PORT = 2;
    private static final int MIDDLE_RIGHT_PORT = 3;
    private static final int BOTTOM_LEFT_PORT = 4;
    private static final int BOTTOM_RIGHT_PORT = 5;

    private HSJoystickButton topLeft;
    private HSJoystickButton topRight;
    private HSJoystickButton middleLeft;
    private HSJoystickButton middleRight;
    private HSJoystickButton bottomLeft;
    private HSJoystickButton bottomRight;
    
    /**
     * Creates a new custom operator gamepad with a specific port.
     */
    public CustomOperatorGamepad(int port) {
        super(port);
        topLeft = new HSJoystickButton(this, TOP_LEFT_PORT);
        topRight = new HSJoystickButton(this, TOP_RIGHT_PORT);
        middleLeft = new HSJoystickButton(this, MIDDLE_LEFT_PORT);
        middleRight = new HSJoystickButton(this, MIDDLE_RIGHT_PORT);           
        bottomLeft = new HSJoystickButton(this, BOTTOM_LEFT_PORT);
        bottomRight = new HSJoystickButton(this, BOTTOM_RIGHT_PORT);
    }

    /**
     * Gets the top left button.
     * @return the top left button 
     */
    public HSJoystickButton getTopLeftButton() {
        return topLeft;
    }
    
    /**
     * Gets the top right button.
     * @return the top right button 
     */
    public HSJoystickButton getTopRightButton() {
        return topRight;
    }
    
    /**
     * Gets the middle left button.
     * @return the middle left button 
     */
    public HSJoystickButton getMiddleLeftButton() {
        return middleLeft;
    }
    
    /**
     * Gets the middle right button.
     * @return the middle right button 
     */
    public HSJoystickButton getMiddleRightButton() {
        return middleRight;
    }
    
    /**
     * Gets the bottom left button.
     * @return the bottom left button 
     */
    public HSJoystickButton getBottomLeftButton() {
        return bottomLeft;
    }
    
    /**
     * Gets the bottom right button.
     * @return the bottom right button 
     */
    public HSJoystickButton getBottomRightButton() {
        return bottomRight;
    }
    
    /**
     * Checks if the top left button is pressed.
     * @return whether the top left button is pressed
     */
    public boolean getTopLeftPressed() {
        return topLeft.get();
    }
    
    /**
     * Checks if the top right button is pressed.
     * @return whether the top right button is pressed
     */
    public boolean getTopRightPressed() {
        return topRight.get();
    }
    
    /**
     * Checks if the middle left button is pressed.
     * @return whether the middle left button is pressed
     */
    public boolean getMiddleLeftPressed() {
        return middleLeft.get();
    }

    /**
     * Checks if the middle right button is pressed.
     * @return whether the middle right button is pressed
     */
    public boolean getMiddleRightPressed() {
        return middleRight.get();
    }

    /**
     * Checks if the bottom left button is pressed.
     * @return whether the bottom left button is pressed
     */
    public boolean getBottomLeftPressed() {
        return bottomLeft.get();
    }

    /**
     * Checks if the bottom right button is pressed.
     * @return whether the bottom right button is pressed
     */
    public boolean getBottomRightPressed() {
        return bottomRight.get();
    }
}