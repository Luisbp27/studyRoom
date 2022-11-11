public class Student implements Runnable {

    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        try {

            // Critical Region
            StudyRoom.student.acquire();
            StudyRoom.studentsCounter++;
            System.out.println(this.name + ": goes inside the study room. Current number of students: "
                + StudyRoom.studentsCounter);

            // Check if the students is studying or party
            if (StudyRoom.studentsCounter < StudyRoom.party) {
                System.out.println(this.name + " is studying");
                Thread.sleep((long) (Math.random() + 1000));

            } else {
                System.out.println(this.name + " PARTY!!!!");
                Thread.sleep((long) (Math.random() + 1000));

                // When there is a party and the director is waiting
                if (StudyRoom.roomState == Director.States.WAITING) {
                    System.out.println("    The director is in the study room: THE PARTY IS OVER");
                    StudyRoom.roomState = Director.States.IN;
                    StudyRoom.student.acquire();
                    StudyRoom.director.release();
                } else {
                    StudyRoom.student.release();
                }
            }
            StudyRoom.student.release();

            // If the study room is empty, the director go in
            StudyRoom.door.acquire();
            if (StudyRoom.studentsCounter == 0) {
                System.out.println("    The director checks there is nobody in the student room");
                StudyRoom.director.release();
            }
            StudyRoom.door.release();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String getName() {
        return this.name;
    }
}
