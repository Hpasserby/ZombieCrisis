package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Pathfinder{
    private List<PathNode> openNodes;
    private List<PathNode> closedNodes;
    private List<PathNode> nodesToGoal;
    private List<Grid> pathToGoal;
    private WorldGrids worldGrids;
    private int depth;
    private GameObject object;
    private List<Grid> gridsOfObject;
    private int centre;

    public Pathfinder(WorldGrids worldGrids, GameObject object) {
        this.worldGrids = worldGrids;
        this.object = object;
        this.pathToGoal = new ArrayList<>();
        this.nodesToGoal = new ArrayList<>();
        this.gridsOfObject = worldGrids.getGrid(object);
        Grid tmp = this.worldGrids.getGrid(object.getX(), object.getY());
        for(int i = 0; i < gridsOfObject.size(); i++){
            if(gridsOfObject.get(i).equals(tmp)){
                centre = i;
                break;
            }
        }
    }

    public List<PathNode> getOpenNodes() {
        return openNodes;
    }

    public void setOpenNodes(List<PathNode> openNodes) {
        this.openNodes = openNodes;
    }

    public List<PathNode> getClosedNodes() {
        return closedNodes;
    }

    public void setClosedNodes(List<PathNode> closedNodes) {
        this.closedNodes = closedNodes;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<PathNode> getNodesToGoal() {
        return nodesToGoal;
    }

    public void setNodesToGoal(List<PathNode> nodesToGoal) {
        this.nodesToGoal = nodesToGoal;
    }

    public List<Grid> getPathToGoal() {
        return pathToGoal;
    }

    public void setPathToGoal(List<Grid> pathToGoal) {
        this.pathToGoal = pathToGoal;
    }

    public WorldGrids getWorldGrids() {
        return worldGrids;
    }

    public boolean ownGrid(Grid currentGrid, Grid otherGird){
        int deltaX = currentGrid.getGridX() - getCentreGrid().getGridX();
        int deltaY = currentGrid.getGridY() - getCentreGrid().getGridY();
        for(Grid grid : gridsOfObject){
            Grid tmp = worldGrids.get(grid.getGridX() + deltaX, grid.getGridY() + deltaY);
            if(tmp.equals(otherGird)){
                return true;
            }
        }
        return false;
    }

    public List<Grid> nextMoves(Grid grid){
        List<Grid> moves = new ArrayList<>();
        int[] x = {0, 0, -1, 1, -1, -1, 1, 1};
        int[] y = {-1, 1, 0, 0, -1, 1, 1, -1};
        int deltaX = grid.getGridX() - getCentreGrid().getGridX();
        int deltaY = grid.getGridY() - getCentreGrid().getGridY();
        for(int i = 0; i < 8; i++){
            boolean flag = true;
            for(int j = 0; j < gridsOfObject.size(); j++){
                int nextX = gridsOfObject.get(j).getGridX() + deltaX + x[i];
                int nextY = gridsOfObject.get(j).getGridY() + deltaY + y[i];
                Grid next = worldGrids.get(nextX, nextY);
                if(!ownGrid(grid, next) && !next.isAccessible()){
                    flag = false;
                    break;
                }
            }
            if(flag){
                moves.add(worldGrids.get(grid.getGridX() + x[i], grid.getGridY() + y[i]));
            }
        }
        return moves;
    }

    public int getHeuristic(Grid currentPos, Grid goalPos){
        return (Math.abs(goalPos.getGridX() - currentPos.getGridX()) + Math.abs(goalPos.getGridY() - currentPos.getGridY())) * 10;
    }

    public int getCost(Grid currentPos, Grid goalPos){
        if(Math.abs(goalPos.getGridX() - currentPos.getGridX()) != 0 && Math.abs(goalPos.getGridY() - currentPos.getGridY()) != 0){
            return 14;
        } else {
            return 10;
        }
    }

    public int getDistance(int x1, int y1, int x2, int y2){
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return (int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    public Grid getCentreGrid(){
        return this.gridsOfObject.get(centre);
    }

    public List<Grid> shortestPath(Grid goalPos){
    	if(((Enemy)object).getTarget() == null) return null;
        worldGrids.updateGrids();

        Grid startPos = (Grid) getCentreGrid().clone();

        openNodes = new ArrayList<>();
        closedNodes = new ArrayList<>();
        depth = 0;
        boolean hasGoal = false;

        openNodes.add(new PathNode(startPos, null, 0, getHeuristic(startPos, goalPos), depth));

        while(openNodes.size() != 0){
            closedNodes.add(openNodes.get(openNodes.size() - 1));
            PathNode currentNode = closedNodes.get(closedNodes.size() - 1);
            Grid current = currentNode.getStateData();
            openNodes.remove(openNodes.size() - 1);

            int distance = getDistance(current.getX(), current.getY(), goalPos.getX(), goalPos.getY());
            int r = ((Enemy)object).getTarget().getRadius();
            if (distance < object.getRadius() + r + 10 || currentNode.getDepth() > 25) {
                hasGoal = true;
                break;
            }

            List<Grid> expanded = nextMoves(current);

            NodeLoop:
            for(int i = 0; (i < openNodes.size() || i < closedNodes.size()); i++){
                int s = expanded.size() - 1;
                while(s >= 0){
                    if(i < openNodes.size()){
                        Grid OpenstateData = openNodes.get(i).getStateData();
                        if(OpenstateData.equals(expanded.get(s))){
                            if((currentNode.getG() + getCost(current, OpenstateData)) < openNodes.get(i).getG()){
                                openNodes.get(i).setG(currentNode.getG() + getCost(current, OpenstateData));
                                openNodes.get(i).setH(getHeuristic(expanded.get(s), goalPos));
                                openNodes.get(i).setF(openNodes.get(i).getG() + openNodes.get(i).getH());
                                openNodes.get(i).setParentNode(currentNode);
                            }
                            expanded.remove(s);
                            if (expanded.isEmpty()) {
                                break NodeLoop;
                            }
                            s--;
                            continue ;
                        }
                    }
                    if(i < closedNodes.size()){
                        if (closedNodes.get(i).getStateData().equals(expanded.get(s))){
                            expanded.remove(s);
                            if (expanded.isEmpty())
                            {
                                break NodeLoop;
                            }
                        }
                    }
                    s--;
                }
            }
            if (!expanded.isEmpty()) {
                for (int i = 0; i < expanded.size(); i++) {
                    openNodes.add(new PathNode(
                            expanded.get(i),
                            currentNode,
                            currentNode.getG() + getCost(current, expanded.get(i)),
                            getHeuristic(expanded.get(i), goalPos),
                            currentNode.getDepth() + 1));
                }
            }
            Collections.sort(openNodes);
        }
        try {
            if (hasGoal) {
                int depth = closedNodes.get(closedNodes.size() - 1).getDepth();
                PathNode parent = closedNodes.get(closedNodes.size() - 1);

                for (int s = 0; s <= depth; s++) {
                    nodesToGoal.add(parent);
                    pathToGoal.add(parent.getStateData());
                    parent = nodesToGoal.get(s).getParentNode();
                }
                Collections.reverse(pathToGoal);
                return pathToGoal;
            }
            return null;
        } catch (NullPointerException e){
            if(pathToGoal != null) return pathToGoal;
            return null;
        }
    }
}
