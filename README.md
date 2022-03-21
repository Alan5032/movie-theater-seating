Code for an interview assignment.

Instructions:
  1. Download "Theater.java" and "Main.java" to the same directory.
  2. Open up commmand line and navigate the directory with the downloaded files.
  3. Compile the files by running "javac Theater.java Main.java" in the command line.
  4. Execute the program with an input file by running "java Main.java \<absolute path of input file>"
  5. View the output file that is located via the path printed to console by the program.

Assumptions:
* No groups of people will have more than one reservation. Otherwise, they would've cancelled their first reservation and would've made an updated second reservation.
* Everyone has the same preferences. They all prefer the middle row the most, then the row behind the middle row, ..., then the last row, then the first row, and then finally the row before the middle row. All seats in the same row are equally preferred.
* Groups prefer to be seated together if possible. Otherwise, they are fine being split among multiple rows.
* A group prefers not to attend a movie if there are not enough seats to accommodate the whole reservation.
