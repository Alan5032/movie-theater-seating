import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final int NUM_ROWS = 10;  // The number of rows in the seating arrangement of a movie theater
    private static final int NUM_COLUMNS = 20;  // The number of columns in the seating arrange of a movie theater
    private static final int BUFFER = 3;  // The horizontal buffer space required between reservation groups
    private static final int UPPERCASE_A_ASCII = 65;  // The ASCII value of 'A'
    private static final int SIZE_INDEX = 1;  // Index of the size of a reservation
    private static final int IDENTIFIER_INDEX = 0;  // Index of the reservation identifier
    private static final int ROW_INDEX = 0;  // Index for row of assigned seats
    private static final int STARTING_COLUMN_INDEX = 1;  // Index for starting column of assigned seats
    private static final int ENDING_COLUMN_INDEX = 2;  // Index for ending column of assigned seats

    public static void main(String[] args) {
        Theater theater = new Theater(NUM_ROWS, NUM_COLUMNS, BUFFER);  // Theater for the reservations
        // Map integers to alphabetical characters
        // 0 : A, 1 : B, ..., 9 : J
        Map<Integer, Character> intToRow = new HashMap<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            intToRow.put(i, (char) (i + UPPERCASE_A_ASCII));
        }
        try {
            // Check that there is an inputted file path
            if (args.length == 0) {
                System.out.println("No input file found.");
                return;
            }
            // Get a parseable scanner for the input file and create an output file
            Scanner file = new Scanner(new File(args[0]));
            File outputFile = new File("output.txt");
            PrintStream output = new PrintStream(outputFile);
            // While the input file has more reservations
            while (file.hasNextLine()) {
                // Split the reservation into its identifier and number of people
                String[] reservation = file.nextLine().split("\\s+");
                int people = Integer.parseInt(reservation[SIZE_INDEX]);  // Number of people in this reservation
                // Get all assigned seats for this reservation
                List<Integer[]> allReservedSeats = theater.getSeats(people);
                // Check if the reservation can be accommodated for
                if (allReservedSeats == null) {
                    System.out.println("Not enough seats for reservation: " + reservation[IDENTIFIER_INDEX]);
                    continue;
                }
                // The reservation can be accommodated for
                // Print all seating spots for this reservation
                output.print(reservation[IDENTIFIER_INDEX] + " ");
                for (int i = 0; i < allReservedSeats.size(); i++) {
                    Integer[] reservedSeats = allReservedSeats.get(i);
                    for (int j = reservedSeats[STARTING_COLUMN_INDEX]; j <= reservedSeats[ENDING_COLUMN_INDEX]; j++) {
                        if (i != 0 || j != reservedSeats[STARTING_COLUMN_INDEX]) {
                            output.print(",");
                        }
                        output.print(intToRow.get(reservedSeats[ROW_INDEX]));
                        output.print(j + 1);
                    }
                }
                output.println();
            }
        System.out.println("Output File Path: " + outputFile.getAbsolutePath());
        // Failed to open a file based on the inputted path
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open file.");
        }
    }
}
