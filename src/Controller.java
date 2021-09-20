import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;

public class Controller implements MouseWheelListener, MouseListener, MouseMotionListener{
    private Gui gui;
    private ImagePanel image;
    private Terrain terrain;
    private PlantLayer undergrowth;
    private PlantLayer canopy;
    private FileController files;
    private FireController fireController;
    private boolean fireMode;

    public Controller(Gui gui, Terrain terrain, PlantLayer undergrowth, PlantLayer canopy){
        this.gui = gui;
        this.image = gui.getImage();
        this.terrain = terrain;
        this.undergrowth = undergrowth;
        this.canopy = canopy;
        this.files = new FileController();    
        fireController = new FireController(Terrain.getDimX(),Terrain.getDimY(),undergrowth,canopy);

    }

    public void initController(){
        gui.getFireBtn().addActionListener(e -> openFireSim());
        gui.getBackBtn().addActionListener(e -> closeFireSim());
        gui.getLoadBtn().addActionListener(e -> loadFiles());
        gui.getResetBtn().addActionListener(e -> fireController.getFire().clearGrid());
        gui.getMenu1().addActionListener(e -> loadFiles());
        gui.getMenu2().addActionListener(e -> gui.exportView());
        gui.getMenu3().addActionListener(e -> goodbye());
        gui.getMenu4().addActionListener(e -> gui.changeTheme(0));
        gui.getMenu5().addActionListener(e -> gui.changeTheme(1));
        gui.getMenu6().addActionListener(e -> gui.changeTheme(2));
        gui.getMenu7().addActionListener(e -> gui.changeTheme(3));
        image.addMouseListener(this);
        image.addMouseMotionListener(this);
        image.addMouseWheelListener(this);
        //gui.changeTheme(0); //###
        initView();
    }

    public void openFireSim(){
        gui.getFireBtn().setVisible(false);
        gui.getBackBtn().setVisible(true);
        gui.getPauseBtn().setVisible(true);
        gui.getResetBtn().setVisible(true);
        gui.getPlayBtn().setVisible(true);
        fireMode=true;

    }

    public void closeFireSim(){
        gui.getFireBtn().setVisible(true);
        gui.getBackBtn().setVisible(false);
        gui.getPauseBtn().setVisible(false);
        gui.getResetBtn().setVisible(false);
        gui.getPlayBtn().setVisible(false);
        fireMode=false;
    }

    public void runFireSim(){
        System.out.println("Running Fire Simulation");
        
    }

    public void initView(){
        gui.getLoadFrame().setVisible(true);
        //gui.getMain().setVisible(false);
    }

    public void goodbye(){
        System.exit(0);
    }

    public void loadFiles(){
        //gui.getChooser().setMultiSelectionEnabled(true);
        if(gui.showChooser()){
            File[] list = gui.getChooser().getSelectedFiles();
            String[] filenames = new String[4];
            if(list.length != 4){
                System.out.println("Incorrect number of files selected!\nPlease select:\n   > 1 '.elv' file\n   > 2 '.pdb' files\n  > 1 '.spc.txt' file.");
                loadFiles();
            }else{
                if (files.validateFiles(list, filenames) != 4){
                    System.out.println("The selected files could not be loaded, please try again.");
                    loadFiles();
                }else{
                    try{
                        files.readElevation(terrain, filenames[0]);
                        files.readSpecies(filenames[1]);
                        files.readLayer(undergrowth, filenames[2]);
                        files.readLayer(canopy, filenames[3]);
                        System.out.println("All files read in successfully.");
                        refreshView();
                    }catch(FileNotFoundException e){
                        System.out.println("Could not complete file input.");
                    }
                }
            }       
        }
        //cancelled message
        //erro cehck incorrect
    }

    public void refreshView(){
        gui.getLoadFrame().setVisible(false);
        gui.getEast().setPreferredSize(new Dimension(200,Terrain.getDimY()));
        image.deriveImg(terrain);
        image.deriveImg(undergrowth, false);
        image.deriveImg(canopy, true);
        image.setPreferredSize(new Dimension((int)Math.round(Terrain.getDimX()*0.9144),(int)Math.round(Terrain.getDimY()*0.9144)));
        gui.getMain().setPreferredSize(new Dimension(Terrain.getDimX() + 220 ,Terrain.getDimY() + 50));
        gui.getMain().pack();
        gui.getMain().setLocationRelativeTo(null);      
        gui.getMain().setVisible(true);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        image.setZoom(true);
        double multiplier = image.getZoomMult();
		if (e.getWheelRotation() < 0) {	// Zoom in
			multiplier *=1.1;	//Adjust for smoothness
            image.setZoomMult(multiplier);
			image.repaint();
		}
		if (e.getWheelRotation() > 0) {	// Zoom out
			multiplier /=1.1;	//Adjust for smoothness
            image.setZoomMult(multiplier);
			image.repaint();
		}        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point cursor = e.getLocationOnScreen();
		image.setXDiff(cursor.x - image.getStartX());
		image.setYDiff(cursor.y - image.getStartY());

		image.setDragger(true);
		image.repaint();
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point click = e.getPoint();

        //Details on Demand:
        int col = image.getCanopy().getRGB(click.x, click.y);
        int[] speciesColours = Species.getCOLOURS();
        String[][] specieslist = Species.getSPECIES();
        for(int idx = 0; idx < speciesColours.length; ++idx){
            if(speciesColours[idx] == col){
                gui.setSpeciesDetails("Common name:\n" + specieslist[idx][0] + "\nLatin name:\n" + specieslist[idx][1]);
                break;
            }
        }
        
        //Fire Placement:
        if (fireMode){
        fireController.getFire().addFire(click.x, click.y);
        System.out.println("Fire Added");
        }
    }

    @Override
	public void mousePressed(MouseEvent e) {
        image.setReleased(false);
        image.setStartPoint( MouseInfo.getPointerInfo().getLocation() );
		
	}

    @Override
	public void mouseReleased(MouseEvent e) {
		image.setReleased(true);
		image.repaint();
		
	}


    @Override
    public void mouseMoved(MouseEvent e) {
        //
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        //
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //       
    }
}
