public class Student implements Runnable {

    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            StudyRoom.mutex.acquire();
            System.out.println("hola");
            // The director is OUT, WAITING
            if (Director.directorState != Director.State.IN) {
                StudyRoom.mutex.release();
                System.out.println("he entrado");
                // Critical Region to go inside the room
                StudyRoom.student.acquire();
                StudyRoom.studentsCounter++;
                System.out.println(this.name + ": goes inside the study room. Current number of students: "
                        + StudyRoom.studentsCounter);
                StudyRoom.student.release();

                StudyRoom.mutex.acquire();
                // If students are studying or making a party
                if (StudyRoom.studentsCounter < StudyRoom.party) {
                    System.out.println(this.name + " is studying");
                } else {
                    System.out.println(this.name + " PARTY!!!");
                }
                StudyRoom.mutex.release();
                Thread.sleep((long) (Math.random() + 1000));
            } 
                   
            // Critical region to go out the room
            StudyRoom.student.acquire();
            StudyRoom.studentsCounter--;
            System.out.println(this.name + ": goes outside the study room. Current number of students: "
                    + StudyRoom.studentsCounter);
            StudyRoom.student.release();

            StudyRoom.mutex.acquire();
            // If is the last student and the director is IN or WAITING
            if (StudyRoom.studentsCounter == 0 && Director.directorState != Director.State.OUT) {
                StudyRoom.mutex.release(); 
                System.out.println(this.name + " is the last student. Good bye director");
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
