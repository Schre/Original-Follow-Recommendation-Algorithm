package server.network.relatednesstree;

import java.util.*;


public class RelatednessTreeNode {
    private HashMap<String,RelatednessTreeNode> subfields;
    private String field;
    private Integer depth;
    private int height;

    public RelatednessTreeNode(String field) {
        this.field = field;
        this.subfields = new HashMap<>();
        this.depth = 0;
        this.height = -1;
    }


    public int getDepth() {
        return this.depth;
    }

    public int getHeight() {
        return this.height;
    }

    public Map<String,RelatednessTreeNode> getSubfields() {
        return this.subfields;
    }

    public String getField() {
        return this.field;
    }

    public void addSubtree(RelatednessTreeNode node) {
        /*if (this.getField().equals("computer science")) {
            System.out.print("cs");
        }*/
        this.subfields.put(node.getField(), node);
        //calculateHeights();
        incrementDepths(node);
    }

    public int calculateHeights() {
        return calculateHeights(this);
    }

    private int calculateHeights(RelatednessTreeNode node) {
        int max = 0;

        for (RelatednessTreeNode adj : node.getSubfields().values()) {
            int adjHeight = calculateHeights(adj);
            max = Math.max(max, adjHeight + 1);
        }
        node.height = max;
        return node.height;
    }
    private void incrementDepths(RelatednessTreeNode node) {
        node.depth++;;

        for (RelatednessTreeNode adj : node.getSubfields().values()) {
            incrementDepths(adj);
        }
    }
    public RelatednessTreeNode addField(String field) {
        RelatednessTreeNode node = new RelatednessTreeNode(field);
        node.depth = this.depth + 1;
        subfields.put(node.getField(), node);
        return node;
    }
}
