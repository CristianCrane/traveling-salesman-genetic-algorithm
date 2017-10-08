/* Modified version of Lee Jacobson's code.
 * Source: http://www.theprojectspot.com/tutorial-post/applying-a-genetic-algorithm-to-the-travelling-salesman-problem/5
 * Modifications by: Cristian Crane 
 * */

package application;

import java.util.ArrayList;

public class TourManager {

    // Holds our cities
    private static ArrayList<City> destinationCities = new ArrayList<City>();

    // Adds a destination city
    public static void addCity(City city) {
        destinationCities.add(city);
    }
    
    // Get a city
    public static City getCity(int index){
        return (City)destinationCities.get(index);
    }
    
    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }
    
    // Clears the TourManager
    public static void clearTour()
    {
    	destinationCities.clear();
    }
}
