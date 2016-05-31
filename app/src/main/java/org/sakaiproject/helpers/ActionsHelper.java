package org.sakaiproject.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.logout.Logout;
import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.customviews.custom_volley.InputStreamVolleyRequest;
import org.sakaiproject.general.AttachmentType;
import org.sakaiproject.general.Connection;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.UserMainNavigationDrawerHelper;
import org.sakaiproject.sakai.MainActivity;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 10/18/15.
 */
public class ActionsHelper {

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
    public static void writeJsonFile(Context context, String jsonString, String fileName, String path) throws IOException {
        File externalStoragePath = new File(context.getFilesDir(), path);

        File file = new File(externalStoragePath, fileName + ".json");

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
    public static String readJsonFile(Context context, String fileName, String path) throws IOException {

        File externalStoragePath = new File(context.getFilesDir(), path);
        File file = new File(externalStoragePath, fileName + ".json");

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
    public static void saveImage(Context context, Bitmap bitmap, String imageName, String path) throws IOException {
        File externalStoragePath = new File(context.getFilesDir(), path);

        File file = new File(externalStoragePath, imageName);

        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();

    }

    public static File saveFile(Context context, byte[] response, String path, InputStreamVolleyRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        File file = null;
        try {
            if (response != null) {
                String content = request.responseHeaders.get("Content-Disposition");
                StringTokenizer st = new StringTokenizer(content, "=");
                //String[] arrTag = st.toArray();

                //String filename = arrTag[1];
                String filename = st.nextToken();
                filename = st.nextToken();
                filename = filename.replace(":", ".");

                //covert response to input stream
                InputStream input = new ByteArrayInputStream(response);
                if (ActionsHelper.createExternalDirIfNotExists(path)) {
                    file = new File(path, filename);
                    file.createNewFile();
                }
                map.put("resume_path", file.toString());
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();

                output.close();
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * get image from internal storage
     *
     * @param context
     * @param name    the name for the image
     * @throws IOException
     */
    public static Bitmap getImage(Context context, String name, String path) throws FileNotFoundException {
        File externalStoragePath = new File(context.getFilesDir(), path);
        File f = new File(externalStoragePath, name + ".jpg");
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
        if (file.startsWith("http") || file.startsWith("ftp") || file.startsWith("https"))
            return AttachmentType.URL;
        return AttachmentType.UNKwOWN;
    }


    public static Intent openFile(Context context, File file) {
        String fileName = file.getName();

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        String type = "application/" + extension;

        if (getAttachmentType(fileName) == AttachmentType.IMAGE) {
            type = "image/*";
        } else if (getAttachmentType(fileName) == AttachmentType.AUDIO) {
            type = "audio/*";
        } else if (getAttachmentType(fileName) == AttachmentType.VIDEO) {
            type = "video/*";
        } else if (getAttachmentType(fileName) == AttachmentType.WORD) {
            type = "application/msword";
        } else if (getAttachmentType(fileName) == AttachmentType.EXCEL) {
            type = "application/vnd.ms-excel";
        } else if (getAttachmentType(fileName) == AttachmentType.POWERPOINT) {
            type = "application/vnd.ms-powerpoint";
        } else if (getAttachmentType(fileName) == AttachmentType.TXT) {
            type = "text/*";
        }

        Uri path = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooser = Intent.createChooser(intent, context.getResources().getString(R.string.open_file_with));
        intent.setDataAndType(path, type);
        return chooser;
    }

    /**
     * get the image for the file from resources as a bitmap
     *
     * @param context the Application context
     * @param name    the file name
     * @return the image as bitmap
     */
    public static Bitmap getAttachmentTypeImage(Context context, String name) {
        switch (getAttachmentType(name)) {
            case URL:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_public);
            case AUDIO:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_audiotrack);
            case VIDEO:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_videocam);
            case IMAGE:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_image);
            case ZIP:
            case WORD:
            case TXT:
            case POWERPOINT:
            case PDF:
            case EXCEL:
            case UNKwOWN:
                return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_insert_drive_file);
        }
        return null;
    }


    /**
     * get the month from a string index
     *
     * @param index the month index
     * @return the abbreviation of the month with the selected index
     */
    public static String getMonthfromIndex(String index) {
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

    /**
     * get the month from a index
     *
     * @param index the month index
     * @return the abbreviation of the month with the selected index
     */
    public static String getMonthfromIndex(int index) {
        switch (index) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return null;
    }

    /**
     * get the month index from month abbreviation
     *
     * @param month the abbreviation of the month
     * @return the index from the abbreviation month
     */
    public static String getIndexfromMonth(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return null;
        }
    }

