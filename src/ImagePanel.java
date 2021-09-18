/*
* File: imgPanel.java
* Author(s): BRNJAM019, FRNOWE001, VJRJAC003
* Status: In progress
*/
//References:
//Zoom functionality : credit to @Thanasis - StackOverflow for the algorithm

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics2D;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.AlphaComposite;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class ImagePanel extends JPanel{

	private int dimY, dimX;
	private BufferedImage terrain;
	private BufferedImage canopy;
	private BufferedImage undergrowth;

	private	double zoomMultiplier = 1;
	private double prevZoomMultiplier = 1;
	private boolean zoom;

	private boolean dragger,released;
	private double xOffset,yOffset = 0;
	private int xDiff,yDiff;
	private Point startPoint;

	private boolean showCanopy;
	private boolean showUnderGrowth;

	public ImagePanel(){
		/*this.gui = gui;
		imgcontroller = new imgController(this, gui);
		addMouseWheelListener(imgcontroller);
		addMouseListener(imgcontroller);
		addMouseMotionListener(imgcontroller);*/
		showCanopy=true;
		showUnderGrowth=true;
	}

	public int getStartX(){
		return this.startPoint.x;
	}

	public int getStartY(){
		return this.startPoint.y;
	}

	public double getZoomMult(){
		return this.zoomMultiplier;
	}

	public BufferedImage getTerrain(){
		return this.terrain;
	}

	public BufferedImage getCanopy(){
		return this.canopy;
	}

	public BufferedImage getUndergrowth(){
		return this.undergrowth;
	}

	public void setStartPoint(Point p){
		this.startPoint = p;
	}

	public void setXDiff(int x){
		this.xDiff = x;
	}

	public void setYDiff(int y){
		this.yDiff = y;
	}

	public void setDragger(boolean b){
		this.dragger = b;
	}

	public void setReleased(boolean b){
		this.released = b;
	}

	public void setZoom(boolean b){
		this.zoom = b;
	}

	public void setZoomMult(double multiplier){
		this.zoomMultiplier = multiplier;
	}



	//========================================================================
	//      Create the greyscale top-down view
	//========================================================================
	public void deriveImg(Terrain terrain){
		dimX = Terrain.getDimX();
		dimY = Terrain.getDimY();
		BufferedImage img = new BufferedImage(dimX,dimY,BufferedImage.TYPE_INT_ARGB);
		double maxh = -10000.0f;
		double minh = 10000.0f;
		double[][] elevations = terrain.getElevations();
		//determine the range of heights
		for (int y = 0; y <dimY;y++){
			for (int x = 0; x < dimX; x++){
				double h = elevations[y][x];
				if (h>maxh){maxh = h;}
				if (h<minh){minh = h;}  //Can be cut out and done in read loop - optimization
			}
		}

		//find normalized height value
		for (int y = 0; y <dimY;y++){
			for (int x = 0; x < dimX; x++){
				float val = (float) ((elevations[y][x]-minh)/(maxh-minh));
				Color col = new Color(val, val, val, 1.0f);
				img.setRGB(x, y, col.getRGB());
			}
		}
		this.terrain = img;
	}

	//========================================================================
    //      Create the colourful circles
    //========================================================================
    public void deriveImg(PlantLayer layer, boolean canopy){
		int dimx = Terrain.getDimX();
		int dimy = Terrain.getDimY();
  
		BufferedImage img = new BufferedImage(dimx,dimy,BufferedImage.TYPE_INT_ARGB);
		Graphics2D imgGraphics = img.createGraphics();
  
		imgGraphics.setComposite(AlphaComposite.Clear);
		imgGraphics.fillRect(0, 0, dimx, dimy);
  
		imgGraphics.setComposite(AlphaComposite.Src);
		Species[] specieslist = layer.getSpeciesList();
		int[] colourlist = Species.getCOLOURS();
		for (Species s: specieslist){
		  //Random r = new Random();
		  //imgGraphics.setColor(new Color(r.nextFloat(), r.nextFloat(), r.nextFloat()));
		  imgGraphics.setColor(new Color(colourlist[s.getSpeciesID()], true));
		  for(Plant p: s.getPlants()){
			imgGraphics.fillOval(p.getX(),p.getY(),(int)p.getCanopy()*2,(int)p.getCanopy()*2);
		  }
		}
		if(canopy){
			this.canopy = img;	
		} else {
			this.undergrowth = img;
		}      
	}
	//========================================================================
    //      Overide Paint Component of the Panel:
    //========================================================================
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;
		if (terrain != null) {

		if (zoom) {
			AffineTransform affine = new AffineTransform();

			double xRelative = MouseInfo.getPointerInfo().getLocation().getX()-getLocationOnScreen().getX();
			double yRelative = MouseInfo.getPointerInfo().getLocation().getY()-getLocationOnScreen().getY();

			double divident = zoomMultiplier/prevZoomMultiplier;

			xOffset = divident * xOffset + (1-divident)*xRelative;
			yOffset = divident * yOffset + (1-divident)*yRelative;

			affine.translate(xOffset,yOffset);
			affine.scale(zoomMultiplier, zoomMultiplier);
			prevZoomMultiplier = zoomMultiplier;
			graphics2d.transform(affine);
			zoom = false;
		} 
		
		if (dragger){
			AffineTransform affine = new AffineTransform();
			affine.translate(xOffset+xDiff,yOffset+yDiff);
			affine.scale(zoomMultiplier,zoomMultiplier);
			graphics2d.transform(affine);

			if (released){
				xOffset += xDiff;
				yOffset += yDiff;
				dragger = false;
			}

		}
		
		graphics2d.drawImage(terrain, 0, 0, null);
		if (showUnderGrowth){graphics2d.drawImage(undergrowth, 0, 0, null);}
		if (showCanopy){graphics2d.drawImage(canopy, 0, 0, null);}

		}
	}

	public void exportImage(String nm){
		BufferedImage img = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		paintAll(g);
		try{
			ImageIO.write(img,"png", new File("exports/"+nm+".png"));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setShowCanopy(boolean b){
		showCanopy=b;
		repaint();
	}
	public void setSHowUnderGrowth(boolean b){
		showUnderGrowth=b;
		repaint();
	}

}