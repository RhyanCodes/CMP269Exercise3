/**
 * Custom checked exception thrown when a student's average grade is below 60.
 */
public class LowGradeException extends Exception {
    
    private String studentName;
    private double average;
    
    public LowGradeException(String studentName, double average) {
        super("Student " + studentName + " has a low average grade: " + average);
        this.studentName = studentName;
        this.average = average;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public double getAverage() {
        return average;
    }
}
