package mkk13.colorjudge.Adapters;

/**
 * Created by mkk-1 on 17/05/2017.
 */

import android.net.Uri;

/**
 * Used to represent a photo item.
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoItem {

    // Ivars.
    private Uri thumbnailUri;
    private Uri fullImageUri;
    private long imageId;

    public PhotoItem(Uri thumbnailUri,Uri fullImageUri, long id) {
        this.thumbnailUri = thumbnailUri;
        this.fullImageUri = fullImageUri;
        this.imageId = id;
    }

    /**
     * Getters and setters
     */
    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(Uri thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public Uri getFullImageUri() {
        return fullImageUri;
    }

    public void setFullImageUri(Uri fullImageUri) {
        this.fullImageUri = fullImageUri;
    }

    public long getId() {
        return imageId;
    }
}