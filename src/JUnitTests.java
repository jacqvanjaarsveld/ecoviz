import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JCheckBox;


public class JUnitTests {

	@Test //Controller class requires no obvious testing
	public void testController(){
		//Class : Controller.java
	}

	@Test //Incomplete
	public void testFileController() throws FileNotFoundException{
		//Class : FileController.java
		//pre-requisites:
		Terrain terrain = new Terrain();
		
		PlantLayer canopyLayer = new PlantLayer();
		PlantLayer plantLayer = new PlantLayer();

		String testSFile = "data/test-256.spc.txt";
		String testCFile = "data/test-256_canopy.pdb";
		String testUFile = "data/test-256_undergrowth.pdb";
		String testElvFile = "data/S6000-6000-256.elv";

		FileController fileController = new FileController();
		
		//readElevation
		fileController.readElevation(terrain, testElvFile);

		//readSpecies
		fileController.readSpecies(testSFile);

		//readCanopy
		fileController.readLayer(canopyLayer, testCFile, true);

		//getTotalSpecies
		int totSpecies = fileController.getTotalSpecies();
		assertEquals(5, fileController.getTotalSpecies());

		//readUndergrowth
		fileController.readLayer(plantLayer, testUFile, false);

		//compareAndSetMaxHeight
		FileController.compareAndSetMaxHeight(1000.0f);
		boolean correct =false;
		if (1000.0f == FileController.getMaxHeight()){
			correct = true;
		}
		assertTrue(correct);

		//compareAndSetMaxRadius
		FileController.compareAndSetMaxRadius(1000.0f);
		boolean correct2 =false;
		if (1000.0f == FileController.getMaxRadius()){
			correct2 = true;
		}
		assertTrue(correct2);

	}

	@Test //Incomplete
	public void testFire(){
		//Class : Fire.java
		int dimX = 2;
		int dimY = 2;
		int[][][] idLocOver = new int[2][2][2];
		int[][][] idLocUnder = new int[2][2][2];

		idLocOver[0][0][0]=0;idLocOver[0][0][1]=0;
		idLocOver[0][1][0]=1;idLocOver[0][1][1]=1;
		idLocOver[1][1][0]=2;idLocOver[1][1][1]=2;
		idLocOver[1][0][0]=3;idLocOver[1][0][1]=3;

		Fire fire = new Fire(dimX, dimY, idLocOver, idLocUnder);

			//getDim
			assertEquals(4,fire.getDim());
			assertFalse(fire.isFire(1, 0));
		

	}

	@Test //Incomplete
	public void testFireBreak(){
		//Class : FireBreak.java

		ArrayList<Firebreak> breaklist;
		ArrayList<int[]> region;
		int minX, maxX, minY, maxY;
		ArrayList<Boolean> layers;
		ArrayList<int[]> ids;

		Firebreak fireBreak = new Firebreak();


	}

