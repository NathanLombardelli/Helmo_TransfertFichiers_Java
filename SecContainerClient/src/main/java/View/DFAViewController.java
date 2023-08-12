package View;

import Controller.SyncViewAndModel;
import Models.BaseRegex;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DFAViewController implements Initializable {

    private SyncViewAndModel mvc;

    private final String login;
    private final String password;
    private final String secretKey;

    @FXML
    TextField TextFieldCode;

    @FXML
    private ImageView ImageViewQR;

    public DFAViewController(SyncViewAndModel mvc, String login, String password, String secretKey) {
        this.mvc = mvc;
        this.login = login;
        this.password = password;
        this.secretKey = secretKey;
    }

    public DFAViewController(SyncViewAndModel mvc, String login, String password) {
        this.mvc = mvc;
        this.login = login;
        this.password = password;
        this.secretKey=null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (secretKey != null) {
            setQRImage(secretKey);
        } else {
            ImageViewQR.setFitWidth(0);
            ImageViewQR.setFitHeight(0);
        }
    }

    @FXML
    public void login(MouseEvent event) {
        String code = TextFieldCode.getText();
        if (code.matches(BaseRegex.digits2FA)) {
            mvc.login2FA(login, password, code);
            exit();
        }
    }

    @FXML
    public void cancel(MouseEvent event) {
        exit();
    }

    private void exit() {
        Stage stage = (Stage) ImageViewQR.getScene().getWindow();
        stage.close();
    }

    private void setQRImage(String secretKey) {
        Image img = new Image(new ByteArrayInputStream(getQrCodeFor2FA(secretKey, login)));
        ImageViewQR.setImage(img);
    }

    private byte[] getQrCodeFor2FA(String secretKey, String login) {
        try {
            QrData data = new QrData.Builder()
                    .issuer("Helmo FFE")
                    .label(login)
                    .secret(secretKey)
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6)
                    .period(30)
                    .build();
            QrGenerator generator = new ZxingPngQrGenerator();
            return generator.generate(data);
        } catch (QrGenerationException error) {
            System.out.println("Erreur dans la génération du QrCode.");
        }
        return null;
    }

}
