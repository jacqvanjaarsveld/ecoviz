/*
* File: Fire.java
* Author(s): BRNJAM019, FRNOWE001, VJRJAC003
* Status: In progress
*/

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.Math;
import java.util.ArrayList;

public class Fire {
    private int dimX;
    private int dimY;
    private int[][] fireGrid;
    private int[][] burntPlants;
    private int[][][] underPlants, canopyPlants;
    private int[] traversal;
    private BufferedImage fireImage, burntImage;
    private ArrayList<Integer> permute; // permuted list of integers in range [0, dimx*dimy)
    private boolean showPath;
    private Species[] specieslist;
    private Color ashColor, burntColor, igniteColor;
    private double chance;

    //Paramaterised Constructor
    public Fire(int dimX, int dimY, int[][][] plantGrid1, int[][][] plantGrid2) { // 1 - under //2 - canopy

        //Passed Parameters
        this.dimX = dimX;
        this.dimY = dimY;
        this.underPlants = plantGrid1;
        this.canopyPlants = plantGrid2;

        //Global Variables
        chance = 30; //Chance of fire moving on ground
        specieslist = PlantLayer.getAllSpecies();
        ashColor = new Color(87, 87, 87);
        burntColor = new Color(219, 37, 0);
        igniteColor = Color.RED;
        showPath = true;
        traversal = new int[dimX * dimY];
        fireGrid = new int[dimX][dimY];
        burntPlants = new int[dimX][dimY];

        //Create randomly permuted list of indices for traversal
        genPermute(); 
    }

    //Performs a single iteration of fire movement
    public void simulate(int segmentLow, int segmentHigh) {
        for (int i = segmentLow; i < segmentHigh; i++) {
            getPermute(i, traversal);

            // Move fire where possible:
            if (isFire(traversal[0], traversal[1]) == true) {
                moveFire(traversal[0], traversal[1]);

            }
        }
    }

    //Returns True/False if fire is present at location (x,y)
    public boolean isFire(int x, int y) {
        boolean firePresent = false;
        if (fireGrid[x][y] == 1) {
            firePresent = true;
        }
        return firePresent;
    }

