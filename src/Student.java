public class Student implements Runnable {

    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // The director is IN the study room
            if (Director.directorState == Director.State.IN || StudyRoom.door.tryAcquire()) {
                // Waiting
                while (Director.directorState == Director.State.IN || StudyRoom.door.tryAcquire()) {
                    StudyRoom.student.acquire();
                }

            // The director is OUT, WAITING or the door is open
            } else {
                // Critical Region to go inside the room
                StudyRoom.student.acquire();
                StudyRoom.studentsCounter++;
                System.out.println(this.name + ": goes inside the study room. Current number of students: "
                        + StudyRoom.studentsCounter);
                StudyRoom.student.release();

                // If students are studying or making a party
                if (StudyRoom.studentsCounter < StudyRoom.party) {
                    System.out.println(this.name + " is studying");
                    Thread.sleep((long) (Math.random() + 1000));
                } else {
                    System.out.println(this.name + " PARTY!!!");
                    Thread.sleep((long) (Math.random() + 1000));
                }

                // Critical region to go out the room
                StudyRoom.student.acquire();
                StudyRoom.studentsCounter--;
                System.out.println(this.name + ": goes outside the study room. Current number of students: "
                        + StudyRoom.studentsCounter);
                StudyRoom.student.release();
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
