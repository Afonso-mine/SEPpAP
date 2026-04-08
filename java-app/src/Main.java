import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        TextField rainField = new TextField();
        rainField.setPromptText("Monthly Rainfall (mm)");

        TextField kField = new TextField("0.03");
        TextField lsField = new TextField("1.5");
        TextField cField = new TextField("0.3");
        TextField pField = new TextField("0.8");

        Label resultLabel = new Label("Result: ");

        Button predictBtn = new Button("Predict Erosion");

        predictBtn.setOnAction(e -> {
            try {
                double rain = Double.parseDouble(rainField.getText());
                double k = Double.parseDouble(kField.getText());
                double ls = Double.parseDouble(lsField.getText());
                double c = Double.parseDouble(cField.getText());
                double p = Double.parseDouble(pField.getText());

                double R = 0.5 * rain;
                double erosion = R * k * ls * c * p;

                resultLabel.setText("Predicted erosion: " + erosion + " tons/ha");

            } catch (Exception ex) {
                resultLabel.setText("Invalid input!");
            }
        });

        VBox layout = new VBox(10,
                new Label("Rainfall (mm)"), rainField,
                new Label("Soil Erodibility"), kField,
                new Label("Slope Lenght & Steepness"), lsField,
                new Label("Cover Management"), cField,
                new Label("Support Practices"), pField,
                predictBtn,
                resultLabel
        );

        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 300, 400));
        stage.setTitle("Soil Erosion Predictor");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}