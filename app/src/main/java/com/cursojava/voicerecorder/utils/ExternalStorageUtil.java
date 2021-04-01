package com.cursojava.voicerecorder.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class ExternalStorageUtil {

    public static boolean isExternalStorageMounted () {
        String dirState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(dirState)) {
            return  true;
        } else {
            return false;
        }
    }

    public static String getPrivateExternalStorageBaseDir(Context context) {
        String privateDirPath = "";
        if (isExternalStorageMounted()) {
            File file = context.getExternalFilesDir("Gravacoes");
            privateDirPath = file.getAbsolutePath();
        }
        return  privateDirPath;
    }

}
