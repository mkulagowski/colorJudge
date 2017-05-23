package mkk13.colorjudge;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkk-1 on 14/05/2017.
 */

public class RequestPermissions {
    // Storage Permissions
    public static final int REQUEST_PERMISSIONS = 123;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        boolean gotAllPerms = true;
        for (String perm : PERMISSIONS)
            gotAllPerms &= (ContextCompat.checkSelfPermission(activity.getApplicationContext(), perm) != PackageManager.PERMISSION_GRANTED);

        if (!gotAllPerms) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSIONS
            );
        }
    }


}