/*
* Previously: PlantLayer.java
* File: Gui.java
* Author(s): BRNJAM019, FRNOWE001, VJRJAC003
* Status: In progress
*/

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.FlowLayout;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.event.*;
import com.formdev.flatlaf.*;
//import com.jidesoft.swing.*;

public class Gui extends JPanel implements ChangeListener{

    private JButton load;
    private JFileChooser fChooser;
    private JFrame frame;
    private JFrame loadIn;
    private JMenuItem i1,i2,i3,a1,a2,a3,a4;
    private JLabel pointerLbl;
    private JSlider wDirSlider,spdSlider;
    private ImagePanel mainPanel;
    private JButton btnFire;
    private JButton btnBack;
    private int delay;

    //FireSim Controls:
    private JButton btnRender;
    private JButton btnPause;
    private JButton btnReset;

    PlantLayer canopy,undergrowth;

    //East Panel:
    private JPanel pnlEast;
    private JLabel fontExample;
    private JTextArea plantDescription;
    private JLabel config;
    private miniMap mini;
    private JCheckBox ChkUnderGrowth,ChkCanopy;
    private JLabel lblSpeed;
    private JPanel pnlFilters;
    private JCheckBox[] filterlist;
    private JTabbedPane tabbedPane;
    private JCheckBox speciesToggle;
    private JSlider radSlider;
    
    private String common;
    private String latin;
    private String shortest;
    private String tallest;
    private String avgRat;
    private String totNum;
    private JLabel lblCommon;
    private JLabel lblLatin;
    private JLabel lblTall;
    private JLabel lblShort;
    private JLabel lblAvg;
    private JLabel lblNum;

    private JLabel shTitle;
    private JLabel taTitle;
    private JLabel avTitle;

    //Filter Section:
    private JLabel lblSearch;
    private JTextField search;

    public JFrame getMain(){
        return this.frame;
    }

    public JFrame getLoadFrame(){
        return this.loadIn;
    }

    public int getDelay(){
        return delay;
    }

    public JButton getFireBtn(){
        return this.btnFire;
    }

    public JButton getBackBtn(){
        return this.btnBack;
    }
    public JButton getRenderBtn(){
        return this.btnRender;
    }

    public JButton getResetBtn(){
        return this.btnReset;
    }

    public JButton getPauseBtn(){
        return this.btnPause;
    }

    public JButton getLoadBtn(){
        return this.load;
    }

    public JFileChooser getChooser(){
        return this.fChooser;
    }

    public JMenuItem getMenu1(){
        return this.i1;
    }
    public JMenuItem getMenu2(){
        return this.i2;
    }
    public JMenuItem getMenu3(){
        return this.i3;
    }
    public JMenuItem getMenu4(){
        return this.a1;
    }
    public JMenuItem getMenu5(){
        return this.a2;
    }
    public JMenuItem getMenu6(){
        return this.a3;
    }

    public JMenuItem getMenu7(){
        return this.a4;
    }

    public JCheckBox getChkUndergrowth(){
        return this.ChkUnderGrowth;
    }

    public JCheckBox getChkCanopy(){
        return this.ChkCanopy;
    }

    public ImagePanel getImage(){
        return this.mainPanel;
    }

    public miniMap getMini(){
        return this.mini;
    }

    public JPanel getEast(){
        return this.pnlEast;
    }

    public JPanel getFilterPanel(){
        return this.pnlFilters;
    }

    public JCheckBox[] getFilterList(){
        return this.filterlist;
    }

    public JTabbedPane getTabPane(){
        return this.tabbedPane;
    }

    public JSlider getRadSlider(){
        return this.radSlider;
    }

    public JCheckBox getSpeciesToggle(){
        return this.speciesToggle;
    }

    public JLabel getShortTitle(){
        return this.shTitle;
    }

    public JLabel getTallTitle(){
        return this.taTitle;
    }

    public JLabel getAvTitle(){
        return this.avTitle;
    }

    public void setFilterList(JCheckBox[] list){
        this.filterlist = list;
    }

    public Gui() {
        delay=75; //default
        //canopy=c;
        //undergrowth=u;
        //======================================================================
        //      Load in Files Frame:
        //======================================================================
        loadIn = new JFrame("Initialising");
        loadIn.setSize(400,400);
        loadIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel loading = new JLabel();
        ImageIcon path = new ImageIcon("resources/ECOVIZ.gif");
        loading.setIcon(path);
        fChooser = new JFileChooser();
        fChooser.setMultiSelectionEnabled(true);

        //Add button for loading in files:
        load = new JButton();
        load.setText("Load Files");
        //load.addActionListener(fileController);

        loadIn.add(loading);
        loadIn.getContentPane().add(BorderLayout.SOUTH, load);

        loadIn.setLocationRelativeTo(null);
        loadIn.setVisible(true);

        //======================================================================
        //      Frame:
        //======================================================================
        frame = new JFrame("EcoViz");

        //frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        //======================================================================
        //      West Panel (MAIN PANEL) : 
        //======================================================================
        mainPanel = new ImagePanel();
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());


