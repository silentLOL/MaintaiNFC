package at.stefanirndorfer.maintainfc.viewmodel;

import android.content.Context;
import android.nfc.NdefMessage;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import androidx.lifecycle.ViewModel;
import timber.log.Timber;

import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.OUR_HEADER_LENGTH;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_SIZE;

public class ReadFromTagViewModel extends ViewModel {

    private ReadFromTagFragmentViewModelListener listener;

    public ReadFromTagViewModel() {
        super();
    }

    public void processNewMessage(Parcelable[] rawMessage) {
        NdefMessage[] msgs = null;
        if (rawMessage != null) {
            msgs = new NdefMessage[rawMessage.length];
            for (int i = 0; i < rawMessage.length; i++) {
                msgs[i] = (NdefMessage) rawMessage[i];
            }
        }
        buildTagViews(msgs);
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            Timber.w("msg is null or empty");
            return;
        }

        String redComment = "";
        int redEmployeeId = 0;
        long redTimeStamp = 0;

        //        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        int employeeIdOffset = languageCodeLength + 1 + OUR_HEADER_LENGTH;
        int timestampOffset = employeeIdOffset + EMPLOYEE_ID_SIZE;
        int commentOffset = timestampOffset + TIMESTAMP_SIZE;
        try {
            // get the employee id
            byte[] employeeIdBytes = new byte[EMPLOYEE_ID_SIZE];
            System.arraycopy(payload, employeeIdOffset, employeeIdBytes, 0, EMPLOYEE_ID_SIZE);
            redEmployeeId = ByteBuffer.wrap(employeeIdBytes).getInt();
            Timber.d("the employeeId red from the tag is: " + redEmployeeId);

            // get the timestamp
            byte[] timestampBytes = new byte[TIMESTAMP_SIZE];
            System.arraycopy(payload, timestampOffset, timestampBytes, 0, TIMESTAMP_SIZE);
            redTimeStamp = ByteBuffer.wrap(timestampBytes).getLong();
            Timber.d("the timestamp red from the tag is: " + redTimeStamp);

            // get the Comment
            redComment = new String(payload, commentOffset, payload.length - commentOffset, textEncoding);
            Timber.d("the comment red from the tag is: " + redComment);
        } catch (UnsupportedEncodingException e) {
            Timber.e(e);
        }
        Timber.d("setting text to the output view");
        listener.setSetTagContentToTextOutput(redEmployeeId, redTimeStamp, redComment);
    }

    public void setListener(ReadFromTagFragmentViewModelListener readFromTagFragmentViewModelListener) {
        this.listener = readFromTagFragmentViewModelListener;
    }

    public interface ReadFromTagFragmentViewModelListener {
        Context getContext();

        void setSetTagContentToTextOutput(int redEmployeeId, long redTimeStamp, String redComment);
    }
}