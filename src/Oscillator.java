public class Oscillator {

    private double sampleRate;
    private double frequency;
    private double volume;

    private Waveform waveform;

    public Oscillator(double sampleRate, double frequency, double volume, Waveform waveform){
        this.sampleRate = sampleRate;
        this.frequency =  frequency;
        this.volume = volume;
        this.waveform = waveform;
    }
    public double[] generateSamples(int totalSamples){
        double[] samples = new double[totalSamples];
        //double sample;



        for (int i = 0; i < totalSamples; i++){
            double angle = 2.0 * Math.PI * frequency * i / sampleRate;
            double sample;

            switch (waveform){
                case SINE: sample = Math.sin(angle); break;
                case SQUARE: sample = Math.signum(Math.sin(angle)); break;
                case SAW:    sample = 2.0 *(angle %(2* Math.PI)) / (2 *Math.PI) -1.0; break;
                case TRIANGLE: sample = 2.0 * Math.abs(2.0 * (angle % (2 * Math.PI)) / (2* Math.PI) -1)-1; break;
                default: sample = 0; break;
            }
            samples[i] = sample * volume;

            //samples[i] = Math.sin(angle) * volume;

        }

        return samples;
    }

}
