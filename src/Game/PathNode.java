package Game;

public class PathNode implements Comparable<PathNode>{
    private Grid stateData;
    private PathNode parentNode;
    private int g, h, f, depth; //g 从起点移动到指定方格的移动代价, h 从指定的方格移动到终点的估算成本

    public PathNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(PathNode parentNode) {
        this.parentNode = parentNode;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Grid getStateData() {
        return stateData;
    }

    public void setStateData(Grid stateData) {
        this.stateData = stateData;
    }

    public PathNode(Grid stateData, PathNode parentNode, int g, int h, int depth) {
        this.stateData = stateData;
        this.parentNode = parentNode;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
        this.depth = depth;
    }

    @Override
    public int compareTo(PathNode other)
    {
        int NodeComp = (this.f - other.getF()) * -1;
        if (NodeComp == 0)
        {
            NodeComp = (this.depth - other.getDepth());
        }
        return NodeComp;
    }
}
