package at.stefanirndorfer.maintainfc.viewmodel;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.text.TextUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import androidx.lifecycle.ViewModel;
import timber.log.Timber;

import static at.stefanirndorfer.maintainfc.util.Constants.COMMENT_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.EMPLOYEE_ID_SIZE;
import static at.stefanirndorfer.maintainfc.util.Constants.OUR_HEADER_LENGTH;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_NEXT_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_THIS_INDEX;
import static at.stefanirndorfer.maintainfc.util.Constants.TIMESTAMP_SIZE;

public class WriteToTagViewModel extends ViewModel {

    private WriteToTagViewModelListener listener;

    public void setListener(WriteToTagViewModelListener listener) {
        this.listener = listener;
    }

    /**
     * we write in the following order to the tag:
     * - header
     * - employeeId
     * - timestamp
     * - comment
     *
     * @param timestamp
     * @param employeeId
     * @param comment
     * @param tag
     * @throws IOException
     * @throws FormatException
     */
    public void write(long timestamp, int employeeId, String comment, Tag tag) throws IOException, FormatException {
        long timeStampNext = timestamp + 20000; /*for testing purposes -- replace with real data*/
        if (tag == null) {
            Timber.d("tag is null");
            return;
        }
        if (comment == null || TextUtils.isEmpty(comment)){
            comment = "";
        }
        byte[] employeeIdBytes = ByteBuffer.allocate(EMPLOYEE_ID_SIZE).putInt(employeeId).array();
        byte[] timestampThisBytes = ByteBuffer.allocate(TIMESTAMP_SIZE).putLong(timestamp).array();
        byte[] timestampNextBytes = ByteBuffer.allocate(TIMESTAMP_SIZE).putLong(timeStampNext).array();
        byte[] commentBytes = comment.getBytes();
        int payloadLen = employeeIdBytes.length + timestampThisBytes.length + commentBytes.length;
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
        System.arraycopy(commentBytes, 0, fullBytes, COMMENT_INDEX, commentBytes.length);
        NdefRecord[] records = {createRecord(fullBytes)};
        NdefMessage message = new NdefMessage(records);
        try {
            // this is needed in case you have a new NFC Tag and it needs to be formatted before use
            //            String[] techList = tag.getTechList();
            //            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            //            ndefFormatable.connect();
            //            ndefFormatable.format(message);
            //            ndefFormatable.close();

            // Get an instance of Ndef for the tag.
            Ndef ndef = Ndef.get(tag);
            // Enable I/O
            ndef.connect();
            // Write the message
            ndef.writeNdefMessage(message);
            // Close the connection
            ndef.close();
            listener.successfullyWrittenToTag(true);
        } catch (Exception e) {
            Timber.d("error writing connecting");
            Timber.e(e);
            listener.successfullyWrittenToTag(false);
        }
    }

    private NdefRecord createRecord(byte[] textBytes) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
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


    public interface WriteToTagViewModelListener {
        void successfullyWrittenToTag(boolean isSuccess);
    }
}