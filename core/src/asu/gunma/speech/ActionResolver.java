package asu.gunma.speech;


public interface ActionResolver {
     void startRecognition();
     String getWord();
     void signIn();
     void setWordNull();
}
