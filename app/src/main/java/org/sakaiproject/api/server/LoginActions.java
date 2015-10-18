package org.sakaiproject.api.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by vasilis on 10/18/15.
 */
public class LoginActions {

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
}
