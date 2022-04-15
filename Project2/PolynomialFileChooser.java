/**
 * Project 2
 * 
 * @author Christopher Stoner
 * 
 * Allows for selection of a file (.txt)
 * Get input line by line from that file
 * Attempt to create an arraylist of polynomial objects from each line
 * Throws InvalidPolynomialSyntax in a JOptionPane Dialog box if one or more of the polynomials are invalid
 * Compares each polynomial to determine if the list is in strong and/or weak sorted order
 */

package Project2;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class PolynomialFileChooser {

    /**
     * Is a function that reads from a file line by line, then returns the lines in
     * a list of strings.
     * 
     * @param fileName String that is an absolute path to the file to open and read
     *                 from
     * @return returns a list of strings that are line by line read from the file
     * @throws IOException if the file is not found or cannot be read from
     */
    public static List<String> readFileInList(String fileName) throws IOException {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }

    public static void main(String[] args) throws IOException, InvalidPolynomialSyntax {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./.tests/")); // This is the directory we wish to open upon running

        int result = fileChooser.showOpenDialog(null);

        // Defines our selected file and its path once we have slected a file
        File selectedFile;
        String selectedFilePath;
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFilePath = selectedFile.getAbsolutePath();
        } else {
            throw new FileNotFoundException();
        }

        List<String> list = readFileInList(selectedFilePath); // Reads each line of the file and puts it into a list to
                                                              // then be converted into an ArrayList of polynomials
                                                              // later
        Iterator<String> itr = list.iterator();

        try {
            ArrayList<Polynomial> polynomials = new ArrayList<>();
            while (itr.hasNext()) {
                polynomials.add(new Polynomial(itr.next())); // Creating a new polynomial object for each item in list,
                                                             // using the item itself to be passed to the constructor
            }

            System.out.println(polynomials.toString());

            // Calls on OrderedList to determine if the list is in an ordered form
            Boolean compareBool = OrderedList.checkSorted(polynomials);
            if (compareBool) {
                System.out.println("Polynomials are in STRONG sorted order");
            } else {
                System.out.println("Polynomials are NOT in STRONG sorted order");
            }

            // Lambda expression that implements the Comparator interface
            // Its compare method should consider only the greatest exponent and not the
            // coefficients in comparing the polynomials
            Comparator<Polynomial> polynomialComparator = new Comparator<Polynomial>() {

                @Override
                public int compare(Polynomial o1, Polynomial o2) {
                    ArrayList<Double[]> o1ExpoCoef = Polynomial.expoCoefOf(o1);
                    ArrayList<Double[]> o2ExpoCoef = Polynomial.expoCoefOf(o2);

                    Double[] o1Exponents = o1ExpoCoef.get(0);
                    Double[] o2Exponents = o2ExpoCoef.get(0); // Get exponents of each polynomial

                    int result = 0;
                    if (o1Exponents.length >= o2Exponents.length) { // Check which array has the greatest length
                        for (int i = 0; i < o1Exponents.length; i++) {
                            Double o1Exponent = o1Exponents[i];
                            Double o2Exponent = o2Exponents[i];

                            if (o1Exponent.compareTo(o2Exponent) > 0) { // o1 exponent > o2 exponent
                                result = 1;
                                return result;
                            }
                            if (o1Exponent.compareTo(o2Exponent) == 0) { // o1 exponent == o2 exponent
                                result = 0;
                                return result;
                            }
                            if (o1Exponent.compareTo(o2Exponent) < 0) { // o1 exponent < o2 exponent
                                result = -1;
                                return result;
                            }
                        }
                    } else {
                        for (int i = 0; i < o2Exponents.length; i++) {
                            Double o1Exponent = o1Exponents[i];
                            Double o2Exponent = o2Exponents[i];

                            if (o1Exponent.compareTo(o2Exponent) > 0) {
                                result = 1;
                                return result;
                            }
                            if (o1Exponent.compareTo(o2Exponent) == 0) {
                                result = 0;
                                return result;
                            }
                            if (o1Exponent.compareTo(o2Exponent) < 0) {
                                result = -1;
                                return result;
                            }
                        }
                    }

                    return result;
                }

            };

            compareBool = OrderedList.checkSorted(polynomials, polynomialComparator);
            if (compareBool) {
                System.out.println("Polynomials are in WEAK sorted order");
            } else {
                System.out.println("Polynomials are NOT in WEAK sorted order");
            }

        } catch (InvalidPolynomialSyntax e) {
            JOptionPane.showMessageDialog(null, "Exception Throw: " + e + "\n" +
                    "The file opened contains one or more lines that are not in the standard form of a polynomial.");
        }
    }
}