package at.stefanirndorfer.maintainfc.util;

public class Constants {
    public static final int DATA_OFFSET = 13;
    public static final int OUR_HEADER_LENGTH = 8;
    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static final int EMPLOYEE_ID_SIZE = Integer.SIZE / Byte.SIZE;
    public static final int TIMESTAMP_SIZE = Long.SIZE / Byte.SIZE;
    public static final int EMPLOYEE_ID_INDEX = DATA_OFFSET + OUR_HEADER_LENGTH;
    public static final int TIMESTAMP_THIS_INDEX = EMPLOYEE_ID_INDEX + EMPLOYEE_ID_SIZE;
    public static final int TIMESTAMP_NEXT_INDEX = TIMESTAMP_THIS_INDEX + TIMESTAMP_SIZE;
    public static final int TOTAL_DATA_LENGTH = TIMESTAMP_NEXT_INDEX + TIMESTAMP_SIZE - DATA_OFFSET;

    public static final String FORMATABELE_TECH = "android.nfc.tech.NdefFormatable";

}
