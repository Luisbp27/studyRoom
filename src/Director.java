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

                }

                // If somebody is in the room but they are studying
                if (StudyRoom.maxStudents < StudyRoom.party && StudyRoom.studentsCounter > 0) {
                    System.out.println("    The director is waiting. They don't disturb the students");
                    StudyRoom.roomState = Director.States.WAITING;
                    StudyRoom.director.acquire();
                }

                // If there is a party in the room
                if (StudyRoom.maxStudents == StudyRoom.party) {

                }
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}