package server.network;

import com.fasterxml.jackson.core.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.network.relatednesstree.RelatednessTree;
import server.network.relatednesstree.RelatednessTreeNode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

// TODO: Instead of having mutliple nodes with the same field, have single node with potentially multiple parents and calculate LCA that minimizes distance
// TODO: Alternatively, if have multiple nodes that have same field, make their subtrees identical. Wastes space, but easier to implement for now.

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

   private static void copyFromSubtree(RelatednessTreeNode from, RelatednessTreeNode to) {
        for (RelatednessTreeNode node : from.getSubfields().values()) {
            // Copy subtree
            if (!to.getSubfields().containsKey(node.getField())) {
                RelatednessTreeNode cpy = new RelatednessTreeNode(node.getField());
                copyFromSubtree(node, cpy);
                to.addSubtree(cpy);
            }
        }
    }

    public static void loadTreeFromJSON(RelatednessTree tree, RelatednessTreeNode root, JSONObject json) {
        for (Object key : json.keySet()) {
            String field = key.toString();
            JSONObject children = (JSONObject) json.get(field);
            RelatednessTreeNode node = new RelatednessTreeNode(field);
            loadTreeFromJSON(tree, node, children);
            root.addSubtree(node);
            tree.addField(node);

            RelatednessTreeNode dup = null;
            // if multiple nodes with same field, make sure they have equivalent stucture
            if (tree.getFields().containsKey(field) && tree.getFields().get(field).size() > 1) {
                dup = tree.getFields().get(field).get(0);
                // copy differences into dup
                copyFromSubtree(node, dup);
                // update structure of all trees
                for (RelatednessTreeNode toUpdate : tree.getFields().get(field)) {
                    copyFromSubtree(dup, toUpdate);
                }
            }
        }
    }
    public static void initialize() {
        RelatednessTree relatednessTree = new RelatednessTree();
        RelatednessTreeNode root = relatednessTree.getRoot();

        /* Get fields from json file */
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("src/server/network/relatednessTree.json");

            Object obj = parser.parse(reader);
            JSONObject json = (JSONObject) obj;
            loadTreeFromJSON(relatednessTree, root, json);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        root.calculateHeights();
        /* Hard coded for now */
        RelatednessMatrix.matrix = new HashMap<>();
        for (String field : relatednessTree.getFields().keySet()) {
            addField(field);
        }
        // compute all matrix values
        for (String f1 : relatednessTree.getFields().keySet()) {
            for (String f2: relatednessTree.getFields().keySet()) {
                double bestScore = 0;
                RelatednessTreeNode first = null;
                RelatednessTreeNode second = null;

                /* Find best score amongst all pairs */
                for (int i = 0; i < relatednessTree.getFields().get(f1).size(); ++i) {
                    for (int j = 0; j < relatednessTree.getFields().get(f2).size(); ++j) {
                        RelatednessTreeNode n1 = relatednessTree.getFields().get(f1).get(i);
                        RelatednessTreeNode n2 = relatednessTree.getFields().get(f2).get(j);

                        double score = relatednessTree.findRelatedness(n1, n2);
                        if (score > bestScore) {
                            bestScore = score;
                            first = n1;
                            second = n2;
                        }
                    }
                }
                System.out.println(first.getField() + ", " + second.getField() + " -> " + bestScore);
                addRelation(first.getField(), second.getField(), bestScore);
            }
        }

        /***
            Add exceptions to algorithm here
         ***/

        System.out.println("Created matrix");
    }

    public static Set<String> getSupportedFields() {
        return matrix.keySet();
    }

    public static double getRelatedness(String a, String b) throws InvalidParameterException {
        if (!matrix.containsKey(a) || !matrix.containsKey(b)) {
            throw new InvalidParameterException();
        }

        return matrix.get(a).get(b);
    }
}
