package dwg.climber.oil_climber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import java.util.ArrayList;
import java.util.List;

public class ImageSlider extends AppCompatActivity {

    private ScrollGalleryView scrollGalleryView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        Intent intent = getIntent();

        ArrayList<String> image_list = getIntent().getStringArrayListExtra("DailyResult");
        int index = intent.getIntExtra("index", 0);
        List<MediaInfo> infos = new ArrayList<>(image_list.size());
        for (String url : image_list) infos.add(MediaInfo.mediaLoader(new GlideImageLoader(url)));

        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);
        scrollGalleryView.setCurrentItem(index);
    }
    public void finish_button_click(View v){
        finish();
    }

}
