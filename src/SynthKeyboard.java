import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.awt.event.*;


public class SynthKeyboard implements KeyListener {

    private int sampleRate = 44100;
    private double duration = 0.3;
    private volatile boolean playing = false;
    //lager en audioplayer som brukes når taster trykkes
    private AudioPlayer player = new AudioPlayer();
    private JLabel label = new JLabel("Trykk en tast for god´s sake!");

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Trykket: " + e.getKeyChar());
        //hnter tast trykket og lagrer som tast
        char tast = e.getKeyChar();
        //henter den korresponderende desimalverdien fra getfrq-metode og lagrer som tone
        double tone = getFrequency(tast);

        if (tone == 0.0) {
            return;
        }
        if (playing) {return;}

        playing = true;
        player.start();

        Oscillator osc3 = new Oscillator(44100, tone, 0.5, Waveform.SAW);
        new Thread(()-> {
            while (playing){
                double[] samples = osc3.generateSamples(1024);
                player.write(samples);
            }
            player.stop();
        }).start();

        label.setText("" + tone + "    "+ Character.toUpperCase(tast) );
    }

    @Override
    public void keyReleased(KeyEvent e) {
    playing = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public SynthKeyboard() {
        //Lager ny frame, kaller den synth
        JFrame frame = new JFrame("Synth");
        //Gir framen et vindu
        frame.setSize(400, 200);
        //gir framen muligheten til å overvåke tastaturet i (dette) vinduet
        frame.addKeyListener(this);
        frame.add(label);
        //gjør framen synlig
        frame.setVisible(true);
    }

    //Lager metode for å koble keystrokes til frekvenser i c-dur
    public double getFrequency(char key) {
        switch (key) {
            case 'a':
                return 261.63;
            case 's':
                return 293.66;
            case 'd':
                return 329.63;
            case 'f':
                return 349.23;
            case 'g':
                return 392.00;
            case 'h':
                return 440.00;
            case 'j':
                return 493.88;
            case 'k':
                return 523.24;
            default:
                return 0.0;
        }
    }

    static void main(String[] args) {
        //Oppretter objektet som skal lytte etter keystrokes
        new SynthKeyboard();

    }

    //Metode for å konvertere fra desimaltall(osc bruker desimal) til heltall til bytes (i hexa)
    static byte[] convertToBytes(double[] samples) {

        byte[] buffer = new byte[samples.length * 2];

        for (int i = 0; i < samples.length; i++) {

            //Konverterer til 16bit heltall
            Short sampleAsShort = (short) (samples[i] * Short.MAX_VALUE);


            //skriv til buffer
            buffer[i * 2] = (byte) (sampleAsShort & 0xFF);//0xFF = hexadecimal og må datatypen skrives i parentes foran en utregning?
            buffer[i * 2 + 1] = (byte) (sampleAsShort >> 8 & 0xFF);//>>?

        }
        return buffer;
    }

}
