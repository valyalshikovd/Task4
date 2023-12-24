package com.cgvsu;

import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.FourDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.render_engine.MyRenderEngine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.*;


import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class GuiController {

    final private float TRANSLATION = 10;
    @FXML
    public AnchorPane sliderTheme2;
    public AnchorPane sliderRender;
    public AnchorPane sliderMove;
    public TabPane mainPanel;

    private List<Model> listMesh = new ArrayList<>();
    private Map<String, Model> loadedModels = new HashMap<>();


    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;
    @FXML
    private Slider sliderTheme;

    @FXML
    private Slider translateX;

    @FXML
    private TextField textFileTranslateX;


    @FXML
    private ListView<String> listView;

    private List<TextField> list;

    private Model mesh = null;
    private MyRenderEngine renderEngine;

    private Camera camera = new Camera(
            new ThreeDimensionalVector(0, 0, 100),
            new ThreeDimensionalVector(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        ObservableList<String> observableList = FXCollections.observableArrayList(loadedModels.keySet());
        listView.setItems(observableList);
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(sliderTheme.getValue() - 1);
        canvas.setEffect(colorAdjust);

        sliderTheme.valueProperty().addListener((observable, oldValue, newValue) -> {
            colorAdjust.setBrightness(newValue.floatValue() - 1);
            anchorPane.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 1.5) + ","  + (256 - newValue.doubleValue() * 1.5) + ", "  + (256 - newValue.doubleValue() * 1.5) + ")");
            //  listViewModels.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 1.5) + ","  + (256 - newValue.doubleValue() * 1.5) + ", "  + (256 - newValue.doubleValue() * 1.5) + ")");
            canvas.setEffect(colorAdjust);
        });


//        translateX.valueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
//                String degrees = String.format(Locale.ENGLISH, "%(.2f", translateX.getValue());
//                if (degrees.contains("(")) {
//                    degrees = degrees.substring(1, degrees.length()-1);
//                    degrees = "-" + degrees;
//                }
//                list.get(0).setText(degrees);
//            }
//        });

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        renderEngine = new MyRenderEngine(camera, (int) width, (int) height);


        KeyFrame frame = new KeyFrame(Duration.millis(60), event -> {

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));
            renderEngine.setCamera(camera, (int) width, (int) height);
            renderEngine.drawAllMeshes(canvas.getGraphicsContext2D());
            //System.out.println(renderEngine.getListMesh());

        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }


    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

      //  ObservableList<Model> observableList = FXCollections.observableArrayList(listMesh);
       // listViewModels.setItems(observableList);

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            Model model = ObjReader.read(fileContent);
            mesh = ObjReader.read(fileContent);
            mesh.m = (NDimensionalMatrix)  new AffineTransformation().translate(20,1,1);
            renderEngine.addMesh(mesh);
            loadedModels.put(file.getName(), model);

            for (Map.Entry<String, Model> entry : loadedModels.entrySet()) {
                String key = entry.getKey();
                Model value = entry.getValue();
                System.out.println("Key: " + key + ", Value: " + value);
            }
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }


    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(0, -TRANSLATION, 0));
    }

    public void handleCameraRightMove(ActionEvent actionEvent) {
        System.out.println("Клавиша D нажата");
    }

    public void handleCameraLeftMove(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(0, -TRANSLATION, 0));
        System.out.println("Клавиша A нажата");
    }

    public static void exception(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Reader Exception");
        alert.setHeaderText(text);
        alert.setContentText("Please correct the data.");
        alert.showAndWait();
    }


   // public void onOpenSaveWithChangesModel() {
     //   FileChooser fileChooser = new FileChooser();
      //  fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
      //  File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
      //  try {
           // Model changedModel = new Model(scene.getLoadedModels().get(scene.currentModelName).modifiedVertices(), scene.getLoadedModels().get(scene.currentModelName).getTextureVertices(), scene.getLoadedModels().get(scene.currentModelName).getNormals(), scene.getLoadedModels().get(scene.currentModelName).getPolygons());
            //List<String> fileContent2 = ObjWriter.write("newmodel", changedModel);
          //  ObjWriter.write("file", changedModel);
       // } catch (IOException ignored) {
     //   }}
}