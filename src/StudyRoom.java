import java.util.concurrent.Semaphore;
import java.util.Scanner;

/**
 * 
 * Link video: https://drive.google.com/file/d/1jLY6xyLkGFp6LfLpfE_sf1BIUsVccAOK/view?usp=sharing
 * @author Alejandro Medina & Lluis Barca
 */
public class StudyRoom {

    static int studentsCounter = 0;
    static final int maxStudents = 5;
    static final int party = 15;
    static int directorSleep;

    static Thread directorThread = new Thread(new Director());
    static Thread studentsThread[] = new Thread[maxStudents]; 

    static Semaphore director = new Semaphore(0);
    static Semaphore student = new Semaphore(1);
    static Semaphore mutex = new Semaphore(1);

    static String names[] = {
            "Moises",
            "Carlos",
            "Julio",
            "Gregoria",
            "Juana",
            "Luis",
            "Alejandro",
            "Omar",
            "Jorge",
            "Fabian",
            "Anastasia",
            "Claudia",
            "Isidoro",
            "Manuel",
            "Paula" };

    public static void main(String args[]) throws InterruptedException {

        System.out.println("Introduce the director timeout for each round (in miliseconds): ");
        try (Scanner input = new Scanner(System.in)) {
            directorSleep = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input error: " + e.getMessage());
        }

        System.out.println("Maximum number of students: " + maxStudents);
        System.out.println("Total number of students: " + party + "\n");

        directorThread.start();
        for (int i = 0; i < studentsThread.length; i++) {
            studentsThread[i] = new Thread(new Student(names[i]));
            studentsThread[i].start();
        }

        directorThread.join();
        for (int i = 0; i < studentsThread.length; i++) {
            studentsThread[i].join();
        }

        System.out.println("END OF SIMULATION");
    }
}