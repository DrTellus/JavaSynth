import javax.sound.sampled.*;

public class SynthStep01 {

    static void main(String[] args) throws LineUnavailableException {

        //double frequency = 440.0;
        //double volume = 1.5;
        double durationSeconds = 1.5;

        int sampleRate = 44100;
        int totalSamples = (int) (sampleRate * durationSeconds);//caster matte til int.
        //primitiv datatype byte har 8 bits.Kan lagre nr fra -128 til 127
        byte[] buffer = new byte[totalSamples *2];//Bestemmer lengde på array buffer.
        byte[] buffer2 = new byte[totalSamples *2];

        Oscillator osc = new Oscillator(44100, 440.0, 0.5);
        Oscillator osc2 = new Oscillator(44100, 660.0, 0.5);

        double[] samples = osc.generateSamples(totalSamples);

        double[] samples2 = osc2.generateSamples(totalSamples);

        for(int i = 0; i < totalSamples; i++){

//            //Når i øker, går man videre langs sinusbølgen. Frequency øker/senker tallet
//            double angle = 2.0 * Math.PI * frequency * i /sampleRate;
//
//            //Sinverdi {-1,1} ganger volum
//            double sample = Math.sin(angle) * volume;

            //Konverterer til 16bit heltall
            Short sampleAsShort = (short)(samples[i] * Short.MAX_VALUE);

            //skriv til buffer
            buffer[i * 2] = (byte)(sampleAsShort & 0xFF);//0xFF = hexadecimal og må datatypen skrives i parentes foran en utregning?
            buffer[i * 2 + 1] = (byte)(sampleAsShort >> 8 & 0xFF);//>>?

        }

        for(int i = 0; i < totalSamples; i++){

            //Konverterer til 16bit heltall
            Short sampleAsShort = (short)(samples2[i] * Short.MAX_VALUE);

            //skriv til buffer
            buffer2[i * 2] = (byte)(sampleAsShort & 0xFF);//0xFF = hexadecimal og må datatypen skrives i parentes foran en utregning?
            buffer2[i * 2 + 1] = (byte)(sampleAsShort >> 8 & 0xFF);//>>?

        }
        AudioFormat format = new AudioFormat(sampleRate,16,1, true, false);//bigendian
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(format);
        line.start();
        line.write(buffer, 0, buffer.length);
        line.write(buffer2, 0, buffer2.length);
        line.drain();
        line.close();



    }
}
