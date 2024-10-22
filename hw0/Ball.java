/**
 * This is part of HW0: Environment Setup and Java Introduction.
 */
package hw0;

import java.awt.Color;

/**
 * This is a simple object that has a volume.
 */
// You may not make Ball implement the Comparable interface.
public class Ball {

    public double volume;   
    public Color color;

    /**
     * Constructor that creates a new ball object with the specified volume and color.
     * @param volume the volume of the new ball object
     * @param color the color of the new ball object
     */
    public Ball(double volume, Color color) {
        this.volume = volume;
        this.color = color;
    }
    
    /**
     * Constructor that creates a new ball object with the specified volume given by a string.
     * @param volume A string representing the volume of the new object.
     */
    public Ball(String volume, Color color) {
    	//Checking to see if proper input was obtained for volume
    	try {
    		this.volume = Double.parseDouble(volume);
    	}
    	catch (NumberFormatException e){
    		this.volume = 0.0;
    		System.err.println("Invalid Volume, Volume set to 0.0");
    		
    	}
		
    }    

    /**
     * Returns the volume of the ball.
     * @return the volume of the ball.
    */
    public double getVolume() {
        return this.volume;
    }
    
    /**
     * Returns the color of the ball.
     * @return the color of the ball.
    */
    public Color getColor() {
        return this.color;
    }

}


