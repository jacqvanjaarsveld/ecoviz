/*
* File: Controller.java
* MVC: Controller
* Author(s): BRNJAM019, FRNOWE001, VJRJAC003
* Last edited: 06/10/2021
* Status: Complete
*/

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class Controller implements MouseWheelListener, MouseListener, MouseMotionListener{
    private Gui gui;
    private ImagePanel image;
    private Terrain terrain;
    private PlantLayer undergrowth;
    private PlantLayer canopy;
    private FileController files;
    private boolean fireMode, metric,timerRunning;
    private int numSpecies;
    private Fire fire;
    private Timer timer,timerDerive, captureTimer, iterator;
    private int delay;
    private boolean first;
    private int windMaxKPH, windMaxMPH;
    private float convertion;
    private boolean running;
    private Plant selected;
    private boolean deaf;
    private TimerTask task,task2,task3, iterate;
    private ImageIcon pauseImg,runImg;
    private Firebreak fb;
    private ArrayList<BufferedImage> pathFrames,burntFrames;
    private int frames;
    private String nm;
    private boolean firebreakMode;
    private boolean playing;
    private boolean record;
    private Thread mousecapture;
    private Color speciesColour;
    private boolean scrubUI;

    //==================================================================
    //  Constructor
    //==================================================================
    public Controller(Gui gui, Terrain terrain, PlantLayer undergrowth, PlantLayer canopy) {
        this.gui = gui;
        this.image = gui.getImage();
        this.terrain = terrain;
        this.undergrowth = undergrowth;
        this.canopy = canopy;
        this.files = new FileController();
        this.running = false;
        this.timerRunning = false;
        this.selected = null;
        this.deaf = false;
        this.record = true;
        this.delay = 25;// default - Update Speed
        this.windMaxKPH = 160;
        this.windMaxMPH = 100;
        this.convertion = (float)1.60934;
        this.metric = true;
        this.pathFrames = new ArrayList<BufferedImage>();
        this.burntFrames = new ArrayList<BufferedImage>();
        this.firebreakMode = false;
        this.playing = false;
        this.runImg = new ImageIcon("resources/Running.gif");
        this.pauseImg = new ImageIcon("resources/stamp.gif");
        this.speciesColour = Color.BLACK;
    }

    //==================================================================
    //  Initialise methods
    //==================================================================
    public void initController() {
        gui.getRenderBtn().addActionListener(e -> renderFireSim());
        gui.getFireBtn().addActionListener(e -> openFireSim());
        gui.getBackBtn().addActionListener(e -> closeFireSim());
        gui.getChkShowPath().addActionListener(e -> showPath());
        gui.getChkShowBurnt().addActionListener(e -> showBurnt());
        gui.getChkFirebreak().addItemListener(e -> setupFirebreak());
        gui.getUndoBtn().addActionListener(e -> removeFirebreak());
        gui.getPauseBtn().addActionListener(e -> pauseFireSim());
        gui.getLoadBtn().addActionListener(e -> loadFiles());
        gui.getResetBtn().addActionListener(e -> resetFireSim());
        gui.getMenu1().addActionListener(e -> loadFiles());
        gui.getMenu2().addActionListener(e -> gui.exportView());
        gui.getMenu3().addActionListener(e -> goodbye());
        gui.getMenu4().addActionListener(e -> setGUItheme(0));
        gui.getMenu5().addActionListener(e -> setGUItheme(1));
        gui.getHelp().addActionListener(e -> goHelp());
        gui.getEnlarge().addActionListener(e -> enlargedImage());
        gui.getCloseRender().addActionListener(e -> ScrubbingUI());
        gui.getScrubber().addChangeListener(e -> iterateImages());
        gui.getEndSession().addActionListener(e -> closeScrub());
        gui.getPlayR().addActionListener(e -> playRButton());
        gui.getScrubSpeed().addChangeListener(e -> scrubSpeed());
        gui.getChkCanopy().addItemListener(e -> filterLayers());
        gui.getChkUndergrowth().addItemListener(e -> filterLayers());
        gui.getSpeciesToggle().addItemListener(e -> speciesDetails());
        gui.getCCSpecies().addActionListener(e -> chooseSpeciesColour());
        gui.getHiHeight().addPropertyChangeListener(e -> filterHeightCanopy());
        gui.getLoHeight().addPropertyChangeListener(e -> filterHeightCanopy());
        gui.getHiRadius().addPropertyChangeListener(e -> filterHeightCanopy());
        gui.getLoRadius().addPropertyChangeListener(e -> filterHeightCanopy());
        gui.getRadSlider().addChangeListener(e -> filterByRadius());
        gui.getChkSelectRadius().addItemListener(e -> toggleRadiusSlider());
        gui.getChkMetric().addItemListener(e -> changeUnits());
        JSlider[] sliderList = gui.getSliderList();
        sliderList[0].addChangeListener(e -> extractWindMetrics());
        sliderList[1].addChangeListener(e -> extractWindMetrics());
        sliderList[2].addChangeListener(e -> extractWindMetrics());
        image.addMouseListener(this);
        image.addMouseMotionListener(this);
        image.addMouseWheelListener(this);
        initView();
    }

    public void initView() {
        gui.getChkShowBurnt().setVisible(false);
        gui.getChkShowPath().setVisible(false);
        gui.getLoadFrame().setVisible(true);
    }

    //Colour chooser to choose new selected colour
    public void chooseSpeciesColour(){
        speciesColour = JColorChooser.showDialog(null,"Select a colour",Color.BLACK);
        image.setSpeciesColor(speciesColour);
        image.displayPlant(selected, getViewRadius());
        image.repaint();
    }

    public void enlargedImage(){
        JFrame enlargedImage = new JFrame();
        enlargedImage.setLocationRelativeTo(null);
        enlargedImage.setVisible(true);
        JLabel imgLabel = new JLabel();

        try{
        ImageIcon path = new ImageIcon("resources/enlarged/"+nm+"E.png");
        imgLabel.setIcon(path);
        enlargedImage.add(imgLabel);
        enlargedImage.pack();
        }
        catch (Exception e){

        }
    }

    public void setPlantImage(String name){
        name = name.replace("/","");
        name = name.replace(" ","");
        nm = name;

        try{
            System.out.println("Showing Image");
        ImageIcon path = new ImageIcon("resources/plants/"+nm+".png");
        gui.getEnlarge().setIcon(path);

        } catch (Exception e){        }
    }


    public void removeFirebreak(){
        int latest = Firebreak.getBreakList().size() -1;
        if(latest < 0) return;
            
        
        Firebreak f = Firebreak.getBreakList().get(latest);
        for(int[] i: f.getIDList()){
            fire.restorePlant(i[0], i[1]);
        }
        Species[] slist = PlantLayer.getAllSpecies();
        for(int j = 0; j < f.getIDList().size(); ++j){
            int speciesid = f.getIDList().get(j)[0];
            int plantid = f.getIDList().get(j)[1];
            if(f.getLayers().get(j) == true){       
                slist[speciesid].getCanopyPlants()[plantid].decFirebreak();
            }else{
                slist[speciesid].getUnderPlants()[plantid].decFirebreak();
            }
        }
        Firebreak.removeLatest();
        if(latest < 1) gui.getUndoBtn().setEnabled(false);
        fire.clearGrid();
        fire.genGrid();
        fire.deriveFireImage();
        image.setFire(fire.getImage());
        image.setBurnt(fire.getBurntImage());
        image.deriveImage();
        image.repaint();
    }

    public void setupFirebreak(){
        if(gui.getChkFirebreak().isSelected()){
            firebreakMode = true;
            gui.getEndSession().setEnabled(false);
            gui.getPauseBtn().setEnabled(false);
            gui.getRenderBtn().setEnabled(false);
            gui.getResetBtn().setEnabled(false);
            gui.getBackBtn().setEnabled(false);
            fire.deriveFireImage();
            image.setFire(fire.getImage());
            image.setBurnt(fire.getBurntImage());
            image.repaint();
        }else{
            firebreakMode = false;
            gui.getEndSession().setEnabled(true);
            gui.getPauseBtn().setEnabled(true);
            gui.getRenderBtn().setEnabled(true);
            gui.getResetBtn().setEnabled(true);
            gui.getBackBtn().setEnabled(true); 
        }

    }

    public void closeScrub(){
        if (playing){playRButton();}
        gui.getScrubSpeed().setEnabled(false);
        gui.getBackBtn().setEnabled(true);

        iterate.cancel();
        playing=false;
        iterator.cancel();
        iterator.purge();

        gui.getPlayR().setVisible(false);
        gui.getScrubber().setEnabled(false);
        gui.getScrubber().setVisible(false);
        gui.getEndSession().setVisible(false);
        gui.getFireBtn().setVisible(true);
        scrubUI=false;
        closeFireSim();

        resetFrames();
        first=false;
        image.repaint();

    }

    public void ScrubbingUI(){
        scrubUI=true;
        gui.getScrubSpeed().setEnabled(true);
        if(record){
            record=false;
            captureTimer.schedule(task3,0,125);
            gui.getCloseRender().setText("End Record (closes session)");
            gui.getBackBtn().setEnabled(false);
            gui.getCloseRender().setBackground(new Color(112, 73, 102));
        } else{
            
            task3.cancel();
            captureTimer.cancel();
            captureTimer.purge();
        playRender(25);

        gui.getEndSession().setVisible(true);
        gui.getStamp().setIcon(pauseImg);
        gui.getCloseRender().setEnabled(false);

        gui.getPlayR().setVisible(true);
        gui.getScrubber().setEnabled(true);
        gui.getScrubber().setVisible(true);
        closeFireSim();
        gui.getRenderBtn().setVisible(false);
        gui.getFireBtn().setVisible(false);
        gui.getStamp().setIcon(pauseImg);

        gui.getScrubber().setMaximum(burntFrames.size()-2);
        gui.getScrubber().setValue(0);

        record=true;
        gui.getCloseRender().setText("Record");
        gui.getCloseRender().setBackground(new Color(44, 105, 122));


        System.out.println("Render Session Complete");
        }
    }

    public void playRButton(){
        playing=!playing;

        if (playing){
            gui.getPlayR().setText("Pause");
        } else {
            gui.getPlayR().setText("Play");
        }
    };

    public void end(){
        if (playing){
            gui.getPlayR().setText("Pause");
        } else {
            gui.getPlayR().setText("Play");
        }
    }

    public void scrubSpeed(){
        iterate.cancel();
        iterator.cancel();
        iterator.purge();

        if (gui.getScrubSpeed().getValue() == 1){
            playRender(200);
            gui.setSpeedLbl("Scrubbing Speed: SLOW");
        }
        else{
            playRender(25);
            gui.setSpeedLbl("Scrubbing Speed: FAST");

        }
    }

    public void playRender(int speed){
        iterator = new Timer();

        iterate = new TimerTask() {

            @Override
            public void run() {
                if (playing){
                
                int prev = gui.getScrubber().getValue();
                if (prev==gui.getScrubber().getMaximum()){
                    playing=false;
                    end();
                    
                } else if (prev!=pathFrames.size()){
                    gui.getScrubber().setValue(prev+1);
                    
                }
            }
        }
        };

        iterator.schedule(iterate,0, speed);



    }

    public void iterateImages(){

        //Pass through the bufferedImage to render
        try{
        image.setFire(pathFrames.get(gui.getScrubber().getValue()));
        image.setBurnt(burntFrames.get(gui.getScrubber().getValue()));
        image.repaint();}catch(Exception e){}

    }

    public void storeImage(BufferedImage p, BufferedImage b){
        pathFrames.add(p);
        burntFrames.add(b);
    }

    public void resetFrames(){
        pathFrames = new ArrayList<BufferedImage>();
        burntFrames = new ArrayList<BufferedImage>();
    }

    public void goHelp(){

        try{
        System.out.println("Opening Remote Manual");
            Desktop.getDesktop().browse(new URL(
                "https://drive.google.com/file/d/1DyzfVwKW3HFA8k4Cycb2po5Bd8ccfikE/view?usp=sharing"
                ).toURI());
        } catch (Exception e){
        System.out.println("Unable to open...");


        }
    }

    public void setGUItheme(int theme){
        deaf = true;
        gui.changeTheme(theme);
        deaf = false;
    }

    public void resetFireSim() {
        if (running) pauseFireSim();
        fire.clearGrid();
        fire.genGrid();
        fire.deriveFireImage();
        BufferedImage updatedFireImage = fire.getImage();
        image.setFire(updatedFireImage);

        BufferedImage updatedBurntImage = fire.getImage();
        image.setBurnt(updatedBurntImage);
        fire.setShowBurnt(true);
        fire.setShowPath(true);
        gui.setChkBurnt();
        gui.setChkPath();
        image.repaint();
    }

    public void showPath() {
        if (gui.getChkShowPath().isSelected()){
            fire.setShowPath(true);
        } else {
            fire.setShowPath(false);
        }
        fire.deriveFireImage();
        image.setFire(fire.getImage());
        image.setBurnt(fire.getBurntImage());
        image.repaint();
    }

    public void showBurnt() {
        if (gui.getChkShowBurnt().isSelected()){
            fire.setShowBurnt(true);
        } else {
            fire.setShowBurnt(false);
        }
        fire.deriveFireImage();
        image.setFire(fire.getImage());
        image.setBurnt(fire.getBurntImage());
        image.repaint();
    }

    public void openFireSim() {
        resetAllFilters();

        gui.getChkRecord().setVisible(true);
        gui.getCloseRender().setEnabled(false);
        gui.getTabPane().setSelectedIndex(1);
        gui.getTabPane().setEnabled(false);
        gui.getFireBtn().setVisible(false);
        gui.getChkFirebreak().setVisible(true);
        gui.getChkFirebreak().setEnabled(true);
        gui.getUndoBtn().setVisible(true);
        gui.getPauseBtn().setVisible(true);
        gui.getBackBtn().setVisible(true);
        gui.getResetBtn().setVisible(true);
        gui.getRenderBtn().setVisible(true);
        gui.getCloseRender().setVisible(true);
        gui.getChkShowBurnt().setVisible(true);
        gui.getChkShowPath().setVisible(true);
        fireMode = true;
        gui.getPauseBtn().setEnabled(false);
        // Setup fire:
        fire = new Fire(Terrain.getDimX(), Terrain.getDimY(), undergrowth.getLocations(), canopy.getLocations());
        if (running){
            fire.setWindDirection(gui.getWindDir());
            if (metric) fire.setWindForce(gui.getWindSpd(), windMaxKPH);
            else fire.setWindForce(gui.getWindSpd(), windMaxMPH); 
        }
    }

    public void closeFireSim() {
        gui.getChkRecord().setSelected(false);
        first=false;
        gui.getChkRecord().setVisible(false);

        gui.getCloseRender().setEnabled(false);

        gui.getTabPane().setSelectedIndex(0);
        gui.getTabPane().setEnabled(true);
        gui.getFireBtn().setVisible(true);
        gui.getChkFirebreak().setVisible(false);
        gui.getUndoBtn().setVisible(false);
        gui.getBackBtn().setVisible(false);
        gui.getResetBtn().setVisible(false);
        gui.getRenderBtn().setVisible(false);
        gui.getPauseBtn().setVisible(false);
        gui.getCloseRender().setVisible(false);
        gui.getChkShowBurnt().setVisible(false);
        gui.getChkShowPath().setVisible(false);

        if (!scrubUI){fireMode = false;}
        
        if (timerRunning){
            task.cancel();
            task2.cancel();
            timer.cancel();
            timerDerive.cancel();
            timer.purge();
            timerDerive.purge();
            task3.cancel();
            captureTimer.cancel();
            captureTimer.purge();
            timerRunning=false;
        }
        running = false;

        gui.getRenderBtn().setEnabled(true);
        gui.getPauseBtn().setEnabled(false);
        image.repaint();
        gui.getStamp().setIcon(pauseImg);
        resetFireSim();

    }

    public void pauseFireSim() {
        if (running == false) {
            running = true;
            gui.getPauseBtn().setText("Pause");
            gui.getStamp().setIcon(runImg);
            fire.setWindDirection(gui.getWindDir());
            if (metric) fire.setWindForce(gui.getWindSpd(), windMaxKPH);
            else fire.setWindForce(gui.getWindSpd(), windMaxMPH); 
        } else {
            running = false;
            gui.getPauseBtn().setText("Play");
            gui.getStamp().setIcon(pauseImg);
            fire.setWindDirection(gui.getWindDir());
            if (metric) fire.setWindForce(gui.getWindSpd(), windMaxKPH);
            else fire.setWindForce(gui.getWindSpd(), windMaxMPH); 
        }
    }

    public void renderFireSim() { // Single use
        frames=0;
        gui.getCloseRender().setEnabled(true);
        gui.getChkFirebreak().setEnabled(false);
        System.out.println("Running the Fire Simulation");
        gui.getPauseBtn().setEnabled(true);
        fire.setWindDirection(gui.getWindDir());
        if (metric) fire.setWindForce(gui.getWindSpd(), windMaxKPH);
        else fire.setWindForce(gui.getWindSpd(), windMaxMPH); 
        running = true;
        gui.getStamp().setIcon(runImg);

        timer = new Timer();
        timerDerive = new Timer();
        captureTimer = new Timer();
        timerRunning=true;

        task = new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    fire.simulate(0, (Terrain.getDimX() * Terrain.getDimY())); // Run simulation on all
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        task2 = new TimerTask() {

            @Override
            public void run() {

                if (running) {
                    fire.deriveFireImage();

                    BufferedImage updatedFireImage = fire.getImage();
                    BufferedImage updatedBurnImage = fire.getBurntImage();

                    image.setFire(updatedFireImage);
                    image.setBurnt(updatedBurnImage);

                    image.repaint();
                    
                }
            }
        };
        
        task3 = new TimerTask() {

            @Override
            public void run() {

                if (running) {
                    BufferedImage updatedFireImage = fire.getImage();
                    BufferedImage updatedBurnImage = fire.getBurntImage();

                    if (frames<=150 && (first)){
                        storeImage(updatedFireImage,updatedBurnImage);
                    }else{
                        System.out.println("Simulation Render Complete (No more can be recorded)");
                        captureTimer.cancel();
                        captureTimer.purge();
                        task3.cancel();
                    }
                    frames++;
                    
                }
            }
        };


        timer.schedule(task, 0, 1);
        timerDerive.schedule(task2,0,1);

        if (gui.getChkRecord().isSelected()){ScrubbingUI();}

        gui.getRenderBtn().setEnabled(false);
    }

    public void goodbye() {
        System.exit(0);
    }

    public void loadFiles() {
        if (gui.showChooser()) {
            File[] list = gui.getChooser().getSelectedFiles();
            String[] filenames = new String[4];
            if (list.length != 4) {
                System.out.println(
                        "Incorrect number of files selected!\nPlease select:\n   > 1 '.elv' file\n   > 2 '.pdb' files\n  > 1 '.spc.txt' file.");
                loadFiles();
            } else {
                if (!files.validateFiles(list, filenames)) {
                    System.out.println("The selected files could not be loaded, please try again.");
                    loadFiles();
                } else {
                    try {
                        files.readElevation(terrain, filenames[0]);
                        numSpecies = files.readSpecies(filenames[1]);
                        FileController fileRun = new FileController(filenames[2], undergrowth, false);
                        Thread fileThread = new Thread(fileRun);
                        fileThread.start();
                        files.readLayer(canopy, filenames[3], true); // true = canopy
                        try {
                            fileThread.join();
                        } catch (Exception e) {
                            System.out.println("Premature thread exit.");
                        }
                        Collections.sort(PlantLayer.getPlantList());
                        System.out.println("All files read in successfully.");
                        refreshView();
                    } catch (FileNotFoundException e) {
                        System.out.println("Could not complete file input.");
                    }
                }
            }
        } else {
            System.out.println("Cancelled by the user.");
        }

        //Starts the application maximised
        gui.getMain().setExtendedState(gui.getMain().getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private boolean insideCanopy(Point point, Plant plant) {
        int x = (int) Math.round(point.x / image.getZoomMult() + image.getTLX());
        int y = (int) Math.round(point.y / image.getZoomMult() + image.getTLY());
        return (Math.pow((x - plant.getX()), 2) + Math.pow((y - plant.getY()), 2)) <= Math.pow(plant.getCanopy(), 2);
    }

    public void resetSpeciesColours(){
        for(Species s: PlantLayer.getAllSpecies()){
            s.setColour(s.getPrevColour());
        }
    }

    public void changeSpeciesColour(int id) {
        Species[] specieslist = PlantLayer.getAllSpecies();
        resetSpeciesColours();
        Color c = speciesColour;
        specieslist[id].setColour(c);
    }

    public void refreshView() {
        gui.getLoadFrame().setVisible(false);
        gui.getEast().setPreferredSize(new Dimension(230,Terrain.getDimY()));
        this.deaf = true;
        gui.getRadSlider().setValue(1024);
        gui.getRadSlider().setEnabled(false);
        gui.getChkSelectRadius().setEnabled(false);
        gui.getChkSelectRadius().setSelected(false);
        gui.getHiHeight().setValue(Math.ceil(FileController.getMaxHeight()));
        gui.getHiRadius().setValue(Math.ceil(FileController.getMaxRadius()));
        gui.getLoHeight().setValue(0.00f);
        gui.getLoRadius().setValue(0.00f);
        image.setFilterLimits(0.00f, (float)Math.ceil(FileController.getMaxHeight()), 0.00f, (float)Math.ceil(FileController.getMaxRadius()));
        this.deaf = false;
        
        this.speciesColour = Color.BLACK;
        image.setSpeciesColor(Color.BLACK);
        
        image.setPlantsInView(files.getTotalSpecies());
        image.deriveImg(terrain);
        selected = null;
        image.reset();
        image.calculateView();
        image.deriveImage();
        addSpeciesFilters();
        resetLayerFilters();
        updateFilterSpeciesCounts();      
        image.resetDetails();
        resetDesc();
        
        image.setPreferredSize(new Dimension(Terrain.getDimX(),Terrain.getDimY()));
        gui.getMini().setZone(0, 0, Terrain.getDimX(), Terrain.getDimY());
        gui.getMain().setPreferredSize(new Dimension(Terrain.getDimX() + 250, Terrain.getDimY() + 100));
        gui.getMain().pack();
        gui.getMain().setLocationRelativeTo(null);
        gui.getMain().setVisible(true);

        image.repaint();
    }

    public void addSpeciesFilters() {
        gui.clearFilters();
        Species[] list = PlantLayer.getAllSpecies();
        JCheckBox[] filters = new JCheckBox[numSpecies];
        for (int x = 0; x < numSpecies; ++x) {
            JCheckBox chk = gui.addFilter(list[x].getCommon());
            chk.addItemListener(e -> filterSpecies());
            filters[x] = chk;
        }
        gui.setFilterList(filters);
    }

    public void filterSpecies() {
        Species[] list = PlantLayer.getAllSpecies();
        JCheckBox[] filters = gui.getFilterList();
        for (int idx = 0; idx < numSpecies; ++idx) {
            if (filters[idx].isSelected()) {
                list[idx].setFilter(true);
            } else {
                list[idx].setFilter(false);
            }
        }
        if(deaf) return;
        image.deriveImage();
        updateFilterSpeciesCounts();
        image.repaint();
    }

    public void filterLayers() {
        
        if (gui.getChkCanopy().isSelected())
            image.setShowCanopy(true);
        else
            image.setShowCanopy(false);

        if (gui.getChkUndergrowth().isSelected())
            image.setShowUnderGrowth(true);
        else
            image.setShowUnderGrowth(false);
        if(deaf) return;
        image.deriveImage();
        updateFilterSpeciesCounts();
        image.repaint();
    }

    public void resetLayerFilters() {
        gui.getChkCanopy().setSelected(true);
        gui.getChkUndergrowth().setSelected(true);
    }

    //Filter plants in between given height/canopy range
    public void filterHeightCanopy(){
        if(deaf) return;
        deaf = true;
        float loHeight = Float.valueOf(gui.getLoHeight().getText());
        float hiHeight = Float.valueOf(gui.getHiHeight().getText());
        float loRadius = Float.valueOf(gui.getLoRadius().getText());
        float hiRadius = Float.valueOf(gui.getHiRadius().getText());
        if((loHeight > hiHeight) || (loHeight < 0)){
            loHeight = 0.00f;
            gui.getLoHeight().setValue(loHeight);
        }
        if((loRadius > hiRadius) || (loRadius < 0)){
            loRadius = 0.00f;
            gui.getLoRadius().setValue(loRadius);
        }
        if((hiHeight > Math.ceil(FileController.getMaxHeight())) || hiHeight < 0){
            hiHeight = (float)Math.ceil(FileController.getMaxHeight());
            gui.getHiHeight().setValue(hiHeight);         
        }
        if((hiRadius > Math.ceil(FileController.getMaxRadius())) || hiRadius < 0){
            hiRadius =  (float)Math.ceil(FileController.getMaxRadius());
            gui.getHiRadius().setValue(hiRadius);         
        }
        image.setFilterLimits(loHeight,hiHeight,loRadius,hiRadius);
        deaf = false;
        image.deriveImage();
        updateFilterSpeciesCounts();
        image.repaint();        
    }

    //Update count of plants for each species on species tab
    public void updateFilterSpeciesCounts(){
        int[] speciesCounts = image.getPlantsInView();
        for(int i = 0; i < gui.getFilterList().length; ++i){
            String current = gui.getFilterList()[i].getText();
            int bracket = current.indexOf("(");
            if(bracket != -1) current = current.substring(0,bracket-1);
            current += " (" + speciesCounts[i] + ")";
            if(selected != null){ if(i == selected.getSpeciesID()) current += "*";}
            gui.getFilterList()[i].setText(current);
        }
    }

    //==================================================================
    //  Radius manipulations
    //==================================================================

    //Call filtering methods
    public void filterByRadius(){
        if(deaf) return;
        image.displayPlant(selected, getViewRadius());
        image.deriveImage();
        updateFilterSpeciesCounts();
        image.repaint();
    }

    //Accessor method
    public int getViewRadius(){
        if(gui.getChkSelectRadius().isSelected()) return gui.getRadSlider().getValue();
        return -1;
    }

    //Enable/disable radius slider based on checkbox
    public void toggleRadiusSlider(){
        if(deaf) return;
        deaf = true;
        if(gui.getChkSelectRadius().isSelected()){
            gui.getRadSlider().setEnabled(true);
        }else{
            gui.getRadSlider().setEnabled(false);
            gui.getRadSlider().setValue(1024);
        }
        deaf = false;
        image.displayPlant(selected, getViewRadius());
        image.deriveImage();
        image.repaint();
    }

    //Generates new fire grid for Fire class
    public void updateFireGrid(){
        fire.clearGrid();
        for(int[] i: fb.getIDList()){
            fire.removePlant(i[0], i[1]);
        }
        fire.genGrid();
    }
    
    //==================================================================
    //  Wind manipulation
    //==================================================================

    //Extracts metrics of wind from gui components and sets wind values based on these
    public void extractWindMetrics(){
        if (gui.getChkMetric().isSelected()) gui.setWindSpdLbl("Wind Speed: "+Integer.toString(gui.getWindSpd())+" KPH");
        else gui.setWindSpdLbl("Wind Speed: "+Integer.toString(gui.getWindSpd())+" MPH");

        if (running){
            fire.setWindDirection(gui.getWindDir());
            if (metric) fire.setWindForce(gui.getWindSpd(), windMaxKPH);
            else fire.setWindForce(gui.getWindSpd(), windMaxMPH);
        }
        switch(Integer.toString(gui.getWindDir())){
            case "1":
                gui.setWindDirLbl("Wind Direction: North");
                moveCompass("North");
                break;
            case "2":
                gui.setWindDirLbl("Wind Direction: North East");
                moveCompass("North-East");
                break;
            case "3":
                gui.setWindDirLbl("Wind Direction: East");
                moveCompass("East");
                break;
            case "4":
                gui.setWindDirLbl("Wind Direction: South East");
                moveCompass("South-East");
                break;
            case "5":
                gui.setWindDirLbl("Wind Direction: South");
                moveCompass("South");
                break;
            case "6":
                gui.setWindDirLbl("Wind Direction: South West");
                moveCompass("South-West");
                break;
            case "7":
                gui.setWindDirLbl("Wind Direction: West");
                moveCompass("West");
                break;
            case "8":
                gui.setWindDirLbl("Wind Direction: North West");
                moveCompass("North-West");
                break;
        }
    }

    //Switches compas image to new direction
    public void moveCompass(String direction){
        String path = "resources/compass/" + direction + ".png";
        gui.setCompassPath(path);
        gui.setCompassIcon();
    }

    //Change metrics of wind speed value
    public void changeUnits(){
        if (gui.getChkMetric().isSelected()){
            metric = true;
            gui.setWindSpdMax(windMaxKPH);
            gui.setWindSpd((int)Math.ceil((convertion*gui.getWindSpd())));
            gui.setWindSpdLbl("Wind Speed: "+Integer.toString(gui.getWindSpd())+" KPH");
        }else{
            metric = false;
            gui.setWindSpd((int)Math.floor((gui.getWindSpd()/convertion)));
            gui.setWindSpdMax(windMaxMPH);
            gui.setWindSpdLbl("Wind Speed: "+Integer.toString(gui.getWindSpd())+" MPH");
        }
    }

    //Shows all plants of selected individual's species
    public void speciesDetails(){
        if(selected != null){
            if(gui.getSpeciesToggle().isSelected()){
                int id = selected.getSpeciesID();
                changeSpeciesColour(id);
                setSpeciesDesc(id);
                gui.getEnlarge().setVisible(true);

            }else{
                gui.getEnlarge().setVisible(false);


                setPlantDesc();
                resetSpeciesColours();
            }
            image.deriveImage();
            image.repaint();
        }
    }

    //==================================================================
    //  Plant descriptions
    //==================================================================

    //Sets the details tab to new titles and values based on selected species
    public void setSpeciesDesc(int id) {
        gui.getShortTitle().setText("Shortest plant:");
        gui.getTallTitle().setText("Tallest plant:");
        gui.getAvTitle().setText("Avg. Radius-Height ratio:");
        Species[] specieslist = PlantLayer.getAllSpecies();
        gui.setCommon(specieslist[id].getCommon());
        setPlantImage(specieslist[id].getCommon());

        gui.setLatin(specieslist[id].getLatin());
        gui.setTallest(round(Float.toString(specieslist[id].getMaxHeight())));
        gui.setShortest(round(Float.toString(specieslist[id].getMinHeight())));
        gui.setAvg(round(Float.toString(specieslist[id].getAvgRatio())));
        gui.setNumber(Integer.toString(specieslist[id].getNumPlants()));       
    }

    //Sets the details tab to new titles and values based on selected plant
    public void setPlantDesc() {
        gui.getShortTitle().setText("Canopy radius:");
        gui.getTallTitle().setText("Height:");
        gui.getAvTitle().setText("Radius-Height ratio:");
        Species[] specieslist = PlantLayer.getAllSpecies();
        gui.setCommon(specieslist[selected.getSpeciesID()].getCommon());
        gui.setLatin(specieslist[selected.getSpeciesID()].getLatin());
        gui.setTallest(round(Double.toString(selected.getHeight())));
        gui.setShortest(round(Double.toString(selected.getCanopy())));
        gui.setAvg(round(Double.toString(selected.getHeight() / selected.getCanopy())));
        gui.setNumber("1");
    }

    //Reset description titles and values to default
    public void resetDesc() {
        gui.getShortTitle().setText("Canopy radius:");
        gui.getTallTitle().setText("Height:");
        gui.getAvTitle().setText("Radius-Height ratio:");
        gui.setCommon("No plant selected. \nSelect one");
        gui.setLatin("No plant selected. \nSelect one");
        gui.setTallest("0.0");
        gui.setShortest("0.0");
        gui.setAvg("0.0");
        gui.setNumber("0");
    }

    //Accessor method
    public void setDelay(int d){
        delay = d;
    }
    
    //==================================================================
    //  Reset methods
    //==================================================================

    //Resets all filters and images to a default state for Fire simulation
    public void resetAllFilters(){
        //Reset click and DoD
        selected = null;
        image.displayPlant(selected, getViewRadius());
        image.resetDetails();
        resetDesc();
        resetSpeciesColours();
        //Reset species and layer filters
        deaf = true;
        for(JCheckBox filter: gui.getFilterList()){
            filter.setSelected(true);
        }
        resetLayerFilters();

        //height/canopy filter reset
        gui.getLoHeight().setValue(0.00);
        gui.getHiHeight().setValue(Math.ceil(FileController.getMaxHeight()));
        gui.getLoRadius().setValue(0.00);
        gui.getHiRadius().setValue(Math.ceil(FileController.getMaxRadius()));
        image.setFilterLimits(0.00f,(float)Math.ceil(FileController.getMaxHeight()),0.00f,(float)Math.ceil(FileController.getMaxHeight()));
        gui.getChkSelectRadius().setEnabled(false);
        gui.getRadSlider().setValue(1024);
        deaf = false;

        //Update zoom
        image.setZoomMult(1.0);
        image.setPrevZoomMult(1.0);
        image.setZoom(true);
        image.calculateView();
        updateFilterSpeciesCounts();
        
        //Radius filter
        toggleRadiusSlider();
        gui.getMini().setZone(0, 0, Terrain.getDimX(), Terrain.getDimY());
    }

    //==================================================================
    //  Overrriden methods
    //==================================================================

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(fireMode) return;
        
        double multiplier = image.getZoomMult();
        if (e.getWheelRotation() < 0) { // Zoom in
            multiplier *= 1.1; // Adjust for smoothness
            if(multiplier > 100) multiplier = 100;
            image.setZoomMult(multiplier);
        }
        if (e.getWheelRotation() > 0) { // Zoom out
            if(image.getZoomMult() == 1.0) return;
            multiplier /= 1.1; // Adjust for smoothness
            if (multiplier < 1)
                multiplier = 1;
            image.setZoomMult(multiplier);

        }
        image.setZoom(true);
        image.calculateView();
        image.deriveImage();
        updateFilterSpeciesCounts();
        if(selected != null) image.displayPlant(selected, getViewRadius());
        image.repaint();
        gui.getMini().setZone(image.getTLX(), image.getTLY(), image.getNewDimX(), image.getNewDimY());

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point cursor = e.getLocationOnScreen();
        image.setXDiff(cursor.x - image.getStartX());
        image.setYDiff(cursor.y - image.getStartY());
        image.setDragger(true);
        if(fireMode){
            if(!firebreakMode) return;
            image.drawFirebreak(fb);
            image.repaint();
        }else{
            if(image.getZoomMult()==1.0) return;
            image.calculateView();
            image.deriveImage();
            updateFilterSpeciesCounts();
            if(selected != null) image.displayPlant(selected, getViewRadius());
            image.repaint();
            gui.getMini().setZone(image.getTLX(), image.getTLY(), image.getNewDimX(), image.getNewDimY());
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gui.getEnlarge().setVisible(false);
        gui.getSpeciesToggle().setEnabled(true);
        Point click = e.getPoint();
        int xPos =0;
        int yPos=0;
        double rad = 0.0;
        Boolean clicked=false;
        // Fire Placement:
        if (fireMode) {
            if(firebreakMode) return;

            if (!scrubUI){
                for (int p = PlantLayer.getPlantList().size()-1; p >= 0; --p) {
                    Plant plant = PlantLayer.getPlantList().get(p);
                    // Check if species is not currently filtered
                    if (plant.isInFireBreak() == 0) {
                        if (insideCanopy(click, plant)) {
                            rad = plant.getCanopy();
                            xPos = plant.getX();
                            yPos = plant.getY();
                            clicked=true;
                            break;
                        }
                    }

                }

                if (clicked & !scrubUI){
                    first=true;
                    fire.addFire(xPos, yPos,rad);

                }

                BufferedImage updatedFireImage = fire.getImage();
                BufferedImage burntImage = fire.getBurntImage();

                image.setFire(updatedFireImage);
                image.setBurnt(burntImage);
                
                image.repaint();
                System.out.println("Fire Added");
            }

        } else {
            gui.getSpeciesToggle().setSelected(false);

            int id = -1;
            Species[] list = PlantLayer.getAllSpecies();
            for (int p = PlantLayer.getPlantList().size()-1; p >= 0; --p) {
                Plant plant = PlantLayer.getPlantList().get(p);
                // Check if species is not currently filtered
                if (list[plant.getSpeciesID()].getFilter() && plant.getFilter() && plant.getCanopyFlag() && plant.getHeightFlag()) {
                    if((image.getShowCanopy() && plant.getLayer()) || (image.getShowUndergrowth() && !plant.getLayer())){ 
                        if (insideCanopy(click, plant)) {
                            selected = plant;
                            id = plant.getSpeciesID();
                            image.displayPlant(selected, getViewRadius());
                            image.repaint();
                            break;
                        }
                    }
                }

            }
            if (id > -1) {
                setPlantDesc();
                gui.getTabPane().setSelectedIndex(0);
                gui.getChkSelectRadius().setEnabled(true);
            } else {            
                deaf = true;          
                gui.getSpeciesToggle().setEnabled(false);
                gui.getChkSelectRadius().setEnabled(false);
                gui.getChkSelectRadius().setSelected(false);
                gui.getRadSlider().setEnabled(false);             
                gui.getRadSlider().setValue(1024);
                deaf = false;
                image.displayPlant(selected, getViewRadius());
                selected = null;
                image.resetDetails();
                resetDesc();
                resetSpeciesColours();              
            }

            updateFilterSpeciesCounts();
            image.displayPlant(selected, getViewRadius());
            image.deriveImage();
            image.repaint();
            gui.getMini().setZone(image.getTLX(), image.getTLY(), image.getNewDimX(), image.getNewDimY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        image.setReleased(false);
        image.setStartPoint(MouseInfo.getPointerInfo().getLocation());
        if(firebreakMode) fb = new Firebreak();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(image.getDragger()){
            image.setReleased(true);
            if(fireMode){
                if(!firebreakMode) return;
                image.drawFirebreak(fb);
                image.deriveImage();
                updateFireGrid();
                gui.getUndoBtn().setEnabled(true);
            }else{
                image.calculateView();
                image.deriveImage();
                updateFilterSpeciesCounts();
                if(selected != null) image.displayPlant(selected, getViewRadius());
                
            }
            image.repaint();
            gui.getMini().setZone(image.getTLX(), image.getTLY(), image.getNewDimX(), image.getNewDimY());
        }		
	}

    @Override
    public void mouseMoved(MouseEvent e) {
        //Not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mousecapture = new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(0);
                    }catch(InterruptedException e){
                        gui.setMousePositions(0,0);
                        break;
                    }
                    int x = (int)Math.round(MouseInfo.getPointerInfo().getLocation().getX()-image.getLocationOnScreen().getX());
                    int y = (int)Math.round(MouseInfo.getPointerInfo().getLocation().getY()-image.getLocationOnScreen().getY());
                    gui.setMousePositions(x,y);
                }
            }
        };
        mousecapture.start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mousecapture.interrupt();
    }

    public String round(String f){

        float temp = Math.round(Float.valueOf(f)*100.0f) / 100.0f;
        return Float.toString(temp);
    }
}
