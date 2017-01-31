package dwg.climber.oil_climber;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Image_Search_Activity extends AppCompatActivity {

    private HashMap<Integer,List<String>> close_image;
    private HashMap<Integer,List<String>> coherent_page;//0번째 사진 url, 1번째 title, 2번째 content, 3번째 이동할 url
    Bitmap upload_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        prepareData();
        set_close_image();
        set_coherent_page();
    }

    public void set_close_image(){
        LinearLayout lay = (LinearLayout)findViewById(R.id.close_image_layout);
        for(int i=0;i<close_image.size();i++){
            final List<String> list = close_image.get(i);
            final String url_i = list.get(0);
            ImageView image_object = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(5,5,5,5);
            image_object.setLayoutParams(layoutParams);
            Glide.with(this).load(url_i).into(image_object);
            lay.addView(image_object);
            image_object.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(1)));
                    startActivity(intent);
                }
            });
        }
    }

    public void set_coherent_page(){
        ImageView img;
        TextView text;
        LayoutInflater infalInflater = (LayoutInflater) this.getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i=0;i<coherent_page.size();i++){
            final List<String> list = coherent_page.get(i);
            View n_page = infalInflater.inflate(R.layout.coherent_page_layout, null);
            img = (ImageView) n_page.findViewById(R.id.page_image);
            Glide.with(this).load(list.get(0)).into(img);
            text = (TextView) n_page.findViewById(R.id.page_title);
            text.setText(list.get(1));
            text = (TextView) n_page.findViewById(R.id.page_content);
            text.setText(list.get(2));
            text = (TextView) n_page.findViewById(R.id.page_url);
            text.setText(list.get(3));
            LinearLayout lay = (LinearLayout)findViewById(R.id.coherent_page_layout);
            lay.addView(n_page);
            n_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(3)));
                    startActivity(intent);
                }
            });
        }
    }

    public void add_image(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        final ImageView img =(ImageView) findViewById(R.id.add_image);
        if(requestCode==0&&resultCode ==RESULT_OK)
            Glide.with(this).load(data.getData()).asBitmap().into(new SimpleTarget<Bitmap>(300,300) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    upload_image = resource;
                    img.setImageBitmap(resource);
                }
            });
    }

    public void img_to_server(View v) {}//send upload image to server

    public void app_bar_click(View v){
        RelativeLayout lay = (RelativeLayout) findViewById(R.id.image_search_layout);
        LinearLayout lay_out = (LinearLayout) findViewById(R.id.coherent_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)lay_out.getLayoutParams();
        if(lay.getVisibility()==View.VISIBLE) {
            lay.setVisibility(View.GONE);
            params.weight = 25f;
            lay_out.setLayoutParams(params);
        } else{
            lay.setVisibility(View.VISIBLE);
            params.weight = 10f;
            lay_out.setLayoutParams(params);
        }
    }

    public void finish_button_click(View v){
        finish();
    }

    public void prepareData(){
        close_image= new HashMap<>();
        List<String> close = new ArrayList<String>();
        close.add("http://image.hankookilbo.com/i.aspx?Guid=5f5962508457411cb1882a0747acb9a0&Month=201603&size=640");
        close.add("http://m.naver.com");
        for(int i=0;i<6;i++)
            close_image.put(i,close);

        coherent_page = new HashMap<>();
        List<String> day = new ArrayList<String>();
        day.add("http://image.hankookilbo.com/i.aspx?Guid=5f5962508457411cb1882a0747acb9a0&Month=201603&size=640");
        day.add("제목입니다.");
        day.add("내용입니다.");
        day.add("http://m.naver.com");
        for(int i=0;i<6;i++)
            coherent_page.put(i,day);
    }
}