/* Modified version of Lee Jacobson's code.
 * Source: http://www.theprojectspot.com/tutorial-post/applying-a-genetic-algorithm-to-the-travelling-salesman-problem/5
 * Modifications by: Cristian Crane 
 * */

package application;

public class City {
    int x;
    int y;
    
    // Constructs a randomly placed city
    public City(){
        this.x = (int)(Math.random()*400);
        this.y = (int)(Math.random()*400);
    }
    
    // Constructs a city at chosen x, y location
    public City(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    // Gets city's x coordinate
    public int getX(){
        return this.x;
    }
    
    // Gets city's y coordinate
    public int getY(){
        return this.y;
    }
    
    // Gets the distance to given city
    public double distanceTo(City city){
        int xDistance = Math.abs(getX() - city.getX());
        int yDistance = Math.abs(getY() - city.getY());
        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );
        
        return distance;
    }
    
    @Override
    public String toString(){
        return getX()+", "+getY();
    }
}
