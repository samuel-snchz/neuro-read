package ch.samuelsnchz.neuroread;

import ch.samuelsnchz.neuroread.recognition.CharacterRecognizer;
import ch.samuelsnchz.neuroread.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		System.out.println("Bonne nuit.");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		CharacterRecognizer recognizer;
		MainView view = new MainView(recognizer);
		Scene scene = new Scene(view.getRoot(), 800,350);
		primaryStage.setTitle("Neuro Read");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}