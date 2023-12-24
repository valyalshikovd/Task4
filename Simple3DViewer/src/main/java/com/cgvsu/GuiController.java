package com.cgvsu;

import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.MyRenderEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

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

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;
    @FXML
    private Slider sliderTheme;
    @FXML
    private ListView<String> listViewModels = new ListView<>();

    private Model mesh = null;
    private MyRenderEngine renderEngine;

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

        ObservableList<String> items = FXCollections.observableArrayList(
                "Item 1", "Item 2", "Item 3", "Item 4");

        listViewModels.setItems(items);

        sliderTheme.valueProperty().addListener((observable, oldValue, newValue) -> {
            colorAdjust.setBrightness(newValue.floatValue() - 1);
            anchorPane.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 2) + ","  + (256 - newValue.doubleValue() * 2) + ", "  + (256 - newValue.doubleValue() * 2) + ")");
            listViewModels.setStyle("-fx-background-color:rgb(" + (newValue.doubleValue() * 2.55) + ","  + (newValue.doubleValue() * 2.55) + ", "  + (newValue.doubleValue() * 2.55) + ")");
            sliderRender.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 2) + ","  + (256 - newValue.doubleValue() * 2) + ", "  + (256 - newValue.doubleValue() * 2) + ")");
            sliderMove.setStyle("-fx-background-color:rgb(" + (256 - newValue.doubleValue() * 2) + ","  + (256 - newValue.doubleValue() * 2) + ", "  + (256 - newValue.doubleValue() * 2) + ")");
            canvas.setEffect(colorAdjust);
        });

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


        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            renderEngine.getLoadedModels().put(fileName.toString(), mesh);
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

    public void handleCameraRightMove(ActionEvent actionEvent) {
        System.out.println("Клавиша D нажата");
    }

    public void handleCameraLeftMove(ActionEvent actionEvent) {
        camera.movePosition(new ThreeDimensionalVector(0, -TRANSLATION, 0));
        System.out.println("Клавиша A нажата");
    }


    public void onOpenSaveWithChangesModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        Model changedModel = new Model(renderEngine.getLoadedModels().get(renderEngine.currentModelName).vertices, renderEngine.getLoadedModels().get(renderEngine.currentModelName).textureVertices, renderEngine.getLoadedModels().get(renderEngine.currentModelName).normals, renderEngine.getLoadedModels().get(renderEngine.currentModelName).polygons);

        // List<String> fileContent2 = ObjWriter.write("newmodel", changedModel);
        ObjWriter.write("file", changedModel);
    }

    public void handleModelList(MouseEvent mouseEvent) {
        renderEngine.currentModelName = listViewModels.getSelectionModel().getSelectedItems().get(0);
    }

    public void handleTextFieldActionXTranslate(ActionEvent actionEvent) {
        renderEngine.getLoadedModels().get(renderEngine.currentModelName).translationMatrix = (NDimensionalMatrix)  new AffineTransformation().translate(Integer.parseInt(textFieldTranslationX.getText()), Integer.parseInt(textFieldTranslationY.getText()) ,Integer.parseInt(textFieldTranslationZ.getText()));
    }

    public void handleTextFieldActionYTranslate(ActionEvent actionEvent) {
        renderEngine.getLoadedModels().get(renderEngine.currentModelName).translationMatrix = (NDimensionalMatrix)  new AffineTransformation().translate(Integer.parseInt(textFieldTranslationX.getText()), Integer.parseInt(textFieldTranslationY.getText()) ,Integer.parseInt(textFieldTranslationZ.getText()));
    }

    public void handleTextFieldActionZTranslate(ActionEvent actionEvent) {
        renderEngine.getLoadedModels().get(renderEngine.currentModelName).translationMatrix = (NDimensionalMatrix)  new AffineTransformation().translate(Integer.parseInt(textFieldTranslationX.getText()), Integer.parseInt(textFieldTranslationY.getText()) ,Integer.parseInt(textFieldTranslationZ.getText()));
    }

    public void handleRotateX(ActionEvent actionEvent) {
        renderEngine.getLoadedModels().get(renderEngine.currentModelName).rotationMatrix = (NDimensionalMatrix) new AffineTransformation().rotate(Integer.parseInt(textFieldRotateX.getText()), Integer.parseInt(textFieldRotateY.getText()) ,Integer.parseInt(textFieldRotateZ.getText()));
    }

    public void handleRotateY(ActionEvent actionEvent) {
        renderEngine.getLoadedModels().get(renderEngine.currentModelName).rotationMatrix = (NDimensionalMatrix) new AffineTransformation().rotate(Integer.parseInt(textFieldRotateX.getText()), Integer.parseInt(textFieldRotateY.getText()) ,Integer.parseInt(textFieldRotateZ.getText()));
    }

    public void handleRotateZ(ActionEvent actionEvent) {
        renderEngine.getLoadedModels().get(renderEngine.currentModelName).rotationMatrix = (NDimensionalMatrix) new AffineTransformation().rotate(Integer.parseInt(textFieldRotateX.getText()), Integer.parseInt(textFieldRotateY.getText()) ,Integer.parseInt(textFieldRotateZ.getText()));
    }
}