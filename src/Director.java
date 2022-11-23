/**
 * 
 * @author Alejandro Medina & Lluis Barca
 */
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
                
                System.out.println("  The Director starts the round " + i + "/3");  
                
                StudyRoom.mutex.acquire();
                // If there is nobody in the study room                
                if (StudyRoom.studentsCounter == 0) {
                    StudyRoom.mutex.release();
                    StudyRoom.director.release();
                
                // If there is no party in the study room
                } else if (StudyRoom.studentsCounter < StudyRoom.party && StudyRoom.studentsCounter > 0) {
                        directorState = State.WAITING;
                        System.out.println("    The director is waiting. They don't disturb the students");  
                        
                        StudyRoom.mutex.release();
                        StudyRoom.director.acquire();                       
                        
                        StudyRoom.mutex.acquire();
                        // If there is a party
                        if(StudyRoom.studentsCounter >= StudyRoom.party){
                            directorState = State.IN;
                            System.out.println("    The director is in the study room: THE PARTY IS OVER");     
                            StudyRoom.mutex.release();

                            StudyRoom.student.acquire();    
                            StudyRoom.director.acquire(); 
                            

                        // If there is not a party
                        }else{
                            StudyRoom.mutex.release();
                            StudyRoom.director.release();
                        }
                        
                // If there is a party in the room
                } else {
                    directorState = State.IN;
                    System.out.println("    The director is in the study room: THE PARTY IS OVER");     

                    StudyRoom.mutex.release();
                    StudyRoom.student.acquire();    
                    StudyRoom.director.acquire(); 
                    
                }
                
                StudyRoom.director.acquire();
                StudyRoom.mutex.acquire();

                System.out.println("    The director see that's nobody is in the study room");
                directorState = State.OUT;

                System.out.println("    The director finished the round " + i + "/3");
                StudyRoom.mutex.release();
                // If is the last student, the director lets student leave the study room
                if(StudyRoom.student.availablePermits() == 0){
                    StudyRoom.student.release();  
                }               

                if(StudyRoom.director.availablePermits()==0){
                    StudyRoom.director.release();  
                }
                
                Thread.sleep((long) (Math.random() + 1000));
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}