package observe;

import com.example.notes.NoteStructure;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;

    public Publisher(){
        observers = new ArrayList<>();
    }
    public void subscribe(Observer observer){
        observers.add(observer);
    }
    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }
    public void notifySingle(NoteStructure noteStructure){
        for (Observer observer : observers){
            observer.updateNoteStructure(noteStructure);
            unsubscribe(observer);
        }
    }

}
