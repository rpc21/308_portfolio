import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The BasicGrid implements the Grid interface, implementing the functionality that all grids should have. In the
 * BasicGrid, cells cannot wrap around to the other side of the grid.  The BasicGrid has an underlying structure of a
 * 2-D array of Cell objects.
 * I think the BasicGrid and WrapAroundGrid are good design because they provide a lot of functionality for cells to
 * be able to get their neighbors and put themselves in the grid and remove themselves from the grid.  Also, a lot of
 * functionality is reused between the classes showing how easy it was to add a new type of grid.  The grid classes
 * also show how easily new neighbors can be integrated proving the flexibility and extendability of working with new
 * neighbors using our grid classes.
 */
public class BasicGrid implements Grid{

    // ADJACENT_COL and ADJACENT_ROW work together to specify the four adjacent neighbors to a grid location
    private static final int[] ADJACENT_COL = {0, 0, -1, 1};
    private static final int[] ADJACENT_ROW = {-1, 1, 0, 0};

    protected Cell[][] myGrid;
    protected int myNumRows;
    protected int myNumCols;

    /**
     * Constructor for a basic grid initializes an empty 2-D array of Cells of size as specified by the numRows and
     * numCols parameters passed to the constructor
     * @param numRows is the number of rows in the grid to be created
     * @param numCols is the number of columns in the grid to be created
     */
    public BasicGrid(int numRows, int numCols){
        myGrid = new Cell[numRows][numCols];
        myNumRows = numRows;
        myNumCols = numCols;
    }

    /**
     * Get all the cells in the grid in List so it is easy to iterate over the grid.  This method is used to access
     * the grid and hides the underlying implementation of the grid (2-D array) from other classes
     * @return a List of all the Cells in the grid
     */
    @Override
    public List<Cell> getCells() {
        List<Cell> returnList = new ArrayList<>();
        for (Cell[] rowOfCells : myGrid) {
            returnList.addAll(Arrays.asList(rowOfCells));
        }
        return returnList;
    }

    /**
     * Getter for the number of rows in the grid
     * @return the number of rows in the grid
     */
    @Override
    public int getNumRows() {
        return myGrid.length;
    }

