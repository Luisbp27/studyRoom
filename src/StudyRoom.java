import java.util.concurrent.Semaphore;

public class StudyRoom {

    static final int party = 5;
    static final int maxStudents = 15;
    static int studentsCounter = 0;

    static Director.States roomState = Director.States.OUT;

    static Semaphore director = new Semaphore(0);
    static Semaphore students = new Semaphore(1);

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

        Thread director = new Thread(new Director());
        Thread students[] = new Thread[maxStudents];

        director.start();
        for (int i = 0; i < students.length; i++) {
            students[i] = new Thread(new Student(names[i]));
            students[i].start();
        }

        director.join();
        for (int i = 0; i < students.length; i++) {
            students[i].join();
        }
    }
}
