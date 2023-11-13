package tides;

import java.util.*;

/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 
    private boolean[][] flooded;

    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {
        double lowestPoint = Double.MAX_VALUE;
        double highestPoint = Double.MIN_VALUE;
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (flooded[i][j]) {
                    lowestPoint = Math.min(lowestPoint, terrain[i][j]);
                    highestPoint = Math.max(highestPoint, terrain[i][j]);
                }
            }
        }
        return new double[] {lowestPoint, highestPoint};
        // this gives the extrma for terrain 

        /* WRITE YOUR CODE BELOW */
    
    }

    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */
    public boolean[][] floodedRegionsIn(double height) {
         boolean[][] flooded = new boolean[terrain.length][terrain[0].length];
         ArrayList<GridLocation> waterSources = new ArrayList<>();
         for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if (terrain[i][j] <= height) {
                    waterSources.add(new GridLocation(i, j));
                }
            }
        }
        
        /* WRITE YOUR CODE BELOW */
        return flooded; 
        // just finishing the floodfill.
    }

    /**
     * Checks if a given cell is flooded at a certain water height.
     * 
     * @param height of the water
     * @param cell location 
     * @return boolean, true if cell is flooded, otherwise false
     */
    public boolean isFlooded(double height, GridLocation cell) {
        int row = cell.row;
        int col = cell.col;
     if (row < 0 || row >= terrain.length || col < 0 || col >= terrain[0].length) {
        return false; 
        // the cell is not withing the boundaries.
    }
    return terrain[row][col] <= height;   
    }
        
    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
    public double heightAboveWater(double height, GridLocation cell) {
        double cellHeight = terrain[cell.row][cell.col];
        return cellHeight - height;
        
        /* WRITE YOUR CODE BELOW */
       
    }

    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        int count = 0;
        for (int row = 0; row < terrain.length; row++) {
            for (int col = 0; col < terrain[row].length; col++) {
                double currentHeight = terrain[row][col];
                if (currentHeight > height) {
                    count++;
                }
            }
        }
        return count;
        
        /* WRITE YOUR CODE BELOW */
        
    } 


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        int landatheight = totalVisibleLand(height);
        int landatnewheight = totalVisibleLand(newHeight);
        return landatheight - landatnewheight;
        
        /* WRITE YOUR CODE BELOW */
    }
     
    private boolean isValid(int row, int col) {
        return row >= 0 && row < terrain.length && col >= 0 && col < terrain[0].length;
        // itll check if the values are valid between the ranges.
    }

    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
        int islands = 0;
        int[][] directions  = { {1,0}, {0,1}, {1,1} };
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF( terrain.length, terrain[0].length);
        boolean[][] floodedMap = floodedRegionsIn(height);
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[i].length; j++) {
                if(floodedMap[i][j] == false){
                    for ( int[] dir : directions){
                        int newrow = i + dir[0];
                        int newcol = j + dir[1];
                        if(isValid(newrow,newcol) && terrain[newrow][newcol] > height && floodedMap[newrow][newcol]== false){
                            uf.union(new GridLocation(i,j), new GridLocation(newrow,newcol));
                        }
                    }
                }
            }
        }
        ArrayList<GridLocation> count = new ArrayList<GridLocation>();
         for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                if(floodedMap[i][j] == false){
                    count.add(uf.find(new GridLocation(i,j)));
                }
            } 
        }
        islands = count.size();
        return islands;
        // total islands with the flooded 

        
        /* WRITE YOUR CODE BELOW */
        
    }
}
