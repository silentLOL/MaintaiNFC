package at.stefanirndorfer.maintainfc.viewmodel;

import android.nfc.NdefMessage;
import android.os.Parcelable;

import java.nio.ByteBuffer;

import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.model.ReadFromTagResult;
import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

import static at.stefanirndorfer.maintainfc.util.Constants.COMMENT_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.OUR_HEADER_LENGTH;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_SIZE;

public class ReadFromTagViewModel extends ViewModel {

    public SingleLiveEvent<MaintenanceData> resultData = new SingleLiveEvent<>();
    public SingleLiveEvent<ReadFromTagResult> readingResult = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> okButtonClicked = new SingleLiveEvent<>();

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
            readingResult.setValue(ReadFromTagResult.EMPTY);
            return;
        }

        int redEmployeeId = 0;
        long redTimeStamp = 0;
        long redTimeStampNext = 0;

        //        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        if (payload.length == 0) {
            Timber.w("msg is null or empty");
            readingResult.setValue(ReadFromTagResult.EMPTY);
            return;
        }
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        // check if the content is long enough
        if (languageCodeLength + 1 + COMMENT_INDEX >= payload.length) {
            Timber.w("Tags content cannot be read");
            readingResult.setValue(ReadFromTagResult.FAIL);
            return;
        }

        int employeeIdOffset = languageCodeLength + 1 + OUR_HEADER_LENGTH;
        int timestampOffset = employeeIdOffset + EMPLOYEE_ID_SIZE;
        int timestampNextOffset = timestampOffset + TIMESTAMP_SIZE;

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

        // get the timestampNext
        byte[] timestampNextBytes = new byte[TIMESTAMP_SIZE];
        System.arraycopy(payload, timestampNextOffset, timestampNextBytes, 0, TIMESTAMP_SIZE);
        redTimeStampNext = ByteBuffer.wrap(timestampNextBytes).getLong();
        Timber.d("the timestamp red from the tag is: " + redTimeStampNext);

        // todo: refactor
        readingResult.setValue(ReadFromTagResult.SUCCESS);
        resultData.setValue(new MaintenanceData(redEmployeeId, redTimeStamp, redTimeStampNext));
    }

    public void onOkButtonClicked() {
        okButtonClicked.setValue(true);
    }

}