        JPanel pnlWest = new JPanel();
            pnlWest.add(mainPanel);

        //======================================================================
        //      South Panel:
        //======================================================================
        JPanel pnlSouth = new JPanel();
            pnlSouth.setLayout(new BoxLayout(pnlSouth,BoxLayout.Y_AXIS));

            btnFire = new JButton("Simulate Fire");
            btnBack = new JButton("Leave Fire Simulator");

            btnRender = new JButton("Run");
            btnRender.setVisible(false);

            btnPause = new JButton("Pause");
            btnPause.setVisible(false);

            btnReset = new JButton("Reset");
            btnReset.setVisible(false);

            //Add Components
            JLabel stamp = new JLabel();
            ImageIcon path2 = new ImageIcon("resources/stamp.gif");
            stamp.setIcon(path2);

            btnBack.setVisible(false);
            btnBack.setBackground(Color.RED);

            pnlSouth.add(stamp);

            pnlSouth.add(btnFire);
            pnlSouth.add(btnRender);
            pnlSouth.add(btnPause);
            pnlSouth.add(btnReset);
            pnlSouth.add(btnBack);
    
            
            //pnlSouth.setPreferredSize(new Dimension(100,100));
            Border bFire = BorderFactory.createTitledBorder("Fire Simulation");
            pnlSouth.setBackground(new Color(31, 36, 43));
            pnlSouth.setBorder(bFire);
        
        //======================================================================
        //      East Panel: 
        //======================================================================


        pnlEast = new JPanel(new BorderLayout());
        pnlEast.setPreferredSize(new Dimension(200,Terrain.getDimY()));
        pnlEast.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pnlEast.setBorder(BorderFactory.createRaisedBevelBorder());
        pnlEast.setLayout(new BorderLayout());

        fontExample = new JLabel(); 
        Font f = fontExample.getFont();
        fontExample.setFont(f.deriveFont(f.getStyle() | Font.BOLD));



        plantDescription = new JTextArea("  Common Name:\n  Latin Name:\n  Height:\n  Canopy Radius:");
        plantDescription.setOpaque(false);

        //Configurations
        config = new JLabel("Configurations:");
        config.setFont(f.deriveFont(f.getStyle() | Font.BOLD));



            //fSelection.setBackground(new Color(85,193,219));

        //SLIDER FOR WIND DIRECTION:
        wDirSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        wDirSlider.addChangeListener(this);
        pointerLbl = new JLabel("Wind Direction: 0 Degrees");

        spdSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        spdSlider.setMaximum(5);
        spdSlider.setMinimum(1);
        spdSlider.addChangeListener(this);
        lblSpeed = new JLabel("Simulation Speed: x1");

        search = new JTextField(20);
        search.setMaximumSize(search.getPreferredSize());
        lblSearch = new JLabel("Search: ");
            
        ChkUnderGrowth = new JCheckBox("Show Undergrowth",true);
        //ChkUnderGrowth.addItemListener(this);
        ChkUnderGrowth.setBounds(150,150,50,50);
        ChkCanopy = new JCheckBox("Show Canopy",true);
        //ChkCanopy.addItemListener(this);
        ChkCanopy.setBounds(150,150,50,50);



