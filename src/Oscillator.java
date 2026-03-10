public class Oscillator {

    private double sampleRate;
    private double frequency;
    private double volume;
    private double phase = 0;

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

            double sample;

            switch (waveform){
                case SINE: sample = Math.sin(phase); break;
                case SQUARE: sample = Math.signum(Math.sin(phase)); break;
                case SAW:    sample = 2.0 *(phase %(2* Math.PI)) / (2 *Math.PI) -1.0; break;
                case TRIANGLE: sample = 2.0 * Math.abs(2.0 * (phase % (2 * Math.PI)) / (2* Math.PI) -1)-1; break;
                default: sample = 0; break;
            }
            samples[i] = sample * volume;
            phase += 2.0 * Math.PI * frequency / sampleRate;

            //samples[i] = Math.sin(phase) * volume;

        }

        return samples;
    }

}
