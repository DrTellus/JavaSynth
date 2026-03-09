public class Oscillator {

    private double sampleRate;
    private double frequency;
    private double volume;


    public Oscillator(double sampleRate, double frequency, double volume){
        this.sampleRate = sampleRate;
        this.frequency =  frequency;
        this.volume = volume;
    }
    public double[] generateSamples(int totalSamples){
        double[] samples = new double[totalSamples];

        for (int i = 0; i < totalSamples; i++){
            double angle = 2.0 * Math.PI * frequency * i / sampleRate;
            samples[i] = Math.sin(angle) * volume;

        }

        return samples;
    }

}