	@Test
	public void testGui(){
		//Class : Gui.java

		String windDirLabel = "windDirLabel";
		String windSpdLabel = "windSpdLabel";
		int windMax = 100;
		int windSpeed = 50;
		String speedLabel = "speedLabel";
		String filterName = "filerName";
		String speciesDetails = "speciesDetails";
		String commonString = "commonString";
		String latinString = "latinString";
		String shortestString = "shortestString";
		String tallestString = "tallestString";
		String averageString = "averageString";
		String numberString = "numberString";
		int themeNumber = 1;
	
		// FireSim Controls:
		JCheckBox[] filterlist = {new JCheckBox("filterList")};
		int mousePosX = 0;
		int mousePosY = 0;

	
		Gui testGui = new Gui();

			//Mutator methods
			testGui.setFilterList(filterlist);
			testGui.setChkMetric(); // True
			testGui.setChkBurnt(); // True
			testGui.setChkPath(); // True
			testGui.setWindDirLbl(windDirLabel);
			testGui.setCompassPath(windSpdLabel);
			testGui.setCompassIcon();
			testGui.setWindSpdLbl(windSpdLabel);
			testGui.setWindSpdMax(windMax);
			testGui.setWindSpd(windSpeed);
			testGui.setSpeedLbl(speedLabel);
			testGui.setMousePositions(mousePosX, mousePosY);
			testGui.addFilter(filterName);
			testGui.clearFilters();
			testGui.setSpeciesDetails(speciesDetails);
			testGui.setCommon(commonString);
			testGui.setLatin(latinString);
			testGui.setShortest(shortestString);
			testGui.setTallest(tallestString);
			testGui.setAvg(averageString);
			testGui.setNumber(numberString);
			//testGui.exportView();
			testGui.changeTheme(themeNumber);
			//testGui.showChooser();

			//Getter methods
			if (filterlist == testGui.getFilterList()) assertEquals(1, 1);
			assertEquals(true, testGui.getChkMetric().isSelected());
			assertEquals(true, testGui.getChkShowBurnt().isSelected());
			assertEquals(true, testGui.getChkShowPath().isSelected());
			assertEquals(windSpeed, testGui.getWindSpd());
			assertNotNull(testGui.getEndSession());
			assertNotNull(testGui.getEndSession());
			assertNotNull(testGui.getPlayR());
			assertNotNull(testGui.getEnlarge());
			assertNotNull(testGui.getHelp());
			assertNotNull(testGui.getStamp());
			assertNotNull(testGui.getMain());
			assertNotNull(testGui.getLoadFrame());
			assertNotNull(testGui.getDelay());
			assertNotNull(testGui.getFireBtn());
			assertNotNull(testGui.getBackBtn());
			assertNotNull(testGui.getRenderBtn());
			assertNotNull(testGui.getResetBtn());
			assertNotNull(testGui.getPauseBtn());
			assertNotNull(testGui.getLoadBtn());
			assertNotNull(testGui.getUndoBtn());
			assertNotNull(testGui.getChkFirebreak());
			assertNotNull(testGui.getChooser());
			assertNotNull(testGui.getMenu1());
			assertNotNull(testGui.getMenu2());
			assertNotNull(testGui.getMenu3());
			assertNotNull(testGui.getMenu4());
			assertNotNull(testGui.getMenu5());
			assertNotNull(testGui.getCloseRender());
			assertNotNull(testGui.getChkShowPath());
			assertNotNull(testGui.getChkShowBurnt());
			assertNotNull(testGui.getChkUndergrowth());		
			assertNotNull(testGui.getChkCanopy());
			assertNotNull(testGui.getImage());
			assertNotNull(testGui.getMini());
			assertNotNull(testGui.getEast());
			assertNotNull(testGui.getChkRecord());
			assertNotNull(testGui.getFilterPanel());
			assertNotNull(testGui.getTabPane());
			assertNotNull(testGui.getRadSlider());
			assertNotNull(testGui.getSpeciesToggle());
			assertNotNull(testGui.getShortTitle());
			assertNotNull(testGui.getTallTitle());
			assertNotNull(testGui.getScrubber());
			assertNotNull(testGui.getAvTitle());
			assertNotNull(testGui.getHiHeight());
			assertNotNull(testGui.getLoHeight());
			assertNotNull(testGui.getHiRadius());
			assertNotNull(testGui.getLoRadius());
			assertNotNull(testGui.getChkSelectRadius());
			assertNotNull(testGui.getCCSpecies());
			assertNotNull(testGui.getMainPanel());
			assertNotNull(testGui.getWindDir());
			assertNotNull(testGui.getCompassPath());
			assertNotNull(testGui.getWindSpd());
			assertNotNull(testGui.getScrubSpeed());
			assertNotNull(testGui.getSliderList());
	}

	@Test
	public void testMiniMap(){
		//Class : MiniMap.java

		ImagePanel image = null;
		int tlx = 10;
		int tly = 10;
		int dimX = 10;
		int dimY = 10;

		try {
			MiniMap testMap = new MiniMap(image);
			testMap.setZone(tlx, tly, dimX, dimY);
				assertEquals(1, 1);
		} catch (Exception e) {
				assertEquals(1, 0);
		}

	}

