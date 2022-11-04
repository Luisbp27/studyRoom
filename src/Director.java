public class Director implements Runnable {

    public enum States {
        OUT, WAITING, IN
    }

    public Director() {

    }

    @Override
    public void run() {
        try {
            System.out.println("");

            for (int i = 1; i <= 3; i++) {
                System.out.println("    The Director starts the round " + i + "/3");

                // If nobody is in the study room
                if (StudyRoom.studentsCounter == 0) {
                    StudyRoom.student.acquire();
                    System.out.println("    The director see that's nobody is in the study room");
                    StudyRoom.roomState = Director.States.IN;
                    
                    StudyRoom.director.release();
                    StudyRoom.student.release();
                }

                // If somebody is in the room but they are studying
                if (StudyRoom.studentsCounter <= StudyRoom.party && StudyRoom.studentsCounter > 0) {
                    StudyRoom.director.acquire();
                    System.out.println("    The director is waiting. They don't disturb the students");
                    Thread.sleep((long) (Math.random() + 1000));
                    StudyRoom.roomState = Director.States.WAITING;
                }

                // If there is a party in the room
                if (StudyRoom.studentsCounter >= StudyRoom.party) {
                    // Block then entry of students and change the director state
                    StudyRoom.student.acquire();
                    StudyRoom.roomState = Director.States.IN;

                    // Go out all students of the study room
                    int counter = StudyRoom.studentsCounter;
                    for (int j = 0; j < counter; j++) {
                        System.out.println(StudyRoom.studentsThread[j].getName()
                                + " goes out the study room. Number of students now: " + StudyRoom.studentsCounter);
                        StudyRoom.studentsThread[j].join();
                        StudyRoom.studentsCounter--;
                    }

                    // Release all threads
                    StudyRoom.director.release();
                    StudyRoom.roomState = Director.States.OUT;

                    StudyRoom.student.release();
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}