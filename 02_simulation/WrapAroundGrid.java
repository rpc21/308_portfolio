import java.util.ArrayList;
import java.util.List;

/**
 * The WrapAroundGrid lets cells on the edges have neighbors that wrap around to the other side of the grid.  See
 * BasicGrid for why I thought this is good design and decided to include it in the masterpiece
 */
public class WrapAroundGrid extends BasicGrid {

    /**
     * Constructor for the wrap around grid
     * @param rows number of rows in the grid
     * @param cols number of columns in the grid
     */
    public WrapAroundGrid(int rows, int cols){
        super(rows, cols);
    }

    /**
     * Get the valid neighbors of a cell applying the wrap around rules
     * @param loc the location of the cell whose neighbors you want
     * @param deltaRow used with deltaCol to specify the relative positions of possible neighbors
     * @param deltaCol used with deltaRow to specify the relative positions of possible neighbors
     * @return valid neighbors of a cell using wrap around grid
     */
    @Override
    public List<Location> getValidNeighbors(Location loc, int[] deltaRow, int[] deltaCol) {
        List<Location> validNeighbors = new ArrayList<>();
        for (int i = 0; i<deltaCol.length; i++){
            int rowToCheck = (loc.getRow() + deltaRow[i] + myNumRows) % myNumRows;
            int colToCheck = (loc.getCol() + deltaCol[i] + myNumCols) % myNumCols;
            validNeighbors.add(new Location(rowToCheck, colToCheck));
        }
        return validNeighbors;
    }

    /**
     * Get the valid neighbors if passed a neighbors definition
     * @param loc location to check
     * @param neighborsDefinitions the neighbors to check
     * @return valid neighbors applying the wrap around grid rules
     */
    @Override
    public List<Location> getValidNeighbors(Location loc, NeighborsDefinitions neighborsDefinitions) {
        return getValidNeighbors(loc, neighborsDefinitions.getDeltaRow(), neighborsDefinitions.getDeltaCol());
    }
}
