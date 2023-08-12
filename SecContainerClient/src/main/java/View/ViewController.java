package View;

import Controller.SyncViewAndModel;
import Models.BaseRegex;
import Models.File;
import View.Exception.InvalidFieldException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.FlatTextInputDialog;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.controlsfx.control.StatusBar;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewController implements Initializable {

    private SyncViewAndModel mvc;

    //region FXML
    @FXML
    private VBox MainVBox;
    @FXML
    private Button SignOutButton;
    @FXML
    private Button SignUpButton;
    @FXML
    private Button SignInButton;
    @FXML
    private Button RefreshButton;
    @FXML
    private Button UploadButton;
    @FXML
    private Button DownloadButton;
    @FXML
    private Button RemoveButton;
    @FXML
    private TextField LoginTextField;
    @FXML
    private TextField PasswordTextField;
    @FXML
    private TextField HostTextField;
    @FXML
    private TextField PortTextField;
    @FXML
    private CheckBox TLSCheckBox;
    @FXML
    private TableColumn FileNameTable;
    @FXML
    private TableColumn FileSizeTable;
    @FXML
    private TableView FileTableView;
    @FXML
    private MenuItem MenuItemUpload;
    @FXML
    private MenuItem MenuItemDownload;
    @FXML
    private MenuItem MenuItemRemove;
    //endregion

    private StatusBar statusBar;

    public ViewController(SyncViewAndModel mvc) {
        this.mvc = mvc;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create Jmetro status bar
        statusBar = new StatusBar();
        statusBar.setText("Status:");
        MainVBox.getChildren().add(statusBar);

        // Create Jmetro Column
        TableColumn<File, String> fileName = new TableColumn<File, String>("File name");
        TableColumn<File, String> fileSize = new TableColumn<File, String>("File size");
        FileTableView.getColumns().addAll(fileName, fileSize);
        fileName.setCellValueFactory(new PropertyValueFactory<>("filename"));
        fileSize.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
    }

    @FXML
    public void signUp() {
        try {
            mvc.signUp(getHost(), getPort(), getTls(), getLogin(), getPassword());
        } catch (InvalidFieldException e) {
            setStatus(e.getMessage());
        }
    }

    @FXML
    public void signIn() {
        try {
            mvc.signIn(getHost(), getPort(), getTls(), getLogin(), getPassword());
        } catch (InvalidFieldException e) {
            setStatus(e.getMessage());
        }
    }

    @FXML
    public void signOut() {
        if (mvc.signOut()) {
            setStatus("You have been disconnected");
            setConnectionGroupEnabled(false);
        }
    }

    @FXML
    public void refreshList() {
        mvc.refreshList();
    }

    @FXML
    public void uploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select you file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        java.io.File selectedFile = fileChooser.showOpenDialog(MainVBox.getScene().getWindow());
        if (selectedFile != null) {
            try {
                mvc.uploadFile(selectedFile.getPath(), getFilename(selectedFile), selectedFile.length());
            } catch (InvalidFieldException e) {
                setStatus(e.getMessage());
            }
        }
    }

    @FXML
    public void getFile() {
        File file = (File) FileTableView.getSelectionModel().getSelectedItem();
        if (file == null) return;

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select your directory");

        java.io.File selectedDirectory = directoryChooser.showDialog(MainVBox.getScene().getWindow());
        if (selectedDirectory != null) {
            mvc.setDownloadDirectory(selectedDirectory.getPath());
            mvc.downloadFile(file.getFilename());
        }
    }

    @FXML
    public void removeFile() {
        File file = (File) FileTableView.getSelectionModel().getSelectedItem();
        if (file == null) return;
        mvc.removeFile(file.getFilename());
    }

    @FXML
    public void activate2FA() {
        try {
            mvc.activate2FA(getHost(), getPort(), getTls(), getLogin(), getPassword());
        } catch (InvalidFieldException e) {
            setStatus(e.getMessage());
        }

    }

    @FXML
    public void gitlabClient() {
        try {
            Desktop.getDesktop().browse(new URL("https://git.cg.helmo.be/e190061/SecContainerClient").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gitlabFFE() {
        try {
            Desktop.getDesktop().browse(new URL("https://git.cg.helmo.be/e190061/FileFrontEnd").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gitlabSBE() {
        try {
            Desktop.getDesktop().browse(new URL("https://git.cg.helmo.be/e190061/StorBackEnd").toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setConnectionGroupEnabled(boolean value) {
        LoginTextField.setDisable(value);
        PasswordTextField.setDisable(value);
        HostTextField.setDisable(value);
        PortTextField.setDisable(value);
        TLSCheckBox.setDisable(value);
        SignUpButton.setDisable(value);
        SignInButton.setDisable(value);
        SignOutButton.setDisable(!value);

        setDownloadGroupEnabled(value);
    }

    public void setDownloadGroupEnabled(boolean value) {
        UploadButton.setDisable(!value);
        RefreshButton.setDisable(!value);
        DownloadButton.setDisable(!value);
        RemoveButton.setDisable(!value);

        MenuItemUpload.setDisable(!value);
        MenuItemDownload.setDisable(!value);
        MenuItemRemove.setDisable(!value);
    }

    public void setStatus(String status) {
        statusBar.setText("Status: " + status);
    }

    public void setFilesList(List<File> f) {
        ObservableList<File> list = FXCollections.observableArrayList(f);
        FileTableView.setItems(list);
    }

    public Boolean confimationSet2FA() {
        Alert dialogConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        dialogConfirm.setTitle("Setting 2FA: Confirmation");
        dialogConfirm.setHeaderText(null);
        dialogConfirm.setContentText("Do you want to activate 2FA on your account ?");
        Optional<ButtonType> answer = dialogConfirm.showAndWait();
        return answer.isPresent() && answer.get() == ButtonType.OK;
    }

    public void open2FAWindow(String key2FA) {
        try {
            String login = getLogin();
            String password = getPassword();

            // 2FA ViewController
            DFAViewController dfaViewController = key2FA == null ? new DFAViewController(mvc, login, password) : new DFAViewController(mvc, login, password, key2FA);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/2FAGUI.fxml"));
            fxmlLoader.setController(dfaViewController);

            Stage stage = new Stage(StageStyle.DECORATED);

            // JMetro for JavaFx
            Scene scene = new Scene(fxmlLoader.load());
            JMetro jMetro = new JMetro(Style.DARK);
            jMetro.setScene(scene);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFieldException e) {
            setStatus(e.getMessage());
        }
    }

    private String getLogin() throws InvalidFieldException {
        String login = LoginTextField.getText();
        if (login != null && !login.matches(BaseRegex.login))
            throw new InvalidFieldException("Invalid format for Login field (5-20 digits or letters)");
        return login;
    }

    private String getPassword() throws InvalidFieldException {
        String password = PasswordTextField.getText();
        if (password != null && !password.matches(BaseRegex.password))
            throw new InvalidFieldException("Invalid format for Password field (5-50)");
        return password;
    }

    private int getPort() throws InvalidFieldException {
        String port = PortTextField.getText();
        if (port != null && !port.matches(BaseRegex.port))
            throw new InvalidFieldException("Invalid format for Port field (1 - 65535)");
        return Integer.parseInt(port);
    }

    private String getHost() throws InvalidFieldException {
        String host = HostTextField.getText();
        if (host != null && host.isBlank())
            throw new InvalidFieldException("Invalid format for Host field");
        return host;
    }

    private boolean getTls() {
        return TLSCheckBox.isSelected();
    }

    private String getFilename(java.io.File file) throws InvalidFieldException {
        String filename = file.getName();
        if (filename != null && !filename.matches(BaseRegex.filename))
            throw new InvalidFieldException("Invalid format for the Filename");
        return filename;
    }
}
