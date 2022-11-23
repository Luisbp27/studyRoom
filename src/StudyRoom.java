import java.util.concurrent.Semaphore;
import java.util.Scanner;

/**
 * 
 * @author Alejandro Medina & Lluis Barca
 */
public class StudyRoom {

    static int studentsCounter = 0;
    static final int maxStudents = 10;
    static final int party = 4;

    static Thread directorThread;
    static Thread studentsThread[] = new Thread[maxStudents]; 

    static Semaphore director = new Semaphore(1);
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
            directorThread = new Thread(new Director(Integer.parseInt(input.nextLine())));
        } catch (Error e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println("Maximum number of students: " + maxStudents);
        System.out.println("Total number of students: " + party);

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