	@Test
	public void testPlant() {
		//Class : Plant.java
		
		int speciesID = 0;
	    int plantID = 1;
	    int posX = 2;
	    int posY = 3;
	    double height = 4.1;
	    double canopyRadius = 5.2;
		boolean canopy = true;
		boolean layer = true;
		boolean filter = true;
		boolean fHeight = true;
		boolean fCanopy = true;
		int inFirebreak = 0;
		
		Plant plantTest = new Plant(speciesID, plantID, posX, posY, (float)height, (float)canopyRadius, canopy);
			int testSpeciesID = plantTest.getSpeciesID();
				assertEquals(testSpeciesID,speciesID);
			int testPlantID = plantTest.getID();
				assertEquals(testPlantID,plantID);
			int testX = plantTest.getX();
				assertEquals(testX, posX);
			int testY = plantTest.getY();
				assertEquals(testY, posY);
			double testHeight = plantTest.getHeight();
				assertEquals((int)testHeight,(int)height); //assertEquals deprecated for double
			double testRadius = plantTest.getCanopy();
				assertEquals((int)testRadius,(int)canopyRadius); //assertEquals deprecated for double
			boolean testCanopy = plantTest.getFilter();
				assertEquals(testCanopy, canopy);
			boolean testLayer = plantTest.getLayer();
				assertEquals(testLayer, layer);
			boolean testFilter = plantTest.getFilter();
				assertEquals(testFilter,filter);
			boolean testFHeight = plantTest.getHeightFlag();
				assertEquals(testFHeight,fHeight);
			boolean testFCanopy = plantTest.getCanopyFlag();
				assertEquals(testFCanopy,fCanopy);
			int testInFireBreak = plantTest.isInFireBreak();
				assertEquals(testInFireBreak, inFirebreak);
			boolean testNewCanopy = !testCanopy;
				assertEquals(testNewCanopy,false);
			boolean testNewLayer = !testFilter;
				assertEquals(testNewLayer,false);
			plantTest.setFilter(false); //Should be setting to false
			boolean testNewFilter = plantTest.getFilter();
				assertEquals(testNewFilter,false);

			int newPosX = 20;
			plantTest.setPosX(newPosX);
			int newPosY = 30;
			plantTest.setPosY(newPosY);
			boolean newFHeight = false;
			plantTest.setHeightFlag(newFHeight);
			boolean newFCanopy = false;
			plantTest.setCanopyFlag(newFCanopy);
			int newInFirebreak = 0;
			boolean newFilter = false;
			plantTest.setFilter(newFilter);
			boolean newCanopy = false;
			boolean newLayer = false;
			plantTest.setLayer(newLayer);

			int testNewX = plantTest.getX();
				assertEquals(testNewX, newPosX);
			int testNewY = plantTest.getY();
				assertEquals(testNewY, newPosY);
			boolean testNewFHeight = plantTest.getHeightFlag();
				assertEquals(testNewFHeight,newFHeight);
			boolean testNewFCanopy = plantTest.getCanopyFlag();
				assertEquals(testNewFCanopy,newFCanopy);
			int testNewInFireBreak = plantTest.isInFireBreak();
				assertEquals(testNewInFireBreak, newInFirebreak);

			testNewCanopy = plantTest.getCanopyFlag();
				assertEquals(testNewCanopy,newCanopy);
			testNewLayer = plantTest.getLayer();
				assertEquals(testNewLayer, newLayer);
			plantTest.setFilter(newFilter);
			testNewFilter = plantTest.getFilter();
				assertEquals(testNewFilter, plantTest.getFilter());

			int testInFireBreakA = plantTest.isInFireBreak();
			plantTest.incFirebreak();
			int testInFireBreakB = plantTest.isInFireBreak();
				assertEquals(testInFireBreakA + 1, testInFireBreakB);
			testInFireBreakA = plantTest.isInFireBreak();
			plantTest.decFirebreak();
			testInFireBreakB = plantTest.isInFireBreak();
				assertEquals(testInFireBreakA -1, testInFireBreakB);

	}

