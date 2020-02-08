package ch.kidin.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVtoPDF {

    public static final int YEAR = 2019;

    public static void main(String[] args) throws IOException {

        HashMap<String, Double[]> result = new CSVReader(18).getAmountPerFamily();

        for (Map.Entry<String,Double[]> entry : result.entrySet()) {
            try {
                GeneratePDF pdf = new GeneratePDF(entry.getKey().replace("/", " und "),entry.getValue());
                pdf.writePDF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
