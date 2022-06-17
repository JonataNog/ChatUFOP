package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception{
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(ClienteViewController.class.getResource("ClienteView.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        ClienteViewController controller = loader.getController();
	        controller.setDialogStage(stage);
	        
	        Scene scene = new Scene(page);
	        
	        stage.setScene(scene);
	        stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
