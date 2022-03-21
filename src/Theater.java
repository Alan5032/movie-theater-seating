import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Theater represents the seating availability of a movie theater.
 */
public class Theater {

    private final int columns;  // The number of columns in the theater's seating arrangement
    private final int buffer;  // The horizontal buffer space required between reservation groups
    // List to rank the seating preference of each row. List[0] = row of preference 1, ..., List[preferences.size() - 1]
    // = row of last preference
    private final List<Integer> preferences;
    // Map to store the first column with an available seat for each row
    private final Map<Integer, Integer> firstAvailableColumns;
    private int availableSeats;  // Number of available seats remaining the theater

    /**
     * Constructs a new Theater with all empty seats
     * @param rows the number of rows in the seating arrangement
     * @param columns the number of columns in the seating arrangement
     * @param buffer the horizontal buffer space required between reservation groups
     */
    public Theater(int rows, int columns, int buffer) {
        this.columns = columns;
        this.buffer = buffer;
        preferences = new ArrayList<>();
        firstAvailableColumns = new HashMap<>();
        availableSeats = rows * Theater.this.columns;
        // Fill out preferences assuming people prefer the middle row the most, then the row behind the middle row, ...,
        // then the farthest row back, then the starting row, then the row behind the starting row, ..., then finally
        // the row before the middle row
        for (int i = rows / 2; i < rows; i++) {
            preferences.add(i);
            firstAvailableColumns.put(i, 0);
        }
        for (int i = 0; i < rows / 2; i++) {
            preferences.add(i);
            firstAvailableColumns.put(i, 0);
        }
    }

    /**
     * Returns the assigned seats for the people in a reservation
     * @param people the number of people in a reservation
     * @return a list of assigned seating spots. Returns null if the reservation can't be accommodated for.
     */
    public List<Integer[]> getSeats(int people) {
        List<Integer[]> allSelectedSeats = new ArrayList<>();
        // If there are not enough available seats for the reservation
        if (people > availableSeats) {
            return null;
        }
        int row = findFirstAvailableRow(people);
        // If there is a single row that can accommodate all the people in a reservation
        if (row != -1) {
            fillSingleRow(allSelectedSeats, people, row);
            // If there is no single row that can accommodate all the people in a reservation
        } else {
            fillMultipleRows(allSelectedSeats, people);
        }
        return allSelectedSeats;
    }

    /**
     * Returns the most preferred row that can accommodate a reservation
     * @param people the number of people in a reservation
     * @return the most preferred row that can accommodate a reservation. Returns -1 if there is no single row that can
     * do so.
     */
    private int findFirstAvailableRow(int people) {
        for (int row : preferences) {
            if (firstAvailableColumns.get(row) + people <= columns) {
                return row;
            }
        }
        return -1;
    }

    /**
     * Updates the number of available seats remaining after seats have been assigned
     * @param initialAvailableColumn the column of the first seat assigned to a group of people
     * @param row the row of the seats assigned to a group of people
     */
    private void updateAvailableSeats(int initialAvailableColumn, int row) {
        if (firstAvailableColumns.get(row) >= columns) {
            availableSeats -= columns - initialAvailableColumn;
        } else {
            availableSeats -= firstAvailableColumns.get(row) - initialAvailableColumn;
        }
    }

    /**
     * Assigns a group of people to theater seats on a single row
     * @param allSelectedSeats list containing all the seat assignments for a reservation
     * @param people the number of people to assign seats to
     * @param row the row containing the seats to be assigned
     */
    private void fillSingleRow(List<Integer[]> allSelectedSeats, int people, int row) {
        int firstAvailableColumn = firstAvailableColumns.get(row);
        // Assign seats and update the first available column for the row
        firstAvailableColumns.put(row, firstAvailableColumn + people + buffer);
        // Update the number of available seats remaining
        updateAvailableSeats(firstAvailableColumn, row);
        // Create a list to document the assigned spots
        // Selected[0] = row of seats
        // Selected[1] = starting column of assigned seats, inclusive
        // Selected[2] = ending column of assigned seats, inclusive
        Integer[] selectedSeats = {row, firstAvailableColumn, firstAvailableColumn + people - 1};
        allSelectedSeats.add(selectedSeats);
    }

    /**
     * Assigns a group of people to theater seats on multiple rows
     * @param allSelectedSeats list containing all the seat assignments for a reservation
     * @param people the number of people to assign seats to
     */
    private void fillMultipleRows(List<Integer[]> allSelectedSeats, int people) {
        // Assign seats until there are 0 people to assign seats to
        while (people != 0) {
            // Iterate through all the rows of seats in preference order
            for (int row : preferences) {
                // If there are available seats in the row
                if (firstAvailableColumns.get(row) < columns) {
                    // Assign as many seats in the row as possible
                    int firstAvailableColumn = firstAvailableColumns.get(row);
                    int filledSeats = 0;
                    for (int j = firstAvailableColumn; j < firstAvailableColumn + people; j++) {
                        if (j >= columns) {
                            break;
                        }
                        filledSeats++;
                    }
                    // Update the first available column for the row
                    firstAvailableColumns.put(row, firstAvailableColumn + filledSeats + buffer);
                    // Update the number of available seats remaining
                    updateAvailableSeats(firstAvailableColumn, row);
                    // Create a list to document the assigned spots
                    // Selected[0] = row of seats
                    // Selected[1] = starting column of assigned seats, inclusive
                    // Selected[2] = ending column of assigned seats, inclusive
                    Integer[] selectedSeats = {row, firstAvailableColumn, firstAvailableColumn + filledSeats - 1};
                    allSelectedSeats.add(selectedSeats);
                    // Update the remaining number of people that still need to be assigned
                    people -= filledSeats;
                }
            }
        }
    }
}
