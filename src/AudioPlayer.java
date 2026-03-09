import javax.sound.sampled.*;

public class AudioPlayer {

    private int sampleRate = 44100;

    public void play(double[] samples) {
        //lager et byte-array for å konvertere samplesene til bytes for lydkortet
        byte[] buffer = convertToBytes(samples);

        //Konvertering til lyd, etc
        try {
            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format);
            line.start();
            line.write(buffer, 0, buffer.length);
            line.drain();
            line.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Metode for å konvertere fra desimaltall(osc bruker desimal) til heltall til bytes (i hexa)
    private byte[] convertToBytes(double[] samples) {

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


