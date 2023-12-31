package com.cgvsu;

import com.cgvsu.Math.AffineTransormation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Parser.Parser;
import com.cgvsu.deleter.DeleterVertices;
import com.cgvsu.deleter.PolygonsDeleter;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.Scene;
import com.cgvsu.terminal.Terminal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

import static com.cgvsu.model.Model.multiplyMatrix4ByVector3;

public class GuiController {


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
    public RadioMenuItem night;
    public RadioMenuItem light;
    public AnchorPane addCamerasPane;
    public TextField xCamera;
    public TextField yCamera;
    public TextField zCamera;
    public TextField nameOfCamera;
    public ListView listViewLights;
    public AnchorPane terminal;
    public TextArea terminalText;
    public CheckBox texturesCheckBox;
    public Button buttonLeft;
    public Button buttonRight;
    public Button buttonLight;
    public TextField xlight;
    public TextField ylight;
    public TextField zlight;
    public Button deleter;
    public TextField verticesToDeleteField;
    public TextField polygonsToDeleteField;
    public CheckBox checkFreeVertices;
    public Text texturesIsLoaded;
    private double prevX;
    private double prevY;

    @FXML
    private void handleMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            double x = event.getX();
            double y = event.getY();
        }
    }
    @FXML
    private void handleScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();

        if (deltaY > 0) {
            scene.getCamera().movePosition(scene.getCamera().getPosition().normalization());
            scene.getCamera().setCurrentDistantionToTarget(scene.getCamera().getPosition().length());
        } else if (deltaY < 0) {
            scene.getCamera().movePosition(scene.getCamera().getPosition().normalization().scale(-1));
            scene.getCamera().setCurrentDistantionToTarget(scene.getCamera().getPosition().length());

        }
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            if(prevY < event.getY()){
                if(Math.abs(camera.getPosition().getArrValues()[0]) < 0.5 && Math.abs(camera.getPosition().getArrValues()[2] - 0) < 0.5 && camera.getPosition().getArrValues()[1] > 0){
                    return;
                }
                double distantionToTarget = camera.getCurrentDistantionToTarget();
                if(camera.getPosition().getArrValues()[1] < 0){
                    double x = camera.getPosition().getArrValues()[0];
                    double z = camera.getPosition().getArrValues()[2];
                    double z1 = Math.pow((distantionToTarget * distantionToTarget / ((x / z) * (x / z) + 1) ), 0.5);
                    double x1 = (z1 * x) / z;
                    NDimensionalVector normalizideVectorOfShifting = (NDimensionalVector) new NDimensionalVector(x1,0,z1).subtraction(camera.getPosition()).normalization();
                    camera.setPosition(camera.getPosition().addition(normalizideVectorOfShifting));
                    return;
                };
                NDimensionalVector normalizideVectorOfShifting = (NDimensionalVector) new NDimensionalVector(0,distantionToTarget,0).subtraction(camera.getPosition()).normalization();
                camera.setPosition(camera.getPosition().addition(normalizideVectorOfShifting));
            }
            if(prevY > event.getY()){
                if(Math.abs(camera.getPosition().getArrValues()[0]) < 0.5 && Math.abs(camera.getPosition().getArrValues()[2]) < 0.5 && camera.getPosition().getArrValues()[1] < 0){
                    return;
                }
                double distantionToTarget = camera.getCurrentDistantionToTarget();
                if(camera.getPosition().getArrValues()[1] > 0){
                    double x = camera.getPosition().getArrValues()[0];
                    double z = camera.getPosition().getArrValues()[2];
                    double z1 = Math.pow((distantionToTarget * distantionToTarget / ((x / z) * (x / z) + 1) ), 0.5);
                    double x1 = (z1 * x) / z;
                    NDimensionalVector normalizideVectorOfShifting = (NDimensionalVector) new NDimensionalVector(x1,0,z1).subtraction(camera.getPosition()).normalization();
                    camera.setPosition(camera.getPosition().addition(normalizideVectorOfShifting));
                    return;
                }
                NDimensionalVector normalizideVectorOfShifting = (NDimensionalVector) new NDimensionalVector(0,-distantionToTarget,0).subtraction(camera.getPosition()).normalization();
                camera.setPosition(camera.getPosition().addition(normalizideVectorOfShifting));
            }
            if(prevX > event.getX()){
                camera.setPosition(multiplyMatrix4ByVector3((NDimensionalMatrix) new AffineTransformation().rotate(0, 0.03F,0),
                        new ThreeDimensionalVector(camera.getPosition().getArrValues()[0],camera.getPosition().getArrValues()[1],camera.getPosition().getArrValues()[2])));
            }
            if(prevX < event.getX()){
                camera.setPosition(multiplyMatrix4ByVector3((NDimensionalMatrix) new AffineTransformation().rotate(0, -0.03F,0),
                        new ThreeDimensionalVector(camera.getPosition().getArrValues()[0],camera.getPosition().getArrValues()[1],camera.getPosition().getArrValues()[2])));
            }
            prevX = event.getX();
            prevY = event.getY();
        }
    }
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
    @FXML
    private ListView<String> listViewCameras;
    private List<String> selectedCameras = new ArrayList<>();





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

        listViewModels.setItems(items);
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        scene = new Scene(camera, (int) width, (int) height);

        scene.setCurrentCamera(camera);
        this.camera.setAspectRatio((float) (width / height));
        scene.getAddedCameras().put("mainCamera", camera);
        listViewCameras.getItems().add("mainCamera");


        KeyFrame frame = new KeyFrame(Duration.millis(50), event -> {

            canvas.getGraphicsContext2D().clearRect(0, 0, 1600 , 900);
            scene.drawAllMeshes(canvas.getGraphicsContext2D());

        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
        texturesIsLoaded.setText("The texture is not loaded.");

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
            addModel(fileName.toString(), mesh);
        } catch (IOException exception) {

        }
    }
    public void addModel(String path, Model mesh){
        Terminal terminalWrite = new Terminal(terminalText);
        mesh.setScene(scene);
        if(scene.getLoadedModels().get(path) != null){
            try{
                int num = Integer.parseInt(scene.currentModelName.substring(path.length() - 4, path.length() -3));
                String string = scene.currentModelName.substring(0, path.length() - 4) + (num + 1) + ".obj";
                scene.getLoadedModels().put(string, mesh);
                items.add(string);
                scene.currentModelName = string;
                terminalWrite.logModelLoading(string);
                return;
            }catch (Exception ignored){
                String string = path.substring(0, path.length() - 4) + 1 + ".obj";
                scene.getLoadedModels().put(string, mesh);
                items.add(string);
                scene.currentModelName = string;
                terminalWrite.logModelLoading(string);
                return;
            }
        }
        scene.getLoadedModels().put(path, mesh);
        items.add(path);
        scene.currentModelName = path;
        terminalWrite.logModelLoading(path);

    }

    @FXML
    private void onOpenTextureClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Load Image");


        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Image image = new Image(file.toURI().toString());
        scene.getLoadedModels().get(scene.currentModelName).image = image;
        texturesIsLoaded.setText("The texture is loaded.");

    }

    public void deleteTexture(){
        Image image = null;
        scene.getLoadedModels().get(scene.currentModelName).image = image;
        texturesIsLoaded.setText("The texture is not loaded.");
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

        texturesCheckBox.setSelected(scene.getLoadedModels().get(scene.currentModelName).isTextured);
        fillCheckBox.setSelected(scene.getLoadedModels().get(scene.currentModelName).isFill);
        triangulationCheckBox.setSelected(scene.getLoadedModels().get(scene.currentModelName).isTriangulate);
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

        System.out.println(scene.getLoadedModels().get(scene.currentModelName).normals.size());
        System.out.println(scene.getLoadedModels().get(scene.currentModelName).textureVertices.size());

    }

    public void handleTriangulation(ActionEvent actionEvent) {
        scene.getLoadedModels().get(scene.currentModelName).isTriangulate = triangulationCheckBox.isSelected();
    }

    public void handleFill(ActionEvent actionEvent) {
        if(texturesCheckBox.isSelected()){
            triangulationCheckBox.setSelected(true);
            fillCheckBox.setSelected(true);
            return;
        }
        javafx.scene.paint.Color selectedColor = colorpick.getValue();
        if(scene.getLoadedModels().get(scene.currentModelName) == null){
            return;
        }
        scene.getLoadedModels().get(scene.currentModelName).fillingColor = selectedColor;
        scene.getLoadedModels().get(scene.currentModelName).isFill = fillCheckBox.isSelected();
        scene.getLoadedModels().get(scene.currentModelName).isTriangulate = true;
        triangulationCheckBox.setSelected(true);
        if(!fillCheckBox.isSelected()){
            scene.getLoadedModels().get(scene.currentModelName).isTriangulate = false;
            triangulationCheckBox.setSelected(false);
        }
    }

    public static void exception(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Reader Exception");
        alert.setHeaderText(text);
        alert.setContentText("Please correct the data.");
        alert.showAndWait();
    }
    public void nightTheme () {
        if (!night.isSelected()) {
            night.setSelected(true);
        }
        light.setSelected(false);
        anchorPane.setStyle("-fx-background-color: #818080;");
        listViewModels.setStyle("-fx-control-inner-background: #888888;");
        listViewCameras.setStyle("-fx-control-inner-background: #888888;");
        listViewLights.setStyle("-fx-control-inner-background: #888888;");
    }

    public void lightTheme () {
        night.setSelected(false);
        anchorPane.setStyle("-fx-background-color: white;");
        listViewModels.setStyle("-fx-control-inner-background: white;");
        listViewCameras.setStyle("-fx-control-inner-background: #ffffff;");
        listViewLights.setStyle("-fx-control-inner-background: #ffffff;");


    }

    public void deleterVertices () {
        String listOfVerticesToDelete = verticesToDeleteField.getText();
        List<Integer> verticesToDelete = Parser.parsing(listOfVerticesToDelete);
        DeleterVertices.removeVerticesFromModel(scene.getLoadedModels().get(scene.currentModelName), verticesToDelete);
    }
    public void deleterPolygons() {
        String listOfPolygonsToDelete = polygonsToDeleteField.getText();
        List<Integer> polygonsToDelete = Parser.parsing(listOfPolygonsToDelete);
        boolean vrt = checkFreeVertices.isSelected();
        assert polygonsToDelete != null;
        PolygonsDeleter.deletePolygons(scene.getLoadedModels().get(scene.currentModelName), polygonsToDelete, vrt);
    }

    //работа с камерой

    public void cameraSelected (MouseEvent event) throws IOException {
        if (Objects.equals(event.getButton().toString(), "PRIMARY")) {
            checkSelectedCameras();
            int index = listViewCameras.getSelectionModel().getSelectedIndex();
            scene.setCurrentCamera(scene.getAddedCameras().get(listViewCameras.getItems().get(index)));

        }
    }

    public void checkSelectedCameras () {
        selectedCameras = new ArrayList<>();
        List<Integer> list = listViewCameras.getSelectionModel().getSelectedIndices();
        for (Integer i : list) {
            selectedCameras.add(listViewCameras.getItems().get(i));
        }
    }
    //окошко добавления
    public void onAddCameraMenuItemClick() {
        addCamerasPane.setVisible(true);
    }

    public void okCameraPane () {
        Camera newCamera = new Camera(
                new ThreeDimensionalVector(Integer.parseInt(xCamera.getText()), Integer.parseInt(yCamera.getText()), Integer.parseInt(zCamera.getText())),
                new ThreeDimensionalVector(0, 0, 0),
                1.0F, 1, 0.01F, 100);
        Map<String, Camera> addedCameras = scene.getAddedCameras();
        String name =  nameOfCamera.getText();
        name = checkContainsTexture(name);
        addedCameras.put(name, newCamera);
        listViewCameras.getItems().add(name);
        addCamerasPane.setVisible(false);
        Terminal terminalWrite = new Terminal(terminalText);
        terminalWrite.logCameraLoading(name, xCamera.getText(), yCamera.getText(), zCamera.getText());

    }


    public String checkContainsTexture (String str) {
        int count = 0;
        for (String name : scene.getLoadedTextures().keySet()) {
            if (name.contains(str)) {
                count++;
            }
        }
        if (count > 0) {
            str += "(" + count + ")";
        }
        return str;
    }

    public void closeCameraPane () {
        addCamerasPane.setVisible(false);
    }

    public void handleTexture(ActionEvent actionEvent) {
        if(texturesCheckBox.isSelected()){
            scene.getLoadedModels().get(scene.currentModelName).isTextured = true;
            scene.getLoadedModels().get(scene.currentModelName).isTriangulate = true;
            scene.getLoadedModels().get(scene.currentModelName).isFill = true;
            texturesCheckBox.setSelected(true);
            triangulationCheckBox.setSelected(true);
            fillCheckBox.setSelected(true);
        }else{
            scene.getLoadedModels().get(scene.currentModelName).isTextured = false;
            scene.getLoadedModels().get(scene.currentModelName).isTriangulate = false;
            scene.getLoadedModels().get(scene.currentModelName).isFill = false;
            texturesCheckBox.setSelected(false);
            triangulationCheckBox.setSelected(false);
            fillCheckBox.setSelected(false);
        }


    }

    public void addLight(ActionEvent actionEvent) {
        scene.getLightSources().put(String.valueOf(scene.getLightSources().size()), new ThreeDimensionalVector(Double.parseDouble(xlight.getText()), Double.parseDouble(ylight.getText()), Double.parseDouble(zlight.getText())));
    }

    public void onOpenDeleteUnchangedModel(ActionEvent actionEvent) {
        scene.getLoadedModels().remove(scene.currentModelName);
        items.remove(scene.currentModelName);
    }

}