    /**
     * Getter for the number of columns in the grid
     * @return the number of columns in the grid
     */
    @Override
    public int getNumCols() {
        try{
            return myGrid[0].length;
        } catch(ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }

    /**
     * Checks whether a location is valid in this grid.  For a BasicGrid the location must be within the bounds of
     * the number of rows and number of columns in the grid
     * @param loc the location to check
     * @return true if the location is valid in the grid and false if it is not
     */
    @Override
    public boolean isValid(Location loc) {
        return loc.getCol() >= 0 && loc.getCol() < myNumCols && loc.getRow() >= 0 && loc.getRow() < myNumRows;
    }

    /**
     * Puts a Cell into a specified location in the grid
     * @param loc the location at which to put the object
     * @param obj the new object to be added
     */
    @Override
    public void put(Location loc, Cell obj) throws ArrayIndexOutOfBoundsException {
        if (isValid(loc)){
            myGrid[loc.getRow()][loc.getCol()] = obj;
        }
        else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * If the location in the grid is valid, it will be removed and replaced with an empty cells
     * @param loc the location of the object that is to be removed
     */
    @Override
    public void remove(Location loc) throws ArrayIndexOutOfBoundsException {
        if (isValid(loc)) {
            myGrid[loc.getRow()][loc.getCol()] = new EmptyCell(loc);
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Get the Cell object in the specified location in the grid
     * @param loc a location in this grid
     * @return Cell at the Location loc
     */
    @Override
    public Cell get(Location loc) throws ArrayIndexOutOfBoundsException{
        if (isValid(loc)) {
            return myGrid[loc.getRow()][loc.getCol()];
        }
        else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * A list of the locations currently occupied by cells
     * Does NOT return empty cells
     * @return a list of the locations currently occupied by cells
     */
    @Override
    public List<Location> getOccupiedLocations() {
        List<Location> occupiedLocations = new ArrayList<>();
        for (Cell cell : getCells()){
            if (!cell.isEmpty()){
                occupiedLocations.add(cell.getMyLocation());
            }
        }
        return occupiedLocations;
    }

    /**
     * A list of the locations currently not occupied by cells/occupied by empty cells
     * Only returns empty cells (OPEN for percolation)
     * @return a list of the locations currently occupied by empty cells
     */
    @Override
    public List<Location> getEmptyLocations() {
        List<Location> emptyLocations = new ArrayList<>();
        for (Cell cell : getCells()){
            if (cell.isEmpty()){
                emptyLocations.add(cell.getMyLocation());
            }
        }
        return emptyLocations;
    }

    /**
     * Returns the valid neighbors of a cell as determined by the rules of that simulation
     * Cells on the borders have fewer neighbors since a BasicGrid does not wrap around
     * @param loc the location of the cell whose neighbors you want
     * @param deltaRow used with deltaCol to specify the relative positions of possible neighbors
     * @param deltaCol used with deltaRow to specify the relative positions of possible neighbors
     * @return an List of the locations of valid neighbors
     */
    public List<Location> getValidNeighbors(Location loc, int[] deltaRow, int[] deltaCol) {
        int locRow = loc.getRow();
        int locCol = loc.getCol();
        List<Location> validLocations = new ArrayList<>();
        for (int i = 0; i < deltaCol.length; i++) {
            Location locationToBeChecked = new Location(locRow + deltaRow[i], locCol + deltaCol[i]);
            if (isValid(locationToBeChecked)) {
                validLocations.add(locationToBeChecked);
            }
        }
        return validLocations;
    }

    /**
     * Returns the valid neighbors of a location with give neighbors definitions
     * @param loc location to check
     * @param neighborsDefinitions the neighbors to check
     * @return list of valid neighbor locations
     */
    public List<Location> getValidNeighbors(Location loc, NeighborsDefinitions neighborsDefinitions){
        return getValidNeighbors(loc, neighborsDefinitions.getDeltaRow(), neighborsDefinitions.getDeltaCol());
    }

    /**
     * Get the adjacent locations to a specified location where the cell is marked as empty
     * @param loc a location in this grid
     * @return List of empty adjacent locations
     */
    @Override
    public List<Location> getEmptyAdjacentLocations(Location loc) {
        List<Location> emptyAdjacentLocations = new ArrayList<>();
        if (isValid(loc)) {
            for (Location location : getValidNeighbors(loc, ADJACENT_ROW, ADJACENT_COL)) {
                if (get(location).isEmpty()) {
                    emptyAdjacentLocations.add(location);
                }
            }
        }
        return emptyAdjacentLocations;
    }

    /**
     * Get the adjacent locations to a specified location where the cell is not marked as empty
     * @param loc a location in this grid
     * @return List of occupied adjacent locations
     */
    @Override
    public List<Location> getOccupiedAdjacentLocations(Location loc) {
        List<Location> occupiedAdjacentLocations = new ArrayList<>();
        if (isValid(loc)) {
            for (Location location : getValidNeighbors(loc, ADJACENT_ROW, ADJACENT_COL)) {
                if (!get(location).isEmpty()) {
                    occupiedAdjacentLocations.add(location);
                }
            }
        }
        return occupiedAdjacentLocations;
    }


    /**
     * Prints the grid row by row using the toStrings specified by each of the cells
     */
    @Override
    public void printGrid() {
        for (Cell[] row : myGrid) {
            StringBuilder rowOfCells = new StringBuilder();
            for (int j = 0; j < myGrid[0].length; j++) {
                rowOfCells.append(row[j]);
            }
            System.out.println(rowOfCells);
        }
    }
}
