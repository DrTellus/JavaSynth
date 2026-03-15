import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.awt.event.*;
import java.util.concurrent.ConcurrentHashMap;


public class SynthKeyboard implements KeyListener {

    private int sampleRate = 44100;
    private double duration = 0.3;
    //private volatile boolean playing = false;
    private ConcurrentHashMap<Character, Oscillator> activeOscillators = new ConcurrentHashMap<>();
    //lager en audioplayer som brukes når taster trykkes
    private AudioPlayer player = new AudioPlayer();
    private JLabel label = new JLabel("Trykk en tast for god´s sake!");
    private double releaseTime = 0.3;

    @Override
    public void keyPressed(KeyEvent e) {

        //Legger til releasekontroll

        //hnter tast trykket og lagrer som tast
        char tast = e.getKeyChar();
        //henter den korresponderende desimalverdien fra getfrq-metode og lagrer som tone
        double tone = getFrequency(tast);

        if (tast == '1'){
            releaseTime = Math.max(0.01, releaseTime-0.1);
            label.setText("Release: " + releaseTime);
            return;
        }
        if (tast == '2'){
            releaseTime = Math.min(2.0, releaseTime+0.1);
            label.setText("Release; " + releaseTime);
            return;
        }

        if (tone == 0.0) {

            return;
        }
        if (activeOscillators.containsKey(tast)){
            return;
        }
        System.out.println("Trykket: " + e.getKeyChar());
       activeOscillators.put(tast, new Oscillator(44100, tone, 0.12, Waveform.SAW));
        label.setText("" + tone + "   " + Character.toUpperCase(tast));

    }

    @Override
    public void keyReleased(KeyEvent e) {
      char tast = e.getKeyChar();
      Oscillator osc = activeOscillators.get(tast);
      if (osc == null){return;}

      //gir releasetid
      new Thread(() -> {
          int steps = 50;
          double startVolume = 0.12;
          double sleepTime = releaseTime/steps*1000;
          for(int i = steps; i >= 0; i--){
              osc.setVolume(startVolume * i / steps);
              try {
                  Thread.sleep((long) sleepTime); }catch (Exception ex){}
              }
          activeOscillators.remove(tast);
          }).start();

    //activeOscillators.remove(e.getKeyChar());
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
        player.start();
        new Thread(() -> {
            while (true) {
                double[] mix =new double[1024];
                for (Oscillator osc :activeOscillators.values()){
                    double[] samples = osc.generateSamples(1024);
                    for (int i = 0; i < mix.length; i++) {
                        mix[i] += samples[i];
                    }
                }
                int count = activeOscillators.size();

                    player.write(mix);
            }
        }).start();

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