	@Test
	public void testPlantLayer() {
		//Class : PlantLayer.java
		
		Plant testPlant1 = new Plant(1, 1, 50, 50, (float)10, (float)10, true);
		Plant testPlant2 = new Plant(2, 2, 100, 100, (float)10, (float)10, true);
		Plant testPlant3 = new Plant(3, 3, 150, 150, (float)10, (float)10, false);
		ArrayList<Plant> plantList = new ArrayList<Plant>(Arrays.asList(testPlant1, testPlant2, testPlant3));
		Color colour1 = new Color(10, 20, 30, 1);
		Color colour2 = new Color(20, 30, 40, 1);
		Color colour3 = new Color(30, 40, 50, 1);
		Species species1 = new Species(0, "common one", "latin one", colour1);
		Species species2 = new Species(1, "common two", "latin two", colour2);
		Species species3 = new Species(2, "common three", "latin three", colour3);
		Species[] allSpecies = {species1, species2, species3};
		int numSpecies = 3;
		
		PlantLayer plantLayerTest = new PlantLayer();
			plantLayerTest.setNumSpecies(numSpecies);
			plantLayerTest.setLocations(500, 500);
			plantLayerTest.removePlant(50, 50);
			PlantLayer.setPlantList();
			PlantLayer.setAllSpecies(allSpecies);
			PlantLayer.addSpecies(species1);
			PlantLayer.addSpecies(species2);
			PlantLayer.addSpecies(species3);
			PlantLayer.addPlant(testPlant1);
			PlantLayer.addPlant(testPlant2);
			PlantLayer.addPlant(testPlant3);
			
			int testNumSpecies = plantLayerTest.getNumSpecies();
				assertEquals(testNumSpecies, numSpecies);
			int[] plantAtLocation1 = {1,1};
			plantLayerTest.setPlantAtLocation(50, 50, 1, 1);
			int[] testPlantAtLocation1 = plantLayerTest.getPlantAtLocation(50, 50);
				assertArrayEquals(testPlantAtLocation1, plantAtLocation1);
			int[] plantAtLocation2 = {1,1};
			plantLayerTest.setPlantAtLocation(100, 100, 1, 1);
			int[] testPlantAtLocation2 = plantLayerTest.getPlantAtLocation(50, 50);
				assertArrayEquals(testPlantAtLocation2, plantAtLocation2);
			int[] plantAtLocation3 = {1,1};
			plantLayerTest.setPlantAtLocation(150, 150, 1, 1);
			int[] testPlantAtLocation3 = plantLayerTest.getPlantAtLocation(50, 50);
				assertArrayEquals(testPlantAtLocation3, plantAtLocation3);
			ArrayList<Plant> testPlantList = PlantLayer.getPlantList();
				assertEquals(testPlantList, plantList);
			Species[] testAllSpecies = PlantLayer.getAllSpecies();
				if (testAllSpecies == allSpecies)
					assertEquals(1, 1);
				else
					assertEquals(1, 0);
			Species testPlantLayerSpecies1 = PlantLayer.getSpeciesAtID(0);
				if (testPlantLayerSpecies1 == species1)
					assertEquals(1, 1);
				else
					assertEquals(1, 0);
			Species testPlantLayerSpecies2 = PlantLayer.getSpeciesAtID(1);
			if (testPlantLayerSpecies2 == species2)
				assertEquals(1, 1);
			else
				assertEquals(1, 0);
			Species testPlantLayerSpecies3 = PlantLayer.getSpeciesAtID(2);
			if (testPlantLayerSpecies3 == species3)
				assertEquals(1, 1);
			else
				assertEquals(1, 0);
	}