    /**
     * change color on a drawable image
     *
     * @param context the context
     * @param imageId the id from the image
     * @param color   the color hex
     * @return
     */
    public static Drawable setCustomDrawableColor(Context context, int imageId, int color) {

        try {
            Drawable image;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image = context.getResources().getDrawable(imageId, context.getTheme());
            } else {
                image = context.getResources().getDrawable(imageId);
            }
            if (image != null)
                image.setColorFilter(color, PorterDuff.Mode.SRC_IN);

            return image;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * user logout
     *
     * @param context the context
     * @param waiter  the waiter object for the session expiration
     */
    public static void logout(final Context context, final Waiter waiter) {

        AlertDialog.Builder adb = new AlertDialog.Builder(((AppCompatActivity) context).getSupportActionBar().getThemedContext());

        adb.setTitle(context.getResources().getString(R.string.logout_message));

        adb.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (NetWork.getConnectionEstablished()) {
                            waiter.stop = true;
                            try {
                                Logout logout = new Logout();
                                logout.logout(context.getResources().getString(R.string.url) + "session/" + Connection.getSessionId());
                                clear();

                                Intent i = new Intent(((AppCompatActivity) context).getApplication(), MainActivity.class);
                                context.startActivity(i);
                                ((AppCompatActivity) context).finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            clear();

                            Intent i = new Intent(((AppCompatActivity) context).getApplication(), MainActivity.class);
                            context.startActivity(i);
                            ((AppCompatActivity) context).finish();
                        }
                    }
                });

                thread.start();

            }
        });

        adb.setNegativeButton(((AppCompatActivity) context).getResources().getString(R.string.cancel), null);

        Dialog d = adb.show();
    }

    public static void clear() {
        User.nullInstance();
        Profile.nullInstance();
        Connection.nullSessionId();
        SiteData.getSites().clear();
        SiteData.getProjects().clear();
        EventsCollection.getEventsList().clear();
        EventsCollection.getMonthEvents().clear();
    }

    /**
     * Lazy method for change Fragments
     *
     * @param f       the fragment to move
     * @param id      the FrameLayout id
     * @param context the context
     */
    public static void selectFragment(Fragment f, int id, Context context) {
        FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, f);
        fragmentTransaction.commit();
    }

    /**
     * delete the html tags on some texts like description etc
     *
     * @param text the text to remove the html tags
     * @return the text without html tags
     */
    public static String deleteHtmlTags(String text) {
        //text = URLDecoder.decode(text, "UTF-8");

        text = text.replaceAll("<p .+?>", "");
        text = text.replaceAll("<\\/p>", "");
        //text = text.replaceAll("<span .+?>", "");
        //text = text.replaceAll("<\\/span>", "");
        text = text.replaceAll("<div .+?>", "");
        text = text.replaceAll("<\\/div>", "");
        text = text.replaceAll("(\\r)+", "");
        text = text.replaceAll("(\\n)+", "\n");
        text = text.replaceAll("&nbsp;", " ");
        text = text.replaceAll("( )+", " ");
        text = text.replaceAll("(<br>|</br>|</ br>)+", "\n");
        //text = text.replaceAll("<img .+?\\/>", "");
        //text = text.replaceAll("<audio .+>.+<\\/audio>", "");
        return text;
    }

    /**
     * find audios tags on some texts like description, MOTD etc
     *
     * @param text the text to search for audios tags
     * @return the list with the audio urls
     */
    public static List<String> findAudios(String text) {
        List<String> audioPaths = new ArrayList<>();

        Pattern pattern = Pattern.compile("<audio .+>.+<\\/audio>");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            Pattern p = Pattern.compile("src=\".+?\"");
            Matcher m = p.matcher(matcher.group(0));
            if (m.find()) {
                String found = m.group(0);
                found = found.replaceAll("src( )?=( )?", "");
                found = found.replaceAll("\"", "");
                found = found.replaceAll("\\\\", "");
                audioPaths.add(found);
            }
        }

        return audioPaths;
    }

    /**
     * try to find the directory on the given path. If the directory does not exists,
     * then creates the directory and returns true. If exists returns true. Otherwise
     * if it can't create the directory returns false
     *
     * @param context
     * @param path    the given path
     * @return true if the directory exists or if it has created successfully, otherwise returns false
     */
    public static boolean createDirIfNotExists(Context context, String path) {
        boolean ret = true;

        File externalStoragePath = context.getFilesDir();
        File file = new File(externalStoragePath, path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("Log :: ", "Problem creating folder");
                ret = false;
            }
        }
        return ret;
    }

    /**
     * try to find the directory on the given path. If the directory does not exists,
     * then creates the directory and returns true. If exists returns true. Otherwise
     * if it can't create the directory returns false
     *
     * @param path the given path
     * @return true if the directory exists or if it has created successfully, otherwise returns false
     */
    public static boolean createExternalDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("Log :: ", "Problem creating folder");
                ret = false;
            }
        }
        return ret;
    }
}
