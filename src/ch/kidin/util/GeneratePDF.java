package ch.kidin.util;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class GeneratePDF {

    private final String PATH = "D:\\test\\";
    private String fileName;
    private String fullPath;

    public GeneratePDF(String fileName) {
        this.fileName = fileName;
        fullPath = PATH + fileName + ".pdf";
    }

    public void writePDF() throws IOException {

        //create doc
        PDDocument document = new PDDocument();

        //create page
        PDPage page = new PDPage();

        //add page to doc
        document.addPage(page);

        //add content


        PDPageContentStream layout = new PDPageContentStream(document,page);

        //add header
        layout.beginText();
        layout.setFont(PDType1Font.HELVETICA, 14);
        layout.newLineAtOffset(100, 750);
        layout.showText("Kinderwelt kidin.ch | Schulweg 1 | 8616 Riedikon | info@kidin.ch");
        layout.endText();


        //add title
        layout.beginText();
        layout.setFont(PDType1Font.COURIER_BOLD,26);
        layout.newLineAtOffset(200,600);
        layout.showText("Steuernachweis");
        layout.endText();

        //add logo
        PDImageXObject logoImage = PDImageXObject.createFromFile("C:\\Users\\marku\\Desktop\\kidin.png",document);
        layout.drawImage(logoImage,400,700,50,50);

        layout.close();



        //save doc
        document.save(fullPath);

        //cleaning memory
        document.close();
        System.out.println("Document: " + fileName + ".pdf was generated in " + PATH);
    }
}
