package fr.m1_tlse3.mcs.corpusmic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by silmathoron on 12/11/2016.
 */

public class CorpusAdapter extends BaseAdapter {
    private final String fileExt;
    private final String folder;
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 16000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording;

    private List<String> commands = new ArrayList<>();
    private Context mContext;
    private final String TAG = "CorpusMic/CorpusAdapter";

    public CorpusAdapter(Context context) {
        mContext = context;
        commands = Arrays.asList(mContext.getResources().getStringArray(R.array.commands));
        bufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        folder = mContext.getResources().getString(R.string.app_name);
        fileExt = mContext.getResources().getString(R.string.file_ext);
    }

    @Override
    public int getCount() {
        return commands.size();
    }

    @Override
    public String getItem(int position) {
        return commands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;
        // Notre vue n'a pas encore été construite, nous le faisons
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            v = inflater.inflate(R.layout.command_list_item, parent, false);
        } // Notre vue peut être récupérée, nous le faisons
        else {
            v = convertView;
        }
        ((TextView)v.findViewById(R.id.word)).setText(commands.get(position));
        v.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setupRecordButton(v,position);
        setupPlayButton(v,position);

        return v;
    }

    private void setupPlayButton(final View v, final int position){
        ImageButton play_button = (ImageButton) v.findViewById(R.id.play_button);
        String filepath = Environment.getExternalStorageDirectory().getPath();
        final File file = new File( getFilename(position) );
        play_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer mPlayer = MediaPlayer.create(mContext, Uri.fromFile(file));
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                mPlayer.start();
            }
        });
    }

    private void setupRecordButton(final View v, final int position){
        ImageButton record_button = (ImageButton) v.findViewById(R.id.record_button);
        setButtonsRessources(v, position);
        record_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View button, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG,"Started recording command "+position);
                        startRecording();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"Stoped recording");
                        stopRecording(position);
                        setButtonsRessources(v, position);
                        break;
                }
                return false;
            }
        });
    }

    private void setButtonsRessources(View v, int position) {
        ImageButton record_button = (ImageButton) v.findViewById(R.id.record_button);
        ImageButton play_button = (ImageButton) v.findViewById(R.id.play_button);
        if(fileExists(position)){
            record_button.setImageResource(R.drawable.ic_mic);
            play_button.setEnabled(true);
            play_button.setClickable(true);
        }else{
            record_button.setImageResource(R.drawable.ic_mic_off);
            play_button.setEnabled(false);
            play_button.setClickable(false);
        }
    }

    private boolean fileExists(int position){
        File file = new File(getFilename(position));
        return file.exists();
    }

    private void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);
        if(recorder.getState()==1)
            recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");
        recordingThread.start();
    }

    private String getFilename(int position){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, folder);

        if(!file.exists()){
            file.mkdirs();
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Boolean use_Gender = sharedPref.getBoolean("pref_useGender",false);
        String gender = sharedPref.getString("pref_gender","O");
        gender = use_Gender?gender:"";

        /*int pref_userNumber = Integer.getInteger(sharedPref.getString("pref_userNumber","00"));
        String userNumber = String.format("%02d",2); //fixme*/
        String userNumber = sharedPref.getString("pref_userNumber","99");

        String command = mContext.getResources().getStringArray(R.array.commands_trimmed)[position];

        String fileName = file.getAbsolutePath() + "/" + gender + userNumber +"_"+ command + fileExt;
        return fileName;
    }

    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, folder);

        if(!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

        if(tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void writeAudioDataToFile() {
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int read = 0;

        if (null != os) {
            while (isRecording) {
                read = recorder.read(data, 0, bufferSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording(int position){
        if(null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        String filename = getFilename(position);
        Boolean success = copyWaveFile(getTempFilename(),filename);
        deleteTempFile();
        Toast toast;
        if(success){
            String file = mContext.getResources().getString(R.string.file);
            String created = mContext.getResources().getString(R.string.created);
            toast = Toast.makeText(mContext,file+filename+created,Toast.LENGTH_LONG);
        }else {
            String error = mContext.getResources().getString(R.string.error);
            toast = Toast.makeText(mContext,error,Toast.LENGTH_LONG);
        }
        toast.show();

    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }

    private boolean copyWaveFile(String inFilename,String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            Log.d(TAG,"File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }
}
