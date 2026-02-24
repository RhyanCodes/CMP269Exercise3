import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Processor {

    private static final String INPUT_FILE = "students.txt";
    private static final String OUTPUT_FILE = "grades_report.txt";
    private static final double LOW_GRADE_THRESHOLD = 60.0;

    public static void main(String[] args) {
        processStudentGrades();
    }

    /**
     * Processes student grades from the input file and generates a report.
     */
    public static void processStudentGrades() {
        try (Scanner scanner = new Scanner(new File(INPUT_FILE));
                PrintWriter writer = new PrintWriter(OUTPUT_FILE)) {

            System.out.println("Reading student data from " + INPUT_FILE + "...\n");
            writer.println("=== GRADES REPORT ===");
            writer.println();

            int processedCount = 0;
            int skippedCount = 0;
            int warningCount = 0;

            // Read file line by line using hasNextLine() to avoid NoSuchElementException
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                // Validate line format (name + 3 scores)
                if (parts.length < 4) {
                    System.out.println("Warning: Invalid line format (not enough data): " + line);
                    skippedCount++;
                    continue;
                }

                String studentName = parts[0];

                try {
                    // Parse and calculate average score
                    double score1 = Double.parseDouble(parts[1]);
                    double score2 = Double.parseDouble(parts[2]);
                    double score3 = Double.parseDouble(parts[3]);

                    double average = (score1 + score2 + score3) / 3.0;

                    // Check for low grade and handle with custom exception (Bonus Challenge)
                    try {
                        checkForLowGrade(studentName, average);
                        // No warning flag
                        writer.printf("Student: %s | Average: %.2f%n", studentName, average);
                    } catch (LowGradeException e) {
                        // Mark with warning flag
                        writer.printf("Student: %s | Average: %.2f | *** WARNING: Low Grade ***%n",
                                studentName, average);
                        warningCount++;
                        System.out.println("Low grade alert: " + e.getMessage());
                    }

                    processedCount++;

                } catch (NumberFormatException e) {
                    // Handle invalid score data (e.g., "Error" instead of a number)
                    System.out.println("Warning: Invalid score data for student '" + studentName +
                            "'. Skipping this record. (Error: " + e.getMessage() + ")");
                    skippedCount++;
                }
            }

            // Write summary to report
            writer.println();
            writer.println("=== SUMMARY ===");
            writer.printf("Total students processed: %d%n", processedCount);
            writer.printf("Students with warnings: %d%n", warningCount);
            writer.printf("Records skipped due to errors: %d%n", skippedCount);

            System.out.println("\nReport written to " + OUTPUT_FILE);
            System.out.println("Processed: " + processedCount + " students");
            System.out.println("Skipped: " + skippedCount + " records");
            System.out.println("Warnings: " + warningCount + " students with low grades");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Could not find the file '" + INPUT_FILE + "'");
            System.err.println("Please make sure the file exists in the project directory.");
        } finally {
            // This executes regardless of success or failure
            System.out.println("\nProcessing Complete");
        }
    }

    private static void checkForLowGrade(String studentName, double average) throws LowGradeException {
        if (average < LOW_GRADE_THRESHOLD) {
            throw new LowGradeException(studentName, average);
        }
    }
}
