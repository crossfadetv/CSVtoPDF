package ch.kidin.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    private HashMap<String, Double[]> amountPerFamily = new HashMap<>();
    private String path="revenues2019.csv";
    private String line = "";
    private String csvSplitBy=";";
    private int fromLine;
    private ArrayList<String[]> allLines;

    public CSVReader(int fromLine) {
        this.fromLine = fromLine;
        allLines = new ArrayList<>();
        this.readCSV();
        setAmountPerFamily();
        //printAmounts();
    }
    public CSVReader(int fromLine, String path, String csvSplitBy) {
        this.fromLine = fromLine;
        this.path = path;
        this.csvSplitBy = csvSplitBy;
    }

    /**
     * Reads the lines from the given CSV-File and puts it in an array
     */
    public void readCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split(this.csvSplitBy);
                if (!lineArr[0].equals("")) {
                    lineArr[0] = lineArr[0].substring(1,lineArr[0].length()-1);
                }
                allLines.add(lineArr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters the array for useful values and put those in a HashMap (String, Double[]).
     */
    public void setAmountPerFamily() {
        for (int i = this.fromLine-1; i<allLines.size(); i++) {
            Double[] doubleArr = new Double[12];
            for (int n = 1; n<=12; n++) {
                doubleArr[n-1] = Double.parseDouble(allLines.get(i)[n]);
            }

            amountPerFamily.put(allLines.get(i)[0],doubleArr);
        }
    }

    public HashMap<String, Double[]> getAmountPerFamily() {
        return amountPerFamily;
    }
}