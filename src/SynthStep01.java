import javax.sound.sampled.*;

public class SynthStep01 {

    static void main(String[] args) throws LineUnavailableException {

        double durationSeconds = 1.5;

        int sampleRate = 44100;
        int totalSamples = (int) (sampleRate * durationSeconds);//caster matte til int.
        //primitiv datatype byte har 8 bits.Kan lagre nr fra -128 til 127



        Oscillator osc = new Oscillator(44100, 440.0, 0.5);
        Oscillator osc2 = new Oscillator(44100, 660.0, 0.5);

        double[] samples = osc.generateSamples(totalSamples);

        double[] samples2 = osc2.generateSamples(totalSamples);

        byte[] buffer = convertToBytes(samples);
        byte[] buffer2 = convertToBytes(samples2);


        AudioFormat format = new AudioFormat(sampleRate,16,1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(format);
        line.start();
        line.write(buffer, 0, buffer.length);
        line.write(buffer2, 0, buffer2.length);
        line.drain();
        line.close();



    }
    //Metode for å konvertere fra desimaltall(osc bruker desimal) til heltall til bytes (i hexa)
    static byte[] convertToBytes(double[] samples){

        byte[] buffer = new byte[samples.length*2];

        for(int i = 0; i < samples.length; i++){

            //Konverterer til 16bit heltall
            Short sampleAsShort = (short)(samples[i] * Short.MAX_VALUE);


            //skriv til buffer
            buffer[i * 2] = (byte)(sampleAsShort & 0xFF);//0xFF = hexadecimal og må datatypen skrives i parentes foran en utregning?
            buffer[i * 2 + 1] = (byte)(sampleAsShort >> 8 & 0xFF);//>>?

        }
        return buffer;
    }
}
