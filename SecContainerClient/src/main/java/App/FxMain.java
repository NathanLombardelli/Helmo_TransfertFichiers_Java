package App;

import Controller.MVCController;
import Models.Network.Protocol.*;
import Models.Network.Protocol.Client.ClientGetFileResult;
import Models.Network.Protocol.FFE.*;
import Models.Network.Protocol.ProtocolParser;
import View.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FxMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        try {

            // PROTOCOLS + PARSER
            Map<String, MessageProtocol> protocols = new HashMap<>();
            ProtocolParser parser = new ProtocolParser(protocols);

            // MVC Controller
            MVCController mvcController = new MVCController(parser);


            // PROTOCOLS + EVENT SUBSCRIBE
            // FFESignResultEvent
            FFESignResultMessage FFESignResultMessage = new FFESignResultMessage();
            FFESignResultMessage.subscribe(mvcController);
            protocols.put("SIGN_ERROR", FFESignResultMessage);
            protocols.put("SIGN_OK", FFESignResultMessage);

            // FFEFileResultEvent
            FFEFileListResultMessage FFEFileListResultMessage = new FFEFileListResultMessage();
            FFEFileListResultMessage.subscribe(mvcController);
            protocols.put("FILES", FFEFileListResultMessage);

            // FFESaveFileResultEvent
            FFESaveFileResultMessage FFESaveFileResultMessage = new FFESaveFileResultMessage();
            FFESaveFileResultMessage.subscribe(mvcController);
            protocols.put("SAVEFILE_OK", FFESaveFileResultMessage);
            protocols.put("SAVEFILE_ERROR", FFESaveFileResultMessage);

            // ClientGetFileResultEvent
            ClientGetFileResult ClientGetFileResult = new ClientGetFileResult();
            ClientGetFileResult.subscribe(mvcController);
            protocols.put("GETFILE_OK", ClientGetFileResult);
            protocols.put("GETFILE_ERROR", ClientGetFileResult);

            // FFERemoveFileResultEvent
            FFERemoveFileResultMessage FFERemoveFileResultMessage = new FFERemoveFileResultMessage();
            FFERemoveFileResultMessage.subscribe(mvcController);
            protocols.put("ERASE_OK", FFERemoveFileResultMessage);
            protocols.put("ERASE_ERROR", FFERemoveFileResultMessage);

            // FFESet2FAResultEvent
            FFESet2FAResultMessage FFESet2FAResultMessage = new FFESet2FAResultMessage();
            FFESet2FAResultMessage.subscribe(mvcController);
            protocols.put("SET2FA_OK", FFESet2FAResultMessage);
            protocols.put("SET2FA_ERROR", FFESet2FAResultMessage);

            // FFESignWith2FARequestedEvent
            FFESignWith2FARequestedMessage FFESignWith2FARequestedMessage = new FFESignWith2FARequestedMessage();
            FFESignWith2FARequestedMessage.subscribe(mvcController);
            protocols.put("SIGN_2FA_NEEDED", FFESignWith2FARequestedMessage);

            // View Controller
            ViewController viewController = new ViewController(mvcController);

            // JavaFX
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientGUI.fxml"));

            // MVC 2
            fxmlLoader.setController(viewController);
            mvcController.setView(viewController);
            Parent root = fxmlLoader.load();

            // JMetro for JavaFx
            Scene scene = new Scene(root);
            JMetro jMetro = new JMetro(Style.DARK);
            jMetro.setScene(scene);
            stage.setScene(scene);

            // Stage Close Request
            stage.setOnCloseRequest(e -> mvcController.signOut());

            // Show Window
            stage.show();

        } catch (IOException exception) {
            System.out.println(exception);
        }
    }
}
