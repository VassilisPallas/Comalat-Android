package org.sakaiproject.api.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.sakaiproject.sakai.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by vasilis on 10/18/15.
 */
public class Actions {

    /**
     * Convert InputStream to String
     *
     * @param inputStream the stream for convertion
     * @return the converted String
     * @throws IOException
     */
    public static String readJsonStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        if (inputStream != null)
            inputStream.close();

        return result;
    }

    /**
     * Write json file for offline mode
     *
     * @param context
     * @param jsonString the String that contains tha json
     * @param fileName   the name of the file
     * @throws IOException
     */
    public static void writeJsonFile(Context context, String jsonString, String fileName) throws IOException {
        File path = context.getFilesDir();

        File file = new File(path, fileName + ".json");

        FileOutputStream stream;
        stream = new FileOutputStream(file);
        stream.write(jsonString.getBytes());
        stream.close();
    }

    /**
     * Read the saved json file for offline mode
     *
     * @param context
     * @param fileName the name of the file
     * @return the json string
     * @throws IOException
     */
    public static String readJsonFile(Context context, String fileName) throws IOException {
        File path = context.getFilesDir();
        File file = new File(path, fileName + ".json");

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(file);
        in.read(bytes);

        in.close();
        return new String(bytes);
    }

    /**
     * save image to internal storage
     *
     * @param context
     * @param bitmap    the bitmap for save
     * @param imageName the name for the image
     * @throws IOException
     */
    public static void saveImage(Context context, Bitmap bitmap, String imageName) throws IOException {
        File path = context.getFilesDir();

        File file = new File(path, imageName + ".jpg");

        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();

    }

    /**
     * get image from internal storage
     *
     * @param context
     * @param name    the name for the image
     * @throws IOException
     */
    public static Bitmap getImage(Context context, String name) throws FileNotFoundException {
        File path = context.getFilesDir();
        File f = new File(path, name + ".jpg");
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

        return b;
    }

    /**
     * get the file type of the attachment
     *
     * @param file the name of the file
     * @return an enum of file type
     */
    public static AttachmentType getAttachmentType(String file) {

        if (file.charAt(file.length() - 4) == '.' || file.charAt(file.length() - 5) == '.') {

            String extension = file.substring(file.lastIndexOf('.') + 1).toLowerCase();

            switch (extension.toLowerCase()) {
                case "txt":
                case "srt":
                case "rtf":
                case "nfo":
                case "html":
                case "css":
                case "xml":
                case "json":
                    return AttachmentType.TXT;
                case "pdf":
                    return AttachmentType.PDF;
                case "doc":
                case "dot":
                case "docx":
                case "docm":
                case "dotx":
                case "dotm":
                case "docb":
                    return AttachmentType.WORD;
                case "xls":
                case "xlt":
                case "xlm":
                case "xlsx":
                case "xlsm":
                case "xltx":
                case "xltm":
                case "xlsb":
                case "xla":
                case "xlam":
                case "xll":
                case "xlw":
                    return AttachmentType.EXCEL;
                case "ppt":
                case "pot":
                case "pps":
                case "pptx":
                case "pptm":
                case "potx":
                case "potm":
                case "ppam":
                case "ppsx":
                case "ppsm":
                case "sldx":
                case "sldm":
                    return AttachmentType.POWERPOINT;
                case "wma":
                case "wav":
                case "mp3":
                case "mid":
                case "m4a":
                    return AttachmentType.AUDIO;
                case "gif":
                case "jpeg":
                case "jpg":
                case "jif":
                case "jfif":
                case "jp2":
                case "jpx":
                case "j2k":
                case "j2c":
                case "fpx":
                case "png":
                case "dng":
                    return AttachmentType.IMAGE;
                case "mkv":
                case "flv":
                case "ogv":
                case "ogg":
                case "avi":
                case "mov":
                case "wmv":
                case "mp4":
                case "m4p":
                case "m4v":
                case "mpg":
                case "mp2":
                case "mpeg":
                case "raw":
                    return AttachmentType.VIDEO;
                case "rar":
                case "zip":
                case "tar":
                    return AttachmentType.ZIP;
                default:
                    return AttachmentType.UNKwOWN;
            }

        }
        return AttachmentType.URL;
    }

    /**
     * get the image for the file from resources as a bitmap
     *
     * @param context the Application context
     * @param name the file name
     * @return the image as bitmap
     */
    public static Bitmap getAttachmentTypeImage(Context context, String name) {
        Bitmap image;
        switch (getAttachmentType(name)) {
            case URL:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_public_black);
            case AUDIO:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_audiotrack_black);
            case VIDEO:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_videocam_black);
            case IMAGE:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_image_black);
            case ZIP:
            case WORD:
            case TXT:
            case POWERPOINT:
            case PDF:
            case EXCEL:
            case UNKwOWN:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_insert_drive_file_black);
        }
        return null;
    }


    public static String getMonthfromId(String index) {
        switch (index) {
            case "1":
                return "Jan";
            case "2":
                return "Feb";
            case "3":
                return "Mar";
            case "4":
                return "Apr";
            case "5":
                return "May";
            case "6":
                return "Jun";
            case "7":
                return "Jul";
            case "8":
                return "Aug";
            case "9":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
        }
        return null;
    }

}
