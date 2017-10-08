package application;
	
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Main extends Application {
	
	// dynamic fields
	@FXML private Canvas mainCanvas;
	@FXML private Button startButton;
	@FXML private Button stopButton;
	@FXML private Text initDistanceText;
	@FXML private Text bestDistanceText;
	@FXML private Text numGenerationsText;
	
	// graph stuff
	@FXML private LineChart<Number,Number> fitnessGraph;
	XYChart.Series<Number,Number> fitnessSeries;
	
	// input params
	@FXML private TextField numCities;
	@FXML private TextField popSize;
	@FXML private TextField tournamentSize;
	@FXML private TextField mutRate;
	@FXML private TextField maxGens;
	
	// GA stuff
	Population pop;
	Tour fittestTour; 
	GraphicsContext gc; // this is needed to draw path on the mainCanvas
	Timeline timeline;  // controls execution rate of GA
	int genCount;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	@FXML
	protected void onStartClick()
	{
		resetScene();
        createCities();
        initializePopulation();
        setParams();
        initializeFitnessGraph();
        startGA();
	}

	@FXML
	protected void onStopClick()
	{
		timeline.stop();
	}
	
	public void createCities() 
	{
		int cityCount = Integer.parseInt(numCities.getText());
		for (int i = 0; i < cityCount; i++)
		{
			TourManager.addCity(new City());
		}
	}
	
	public void drawCities()
	{	
		gc.setFill(Color.BLACK);
		for (int i = 0; i < TourManager.numberOfCities(); i++)
		{
			City c = TourManager.getCity(i);
			gc.fillOval(c.getX(), c.getY(), 5, 5);
		}
	}
	
	public void initializePopulation()
	{
		int pSize = Integer.parseInt(popSize.getText());
		pop = new Population(pSize, true);
		fittestTour = pop.getFittest();
        drawFittestTour();
        initDistanceText.setText("Init Distance: "+fittestTour.getDistance());
	}
	
	public void drawFittestTour()
	{
		Tour currentPopFittest = pop.getFittest();
		
		// if new pop has higher fitness than current best
		if (currentPopFittest.getFitness() > fittestTour.getFitness())
		{
			// that's our new best tour
	        fittestTour = currentPopFittest;
			
	        // now draw it
		    gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
	        drawCities();
	        
			for (int i = 0; i < fittestTour.tourSize()-1; i++)
			{
				City c1 = fittestTour.getCity(i);
				City c2 = fittestTour.getCity(i+1);
				
				gc.strokeLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
			}
			
			// connect last city to first
			City first = fittestTour.getCity(0);
			City last = fittestTour.getCity(fittestTour.tourSize()-1);
			gc.strokeLine(last.getX(), last.getY(), first.getX(), first.getY());
			
			// color first city green & last city red (to see where to start/end maybe?)
			gc.setFill(Color.GREEN);
			gc.fillOval(first.getX(), first.getY(), 5, 5);
			
			gc.setFill(Color.RED);
			gc.fillOval(last.getX(), last.getY(), 5, 5);
			
			bestDistanceText.setText("Current distance: "+ fittestTour.getDistance());
		}
		
	}
	
	public void startGA()
	{
		/*
		 * GA loop is done in a timeline which allows adjustable execution/evolution rate
		 */
		timeline = new Timeline(
	            new KeyFrame(
	                Duration.seconds(.01),
	                event -> {
	                	// do evolution and drawing iteration here
	                	pop = GA.evolvePopulation(pop);
	                	drawFittestTour();
	                	numGenerationsText.setText("Generation: "+ ++genCount);
	                	updateFitnessGraph();
	                } 
	            )
	        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();	
	}
	
	public void setParams()
	{
		GA.setTournamentSize(Integer.parseInt(tournamentSize.getText()));
		GA.setMutationRate(Double.parseDouble(mutRate.getText()));
	}
	
	public void initializeFitnessGraph()
	{
		// couldnt find XFML properties to do this in the fxml file so doing it here
		fitnessSeries = new XYChart.Series<Number,Number>();
        fitnessGraph.getData().add(fitnessSeries);
        fitnessGraph.setCreateSymbols(false);
	}
	
	public void updateFitnessGraph()
	{
        //populating the series with data
        fitnessSeries.getData().add(new XYChart.Data<Number,Number>(genCount, pop.averageFitness()));
	}
	
	public void resetScene()
	{
		if (timeline != null)
			timeline.stop();
		
		// reset (clear board, TourManager, Params, graphs, and info tab)
        gc = mainCanvas.getGraphicsContext2D();
	    gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
	    TourManager.clearTour();
	    initDistanceText.setText("Init Distance: ");
	    bestDistanceText.setText("bestDistance: ");
	    numGenerationsText.setText("Generation: ");
	    genCount = 0;
	    fitnessGraph.getData().removeAll(fitnessSeries);
	}
}
