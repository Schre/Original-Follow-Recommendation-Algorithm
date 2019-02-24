package server.network;

import server.network.relatednesstree.RelatednessTree;
import server.network.relatednesstree.RelatednessTreeNode;

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

    private static void addField(String field) {
        matrix.put(field, new HashMap<>());
    }

    /***
     * Assume symmetric matrix for now:
     * I.e., if A->B = v,
     *          B->A = v also
     */
    private static void addRelation(String first, String second, double value) {
        matrix.get(first).put(second, value);
        //matrix.get(second).put(first, value);
    }
    public static void initialize() {
        RelatednessTree relatednessTree = new RelatednessTree();
        RelatednessTreeNode root = relatednessTree.getRoot();
        Map<String, RelatednessTreeNode> fields = new HashMap<>();


        /* Hard coded for now */
        RelatednessTreeNode Scientist = new RelatednessTreeNode("Scientist");
        RelatednessTreeNode Engineer = new RelatednessTreeNode("Engineer");
        Scientist.addField("Computer Scientist");
        Scientist.addField("Biologist");
        Engineer.addField("Bio Engineer");
        Engineer.addField("Computer Engineer");

        root.addSubtree(Scientist);
        root.addSubtree(Engineer);

        fields.put(Scientist.getField(), Scientist);
        fields.put(Engineer.getField(), Engineer);

        for (RelatednessTreeNode node : Scientist
                .getSubfields()) {
            fields.put(node.getField(), node);
        }
        for (RelatednessTreeNode node : Engineer.getSubfields()) {
            fields.put(node.getField(), node);
        }


        relatednessTree.addFields(fields);

        RelatednessMatrix.matrix = new HashMap<>();
        for (String field : relatednessTree.getFields().keySet()) {
            addField(field);
        }
        // compute all matrix values
        for (RelatednessTreeNode n1 : relatednessTree.getFields().values()) {
            for (RelatednessTreeNode n2: relatednessTree.getFields().values()) {
                double score = relatednessTree.findRelatedness(n1, n2);
                System.out.println(n1.getField() + ", " + n2.getField() + " -> " + score);
                addRelation(n1.getField(), n2.getField(), score);
            }
        }

 /*       *//*RelatednessMatrix.matrix = new HashMap<>();

        /* Add fields first *//*
        addField("Scientist");
        addField("Engineer");
        addField("Computer Scientist");
        addField("Computer Engineer");
        addField("Biologist");
        addField("Bio Engineer");

        *//***
         * Scientist relationships
         *//*
        addRelation("Scientist", "Engineer", .5);
        addRelation("Scientist", "Computer Scientist", .6);
        addRelation("Scientist", "Computer Engineer", .3);
        addRelation("Scientist", "Biologist", .5);
        addRelation("Scientist", "Bio Engineer", .4);


        *//***
         * Engineer relationships
         *//*
        addRelation("Engineer", "Computer Scientist", .5);
        addRelation("Engineer", "Computer Engineer", .7);
        addRelation("Engineer", "Biologist", .2);
        addRelation("Engineer", "Bio Engineer", .7);

        *//***
         * Computer Scientist relationships
         *//*
        addRelation("Computer Scientist", "Computer Engineer", .8);
        addRelation("Computer Scientist", "Biologist", .1);
        addRelation("Computer Scientist", "Bio Engineer", .4);

        *//***
         * Computer Engineer relationships
         *//*
        addRelation("Computer Engineer", "Biologist", .1);
        addRelation("Computer Engineer", "Bio Engineer", .5);

        *//***
         * Biologist relationships
         *//*
        addRelation("Biologist", "Bio Engineer", .7);




        *//*** Matrix pictorial interpretation:
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
            throw new InvalidParameterException();
        }

        if (a.equals(b)) {
            return 1.0;
        }

        return matrix.get(a).get(b);
    }
}
