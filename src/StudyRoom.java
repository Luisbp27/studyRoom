import java.util.concurrent.Semaphore;

public class StudyRoom {

    static final int party = 5;
    static final int maxStudents = 15;
    static int studentsCounter = 0;

    static Director.States roomState = Director.States.OUT;

    static Semaphore director = new Semaphore(0);
    static Semaphore student = new Semaphore(1);
    static Semaphore door = new Semaphore(1);

    static Thread directorThread = new Thread(new Director());
    static Thread studentsThread[] = new Thread[maxStudents];

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
    }

}