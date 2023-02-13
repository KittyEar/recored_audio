package recored;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecordAudio {
    public static void main(String[] args) {
        // Define the format of audio file
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                44100.0F, 16, 2, 4, 44100.0F, false);

        // Define target data line
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        try {
            // Get a target data line
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            // Create a thread to capture audio data
            Thread thread = new CaptureThread(targetDataLine);
            thread.start();

            System.out.println("Recording started.");
            System.out.println("Press Enter to stop recording.");
            System.in.read();

            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Recording stopped.");
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class CaptureThread extends Thread {
    private TargetDataLine targetDataLine;

    public CaptureThread(TargetDataLine targetDataLine) {
        this.targetDataLine = targetDataLine;
    }

    @Override
    public void run() {
        // Define the audio file
        File audioFile = new File("audio.wav");

        try (AudioInputStream audioInputStream = new AudioInputStream(targetDataLine)) {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
