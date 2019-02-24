package server.network.relatednesstree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RelatednessTreeNode {
    private Set<RelatednessTreeNode> subfields;
    private String field;
    private Integer depth;

    public RelatednessTreeNode(String field) {
        this.field = field;
        this.subfields = new HashSet<>();
        this.depth = 0;
    }

    public RelatednessTreeNode(String field, Set<RelatednessTreeNode> children) {
        this.field = field;
        this.subfields = children;
        this.depth = 0;
    }

    public int getDepth() {
        return this.depth;
    }

    public Set<RelatednessTreeNode> getSubfields() {
        return this.subfields;
    }

    public String getField() {
        return this.field;
    }

    public void addSubtree(RelatednessTreeNode node) {
        incrementDepths(node);
        this.subfields.add(node);
    }

    private void incrementDepths(RelatednessTreeNode node) {
        node.depth++;

        for (RelatednessTreeNode adj : node.getSubfields()) {
            incrementDepths(adj);
        }
    }
    public RelatednessTreeNode addField(String field) {
        RelatednessTreeNode node = new RelatednessTreeNode(field);
        node.depth = this.depth + 1;
        subfields.add(node);
        return node;
    }
}
