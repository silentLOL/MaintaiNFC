package at.stefanirndorfer.maintainfc.viewmodel;

import com.st.st25sdk.NFCTag;
import com.st.st25sdk.STException;
import com.st.st25sdk.type5.STType5PasswordInterface;
import com.st.st25sdk.type5.st25dv.ST25DVTag;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import at.stefanirndorfer.maintainfc.model.WriteToTagResult;
import at.stefanirndorfer.maintainfc.util.Constants;
import timber.log.Timber;

import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.OUR_HEADER_LENGTH;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_NEXT_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_THIS_INDEX;

public class WriteToTagViewModel extends ViewModel {
    private static final Integer HARD_CODED_EMPLOYEE_ID = 1; // this will eventually be replaced with actual data
    public SingleLiveEvent<WriteToTagResult> writingResult = new SingleLiveEvent<>();

    private STType5PasswordInterface mSTType5PasswordInterface;

    public WriteToTagViewModel() {
    }

    /**
     * we write in the following order to the tag:
     * - header
     * - employeeId
     * - timestamp
     * - timestamp next
     *
     * @param data
     * @param tag
     * @throws IOException
     */
    public void write(MaintenanceData data, NFCTag tag) throws IOException {
        long timeStampNext = data.getNextTimestamp();
        Integer employeeId = HARD_CODED_EMPLOYEE_ID;
        Long timestamp = data.getTimestamp();
        if (tag == null) {
            Timber.d("tag is null");
            return;
        }

        // This dialog box will fail if the tag doesn't implement a STType5PasswordInterface
        try {
            mSTType5PasswordInterface = (STType5PasswordInterface) tag;
        } catch (ClassCastException e) {
            // Tag not implementing STType5PasswordInterface
            Timber.e("Error! Tag not implementing STType5PasswordInterface!");
            writingResult.postValue(WriteToTagResult.INVALID_TAG_TYPE);
            return;
        }

        byte[] employeeIdBytes = ByteBuffer.allocate(EMPLOYEE_ID_SIZE).putInt(employeeId).array();
        byte[] timestampThisBytes = ByteBuffer.allocate(TIMESTAMP_SIZE).putLong(timestamp).array();
        byte[] timestampNextBytes = ByteBuffer.allocate(TIMESTAMP_SIZE).putLong(timeStampNext).array();
        int payloadLen = employeeIdBytes.length + timestampThisBytes.length + timestampNextBytes.length;
        byte[] headerBytes = new byte[OUR_HEADER_LENGTH];
        short len = (short) (payloadLen + OUR_HEADER_LENGTH);
        headerBytes[4] = (byte) (len >>> 8);
        headerBytes[5] = (byte) len;
        byte[] tempHeader = Arrays.copyOfRange(headerBytes, 0, 6);
        int crc = calculate_crc(tempHeader);
        Timber.d("crc: " + crc);
        headerBytes[6] = (byte) (crc >>> 8);
        headerBytes[7] = (byte) crc;
        byte[] fullBytes = new byte[OUR_HEADER_LENGTH + payloadLen];
        System.arraycopy(headerBytes, 0, fullBytes, 0, OUR_HEADER_LENGTH);
        System.arraycopy(employeeIdBytes, 0, fullBytes, EMPLOYEE_ID_INDEX, employeeIdBytes.length);
        System.arraycopy(timestampThisBytes, 0, fullBytes, TIMESTAMP_THIS_INDEX, timestampThisBytes.length);
        System.arraycopy(timestampNextBytes, 0, fullBytes, TIMESTAMP_NEXT_INDEX, timestampNextBytes.length);

        ST25DVTag st25tag = (ST25DVTag) tag;
        try {
            st25tag.presentPassword(1, Constants.TAG_PASSWORD);
        } catch (STException e) {
            Timber.e(e, "error presenting password");
            writingResult.postValue(WriteToTagResult.FAIL_INVALID_PASSWORD);
            return;
        }

        try {
            tag.writeBytes(Constants.DATA_OFFSET, fullBytes);

            writingResult.postValue(WriteToTagResult.SUCCESS);
        } catch (STException e) {
            Timber.d("error writing to Tag");
            Timber.e(e);
            writingResult.postValue(WriteToTagResult.FAIL);
        }
    }

    int calculate_crc(byte[] bytes) {
        int i;
        int crc_value = 0;
        for (int len = 0; len < bytes.length; len++) {
            for (i = 0x80; i != 0; i >>= 1) {
                if ((crc_value & 0x8000) != 0) {
                    crc_value = (crc_value << 1) ^ 0x8005;
                } else {
                    crc_value = crc_value << 1;
                }
                if ((bytes[len] & i) != 0) {
                    crc_value ^= 0x8005;
                }
            }
        }
        return crc_value;
    }

}