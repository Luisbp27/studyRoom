
public class Director implements Runnable {
    public enum State {
        IN, WAITING, OUT
    }

    static State directorState = State.OUT;

    public Director() {

    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 3; i++) {
                System.out.println("    The Director starts the round " + i + "/3");
                Thread.sleep((long) (Math.random() + 1000));

                // The director is in front of the door
                Director.directorState = State.WAITING;
                StudyRoom.door.acquire();
                // If there is no party in the study room
                if (StudyRoom.studentsCounter < StudyRoom.party && StudyRoom.studentsCounter > 0) {
                    System.out.println("    The director is waiting. They don't disturb the students");
                
                // If there is nobody in the study room
                } else if (StudyRoom.studentsCounter == 0) {
                    System.out.println("    The director see that's nobody is in the study room");

                // If there is a party in the room
                } else {
                    Director.directorState = State.IN;
                    StudyRoom.director.acquire();
                    System.out.println("    The director is in the study room: THE PARTY IS OVER");

                    // Go out all the students of the study room
                    int counter = StudyRoom.studentsCounter;
                    for (int j = 0; j < counter; j++) {
                        StudyRoom.studentsCounter--;
                        System.out.println(StudyRoom.studentsThread[j].getName()
                                + " goes out the study room. Number of students now: " + StudyRoom.studentsCounter);
                        StudyRoom.studentsThread[j].join();
                    }
                }
                StudyRoom.door.release();

                // The director goes out and take a coffe
                Director.directorState = State.OUT;
                Thread.sleep((long) (Math.random() + 1000));
                
                System.out.println("    The director finished the round " + i + "/3");
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        
    }
}
