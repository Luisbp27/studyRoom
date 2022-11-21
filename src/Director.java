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
                Thread.sleep(400);

                StudyRoom.mutex.acquire();
                System.out.println("  The Director starts the round " + i + "/3");  

                // If there is nobody in the study room                
                if (StudyRoom.studentsCounter == 0) {
                    
                    // System.out.println("    The director see that's nobody is in the study room");
                    // directorState = State.OUT;
                    StudyRoom.mutex.release();
                    //StudyRoom.director.release();
                
                // If there is no party in the study room
                } else if (StudyRoom.studentsCounter < StudyRoom.party && StudyRoom.studentsCounter > 0) {
                    
                        directorState = State.WAITING;
                        System.out.println("    The director is waiting. They don't disturb the students");  
                        System.out.println(" Director permisos: " + StudyRoom.director.availablePermits());
                        StudyRoom.director.acquire();
                        StudyRoom.mutex.release();                       
                        
                        StudyRoom.mutex.acquire();
                        
                        if(StudyRoom.studentsCounter >= StudyRoom.party){
                            directorState = State.IN;
                            System.out.println("    The director is in the study room: THE PARTY IS OVER");
                            StudyRoom.director.acquire();
                            StudyRoom.student.release();
                            StudyRoom.mutex.release();
                        }

                // If there is a party in the room
                } else {
                    
                    directorState = State.IN;
                    System.out.println("    The director is in the study room: THE PARTY IS OVER");
                    StudyRoom.director.acquire();
                    StudyRoom.student.release();
                    StudyRoom.mutex.release();
                }
                //StudyRoom.director.acquire();
                //Thread.sleep((long) (Math.random() + 1000));
                
                // The director goes out and take a coffe
                StudyRoom.mutex.acquire();
                System.out.println("    The director see that's nobody is in the study room");
                directorState = State.OUT;
                System.out.println("    The director finished the round " + i + "/3");
                StudyRoom.mutex.release();

                //StudyRoom.director.release();
                Thread.sleep((long) (Math.random() + 1000));
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}