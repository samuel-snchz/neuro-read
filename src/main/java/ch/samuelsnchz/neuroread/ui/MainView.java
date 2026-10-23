package ch.samuelsnchz.neuroread.ui;

import ch.samuelsnchz.neuroread.recognition.CharacterRecognizer;
import ch.samuelsnchz.neuroread.recognition.GrayscaleImage;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

public class MainView {
	// Multiple of 28: each cell of the future 28x28 grid will be exactly 12 px
	private static final double CANVAS_SIZE = 336;

	private final GridPane root;

	private final Canvas mainCanvas;
	private final TextArea outputTextArea;
	private final Button resetButton;
	private final Button enterButton;

	private final GraphicsContext gc;

	public MainView() {
		this.root = new GridPane();

		/*-------------------------- Static View --------------------------*/

		//GridPane rules 50:50 right/left and button row
		ColumnConstraints leftColumn = new ColumnConstraints();
		ColumnConstraints rightColumn = new ColumnConstraints();

		leftColumn.setPercentWidth(50);
		rightColumn.setPercentWidth(50);
		root.getColumnConstraints().addAll(leftColumn, rightColumn);

		RowConstraints row = new RowConstraints();
		row.setVgrow(Priority.ALWAYS);
		root.getRowConstraints().add(row);

		//Creating Canvas
		this.mainCanvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
		GridPane.setHalignment(mainCanvas, HPos.CENTER);
		GridPane.setValignment(mainCanvas, VPos.CENTER);


		//Creating Label for output textArea
		final Label outputLabel = new Label("output");
		outputLabel.setMaxWidth(Double.MAX_VALUE);
		outputLabel.setAlignment(Pos.CENTER);

		//Creating textArea for output text
		this.outputTextArea = new TextArea();
		outputTextArea.setEditable(false);
		outputTextArea.setFont(Font.font("Monospaced", 16));
		VBox.setVgrow(outputTextArea, Priority.ALWAYS);

		//Creating enter and reset buttons
		this.resetButton = new Button("reset");
		this.enterButton = new Button("enter");
		final ToolBar toolBar = new ToolBar(enterButton, resetButton);

		final VBox rightPanel = new VBox(10);
		rightPanel.setPadding(new Insets(10));
		rightPanel.getChildren().addAll(outputLabel, outputTextArea, toolBar);

		root.add(mainCanvas, 0, 0);
		root.add(rightPanel, 1, 0);

		/*-------------------------- Drawing --------------------------*/

		//Initialization of graphic context
		this.gc = mainCanvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

		installBrush();
		installEnterButton();
		installResetButton();
		root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			switch (event.getCode()) {
				case ENTER -> { submitDrawing(); event.consume(); }
				case BACK_SPACE, DELETE -> { clearCanvas(); event.consume(); }
			}
		});
	}

	public Parent getRoot() {
		return root;
	}

	private GrayscaleImage captureImage() {
		int cmpd_pxl_size = 12;
		int output_size = CharacterRecognizer.INPUT_SIZE;
		int total_output_size = CharacterRecognizer.TOTAL_INPUT_SIZE;

		GrayscaleImage.Builder grayscaleImageBuilder = new GrayscaleImage.Builder(output_size, output_size);

		WritableImage snapshot = mainCanvas.snapshot(null, null);
		PixelReader reader = snapshot.getPixelReader();

		for (int i = 0; i < output_size; ++i ){
			for (int j = 0; j < output_size; ++j ) {
				double sum = 0;

				//TODO: put 12 somewhere else, in a final and static variable
				for (int x = 0; x < cmpd_pxl_size; ++x) {
					for (int y = 0; y < cmpd_pxl_size; ++y) {
						Color color = reader.getColor(
								i * cmpd_pxl_size + x,
								j * cmpd_pxl_size + y
						);

						sum += (1 - color.getBrightness());
					}
				}

				double average_brightness = sum / (cmpd_pxl_size * cmpd_pxl_size);
				grayscaleImageBuilder.setPixel(i, j, average_brightness);
			}
		}
		return grayscaleImageBuilder.build();
	}

	private  void submitDrawing() {
		GrayscaleImage image = captureImage();

		/* ... */

		clearCanvas();
	}

	private void clearCanvas() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
	}

	private void installResetButton() {
		resetButton.setOnAction(event -> clearCanvas());
	}

	private void installEnterButton() {
		enterButton.setOnAction(event -> submitDrawing());
	}

	private void installBrush() {
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(18);
		gc.setLineCap(StrokeLineCap.ROUND);

		double[] pos = new double[2];
		double[] pos_old = new double[2];

		mainCanvas.setOnMousePressed(event -> {
			pos[0] = event.getX();
			pos[1] = event.getY();

			gc.strokeLine(
					pos[0],
					pos[1],
					pos[0],
					pos[1]
			);
		});

		mainCanvas.setOnMouseDragged(event -> {
			pos_old[0] = pos[0];
			pos_old[1] = pos[1];
			pos[0] = event.getX();
			pos[1] = event.getY();

			gc.strokeLine(
					pos_old[0],
					pos_old[1],
					event.getX(),
					event.getY()
			);

		});

	}


}