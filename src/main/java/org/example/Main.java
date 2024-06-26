package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws AWTException, IOException {

        Robot r = new Robot();

        BufferedImage i = r.createScreenCapture(new Rectangle(0,0,1920,200));
        File outputFile = new File("screenshot.png");

        ImageIO.write(i, "PNG", outputFile);
        System.out.println("Screenshot saved as TIFF: " + outputFile.getAbsolutePath());
        System.setProperty("jna.library.path", "C:\\Users\\tac52\\OneDrive\\Desktop\\tessdata-main");

        // Initialize Tesseract
        Tesseract tesseract = new Tesseract();

        try {
            // Specify the path to the PNG image file you want to extract text from
            File imageFile = new File(outputFile.getAbsolutePath());

            // Perform OCR on the image
            String text = tesseract.doOCR(imageFile);

            // Print the extracted text
            System.out.println("Extracted Text:\n" + text);
            String sourceLang = "de";
            String targetLang = "en";
            String body = "source_language="+sourceLang+"&target_language="+targetLang+"&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);
            System.out.println(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://text-translator2.p.rapidapi.com/translate"))
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("X-RapidAPI-Key", "424579e75fmshd14b45ac40bfaffp1463e9jsnb07b439426e4")
                    .header("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                    .method("POST", HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*
        BytePointer outText;


        tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(null, "eng") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library

        lept.PIX image = pixRead(args.length > 0 ? args[0] : "/usr/src/tesseract/testing/phototest.tif");
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        System.out.println("OCR output:\n" + outText.getString());

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image); */
    }
}