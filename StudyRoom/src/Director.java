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
                Thread.sleep((long) (Math.random() + 1000));
                
                // If nobody is in the study room
                // StudyRoom.roomState = States.WAITING;
                StudyRoom.door.acquire();
                if (StudyRoom.studentsCounter == 0) {
                    StudyRoom.student.acquire();
                    System.out.println("    The director see that's nobody is in the study room");
                    StudyRoom.student.release();

                    StudyRoom.roomState = States.OUT;
                    Thread.sleep((long) (Math.random() + 1000));
                }
                StudyRoom.door.release();

                // If somebody is in the room but they are studying
                StudyRoom.door.acquire();
                if (StudyRoom.studentsCounter < StudyRoom.party && StudyRoom.studentsCounter > 0) {
                    StudyRoom.roomState = States.WAITING;
                    StudyRoom.director.acquire();
                    System.out.println("    The director is waiting. They don't disturb the students");
                    StudyRoom.door.release();
                    Thread.sleep((long) (Math.random() + 1000));
                }
                

                // If there is a party in the room
                StudyRoom.door.acquire();
                if (StudyRoom.studentsCounter >= StudyRoom.party) {
                    // Block then entry of students and change the director state
                    StudyRoom.roomState = Director.States.IN;
                    StudyRoom.director.acquire();
                    StudyRoom.student.acquire();

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
                    StudyRoom.door.release();
                    StudyRoom.student.release();
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}