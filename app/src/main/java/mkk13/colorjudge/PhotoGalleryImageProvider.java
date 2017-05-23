package mkk13.colorjudge;

/**
 * Created by mkk-1 on 17/05/2017.
 */
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mkk13.colorjudge.Adapters.PhotoItem;

/**
 * This is a helper utility which automatically fetches paths to full size and thumbnail sized gallery images.
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoGalleryImageProvider {

    // Consts
    public static final int IMAGE_RESOLUTION = 15;

    // Buckets where we are fetching images from.
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);

    /**
     * Fetch both full sized images and thumbnails via a single query.
     * Returns all images not in the Camera Roll.
     * @param context
     * @return
     */
    public static List<PhotoItem> getAlbumThumbnails(Context context){

        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        long id = 0;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID};

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);
        ArrayList<PhotoItem> result = new ArrayList<PhotoItem>(cursor.getCount());

        int column_index_id = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            id = Long.valueOf(cursor.getString(column_index_id));

            listOfAllImages.add(absolutePathOfImage);
            // Create the list item.
            Uri photoUri = Uri.parse(absolutePathOfImage);
            Uri thumbUri = photoUri;

            PhotoItem newItem = new PhotoItem(thumbUri, photoUri, id);
            result.add(newItem);
        }


/*
        //final String[] projection = {MediaStore.Images.Thumbnails.DATA,MediaStore.Images.Thumbnails.IMAGE_ID};
        final String[] projection2 = {MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID};

        Cursor thumbnailsCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                //MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection2, // Which columns to return
                null,       // Return all rows
                null,
                null);

        // Extract the proper column thumbnails
        int thumbnailColumnIndex = thumbnailsCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        ArrayList<PhotoItem> result = new ArrayList<PhotoItem>(thumbnailsCursor.getCount());

        if (thumbnailsCursor.moveToFirst()) {
            do {
                // Generate a tiny thumbnail version.
                int thumbnailImageID = thumbnailsCursor.getInt(thumbnailColumnIndex);
                String thumbnailPath = thumbnailsCursor.getString(thumbnailImageID);
                Uri thumbnailUri = Uri.parse(thumbnailPath);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                String filePath = thumbnailUri.getPath();
                File thumbFile = new File(filePath);
                String absPath = thumbFile.getAbsolutePath();
                BitmapFactory.decodeFile(absPath, options);
                int thumbHeight = options.outHeight;
                int thumbWidth = options.outWidth;

                //if (thumbHeight > 0) {
                    Uri fullImageUri = uriToFullImage(thumbnailsCursor, context);

                    // Create the list item.
                    PhotoItem newItem = new PhotoItem(thumbnailUri, fullImageUri);
                    result.add(newItem);
               // }
            } while (thumbnailsCursor.moveToNext());
        }
        thumbnailsCursor.close();
*/
        return result;
    }

    /**
     * Get the path to the full image for a given thumbnail.
     */
    private static Uri uriToFullImage(Cursor thumbnailsCursor, Context context){
        String imageId = thumbnailsCursor.getString(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));

        // Request image related to this thumbnail
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor imagesCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[]{imageId}, null);

        if (imagesCursor != null && imagesCursor.moveToFirst()) {
            int columnIndex = imagesCursor.getColumnIndex(filePathColumn[0]);
            String filePath = imagesCursor.getString(columnIndex);
            imagesCursor.close();
            return Uri.parse(filePath);
        } else {
            imagesCursor.close();
            return Uri.parse("");
        }
    }

    /**
     * Render a thumbnail photo and scale it down to a smaller size.
     * @param path
     * @return
     */
    private static Bitmap bitmapFromPath(String path){
        File imgFile = new  File(path);
        Bitmap imageBitmap = null;

        if(imgFile.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = IMAGE_RESOLUTION;
            imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        }
        return imageBitmap;
    }

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }
}