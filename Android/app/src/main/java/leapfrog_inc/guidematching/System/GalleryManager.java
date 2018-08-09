package leapfrog_inc.guidematching.System;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;

public class GalleryManager {

    public static int requestCodeGallery = 1000;
    public static int requestCodePermission = 1001;

    private static GalleryManager manager = new GalleryManager();
    private Uri mImageUri;
    private Callback mCallback;

    private GalleryManager(){}

    public static GalleryManager getInstance() {
        return manager;
    }

    public void openGallery(Activity activity, Callback callback) {

        mCallback = callback;

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                open(activity);
            }
            else{
                ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, requestCodePermission);
            }
        } else {
            open(activity);
        }
    }

    private void open(Activity activity) {

        Intent intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
        intentGallery.setType("image/jpeg");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "tmp.jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mImageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        Intent intent = Intent.createChooser(intentCamera, "画像の選択");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intentGallery});
        activity.startActivityForResult(intent, requestCodeGallery);
    }

    public void didGrantPermission(Activity activity) {
        open(activity);
    }

    public void didSelectImage(Activity activity, Intent data) {

        Uri uri = null;
        if (data != null) {
            Uri dataUri = data.getData();
            if (dataUri != null) {
                uri = dataUri;
            }
        }
        if (uri == null) {
            uri = mImageUri;
        }
        if(uri == null) {
            return;
        }
        MediaScannerConnection.scanFile(activity, new String[]{ uri.getPath() }, new String[]{"image/jpeg"}, null);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (Exception e) {
            return;
        }
        int bmpWidth = bitmap.getWidth();
        Matrix scale = new Matrix();
        scale.postScale((200.0f / (float)bmpWidth), (200.0f / (float)bmpWidth));
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), scale, false);
        bitmap.recycle();
        bitmap = null;

        mCallback.didSelectImage(scaledBitmap);
    }

    public interface Callback {
        void didSelectImage(Bitmap bitmap);
    }
}
