/**
 *  * Project 2
 * 
 * @author Christopher Stoner
 * 
 * This OrderedList class determines how the polynomials are sorted from the given input.
 * Contains 2 overloaded classes, on without a Comparator object, one with. (Strong sorted order)
 * The first helps compare with the polynomial's internal comparer that compares coefficents and exponents
 * The second compares with the comparator class that has overridden methods in PolynomialFileChooser,
 * this should allow for comparing of the exponents ONLY. (Weak sorted order)
 */

package Project2;

import java.util.*;

public class OrderedList {

    public static Boolean checkSorted(ArrayList<Polynomial> list) {
        int result;
        for (int i = 1; i < list.size(); i++) {
            result = list.get(i - 1).compareTo(list.get(i));

            if (result < 0) {
                return false;
            }
        }
        return true;
    }

    public static Boolean checkSorted(ArrayList<Polynomial> list, Comparator<Polynomial> comparator) {
        int result;
        for (int i = 1; i < list.size(); i++) {
            result = comparator.compare(list.get(i - 1), list.get(i));

            if (result < 0) {
                return false;
            }
        }
        return true;
    }
}
