package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import java.io.*;
import java.util.List;

public class RUSLEApp extends Application {
    private TextField rField = new TextField();
    private TextField kField = new TextField();
    private TextField lsField = new TextField();
    private TextField cField = new TextField();
    private TextField pField = new TextField();
    private TextField outputField = new TextField();
    private ProgressBar progressBar = new ProgressBar();
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(15);
        root.setStyle("-fx-padding: 20;");
        root.getChildren().add(createInputRow("Rainfall Erosivity (R)", rField));
        root.getChildren().add(createInputRow("Soil Erodibility (K)", kField));
        root.getChildren().add(createInputRow("Slope Length & Steepness (LS)", lsField));
        root.getChildren().add(createInputRow("Cover Management (C)", cField));
        root.getChildren().add(createInputRow("Support Practice (P)", pField));
        root.getChildren().add(createOutputRow());
        progressBar.setVisible(false);
        progressBar.setPrefWidth(400);
        Button run = new Button("Calculate Soil Erosion");
        run.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
        run.setMaxWidth(Double.MAX_VALUE);
        run.setOnAction(e -> runModel());
        root.getChildren().addAll(progressBar, run);
        Scene scene = new Scene(root, 750, 450);
        stage.setTitle("SEPpAP");
        stage.setScene(scene);
        stage.show();
    }
    private HBox createInputRow(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.setPrefWidth(250);

        Button browse = new Button("Browse");
        browse.setOnAction(e -> {
            File homeDir = new File(System.getProperty("user.home"));
            if (! homeDir.exists()) {
                homeDir.mkdirs();
            };
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            fc.setTitle("Select Raster");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("GeoTIFF", "*.tif", "*.tiff")
            );
            File file = fc.showOpenDialog(null);
            if (file != null) field.setText(file.getAbsolutePath());
        });
        field.setOnDragOver(e -> {
            if (e.getGestureSource() != field && e.getDragboard().hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });

        field.setOnDragDropped(e -> {
            List<File> files = e.getDragboard().getFiles();
            if (!files.isEmpty()) {
                field.setText(files.get(0).getAbsolutePath());
            }
            e.setDropCompleted(true);
            e.consume();
        });

        HBox row = new HBox(10, label, field, browse);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return row;
    }

    private HBox createOutputRow() {
        Label label = new Label("Calculated Raster");
        label.setPrefWidth(250);

        Button save = new Button("Save As");
        save.setOnAction(e -> {
            File homeDir = new File(System.getProperty("user.home"));
            if (! homeDir.exists()) {
                homeDir.mkdirs();
            };
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            fc.setTitle("Save Output Raster");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("GeoTIFF", "*.tif")
            );

            File file = fc.showSaveDialog(null);
            if (file != null) outputField.setText(file.getAbsolutePath());
        });

        HBox row = new HBox(10, label, outputField, save);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return row;
    }

    private void runModel() {
        if (rField.getText().isEmpty() ||
            kField.getText().isEmpty() ||
            lsField.getText().isEmpty() ||
            cField.getText().isEmpty() ||
            pField.getText().isEmpty() ||
            outputField.getText().isEmpty()) {

            alert("Please fill all fields.");
            return;
        }

        progressBar.setVisible(true);
        progressBar.setProgress(-1);

        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "python3",
                    "run_rusle.py",
                    rField.getText(),
                    kField.getText(),
                    lsField.getText(),
                    cField.getText(),
                    pField.getText(),
                    outputField.getText()
                );

                pb.redirectErrorStream(true);
                Process p = pb.start();

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
                );

                while (reader.readLine() != null) {
                    // could stream logs here later
                }

                p.waitFor();

                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    alert("RUSLE calculation completed!");
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    alert("Error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}