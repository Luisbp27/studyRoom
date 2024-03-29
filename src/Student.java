/**
 * 
 * @author Alejandro Medina & Lluis Barca
 */
public class Student implements Runnable  {

    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) ((Math.random()*1000) + 500));

            StudyRoom.mutex.acquire();
            // Check if the student can enter to the study room
            if (Director.directorState == Director.State.IN ) {
                StudyRoom.mutex.release();
                StudyRoom.student.acquire();
                StudyRoom.student.release();
                
            } else {
                StudyRoom.mutex.release();
            }

            StudyRoom.mutex.acquire();
            // Critical Region to go inside the room
            StudyRoom.studentsCounter++;
            System.out.println(this.name + ": goes inside the study room. Current number of students: "
                    + StudyRoom.studentsCounter);

            // If students are studying or making a party
            if (StudyRoom.studentsCounter < StudyRoom.party) {
                System.out.println(this.name + " is studying");
            } else {
                System.out.println(this.name + ": PARTY!!!");

                // If the director is WAITING and students are making a party, the last student warns the other students
                if (Director.directorState == Director.State.WAITING) {
                    System.out.println(this.name + ": WARNING the director is coming!!!!!");
                }
            }
            StudyRoom.mutex.release();
  
            Thread.sleep((long) ((Math.random()*1000) + 500));

            // Critical region to go out the room
            StudyRoom.mutex.acquire();
            StudyRoom.studentsCounter--;
            System.out.println(this.name + ": goes outside the study room. Current number of students: "
                    + StudyRoom.studentsCounter);

            // If is the last student and the director is IN or WAITING
            if (StudyRoom.studentsCounter == 0) {
                if (Director.directorState == Director.State.IN) {
                    System.out.println(this.name + ": Good bye director, you are on your own");
                } 
                
                if (Director.directorState == Director.State.WAITING) {
                    System.out.println(this.name + ": Good bye director, you can come in if you want, there is no one here");
                }
                
                StudyRoom.director.release();
            }
            StudyRoom.mutex.release();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