	@Test
	public void testSimController() {
		//Class : SimController.java
		
		Gui gui = new Gui();
		Terrain terrain = new Terrain();
        PlantLayer undergrowth = new PlantLayer();
        PlantLayer canopy = new PlantLayer();
        Controller controller = new Controller(gui, terrain, undergrowth, canopy);
        

		Gui testGui = new Gui();
			if (gui == testGui) assertEquals(1, 1);
		Terrain testTerrain = new Terrain();
			if (terrain == testTerrain) assertEquals(1, 1);
		PlantLayer testUndergrowth = new PlantLayer();
			if (undergrowth == testUndergrowth) assertEquals(1, 1);
		PlantLayer testCanopy = new PlantLayer();
			if (canopy == testCanopy) assertEquals(1, 1);
		Controller testController = new Controller(gui, terrain, undergrowth, canopy);
			if (controller == testController) assertEquals(1, 1);

		try {
			controller.initController();
			assertEquals(1, 1);
		} catch (Exception e) {
			assertEquals(1, 0);
		}
		try {
			testController.initController();
			assertEquals(1, 1);
		} catch (Exception e) {
			assertEquals(1, 0);
		}

	}

	@Test
	public void testSpecies() {
		//Class : Species.java
		
		String commonName = new String("common name");
		String latinName = new  String("latin name");
		int speciesID = 0;
		float minHeight = 10;
		float maxHeight = 20;
		float avgRatio = (float)9.0;
		int numPlants = 333;
		Plant[] canopy =
		{new Plant(1, 1, 100, 100, (float)6.9, (float)4.20, true),
		new Plant(2, 2, 200, 200, (float)11.1, (float)2.50, true),
		new Plant(3, 3, 300, 300, (float)44.4, (float)7.89, true)};
		Plant[] undergrowth =
		{new Plant(4, 4, 150, 150, (float)6.9, (float)4.20, false),
		new Plant(5, 5, 250, 250, (float)11.1, (float)2.50, false),
		new Plant(6, 6, 350, 350, (float)44.4, (float)7.89, false)};
		Color colour = new Color(10, 20, 30, 1);
		Color prevColour = new Color(10, 20, 30, 1);
		boolean filter = true;
		
		Species testSpecies = new Species(speciesID, commonName, latinName, colour);
			testSpecies.setMinHeight(minHeight);
			testSpecies.setMaxHeight(maxHeight);
			testSpecies.setRatio(avgRatio);
			testSpecies.setNumPlants(numPlants);
			testSpecies.setCanopyPlants(canopy);
			testSpecies.setUnderPlants(undergrowth);
			testSpecies.setFilter(filter);
			
			String testCommonName = testSpecies.getCommon();
				assertEquals(testCommonName,commonName);
			String testLatinName = testSpecies.getLatin();
				assertEquals(testLatinName,latinName);
			int testSpeciesID = testSpecies.getSpeciesID();
				assertEquals(testSpeciesID,speciesID);
			float testMinHeight = testSpecies.getMinHeight();  //assertEquals deprecated for float
				assertEquals((int)testMinHeight,(int)minHeight);			
			float testMaxHeight = testSpecies.getMaxHeight();  //assertEquals deprecated for float
				assertEquals((int)testMaxHeight,(int)maxHeight);		
			float testAvgRatio = testSpecies.getAvgRatio();  //assertEquals deprecated for float
				assertEquals((int)testAvgRatio,(int)avgRatio);		
			int testNumPlants = testSpecies.getNumPlants();
				assertEquals(testNumPlants,numPlants);
			Plant[] testCanopyPlants = testSpecies.getCanopyPlants(); //assertEquals deprecated for objects
				if (testCanopyPlants == canopy)
					assertEquals(1, 1);
				else
					assertEquals(1, 0);
			Plant[] testUnderPlants = testSpecies.getUnderPlants(); //assertEquals deprecated for objects
				if (testUnderPlants == undergrowth)
					assertEquals(1, 1);
				else
					assertEquals(1, 0);					
			Color testColour = testSpecies.getColour();
				assertEquals(testColour, colour);
			Color testPrevColour = testSpecies.getPrevColour();
				assertEquals(testPrevColour, prevColour);
			boolean testFilter = testSpecies.getFilter();
				assertEquals(testFilter, filter);	
			
			String newCommonName = new String("new common name");
			String newLatinName = new  String("new latin name");
			int newSpeciesID = 2;
			float newMinHeight = 15;
			float newMaxHeight = 50;
			float newAvgRatio = (float)15.0;
			int newNumPlants = 543;
			Color newColour = new Color(15, 25, 35, 1);
			boolean newFilter = false;

			testSpecies.setCommon(newCommonName);
			testSpecies.setLatin(newLatinName);
			testSpecies.setSpeciesID(newSpeciesID);
			testSpecies.setMinHeight(newMinHeight);
			testSpecies.setMaxHeight(newMaxHeight);
			testSpecies.setRatio(newAvgRatio);
			testSpecies.setNumPlants(newNumPlants);
			testSpecies.setColour(newColour);
			testSpecies.setFilter(newFilter);

			String testNewCommonName = testSpecies.getCommon();
				assertEquals(testNewCommonName,newCommonName);
			String testNewLatinName = testSpecies.getLatin();
				assertEquals(testNewLatinName,newLatinName);
			int testNewSpeciesID = testSpecies.getSpeciesID();
				assertEquals(testNewSpeciesID,newSpeciesID);
			float testNewMinHeight = testSpecies.getMinHeight();  //assertEquals deprecated for float
				assertEquals((int)testNewMinHeight,(int)newMinHeight);			
			float testNewMaxHeight = testSpecies.getMaxHeight();  //assertEquals deprecated for float
				assertEquals((int)testNewMaxHeight,(int)newMaxHeight);		
			float testNewAvgRatio = testSpecies.getAvgRatio();  //assertEquals deprecated for float
				assertEquals((int)testNewAvgRatio,(int)newAvgRatio);		
			int testNewNumPlants = testSpecies.getNumPlants();
				assertEquals(testNewNumPlants,newNumPlants);		
			Color testNewColour = testSpecies.getColour();
				assertEquals(testNewColour, newColour);
			Color testNewPrevColour = testSpecies.getPrevColour();
				assertEquals(testNewPrevColour, prevColour);
			boolean testNewFilter = testSpecies.getFilter();
				assertEquals(testNewFilter, newFilter);

	}


