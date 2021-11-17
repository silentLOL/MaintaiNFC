package at.stefanirndorfer.maintainfc.viewmodel;

import android.nfc.NdefMessage;

import com.st.st25sdk.ndef.NDEFMsg;
import com.st.st25sdk.ndef.NDEFRecord;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.model.ReadFromTagResult;
import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.OUR_HEADER_LENGTH;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_NEXT_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.TOTAL_DATA_LENGTH;

public class ReadFromTagViewModel extends ViewModel {

    public SingleLiveEvent<MaintenanceData> resultData = new SingleLiveEvent<>();
    public SingleLiveEvent<ReadFromTagResult> readingResult = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> okButtonClicked = new SingleLiveEvent<>();

    public ReadFromTagViewModel() {
        super();
    }

    public void processNewMessage(Serializable serializedBytes) {
        byte[] payload = (byte[]) serializedBytes;


        //        NdefMessage[] msgs = null;
        //        if (nfcTag != null) {
        //            msgs = new NdefMessage[nfcTag.length];
        //            for (int i = 0; i < nfcTag.length; i++) {
        //                msgs[i] = (NdefMessage) nfcTag[i];
        //            }
        //        }
        //        buildTagViews(msgs);
        if (payload.length != TOTAL_DATA_LENGTH) {
            Timber.w("msg is null or empty");
            readingResult.setValue(ReadFromTagResult.EMPTY);
            return;
        }

        int redEmployeeId = 0;
        long redTimeStamp = 0;
        long redTimeStampNext = 0;


        int employeeIdOffset = OUR_HEADER_LENGTH;
        int timestampOffset = employeeIdOffset + EMPLOYEE_ID_SIZE;
        int timestampNextOffset = timestampOffset + TIMESTAMP_SIZE;

        // get the employee id
        byte[] employeeIdBytes = new byte[EMPLOYEE_ID_SIZE];
        System.arraycopy(payload, employeeIdOffset, employeeIdBytes, 0, EMPLOYEE_ID_SIZE);
        redEmployeeId = ByteBuffer.wrap(employeeIdBytes).getInt();
        Timber.d("the employeeId read from the tag is: " + redEmployeeId);

        // get the timestamp
        byte[] timestampBytes = new byte[TIMESTAMP_SIZE];
        System.arraycopy(payload, timestampOffset, timestampBytes, 0, TIMESTAMP_SIZE);
        redTimeStamp = ByteBuffer.wrap(timestampBytes).getLong();
        Timber.d("the timestamp read from the tag is: " + redTimeStamp);

        // get the timestampNext
        byte[] timestampNextBytes = new byte[TIMESTAMP_SIZE];
        System.arraycopy(payload, timestampNextOffset, timestampNextBytes, 0, TIMESTAMP_SIZE);
        redTimeStampNext = ByteBuffer.wrap(timestampNextBytes).getLong();
        Timber.d("the timestamp read from the tag is: " + redTimeStampNext);

        // todo: refactor
        readingResult.setValue(ReadFromTagResult.SUCCESS);
        resultData.setValue(new MaintenanceData(redTimeStamp, redTimeStampNext));
    }

    public void onOkButtonClicked() {
        okButtonClicked.setValue(true);
    }

}