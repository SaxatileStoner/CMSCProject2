/**
 * Project 2
 * 
 * @author Christopher Stoner
 * 
 * Defines an individual polynomial
 * 
 * Each polynomial object should be
 * represented internally by a singly linked list.
 * Each exponent and coefficient placed in a node
 */

package Project2;

import java.util.*;

public class Polynomial implements Comparable<Polynomial> {

    // Singly Linked List of expo and coefs unique to each polynomial
    private SinglyLinkedList<Double> list = new SinglyLinkedList<Double>();

    private Boolean startsWithExpo;

    /**
     * Checks if the String value passed is parseable to a Double
     * 
     * @param value String value to be checked
     * @return boolean True if parseable as a double, false otherwise
     */
    private boolean parseableDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Accepts string that defines one polynomial in the same format as provided in
     * the input file
     * --- example ---
     * input file: "5.6x^3 + 4x + 8.3<\nnext poly or end of file>"
     * polynomial String: "5.6x^3 + 4x + 8.3"
     * 
     * Needs input that has been stripped down to just the polynomial alone before
     * calling the constructor
     * 
     * This constructor needs create a new Singly Linked List,
     * tokenize the exponents and coefficents, and add each to the linked list
     * --- example ---
     * String: "5.6x^3 + 4x + 8.3"
     * Linked List: 5.6 -> 3 -> 4 -> 1 -> 8.3 -> 0
     * 
     * 
     * @param polynomial The polynomial String provided
     */
    Polynomial(String polynomial) throws InvalidPolynomialSyntax {
        StringTokenizer tokenizer = new StringTokenizer(polynomial, "+-^x", true);
        Stack<String> stackReverse = new Stack<>();
        Stack<String> stack = new Stack<>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (!token.isEmpty()) {
                stackReverse.push(token);
            }
        }

        while (!stackReverse.empty()) { // We must reverse the stack to get our main stack to use in operations
            stack.push(stackReverse.pop());
        }

        // Basic check, if the stack doesn't contain an x, no way it's a polynomial
        if (!stack.contains("x")) {
            throw new InvalidPolynomialSyntax();
        }

        Boolean firstValue = true;
        Boolean startsWithExpo = false;
        SinglyLinkedList<Double> polynomialList = new SinglyLinkedList<>();
        while (!stack.empty()) {
            String item = stack.pop();

            if (parseableDouble(item)) {
                Double doubleItem = Double.parseDouble(item);
                try { // try to peek at the last, if the stack is empty, add a 0.0 after the item, and
                      // exit the loop
                    stack.peek();
                } catch (Exception e) {
                    polynomialList.add(doubleItem);
                    polynomialList.add(0.0);
                    break;
                }
                polynomialList.add(doubleItem);
            } else if (item.equalsIgnoreCase("x")) { // check if there's an exponent after x, if not, there's an implied
                                                     // 1.0 exponent
                try { // This may mean that there is no coefficent ending to this polynomial
                    stack.peek();
                } catch (Exception e) {
                    polynomialList.add(1.0);
                    break;
                }
                item = stack.peek();
                if (firstValue && item.equals("^")) {
                    item = stack.pop();
                    Double doubleItem = Double.parseDouble(item);
                    polynomialList.add(0.0); // IMPLIED LACK OF COEF
                    polynomialList.add(doubleItem);
                    startsWithExpo = true;
                }

                if (!item.equals("^")) {
                    polynomialList.add(1.0);
                }
            } else if (item.equals("-")) { // Account for negation of coefficent
                try {// This SHOULD have another value after -, if not, somethings wrong with the
                     // polynomial
                    item = stack.pop();
                } catch (Exception e) {
                    throw new InvalidPolynomialSyntax();
                }
                Double doubleItem = Double.parseDouble(item);
                doubleItem *= -1;
                polynomialList.add(doubleItem);
                try {
                    stack.peek();
                } catch (Exception e) {
                    polynomialList.add(0.0);
                    break;
                }
            } else if (item.equals("+")) { // Checks if there's something after +, if not, this is not a valid
                                           // polynomial
                try {
                    item = stack.peek();
                } catch (Exception e) {
                    throw new InvalidPolynomialSyntax();
                }
            } else if (item.equals("^")) {
                try {
                    item = stack.peek();
                } catch (Exception e) {
                    throw new InvalidPolynomialSyntax();
                }
            } else { // Can ONLY contain what has been listed above, anything else is NOT a
                     // polynomial
                throw new InvalidPolynomialSyntax();
            }
            firstValue = false;
        }