    //Add fire from user input
    public void addFire(int x, int y) {
        // Add Fire To grid
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                if (x + i > 0 && x + i < dimX - 2 && y + j > 0 && y + j < dimY - 2) {
                    fireGrid[x + i][y + j] = 1; // 1 means there is fire here //2 means there is ash there
                }
            }
        }
        deriveFireImage();
    }

    //Reset the simulation
    public void clearGrid() {
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                fireGrid[x][y] = 0;
                burntPlants[x][y] = 0;
            }
        }
        System.out.println("Reset Simulation");
    }

    //Sets a tree on fire (at the root)
    public void fillTree(int x, int y) {
        fireGrid[x][y] = 1;

        if (underPlants[x][y][1] > 0) {
            int specId = underPlants[x][y][0]; // Species ID
            int plantID = underPlants[x][y][1]; // Plant ID

            Plant[] uPlants = specieslist[specId].getCanopyPlants();
            double rad = uPlants[plantID].getCanopy();

            double temp = Math.round(rad);
            int boundary = (int) temp + 1;

            for (int j = y - boundary; j < (y + boundary + 1); j++) {
                for (int i = x - boundary; i < (x + boundary + 1); i++) {
                    if (j < Terrain.getDimY() && j > 0 && i < Terrain.getDimX() && i > 0) {
                        double dist = Math.sqrt(Math.pow((x - i), 2) + Math.pow((y - j), 2));
                        if (dist <= rad && fireGrid[i][j] == 0) {
                            this.fireGrid[i][j] = 1;
                            burntPlants[i][j] = 1;
                        }
                    }
                }
            }
        }

        if (canopyPlants[x][y][1] > 0) {
            int specId = canopyPlants[x][y][0]; // Species ID
            int plantID = canopyPlants[x][y][1]; // Plant ID

            Plant[] cPlants = specieslist[specId].getCanopyPlants();
            double rad = cPlants[plantID].getCanopy();

            double temp = Math.round(rad);
            int boundary = (int) temp + 1;

            for (int j = y - boundary; j < (y + boundary + 1); j++) {
                for (int i = x - boundary; i < (x + boundary + 1); i++) {
                    if (j < Terrain.getDimY() && j > 0 && i < Terrain.getDimX() && i > 0) {
                        double dist = Math.sqrt(Math.pow((x - i), 2) + Math.pow((y - j), 2));
                        if (dist <= rad) {
                            this.fireGrid[i][j] = 1;
                            burntPlants[i][j] = 1;
                        }
                    }
                }
            }
        }
    }

    //Moves Fire and sets Fire to plants/ground
    public void moveFire(int x, int y) {
        double rand = Math.random() * 100;
        fireGrid[x][y] = 2; // ASH cant move anymore
        if (x > 0 || y > 0 || x < Terrain.getDimX() || y < Terrain.getDimY()) {
            try {
                if ((underPlants[x - 1][y][1] > 0 || canopyPlants[x - 1][y][1] > 0) 
                        && fireGrid[x - 1][y] != 2) { // West
                    // 100% chance of fire spread
                    fillTree(x - 1, y);
                    // fireGrid[x-1][y]=1; //FIRE
                } else if (rand < chance && fireGrid[x - 1][y] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x - 1][y] = 1; // FIRE
                }

                if ((underPlants[x + 1][y][1] > 0 || canopyPlants[x + 1][y][1] > 0) 
                        && fireGrid[x + 1][y] != 2) { // East
                    // 100% chance of fire spread
                    fillTree(x + 1, y);
                } else if (rand < chance && fireGrid[x + 1][y] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x + 1][y] = 1;
                }

                if ((underPlants[x + 1][y + 1][1] > 0 || canopyPlants[x + 1][y + 1][1] > 0)
                        && fireGrid[x + 1][y + 1] != 2) { // North East
                    // 100% chance of fire spread
                    fillTree(x + 1, y + 1);
                } else if (rand < chance && fireGrid[x + 1][y + 1] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x + 1][y + 1] = 1;
                }

                if ((underPlants[x][y + 1][1] > 0 || canopyPlants[x][y + 1][1] > 0) 
                        && fireGrid[x][y + 1] != 2) { // North
                    // 100% chance of fire spread
                    fillTree(x, y + 1);
                } else if (rand < chance && fireGrid[x][y + 1] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x][y + 1] = 1; // FIRE
                }

                if ((underPlants[x - 1][y + 1][1] > 0 || canopyPlants[x - 1][y + 1][1] > 0)
                        && fireGrid[x - 1][y + 1] != 2) { // North West
                    // 100% chance of fire spread
                    fillTree(x - 1, y + 1);
                } else if (rand < chance && fireGrid[x - 1][y + 1] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x - 1][y + 1] = 1; // FIRE
                }

                if ((underPlants[x - 1][y - 1][1] > 0 || canopyPlants[x - 1][y - 1][1] > 0)
                        && fireGrid[x - 1][y - 1] != 2) { // South
                    // 100% chance of fire spread
                    fillTree(x - 1, y - 1);
                } else if (rand < chance && fireGrid[x - 1][y - 1] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x - 1][y - 1] = 1; // FIRE
                }

                if ((underPlants[x + 1][y - 1][1] > 0 || canopyPlants[x + 1][y - 1][1] > 0)
                        && fireGrid[x + 1][y - 1] != 2) { // South East
                    // 100% chance of fire spread
                    fillTree(x + 1, y - 1);
                } else if (rand < chance && fireGrid[x + 1][y - 1] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x + 1][y - 1] = 1; // FIRE
                }

                if ((underPlants[x - 1][y - 1][1] > 0 || canopyPlants[x - 1][y - 1][1] > 0)
                        && fireGrid[x - 1][y - 1] != 2) { // South West 🡧
                    // 100% chance of fire spread
                    fillTree(x - 1, y - 1);
                } else if (rand < chance && fireGrid[x - 1][y - 1] != 2) {
                    // 20% chance of fire spread
                    fireGrid[x - 1][y - 1] = 1; // FIRE
                }
            } catch (Exception e) {}
        }
    }

    // Draw graphics on the bufferedImages
    public void deriveFireImage() {
        fireImage = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = fireImage.createGraphics();

        if (showPath){
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                if (fireGrid[x][y] == 1) {
                    g2d.setColor(igniteColor);
                    g2d.fillRect(x, y, 1, 1);
                } else if (fireGrid[x][y] == 2) {
                    g2d.setColor(ashColor); // Usually dark grey
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
        }

        burntImage = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d_burnt = burntImage.createGraphics();
        g2d_burnt.setColor(burntColor);

        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                if (burntPlants[x][y] == 1) {
                    g2d_burnt.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    // Generate a permuted list of linear index positions to allow a random
    // traversal over the terrain
    public void genPermute() {
        permute = new ArrayList<Integer>();
        for (int idx = 0; idx < getDim(); idx++)
            permute.add(idx);
        java.util.Collections.shuffle(permute);
    }

    // Find permuted 2D location from a linear index in the range [0, dimx*dimy)
    public void getPermute(int i, int[] loc) {
        locate(permute.get(i), loc);
    }

    // Convert linear position into 2D location in grid
    public void locate(int pos, int[] index) {
        index[0] = (int) pos / dimY;
        index[1] = (int) pos % dimY;
    }

    // Return Fire Spread Image
    public BufferedImage getImage() {
        return fireImage;
    }

    // Return Burnt Tree Image
    public BufferedImage getBurntImage() {
        return burntImage;
    }

    // Return Dimensions
    public int getDim() {
        return dimX * dimY;
    }

    public void setShowPath(Boolean b){
        showPath = b;
    }
}
