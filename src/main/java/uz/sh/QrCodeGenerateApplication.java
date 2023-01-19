package uz.sh;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SpringBootApplication
public class QrCodeGenerateApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrCodeGenerateApplication.class, args);
    }

}

@RestController
class BaseController {


    private final BaseService service;

    BaseController(BaseService service) {
        this.service = service;
    }


    @GetMapping(value = "/qr_code", produces = MediaType.IMAGE_PNG_VALUE)
    public InputStreamResource getQRCodePngByInn() {
        return service.generateQRCode("9 A yig'ilmaydigan pidarazlar jamoasi");
    }
}

@Service
class BaseService {

    public InputStreamResource generateQRCode(String str) {
        return new InputStreamResource(new ByteArrayInputStream(this.generateQRCodePng(str)));
    }


    private byte[] generateQRCodePng(String barcodeText) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 500, 500);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

}