        this.list = polynomialList;
        this.startsWithExpo = startsWithExpo;

        // After adding the list to the polynomial object, we need to check if
        // that list is valid. Using expoCoefOf, check if exponents are in the proper
        // order greater -> least. If they aren't, throw InvalidPolynomialSyntax
        // exception.
        ArrayList<Double[]> expoCoefArrayList = expoCoefOf(this);
        Double[] exponents = expoCoefArrayList.get(0);
        for (int i = 1; i < exponents.length; i++) {
            if (exponents[i - 1] < exponents[i]) {
                throw new InvalidPolynomialSyntax();
            }
        }
    }

    /**
     * This will seperate the exponents and coefficents from the receieved
     * Polynomial object, then return an arraylist of two arrays, one for exponents
     * the other for coefficents.
     * 
     * Will be used later for checking if the polynomial is valid and that exponents
     * are in the proper order, from greatest -> least (x^4 + x^3)
     * 
     * @param polynomial the polynomial object to be seperated
     * @return ArrayList<Double[]>, this will return an array containing two arrays,
     *         [0] array is exponents, [1] array is coefficents
     */
    public static ArrayList<Double[]> expoCoefOf(Polynomial polynomial) {
        Iterator<Double> itr = polynomial.list.iterator();

        ArrayList<Double> polynomialArray = new ArrayList<>(); // We need to place the iterator values into an array to
                                                               // be processed

        ArrayList<Double> polynomialExpos = new ArrayList<>(); // Holds our exponent values
        ArrayList<Double> polynomialCoef = new ArrayList<>(); // Holds our coefficent values

        while (itr.hasNext()) {
            polynomialArray.add(itr.next()); // Once placed into an array, we can use the index to determine if it's an
                                             // exponent or coefficent
        }

        // array[0] --> coef, array[1] --> exponent
        for (int i = 0; i < polynomialArray.size(); i++) {
            if (i % 2 != 0) { // If the index value is odd, then it will be a exponent
                polynomialExpos.add(polynomialArray.get(i));
            } else { // Otherwise, it will be a coefficent
                polynomialCoef.add(polynomialArray.get(i));
            }
        }

        Object[] expos = polynomialExpos.toArray();
        Object[] coefs = polynomialCoef.toArray(); // Needs to be processed from an object to into a Double[]
                                                   // array

        // These will process each object value into a Double[] array
        Double[] exposArray = new Double[expos.length];
        for (int i = 0; i < expos.length; i++) {
            exposArray[i] = (Double) expos[i];
        }

        Double[] coefsArray = new Double[coefs.length];
        for (int i = 0; i < coefs.length; i++) {
            coefsArray[i] = (Double) coefs[i];
        }

        ArrayList<Double[]> exposCoefs = new ArrayList<>();
        exposCoefs.add(exposArray);
        exposCoefs.add(coefsArray); // Create an array list to hold the double[] array, expos[0] and coefs[1] of the
                                    // array list of Doubles

        return exposCoefs; // Return the Double[] ArrayList object, where index 0 is the exponents, and
                           // index 1 is the coefficents
    }

    /**
     * Compares two polynomials.
     * If poly with different highest order exponents,
     * one with highest exponent is greatest
     * 
     * If highest exponents ==,
     * their coefficients are compared
     * 
     * if highest exponents ==,
     * and coefficients ==,
     * then next highest exponent is examined, and so on
     * 
     * @return returns an int, -1 means this polynomial is less than the other,
     *         0 means this polynomial is equal to the other
     *         1 means this polynomial is greater than the other
     */
    @Override
    public int compareTo(Polynomial other) {
        // use expoCoefOf to get an array list of double arrays containing index 0 ->
        // expos; index 1 -> coefs
        ArrayList<Double[]> thisPolynomial = expoCoefOf(this);
        ArrayList<Double[]> otherPolynomial = expoCoefOf(other);

        // Split into expos and coefs Double[] arrays
        Double[] thisExpos = thisPolynomial.get(0);
        Double[] thisCoefs = thisPolynomial.get(1);

        Double[] otherExpos = otherPolynomial.get(0);
        Double[] otherCoefs = otherPolynomial.get(1);

        if (thisExpos.length > otherExpos.length) {
            for (int i = 0; i < otherExpos.length; i++) { // Need to use the smaller of the two comparisions for an
                                                          // index
                Double thisExpo = thisExpos[i];
                Double otherExpo = otherExpos[i];

                if (thisExpo.compareTo(otherExpo) > 0) {
                    return 1;
                }

                if (thisExpo.compareTo(otherExpo) == 0) { // Already determined that this polynomial has more exponents
                                                          // than the other, and if the other polynomial has less total
                                                          // exponents and the first term is equal to this polynomial,
                                                          // then this polynomial has to be greater than
                    return 1;
                }

                if (thisExpo.compareTo(otherExpo) < 0) { // This polynomial may have more exponents than the other, but,
                                                         // the other polynomial's first term is of a higher power,
                                                         // thus, the other polynomial is greater in value
                    return -1;
                }
            }
        }

        if (thisExpos.length == otherExpos.length) { // This means we must evaluate based on the values of the exponents
                                                     // instead of how many exponents there are
            for (int i = 0; i < thisExpos.length; i++) {
                Double thisExpo = thisExpos[i];
                Double otherExpo = otherExpos[i];
                if (thisExpo.compareTo(otherExpo) > 0) {
                    return 1; // this exponent is greater than the other
                }

                if (thisExpo.compareTo(otherExpo) == 0) {
                    Double thisCoef = thisCoefs[i];
                    Double otherCoef = otherCoefs[i];

                    if (thisCoef.compareTo(otherCoef) > 0) {
                        return 1; // this coefficent is greater than the other
                    }

                    if (thisCoef.compareTo(otherCoef) == 0) {
                        continue; // this coefficent is the same, must continue on to the next exponent to evluate
                                  // further
                    }

                    if (thisCoef.compareTo(otherCoef) < 0) {
                        return -1; // this coefficent is less than the other
                    }
                }

                if (thisExpo.compareTo(otherExpo) < 0) {
                    return -1;
                }
            }
        }

        if (thisExpos.length < otherExpos.length) {
            for (int i = 0; i < thisExpos.length; i++) {
                Double thisExpo = thisExpos[i];
                Double otherExpo = otherExpos[i];

                if (thisExpo.compareTo(otherExpo) > 0) { // Despite the other polynomial have more exponents, the first
                                                         // exponential power of this polynomial is greater, thus, the
                                                         // whole polynomial is greater than the other
                    return 1;
                }

                if (thisExpo.compareTo(otherExpo) == 0) { // The first exponents are equal, and the other polynomial has
                                                          // more exponents, thus, the other polynomial is greater
                    return -1;
                }

                if (thisExpo.compareTo(otherExpo) < 0) {
                    return -1;
                }
            }
        }

        return 2; // Return 2 is an error finder, the above conditionals SHOULD pass as it
                  // accounts for all natrual numbers. If it does not, then something wrong
                  // happened.

    }

    /**
     * converts a polynomial to a string. 0 coefficient terms are omitted.
     * If the double value is a negative, that means the term must be subtracted
     * instead of addition
     * --- Example ---
     * Linked List: 5.6 -> 3.0 -> 4.0 -> 1.0 -> 8.3 -> 0.0
     * String: "5.6x^3 + 4x + 8.3"
     * 
     * --- Example of Negation ---
     * Linked List: 5.6 -> 3.0 -> -4.0 -> 1.0 -> 8.3 -> 0.0
     * String: "5.6x^3 - 4x + 8.3"
     * 
     * 
     * Exponents Rules: for anything > 1, we have to append x^{exponent}
     * for anyting = 1, we have to append x
     * for anything < 1, append nothing to the coefficent
     * 
     * Order of the array MATTERS, exponents[0] and coefficents[0] should be
     * together, with exponents[0] appended to coefficents[0], and continued until
     * the length of the exponents have been reached
     */
    @Override
    public String toString() {
        ArrayList<Double[]> polynomialValues = expoCoefOf(this);

        Double[] exponents = polynomialValues.get(0);
        Double[] coefficents = polynomialValues.get(1);

        StringBuilder str = new StringBuilder();

        Boolean firstLoopFlag = true;
        for (int i = 0; i < exponents.length; i++) {
            Double exponent = exponents[i];
            Double coefficent = coefficents[i];

            if (exponent > 1) { // { sign }{coefficent}x^{exponent}
                if (coefficent > 0) {
                    if (i == 0) {
                        str.append(coefficent + "x^" + String.format("%.0f", exponent)); // at beginning of loop and
                                                                                         // coefficent postive, remove
                                                                                         // redundent sign
                    } else {
                        str.append(" + " + coefficent + "x^" + String.format("%.0f", exponent));
                    }
                }
                if (coefficent < 0) {
                    if (i == 0) {
                        str.append(coefficent + "x^" + String.format("%.0f", exponent)); // at beginning of loop and
                                                                                         // coefficent negative,
                        // reformat negative sign without whitespace
                        // chars
                    } else {
                        str.append(" - " + (coefficent * -1) + "x^" + String.format("%.0f", exponent));
                    }
                }
                if (firstLoopFlag) {
                    if (coefficent == 0 && this.startsWithExpo) { // + x^{exponent}
                        str.append("x^" + String.format("%.0f", exponent));
                    }
                    firstLoopFlag = false;
                } else {
                    if (coefficent == 0) { // + x^{exponent}
                        str.append(" + x^" + String.format("%.0f", exponent));
                    }
                }
            }

            if (exponent == 1) { // { sign }{coefficent}x
                if (coefficent > 0) {
                    if (i == 0) {
                        str.append(coefficent + "x");
                    } else {
                        str.append(" + " + coefficent + "x");
                    }
                }
                if (coefficent < 0) {
                    if (i == 0) {
                        str.append(coefficent + "x");
                    } else {
                        str.append(" - " + (coefficent * -1) + "x");
                    }
                }
                if (coefficent == 0) { // + x
                    str.append(" + x");
                }
            }

            if (exponent < 1) { // { sign }{coefficent}
                if (coefficent > 0) {
                    str.append(" + " + coefficent);
                }
                if (coefficent < 0) {
                    str.append(" - " + (coefficent * -1));
                }
                if (coefficent == 0) { // Nothing to represent
                    str.append("");
                }
            }
            firstLoopFlag = false;
        }

        return str.toString();
    }

    // --- USED FOR DEBUGGING ---
    // public static void main(String[] args) {
    // // Polynomial poly = new Polynomial("5.6x^3 + 4x + 2");
    // Polynomial poly = new Polynomial("x^2 + 3");
    // ArrayList<Double[]> polyArrayList = poly.expoCoefOf(poly);
    // Double[] exponents = polyArrayList.get(0);
    // Double[] coefficents = polyArrayList.get(1);

    // for (Double value : exponents) {
    // System.out.println(value);
    // }
    // System.out.println("-----------");
    // for (Double value : coefficents) {
    // System.out.println(value);
    // }

    // System.out.println("// // //");
    // System.out.println(poly.toString());

    // System.out.println("// // //");
    // // System.out.println(poly2.compareTo(poly));
    // }

}