        ////********************************************************** */
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(200,490));

        //ImageIcon icon = createImageIcon("");

        //=================================================================
        //Details on Demand:
        //=================================================================
        JPanel pnlDetails = new JPanel();
        //pnlDetails.setLayout(new BoxLayout(pnlDetails, BoxLayout.PAGE_AXIS ));
        pnlDetails.setLayout(new GridLayout(0,1));
            //JLabel lblDetails = new JLabel("Details on Demand");
            //lblDetails.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            //Add components to Details on Demand Panel
            //pnlDetails.add(lblDetails);
            
            JLabel comTitle = new JLabel("Common Name:");
            comTitle.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            JLabel latTitle = new JLabel("Latin Name:");
            latTitle.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            //private variables
            shTitle = new JLabel("Canopy radius:");
            shTitle.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            taTitle = new JLabel("Height:");
            taTitle.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            avTitle = new JLabel("Radius-Height ratio:");
            avTitle.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            JLabel numTitle = new JLabel("No. of Plants:");
            numTitle.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

            lblCommon = new JLabel("\n");
            lblLatin = new JLabel("\n");
            lblAvg = new JLabel("\n");
            lblShort = new JLabel("\n");
            lblTall = new JLabel("\n");
            lblNum = new JLabel("\n");

            JPanel pnlName = new JPanel();
            pnlName.setBackground(new Color(31, 36, 43));
            pnlName.setLayout(new BoxLayout(pnlName, BoxLayout.PAGE_AXIS ));
            Border b1 = BorderFactory.createTitledBorder("Species");
            pnlName.add(comTitle);
            pnlName.add(lblCommon);
            pnlName.add(latTitle);
            pnlName.add(lblLatin);
            pnlName.setBorder(b1);
            

            JPanel pnlHeight = new JPanel();
            pnlHeight.setBackground(new Color(31, 36, 43));
            pnlHeight.setLayout(new BoxLayout(pnlHeight, BoxLayout.PAGE_AXIS ));
            Border b2 = BorderFactory.createTitledBorder("Measurements");  //private
            pnlHeight.add(shTitle);
            pnlHeight.add(lblShort);
            pnlHeight.add(taTitle);
            pnlHeight.add(lblTall);
            pnlHeight.setBorder(b2);

            JPanel pnlStats = new JPanel();
            pnlStats.setBackground(new Color(31, 36, 43));
            pnlStats.setLayout(new BoxLayout(pnlStats, BoxLayout.PAGE_AXIS ));
            Border b3 = BorderFactory.createTitledBorder("Stats"); //private
            pnlStats.add(avTitle);
            pnlStats.add(lblAvg);
            pnlStats.add(numTitle);
            pnlStats.add(lblNum);
            pnlStats.setBorder(b3);

            pnlDetails.add(pnlName);
            pnlDetails.add(pnlHeight);
            pnlDetails.add(pnlStats);

            speciesToggle = new JCheckBox("Full species details");
            speciesToggle.setEnabled(false);
            speciesToggle.setSelected(false);
            pnlDetails.add(speciesToggle);
            //pnlDetails.add(plantDescription);

        tabbedPane.addTab("Details",null,pnlDetails,"Shows Details on Demand");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        //Configurations:
        JPanel pnlConfig = new JPanel();
            JLabel lblConfig = new JLabel("Configurations");
            lblConfig.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            //Add components to Config Panel
            pnlConfig.add(lblConfig);
            pnlConfig.add(pointerLbl);
            pnlConfig.add(wDirSlider);
            pnlConfig.add(lblSpeed);
            pnlConfig.add(spdSlider);


        tabbedPane.addTab("Config",null,pnlConfig, "Change Simulation Settings");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        //Filters:
        pnlFilters = new JPanel();
            pnlFilters.setLayout(new BoxLayout(pnlFilters, BoxLayout.PAGE_AXIS ));
            JLabel lblFilters = new JLabel("Filters");
            lblFilters.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            //Add components to Filter Panel
            pnlFilters.add(lblFilters);
            pnlFilters.add(lblSearch);
            pnlFilters.add(search);
            pnlFilters.add(ChkUnderGrowth);
            pnlFilters.add(ChkCanopy);

        tabbedPane.addTab("Species",null,pnlFilters,"Filter by species");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);


        JPanel pnlFilter2 = new JPanel();
        pnlFilter2.setLayout(new GridLayout(5,1));
        JLabel lblFilter2 = new JLabel("Filter by Height/Canopy:");
        //Height filter
        JPanel heightFilters = new JPanel();
        heightFilters.setLayout(new GridLayout(0,2));
        heightFilters.setSize(new Dimension(200, 50));
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);

        JFormattedTextField tHiHeight = new JFormattedTextField(format);
        tHiHeight.setValue(0.00f);
        JLabel lblHiHeight = new JLabel("Max Height:");
        JFormattedTextField tLoHeight = new JFormattedTextField(format);
        tLoHeight.setValue(0.00f);
        JLabel lblLoHeight = new JLabel("Min Height:");

        //Canopy filter
        JPanel canopyFilters = new JPanel();
        canopyFilters.setLayout(new GridLayout(0,2));
        canopyFilters.setSize(new Dimension(200, 50));

        JFormattedTextField tHiRadius = new JFormattedTextField(format);
        tHiRadius.setValue(0.00f);
        JLabel lblHiRadius = new JLabel("Max Radius:");
        JFormattedTextField tLoRadius = new JFormattedTextField(format);
        tLoRadius.setValue(0.00f);
        JLabel lblLoRadius = new JLabel("Min Radius:");

        heightFilters.add(lblLoHeight);
        heightFilters.add(lblHiHeight);
        heightFilters.add(tLoHeight);
        heightFilters.add(tHiHeight);

        canopyFilters.add(lblLoRadius);
        canopyFilters.add(lblHiRadius);
        canopyFilters.add(tLoRadius);
        canopyFilters.add(tHiRadius);

        pnlFilter2.add(lblFilter2);
        pnlFilter2.add(heightFilters);
        pnlFilter2.add(canopyFilters);

        tabbedPane.addTab("Filter",null,pnlFilter2,"Visualisation filters");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        ////********************************************************** */
            
        //DRAW MINIMAP
        mini = new miniMap(mainPanel);
        mini.setPreferredSize(new Dimension(200,200));
        mini.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mini.setOpaque(false);

        

        pnlEast.add(tabbedPane,BorderLayout.NORTH);
        pnlEast.add(pnlSouth,BorderLayout.CENTER);
        pnlEast.add(mini,BorderLayout.SOUTH);
            


        //====================================================================== 
        //      MenuBar:
        //======================================================================
        JMenuBar mb = new JMenuBar();

        JMenu m1 = new JMenu("File");

            i1 = new JMenuItem("Load Files");
                //i1.addActionListener(this);
            i2 = new JMenuItem("Export as PNG");
                //i2.addActionListener(this);
            i3 = new JMenuItem("Exit");
                //i3.addActionListener(this);

                m1.add(i1);
                m1.add(i2);
                m1.add(i3);
    
        JMenu m2 = new JMenu("Help");
        JMenu m3 = new JMenu("Appearance");

        a1 = new JMenuItem("Dark Mode");
        a2 = new JMenuItem("Light Mode");
        a3 = new JMenuItem("Cosmo");
        a4 = new JMenuItem("Forest");
        m3.add(a1);
        //a1.addActionListener(this);

        m3.add(a2);
        //a2.addActionListener(this);

        m3.add(a3);
        //a3.addActionListener(this);

        m3.add(a4);
        //a4.addActionListener(this);



        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

    //====================================================================== 
    //      Adding all to the Frame (ECOVIZ):
    //======================================================================
        frame.setJMenuBar(mb);
        //frame.getContentPane().add(BorderLayout.SOUTH, pnlSouth);
        frame.getContentPane().add(BorderLayout.WEST, pnlWest);
        frame.getContentPane().add(BorderLayout.EAST, pnlEast);

        
        // Show
        frame.pack();
        frame.setLocationRelativeTo(null);
        //frame.setVisible(true);
        //loadIn.setVisible(true);

    }
    //END OF CONSTRUCTOR

    public JCheckBox addFilter(String name){
        JCheckBox chk = new JCheckBox(name, true);
        chk.setBounds(150,150,50,50);
        pnlFilters.add(chk);
        return chk;
    }

    public void clearFilters(){
        if(filterlist == null) return;
        for(JCheckBox chk: this.filterlist){
            pnlFilters.remove(chk);
        }
    }

    public void setSpeciesDetails(String s){
        this.plantDescription.setText(s);
    }
    //////
    public void setCommon(String c){
        common = c;
        lblCommon.setText(common);
    }
    public void setLatin(String l){
        latin = l;
        lblLatin.setText(latin);
    }
    public void setShortest(String s){
        shortest = s;
        lblShort.setText(shortest+"m");
    }
    public void setTallest(String t){
        tallest = t;
        lblTall.setText(tallest+"m");
    }
    public void setAvg(String a){
        avgRat=a;
        lblAvg.setText(avgRat);
    }
    public void setNumber(String n){
        totNum=n;
        lblNum.setText(totNum);
    }
    //////
    public void exportView(){
        JFrame popup = new JFrame();
        String name = JOptionPane.showInputDialog(popup, "Save As:");
        mainPanel.exportImage(name);
    }

    public void changeTheme(int theme){
        LookAndFeel obj = new FlatDarculaLaf();
        String message = "";
        switch(theme){
            
            case 0:
                message = "Dark Mode Enabled";
                break;
            case 1: 
                obj = new FlatLightLaf();
                message = "Light Mode Enabled";
                break;
            case 2: 
                //obj = new FlatDarculaLaf();
                message = "Dark Mode default";
                break;
            case 3: 
                //obj = new FlatDarculaLaf();
                message = "Dark Mode default";
                break;
        }
        try{
            UIManager.setLookAndFeel(obj);
            System.out.println(message);
            SwingUtilities.updateComponentTreeUI(frame);
        }catch (Exception e){
            System.out.println("Caught exception with laf library");
        }
    }

    public boolean showChooser(){
        JFrame fr = new JFrame();
        return fChooser.showOpenDialog(fr) == JFileChooser.APPROVE_OPTION;
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        pointerLbl.setText("Wind Direction: "+Integer.toString(wDirSlider.getValue())+" Degrees");
        lblSpeed.setText("Simulation Speed: x"+Integer.toString(spdSlider.getValue()));

        //delay = 75; // default
        switch(Integer.toString(spdSlider.getValue())){
            case "1":
                delay = 125;
                break;
            case "2":
                delay = 100;
                break;
            case "3":
                delay = 75;
                break;
            case "4":
                delay = 50;
                break;
            case "5":
                delay = 25;
                break;

        }
    }
}
