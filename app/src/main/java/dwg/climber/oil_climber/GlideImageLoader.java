package dwg.climber.oil_climber;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

/**
 * Created by dayounglee on 2017-02-05.
 */

public class GlideImageLoader implements MediaLoader {
    private String url;

    public GlideImageLoader(String url) {
        this.url = url;
    }

    public boolean isImage() {
        return true;
    }

    public void loadMedia(Context context, final ImageView imageView, final MediaLoader.SuccessCallback callback) {
        Glide.with(context).load(url).asBitmap().into(imageView);
    }

    public void loadThumbnail(Context context, ImageView thumbnailView, MediaLoader.SuccessCallback callback) {
        Picasso.with(context)
                .load(url)
                .resize(100, 100)
                .centerCrop()
                .into(thumbnailView, new ImageCallback(callback));
    }

    private static class ImageCallback implements Callback {

        private final MediaLoader.SuccessCallback callback;

        public ImageCallback(MediaLoader.SuccessCallback callback) {
            this.callback = callback;
        }

        public void onSuccess() {
            callback.onSuccess();
        }

        public void onError() {

        }
    }
}
