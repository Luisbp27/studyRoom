
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
                directorState = State.WAITING;
                StudyRoom.director.acquire();
 
                StudyRoom.mutex.acquire();
                // If there is no party in the study room
                if (StudyRoom.studentsCounter < StudyRoom.party && StudyRoom.studentsCounter > 0) {
                    StudyRoom.mutex.release();
                    System.out.println("    The director is waiting. They don't disturb the students");
                
                // If there is nobody in the study room
                } else if (StudyRoom.studentsCounter == 0) {
                    StudyRoom.mutex.release();
                    System.out.println("    The director see that's nobody is in the study room");
                    directorState = State.OUT;
                    StudyRoom.director.release();

                // If there is a party in the room
                } else {
                    StudyRoom.mutex.release();
                    directorState = State.IN;
                    StudyRoom.director.acquire();
                    System.out.println("    The director is in the study room: THE PARTY IS OVER");
                }

                // The director goes out and take a coffe
                directorState = State.OUT;
                StudyRoom.director.release();
                Thread.sleep((long) (Math.random() + 1000));
                
                System.out.println("    The director finished the round " + i + "/3");
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
