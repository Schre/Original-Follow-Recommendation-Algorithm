package server.network;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/***
 * These matrix values are hard-coded for now
 * In the future these should be read in from
 * a configuration file.
 */
public class RelatednessMatrix {
    public static Map<String, Map<String, Double>> matrix;

    public static void initialize() {
        RelatednessMatrix.matrix = new HashMap<>();

        /* Add fields first */
        matrix.put("Scientist", new HashMap<>());
        matrix.put("Engineer", new HashMap<>());

        /* Add relatedness */
        matrix.get("Scientist").put("Engineer", .5);
        matrix.get("Engineer").put("Scientist", .5);
        matrix.get("Scientist").put("Scientist", 1.0);
        matrix.get("Engineer").put("Engineer", 1.0);

        /*** Matrix pictorial interpretation:
         *            Scientist   Engineer
         *
         *  Scientist     1          .5
         *
         *  Engineer      .5          1
         *
         ***/
    }

    public static Set<String> getSupportedFields() {
        return matrix.keySet();
    }

    public static double getRelatedness(String a, String b) throws InvalidParameterException {
        if (!matrix.containsKey(a) || !matrix.containsKey(b)) {
            throw new InvalidParameterException("Either the field " + a + " or " + b + " does not exist!");
        }
        return matrix.get(a).get(b);
    }
}
