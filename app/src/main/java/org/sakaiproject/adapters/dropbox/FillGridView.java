package org.sakaiproject.adapters.dropbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.general.AttachmentType;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by vspallas on 09/03/16.
 */
public class FillGridView {

    private Context context;
    private SiteData siteData;
    private File rootPath;
    private ArrayList<ListItem> imageItems;
    // Check if the first level of the directory structure is the one showing
    private Boolean isTopParent = true;
    private File selectedFile;
    // Stores names of traversed directories
    private ArrayList<String> dirNames = new ArrayList<>();
    private Bitmap bitmap;
    private Map<String, Integer> fileSize;
    private DropboxAdapter dropboxAdapter;
    private RecyclerView recyclerView;
    private final String root;

    public FillGridView(Context context, SiteData siteData, Map<String, Integer> fileSize, RecyclerView recyclerView) {
        this.context = context;
        this.siteData = siteData;
        this.fileSize = fileSize;
        this.recyclerView = recyclerView;
        root = context.getFilesDir() + File.separator + User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "dropbox" + File.separator + "files";
        rootPath = new File(root);
    }

    public void loadFiles() {
        rootPath.mkdirs();

        if (rootPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    // Filters based on whether the file is hidden or not
                    return (sel.isFile() || sel.isDirectory());

                }
            };
            String[] fileList = rootPath.list(filter);

            imageItems = new ArrayList<>();

            for (int i = 0; i < fileList.length; i++) {
                File selected = new File(rootPath, fileList[i]);
                if (selected.isDirectory()) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_folder_white);
                    imageItems.add(new ListItem(bitmap, null, fileList[i], 0));
                } else {
                    extensionImage(fileList[i]);

                    if (!isTopParent) {
                        ArrayList temp = new ArrayList();
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_up);
                        temp.add(0, new ListItem(bitmap, null, "..", 0));
                        for (int ii = 0; ii < imageItems.size(); ii++) {
                            temp.add(ii + 1, imageItems.get(ii));
                        }

                        imageItems = temp;
                    }
                }
            }

            Collections.sort(imageItems);
            dropboxAdapter = new DropboxAdapter(imageItems, siteData.getId());
            recyclerView.setAdapter(dropboxAdapter);

            fillList();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.internal_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void fillList() {
        if (imageItems == null || imageItems.size() == 0) {
            return;
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String chosenFile = imageItems.get(position).getTitle();
                selectedFile = new File(rootPath + "/" + chosenFile);

                if (selectedFile.isDirectory() && !chosenFile.equalsIgnoreCase("..")) {

                    isTopParent = false;
                    // Adds chosen directory to list
                    dirNames.add(chosenFile);
                    imageItems = null;
                    rootPath = new File(selectedFile + "");
                    loadFiles();
                } // Checks if '..' was clicked
                else if (chosenFile.equalsIgnoreCase("..")) {
                    backButtonPressed();
                } else {
                    // open file
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void backButtonPressed() {
        if (!isTopParent) {
            // present directory removed from list
            String s = dirNames.remove(dirNames.size() - 1);

            // rootPath modified to exclude present directory
            rootPath = new File(rootPath.toString().substring(0,
                    rootPath.toString().lastIndexOf(s)));
            imageItems = null;

            if (dirNames.isEmpty() || rootPath.equals(root)) {
                isTopParent = true;
            }

            loadFiles();
        }
    }

    private void extensionImage(String file) {
        AttachmentType type = ActionsHelper.getAttachmentType(file);
        switch (type) {
            case TXT:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_white);
                break;
            case PDF:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_pdf);
                break;
            case WORD:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_word);
                break;
            case EXCEL:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_excel);
                break;
            case POWERPOINT:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_powerpoint);
                break;
            case AUDIO:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_audio);
                break;
            case VIDEO:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_video);
                break;
            case ZIP:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_zip);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_file_white);
                break;
        }

        imageItems.add(new ListItem(bitmap, null, file, fileSize.get(new File(rootPath + "/" + file).getAbsolutePath())));
    }
}
