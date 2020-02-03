package CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    private HashMap<String, Double[]> amountPerFamily = new HashMap<>();
    private String path="testCsv.csv";
    private String line = "";
    private String csvSplitBy=";";
    private int fromLine;
    private ArrayList<String[]> allLines;

    public CSVReader(int fromLine) {
        this.fromLine = fromLine;
        allLines = new ArrayList<>();
        this.readCSV();
        setAmountPerFamily();
        printAmounts();
    }
    public CSVReader(int fromLine, String path, String csvSplitBy) {
        this.fromLine = fromLine;
        this.path = path;
        this.csvSplitBy = csvSplitBy;
    }
    public void printAmounts() {
        // TreeMap to store values of HashMap
        TreeMap<String, Double[]> sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(amountPerFamily);
        // Display the TreeMap which is naturally sorted
        for (Map.Entry<String, Double[]> entry : sorted.entrySet())
            System.out.println("Rechnungszahler = " + entry.getKey() +
                    ", Beträge = " + Arrays.toString(entry.getValue()));


        System.out.println("Total Einträge: " + amountPerFamily.size());
    }


    public void readCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] lineArr = line.split(this.csvSplitBy);
                allLines.add(lineArr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAmountPerFamily() {
        for (int i = this.fromLine-1; i<allLines.size(); i++) {
            Double[] doubleArr = new Double[12];
            for (int n = 1; n<=12; n++) {
                doubleArr[n-1] = Double.parseDouble(allLines.get(i)[n]);
            }

            amountPerFamily.put(allLines.get(i)[0],doubleArr);
        }
    }





}