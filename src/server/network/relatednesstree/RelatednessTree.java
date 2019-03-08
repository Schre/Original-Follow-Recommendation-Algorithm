package server.network.relatednesstree;


import com.mysema.commons.lang.Pair;

import java.util.*;

/***
 * This tree will have the root as the most generic field, and each
 * level deeper in the tree will represent a more specific field.
 *
 * Then, the relatedness will be:
 *
 *
 * CASE A: LCA(n1, n2) = n1
 *
 *  A1: If more generic to less generic, 1:
 *      example:    n1 from
 *                 /  \
 *                x    z
 *               /
 *              y
 *             /
 *             n2 to
 *
 *  A2: If less generic to more generic, Relatedness = 1/DistanceBetweenNodes(n2, n1):
 *  example:          n1 to
 *                   /  \
 *                  x    z
 *                 /
 *                y
 *               /
 *               n2 from
 *
 *  CASE B: LCA(lg, mg) != mg (fields diverge)
 *  Relatedness = DistanceBetweenNodes(n1, n2) * (divergent penalty)
 *
 *  example:
 *          x
 *         / \
 *        y   z
 *         \   \
 *          n1  n2
 *
 *          LCA(n1, n2) = x
 *          DistanceBetweenNodes(n1, n2) = 4
 *
 *          Relatedness = (1/4) * P, where P is the divergent penalty
 *
 *
 *
 *
 *
 *
 *
 * Have each node record their depth in the tree to make it easier to decipher between the two cases!
 *
 */
public class RelatednessTree {
    private static double downPenalty = .9;
    private static double upPenalty = .7;
    private static double heightPenalty = 1.0;


    private RelatednessTreeNode root;
    private Map<String, List<RelatednessTreeNode>> fields;
    private Map<RelatednessTreeNode, Map<RelatednessTreeNode,List<RelatednessTreeNode>>> pathDP;
    public RelatednessTree(RelatednessTreeNode root) {
        this.root = root;
        this.pathDP = new HashMap<>();
        this.root = new RelatednessTreeNode("undefined");
        this.fields = new HashMap<>();
    }

    public RelatednessTree() {
        this.root = new RelatednessTreeNode("undefined");
        this.fields = new HashMap<>();
        this.pathDP = new HashMap<>();
    }

    public void addField(RelatednessTreeNode node) {
        this.fields.putIfAbsent(node.getField(), new ArrayList<>());
        this.fields.get(node.getField()).add(node);
    }

    public void addFields(Map<String, List<RelatednessTreeNode>> fields) {
        this.fields.putAll(fields);
    }

    public Map<String, List<RelatednessTreeNode>> getFields() {
        return this.fields;
    }

    public double findRelatedness(RelatednessTreeNode n1, RelatednessTreeNode n2) {
        Pair<RelatednessTreeNode, Integer> lcaDist = LCADistance(n1, n2);
        RelatednessTreeNode lca =lcaDist.getFirst();
        double distance = lcaDist.getSecond();
        RelatednessTreeNode moreGeneric = null;
        RelatednessTreeNode lessGeneric = null;

        if (n1.getDepth() > n2.getDepth()) {
            moreGeneric = n2;
            lessGeneric = n1;
        }
        else {
            moreGeneric = n1;
            lessGeneric = n2;
        }
        if (n1.getField().equals("science") && n2.getField().equals("science")) {
            System.out.print("here");
        }
        // Case A:
        if (lca == moreGeneric || lca == lessGeneric) {
            // Going from moreGeneric to less generic
            if (n1 == moreGeneric) {
                return Math.pow(downPenalty, distance) * Math.pow(heightPenalty, n2.getHeight());
            } // Less generic to more generic
            else if (n1 == lessGeneric) {
                return Math.pow(upPenalty, distance) * Math.pow(heightPenalty, n2.getHeight());
            }
        }

        // Case B:
        int upMoves = n1.getDepth() - lca.getDepth();
        int downMoves = (int)distance - upMoves;
        return Math.pow(upPenalty, upMoves) * Math.pow(downPenalty, downMoves) * Math.pow(heightPenalty, n2.getHeight());
    }

    public RelatednessTreeNode getRoot() {
        return this.root;
    }
    public Pair<RelatednessTreeNode, Integer> LCADistance(RelatednessTreeNode n1, RelatednessTreeNode n2) {
        // TODO: Fix double counting
        /***
         * Paths always end at root! This guarantees that the paths have a least common ancestor, and that the paths
         * are not empty
         */
        List<RelatednessTreeNode> n1Path = getPathToNode(n1);
        List<RelatednessTreeNode> n2Path = getPathToNode(n2);

        int n1Indx = n1Path.size() - 1;
        int n2Indx = n2Path.size() - 1;

        while (n1Indx >= 0 && n2Indx >= 0 && n1Path.get(n1Indx) == n2Path.get(n2Indx)) {
            --n1Indx;
            --n2Indx;
        }

        ++n1Indx;
        ++n2Indx;


        /**
         * Example:
         * path1: [n1, y, x]
         * path2: [n2, z, x]
         *
         * n1Indx = 2
         * n2Indx = 2
         *
         * Add n1Index + n2Index
         */
        Pair<RelatednessTreeNode, Integer> ret = new Pair<>(n1Path.get(n1Indx), n1Indx + n2Indx);
        return ret;
    }

    public List<RelatednessTreeNode> getPathToNode(RelatednessTreeNode destination) {
        List<RelatednessTreeNode> ret = new ArrayList<>();
        getPathToNode(destination, this.root, ret);
        ret.add(this.root);
        return ret;
    }

    public boolean getPathToNode(RelatednessTreeNode destination, RelatednessTreeNode current, List<RelatednessTreeNode> path) {
        /* Base case */
        if (pathDP.containsKey(current) && pathDP.get(current).containsKey(destination)) {
            // pathDP has all of the paths reversed

            // Impossible to reach dest
            if (pathDP.get(current).get(destination) == null) {
                return false;
            }
            path.addAll(pathDP.get(current).get(destination));
            return true;
        }
        if (current == destination) {
            path.add(destination);
            return true;
        }

        for (RelatednessTreeNode adjacent : current.getSubfields().values()) {
            /* Found path */
            if (getPathToNode(destination, adjacent, path) == true) {
                // may have already been added
                if (!(path.get(path.size() - 1) == adjacent)) {
                    path.add(adjacent);
                }

                // cache this computation
                pathDP.putIfAbsent(adjacent, new HashMap<>());
                pathDP.get(adjacent).putIfAbsent(destination, new ArrayList<>(path));
                return true;
            }
            else {
                // cache this computation
                pathDP.putIfAbsent(adjacent, new HashMap<>());
                pathDP.get(adjacent).putIfAbsent(destination, null);
            }
        }
        return false;
    }

}
