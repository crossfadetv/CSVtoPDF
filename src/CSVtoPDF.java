import ch.kidin.util.CSVReader;
import ch.kidin.util.GeneratePDF;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVtoPDF {

    public static void main(String[] args) throws IOException {

        HashMap<String, Double[]> result = new CSVReader(18).getAmountPerFamily();

        for (Map.Entry<String,Double[]> entry : result.entrySet()) {
            try {
                GeneratePDF pdf = new GeneratePDF(entry.getKey());
                pdf.writePDF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
