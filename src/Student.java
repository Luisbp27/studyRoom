public class Student implements Runnable {

    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // The director is OUT, WAITING
            if (Director.directorState != Director.State.IN) {
                // Critical Region to go inside the room
                StudyRoom.mutex.acquire();
                StudyRoom.studentsCounter++;
                System.out.println(this.name + ": goes inside the study room. Current number of students: "
                        + StudyRoom.studentsCounter);
                        if(StudyRoom.studentsCounter>=StudyRoom.maxStudents){
                            StudyRoom.director.release();
                        }
                StudyRoom.mutex.release();

                StudyRoom.mutex.acquire();
                // If students are studying or making a party
                if (StudyRoom.studentsCounter < StudyRoom.party) {
                    System.out.println(this.name + " is studying");
                } else {
                    System.out.println(this.name + " PARTY!!!");
                }
                StudyRoom.mutex.release();
                Thread.sleep((long) (Math.random() + 1000));
            }else{

                StudyRoom.student.acquire();
            }
                   
            // Critical region to go out the room
            StudyRoom.mutex.acquire();
            StudyRoom.studentsCounter--;
            System.out.println(this.name + ": goes outside the study room. Current number of students: "
                    + StudyRoom.studentsCounter);
            StudyRoom.mutex.release();

            StudyRoom.mutex.acquire();
            // If is the last student and the director is IN or WAITING
            if (StudyRoom.studentsCounter == 0 && Director.directorState != Director.State.OUT) {               
                System.out.println(this.name + " is the last student. Good bye director");
                
            }
            StudyRoom.mutex.release(); 


        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
