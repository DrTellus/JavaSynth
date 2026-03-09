import javax.swing.*;
import java.awt.event.*;


public class SynthKeyboard implements KeyListener{
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Trykket: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public SynthKeyboard(){
        //Lager ny frame, kaller den synth
        JFrame frame = new JFrame("Synth");
        //Gir framen et vindu
        frame.setSize(400, 200);
        //gir framen muligheten til å overvåke tastaturet i (dette) vinduet
        frame.addKeyListener(this);
        //gjør framen synlig
        frame.setVisible(true);
    }

    static void main(String[] args) {
        //Oppretter objektet som skal lytte etter keystrokes
        new SynthKeyboard();

    }
}
