package ch.kidin.util;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class GeneratePDF {

    private final String PATH = "D:\\test\\";
    private String payerName;
    private Double[] amounts;
    private String fullPath;

    public GeneratePDF(String payerName, Double[] amounts) {
        this.payerName = payerName;
        this.amounts = amounts;
        String familyName = payerName;
        if (payerName.contains("Familie")) {
            familyName = payerName.substring(8);
        }
        fullPath = PATH + "Steuerbescheinigung_Fam_" + familyName + ".pdf";
    }

    public int getTextSize(PDFont font, String line, int fontSize) throws IOException {
        return (int) (font.getStringWidth(line) / 1000 * fontSize);
    }

    /**
     * Sets the layout for the PDF (PDFBox-Library), fill it with content and save it.
     * @throws IOException
     */
    public void writePDF() throws IOException {

        //create doc
        PDDocument document = new PDDocument();

        //create page
        PDPage page = new PDPage();

        //add page to doc
        document.addPage(page);

        //add content
        PDPageContentStream layout = new PDPageContentStream(document,page);
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontDefault = PDType1Font.HELVETICA;
        int pageMargin = 50;
        int pageXDimension = (int) page.getMediaBox().getWidth();
        int pageYDimension = (int) page.getMediaBox().getHeight();

        //add logo
        int logoWidth = 90;
        int logoHeight = (int) (logoWidth*0.75);
        PDImageXObject logoImage = PDImageXObject.createFromFile("assets\\kidin.png",document);
        layout.drawImage(logoImage,pageXDimension-pageMargin-logoWidth,pageYDimension-pageMargin-logoHeight, logoWidth, logoHeight);

        //add title
        int titleFontSize = 18;
        String titleText = "Nachweis der Betreuungsgebühren "+ CSVtoPDF.YEAR;
        int titleTextWidth = getTextSize(fontDefault,titleText,titleFontSize);
        layout.beginText();
        layout.setFont(fontBold,titleFontSize);
        layout.newLineAtOffset((pageXDimension-titleTextWidth)/2,620);
        layout.showText(titleText);
        layout.endText();

        //add text
        int textFontSize = 12;
        int yPosition = 560;
        String textLine1 = "Wir bestätigen, dass ";
        String textLine2 = " im Jahr " + CSVtoPDF.YEAR +
                " untenstehende Gebühren für die Kinderbetreuung bezahlt";
        if (payerName.substring(0,7).equals("Familie")) {
            textLine1 +=  "die " + payerName;
            textLine2 += " hat.";
        } else if (payerName.contains(" und ")){
            textLine1 += payerName;
            textLine2 += " haben.";
        } else {
            textLine1 += payerName;
            textLine2 += " hat.";
        }
        int textLine1Width = getTextSize(fontDefault,textLine1,textFontSize);
        int textLine2Width = getTextSize(fontDefault,textLine2,textFontSize);
        layout.beginText();
        layout.setFont(fontDefault,textFontSize);
        layout.newLineAtOffset((pageXDimension-textLine1Width)/2, yPosition);
        layout.showText(textLine1);
        layout.endText();
        layout.beginText();
        layout.setFont(fontDefault,textFontSize);
        layout.newLineAtOffset((pageXDimension-textLine2Width)/2,yPosition-20);
        layout.showText(textLine2);
        layout.endText();

        //add line
        layout.moveTo(pageMargin,yPosition-40);
        layout.lineTo(pageXDimension-pageMargin, yPosition-40);
        layout.stroke();

        //add months
        String[] months = new String[] {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli",
                "August", "September", "Oktober", "November", "Dezember"};

        int tableFontSize = 10;
        layout.setFont(fontDefault, tableFontSize);
        layout.setLeading(18);
        int offset = 70;
        int i = 0;
        for (String month : months) {
            String text = month + ":";
            layout.beginText();
            layout.newLineAtOffset(pageXDimension/2 - 50, yPosition-offset);
            layout.showText(text);
            layout.endText();
            layout.beginText();
            String amount = String.format ("%,.2f", amounts[i]);
            int amountTextSize = getTextSize(fontDefault,amount,tableFontSize);
            layout.newLineAtOffset(pageXDimension/2 + 50 - amountTextSize, yPosition-offset);

            layout.showText(amount);
            layout.endText();
            offset += 20;
            i++;
        }

        //add total amount
        Double totalAmount = 0.0;
        for (Double amount : amounts) {
            totalAmount += amount;
        }
        String totalAmountText = "Total bezahlte Gebühren: CHF " + String.format ("%,.2f", totalAmount);
        int totalAmountTextWidth = getTextSize(fontDefault,totalAmountText,textFontSize);
        layout.beginText();
        layout.setFont(fontBold,tableFontSize+1);
        layout.newLineAtOffset((pageXDimension-totalAmountTextWidth)/2,220);
        layout.showText(totalAmountText);
        layout.endText();

        //add line
        layout.moveTo(pageMargin,200);
        layout.lineTo(pageXDimension-pageMargin, 200);
        layout.stroke();

        //add greeting
        layout.beginText();
        layout.setFont(fontDefault,textFontSize);
        layout.newLineAtOffset(pageMargin,170);
        layout.showText("Freundliche Grüsse");
        layout.newLine();
        layout.showText("Markus Steiner, Geschäftsleitung Kinderwelt kidin.ch");
        layout.endText();

        //add signature
        double divisor = 2.8;
        int signWidth = 287 / (int) divisor;
        int signHeight = 101 / (int) divisor;
        PDImageXObject signImage = PDImageXObject.createFromFile("assets\\signature.jpg",document);
        layout.drawImage(signImage,pageMargin-5,pageMargin+30, signWidth, signHeight);

        //add footer
        int footerFontSize = 10;
        String footerText = "Kinderwelt kidin.ch | Schulweg 1 | 8616 Riedikon | info@kidin.ch";
        int footerTextWidth = getTextSize(fontDefault,footerText,footerFontSize);
        layout.beginText();
        layout.setFont(fontDefault,footerFontSize);
        layout.newLineAtOffset((pageXDimension-footerTextWidth)/2, pageMargin);
        layout.showText(footerText);
        layout.endText();
        layout.close();

        //save doc
        document.save(fullPath);

        //cleaning memory
        document.close();
        System.out.println("Document: " + "Steuerbescheinigung_" + payerName + ".pdf was generated in " + PATH);
    }
}