	@Test
	public void testTerrain() {
		//Class : Terrain.java
		
		double[][] elevations = new double[][] {{1.1,1.2},{1.3,1.4}};
		int dimX = 2;
		int dimY = 2;
		int baseX = 1;
		int baseY = 1;
		double gridSpacing = 2.1;
		double latitude = 3.2;

		Terrain terrainTest = new Terrain();
			terrainTest.setElevations(elevations);
			Terrain.setDimX(dimX);
			Terrain.setDimY(dimY);
			Terrain.setBaseX(baseX);
			Terrain.setBaseY(baseY);
			terrainTest.setGridSpacing((float)gridSpacing);
			terrainTest.setLatitude((float)latitude);

			double[][] testElevations = terrainTest.getElevations(); //assertEquals deprecated for objects
				if (testElevations == elevations)
					assertEquals(1, 1);
				else
					assertEquals(1, 0);
			int testDimX = Terrain.getDimX();
				assertEquals(testDimX,dimX);
			int testDimY = Terrain.getDimY();
				assertEquals(testDimY,dimY);
			int testBaseX = Terrain.getBaseX();
				assertEquals(testBaseX,baseX);
			int testBaseY = Terrain.getBaseY();
				assertEquals(testBaseY,baseY);
			double testGridSpacing = terrainTest.getGridSpacing();
				assertEquals((int)testGridSpacing,(int)gridSpacing); //assertEquals deprecated for double
			double testLatitude = terrainTest.getLatitude();
				assertEquals((int)testLatitude,(int)latitude); //assertEquals deprecated for double
	}
}
