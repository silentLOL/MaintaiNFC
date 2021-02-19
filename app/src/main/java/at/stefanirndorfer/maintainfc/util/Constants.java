package at.stefanirndorfer.maintainfc.util;

public class Constants {
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    public static final int OUR_HEADER_LENGTH = 8;
    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static final int EMPLOYEE_ID_SIZE = Integer.SIZE / Byte.SIZE;
    public static final int TIMESTAMP_SIZE = Long.SIZE / Byte.SIZE;
    public static final int EMPLOYEE_ID_INDEX = OUR_HEADER_LENGTH;
    public static final int TIMESTAMP_THIS_INDEX = OUR_HEADER_LENGTH + EMPLOYEE_ID_SIZE;
    public static final int TIMESTAMP_NEXT_INDEX = TIMESTAMP_THIS_INDEX + TIMESTAMP_SIZE;
    public static final int COMMENT_INDEX = TIMESTAMP_NEXT_INDEX + TIMESTAMP_SIZE;


    // validation constants
    public static final int MAX_COMMENT_LENGTH = 200; // todo: Find out how long it actually can be. This is just a made up number
}
