package com.cgvsu;

import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;


import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 10;
    @FXML
    public AnchorPane sliderTheme2;
    public AnchorPane sliderRender;
    public AnchorPane sliderMove;
    public TabPane mainPanel;
    public TextField textFieldTranslationX;
    public TextField textFieldTranslationY;
    public TextField textFieldTranslationZ;
    public TextField textFieldRotateX;
    public TextField textFieldRotateY;
    public TextField textFieldRotateZ;
    public TextField textFieldScaleX;
    public TextField textFieldScaleY;
    public TextField textFieldScaleZ;
    public CheckBox triangulationCheckBox;
    public CheckBox fillCheckBox;
    public ColorPicker colorpick;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;
    @FXML
    private Slider sliderTheme;
    @FXML
    private ListView<String> listViewModels = new ListView<>();

    private Model mesh = null;
    private Scene scene;

    private ObservableList<String> items = FXCollections.observableArrayList();

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

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(sliderTheme.getValue() - 1);
        canvas.setEffect(colorAdjust);



        listViewModels.setItems(items);

        sliderTheme.valueProperty().addListener((observable, oldValue, newValue) -> {
            colorAdjust.setBrightness(newValue.floatValue() - 1);
            anchorPane.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 1.5) + ","  + (256 - newValue.doubleValue() * 1.5) + ", "  + (256 - newValue.doubleValue() * 1.5) + ")");
            listViewModels.setStyle("-fx-background-color:rgb(" + (newValue.doubleValue() * 2.55) + ","  + (newValue.doubleValue() * 2.55) + ", "  + (newValue.doubleValue() * 2.55) + ")");
            sliderRender.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 2) + ","  + (256 - newValue.doubleValue() * 2) + ", "  + (256 - newValue.doubleValue() * 2) + ")");
            sliderMove.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 2) + ","  + (256 - newValue.doubleValue() * 2) + ", "  + (256 - newValue.doubleValue() * 2) + ")");
            canvas.setEffect(colorAdjust);
        });

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        scene = new Scene(camera, (int) width, (int) height);


        KeyFrame frame = new KeyFrame(Duration.millis(90), event -> {

            canvas.getGraphicsContext2D().clearRect(0, 0, width , height);
            camera.setAspectRatio((float) (width / height));
            scene.setCamera(camera, (int) width, (int) height);
            scene.drawAllMeshes(canvas.getGraphicsContext2D());

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


        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            scene.getLoadedModels().put(fileName.toString(), mesh);
            items.add(fileName.toString());
            listViewModels.setItems(items);
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


    public void handleModelList(MouseEvent mouseEvent) {
        scene.currentModelName = listViewModels.getSelectionModel().getSelectedItems().get(0);

        textFieldTranslationX.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).translateX));
        textFieldTranslationY.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).translateY));
        textFieldTranslationZ.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).translateZ));

        textFieldRotateX.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).rotateX));
        textFieldRotateY.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).rotateY));
        textFieldRotateZ.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).rotateZ));

        textFieldScaleX.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).scaleX));
        textFieldScaleY.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).scaleY));
        textFieldScaleZ.setText(String.valueOf(scene.getLoadedModels().get(scene.currentModelName).scaleZ));

    }

    public void handleTextFieldActionTranslate(ActionEvent actionEvent) {
        scene.getLoadedModels().get(scene.currentModelName).translateX  = Integer.parseInt(textFieldTranslationX.getText());
        scene.getLoadedModels().get(scene.currentModelName).translateY  = Integer.parseInt(textFieldTranslationY.getText());
        scene.getLoadedModels().get(scene.currentModelName).translateZ  = Integer.parseInt(textFieldTranslationZ.getText());
    }

    public void handleRotate(ActionEvent actionEvent) {
        scene.getLoadedModels().get(scene.currentModelName).rotateX  = Double.parseDouble(textFieldRotateX.getText());
        scene.getLoadedModels().get(scene.currentModelName).rotateY  = Double.parseDouble(textFieldRotateY.getText());
        scene.getLoadedModels().get(scene.currentModelName).rotateZ  = Double.parseDouble(textFieldRotateZ.getText());
    }


    public void handleScale(ActionEvent actionEvent) {
        scene.getLoadedModels().get(scene.currentModelName).scaleX  = Double.parseDouble(textFieldScaleX.getText());
        scene.getLoadedModels().get(scene.currentModelName).scaleY  = Double.parseDouble(textFieldScaleY.getText());
        scene.getLoadedModels().get(scene.currentModelName).scaleZ  = Double.parseDouble(textFieldScaleZ.getText());
    }

    public void onOpenSaveUnchangedModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        String fileName = file.getAbsolutePath();
        System.out.println(scene.currentModelName);
        System.out.println(fileName);
        ObjWriter.write(fileName, scene.getLoadedModels().get(scene.currentModelName));

    }

    public void handleTriangulation(ActionEvent actionEvent) {
        scene.getLoadedModels().get(scene.currentModelName).isTriangulate = triangulationCheckBox.isSelected();
    }

    public void handleFill(ActionEvent actionEvent) {
        javafx.scene.paint.Color selectedColor = colorpick.getValue();
        System.out.println(selectedColor);
        scene.getLoadedModels().get(scene.currentModelName).isFill = selectedColor;
    }
}