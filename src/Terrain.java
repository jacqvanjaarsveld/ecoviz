import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Color;


//Create 2D Greyscale enviro
public class Terrain {
    private double[][] elv;
    private int dimX,dimY;
    private double gridSpacing = 0.0;
    private double latitude = 0.0;
    private double[][] canopy;
    private double[][] undergrowth;
    private ArrayList<PlantType> underPlants = new ArrayList<PlantType>();
    private ArrayList<PlantType> canopyPlants = new ArrayList<PlantType>();
    private BufferedImage img;

    public Terrain(){

    }
//Accessor Methods:
    public int getDimX(){
        return dimX;
    }
    public int getDimY(){
        return dimY;
    }
    public BufferedImage getImg(){
        return img;
    }

//========================================================================
//      Read in the height values
//========================================================================
    public void readElevation() throws FileNotFoundException{
        String fileName = "src/data/S6000-6000-256.elv";
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        dimX = Integer.parseInt(scanner.next());
        dimY = Integer.parseInt(scanner.next());
        gridSpacing = Float.parseFloat(scanner.next());
        latitude = Float.parseFloat(scanner.next());

        elv = new double[dimY][dimX];

        while (scanner.hasNext()){
            for (int y=0; y<dimY;y++){
                for (int x=0; x<dimX;x++){
                    elv[y][x] = Double.parseDouble(scanner.next());
                }
            }
        }
        System.out.println("Elevation File has been read...");
        scanner.close();
    }

//========================================================================
//      Create the greyscale top-down view
//========================================================================
    public void deriveImg(){
        img = new BufferedImage(dimX,dimY,BufferedImage.TYPE_INT_ARGB);
        double maxh = -10000.0f;
        double minh = 10000.0f;

        //determine the range of heights
        for (int y = 0; y <dimY;y++){
            for (int x = 0; x < dimX; x++){
                double h = elv[y][x];
                if (h>maxh){maxh = h;}
                if (h<minh){minh = h;}
            }
        }

        //find normalized height value
        for (int y = 0; y <dimY;y++){
            for (int x = 0; x < dimX; x++){
                float val = (float) ((elv[y][x]-minh)/(maxh-minh));
                Color col = new Color(val, val, val, 1.0f);
                img.setRGB(x, y, col.getRGB());
            }
        }
    }

//========================================================================
//      Read in Canopy
//========================================================================
    public void readCanopy() throws FileNotFoundException{
        String fileName = "S6000-6000-256_canopy.pdb";
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        int numSpecies = Integer.parseInt(scanner.next()); 
        
        for (int i=0;i<numSpecies;i++){
            //Plant Details:
            int id =  Integer.parseInt(scanner.next()); 
            float minHeight = Float.parseFloat(scanner.next());
            float maxHeight = Float.parseFloat(scanner.next());
            float avgRatio = Float.parseFloat(scanner.next());
            int numPlants = Integer.parseInt(scanner.next()); 
            PlantType species = new PlantType(id, minHeight, maxHeight, avgRatio, numPlants);  //Create PlantType object
            canopy = new double[numPlants][4];
            for (int y = 0; y < numPlants;y++){
                for (int x = 0; x<5; x++){
                    canopy[y][x] = Float.parseFloat(scanner.next());    //Store 5 data val for each plant in the type.
                }
            }
            species.setData(canopy);    //Set data
            canopyPlants.add(species);
            

        }
        scanner.close();
    }

//========================================================================
//      Read in Undergrowth
//========================================================================
    public void readUndergrowth() throws FileNotFoundException{
        String fileName = "S6000-6000-256_undergrowth.pdb";
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        int numSpecies = Integer.parseInt(scanner.next()); 
        
        for (int i=0;i<numSpecies;i++){
            //Plant Details:
            int id =  Integer.parseInt(scanner.next()); 
            float minHeight = Float.parseFloat(scanner.next());
            float maxHeight = Float.parseFloat(scanner.next());
            float avgRatio = Float.parseFloat(scanner.next());
            int numPlants = Integer.parseInt(scanner.next()); 
            PlantType species = new PlantType(id, minHeight, maxHeight, avgRatio, numPlants);  //Create PlantType object
            undergrowth = new double[numPlants][4];
            for (int y = 0; y < numPlants;y++){
                for (int x = 0; x<5; x++){
                    canopy[y][x] = Float.parseFloat(scanner.next());    //Store 5 data val for each plant in the type.
                }
            }
            species.setData(undergrowth);    //Set data
            underPlants.add(species);
        }
        scanner.close();
    }

//========================================================================
//      Read in Species
//========================================================================
    public void readSpecies(){
        String file = "S6000-6000-256.spc.txt";
    }
}