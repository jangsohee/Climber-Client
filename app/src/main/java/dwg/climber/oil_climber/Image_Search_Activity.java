package dwg.climber.oil_climber;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class Image_Search_Activity extends AppCompatActivity {

    private HashMap<Integer,List<String>> close_image;
    private HashMap<Integer,List<String>> coherent_page;//0번째 사진 url, 1번째 title, 2번째 content, 3번째 이동할 url
    Bitmap upload_image;
    String send_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
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
                    send_img = BitMapToString(upload_image);
                }
            });
    }

    public void img_to_server(View v) {

        if(upload_image == null)
            return;
        new Thread(new ThriftRpcCallThread()).start();

    }//send upload image to server
    class ThriftRpcCallThread implements Runnable {
        @Override
        public void run() {
            try {
                TTransport transport;

                transport = new TSocket("52.32.4.16", 9090);
                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                dwg.climber.oil_climber.Oil.Client client = new dwg.climber.oil_climber.Oil.Client(protocol);

                performThriftRPC(client);

                transport.close();
            }
            catch(TException x) {
                x.printStackTrace();
            }
        }

        private void performThriftRPC(dwg.climber.oil_climber.Oil.Client client) throws TException
        {
            dwg.climber.oil_climber.ImgSearchResult product = client.imgSearch(send_img);
            System.out.println("Recieved");

            class OneShotTask implements Runnable {
                private dwg.climber.oil_climber.ImgSearchResult product;
                private OneShotTask(dwg.climber.oil_climber.ImgSearchResult product) { this.product = product; }
                public void run() {
                    set_close_txt(product.getGuess());
                    set_close_image(product.getImgs());
                    set_coherent_page(product.getPages());
                }
            }
            runOnUiThread(new Thread(new OneShotTask(product)));
        }
    }
    public void set_coherent_page(List<MatchingPage> pages){
        ImageView img;
        TextView text;
        LayoutInflater infalInflater = (LayoutInflater) this.getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i=0;i<pages.size();i++){
            final MatchingPage list = pages.get(i);
            View n_page = infalInflater.inflate(R.layout.coherent_page_layout, null);
            img = (ImageView) n_page.findViewById(R.id.page_image);
            Glide.with(this).load(list.getImgUrl()).into(img);
            text = (TextView) n_page.findViewById(R.id.page_title);
            text.setText(list.getTitle());
            text = (TextView) n_page.findViewById(R.id.page_content);
            text.setText(list.getContent());
            text = (TextView) n_page.findViewById(R.id.page_url);
            text.setText(list.getLinkUrl());
            LinearLayout lay = (LinearLayout)findViewById(R.id.coherent_page_layout);
            lay.addView(n_page);
            n_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.getLinkUrl()));
                    startActivity(intent);
                }
            });
        }
    }
    public void set_close_image(List<SimilarImg> imgs){
        LinearLayout lay = (LinearLayout)findViewById(R.id.close_image_layout);
        for(int i=0;i<imgs.size();i++){
            final SimilarImg list = imgs.get(i);
            final String url_i = list.getImgUrl();
            ImageView image_object = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(5,5,5,5);
            image_object.setLayoutParams(layoutParams);
            Glide.with(this).load(url_i).into(image_object);
            lay.addView(image_object);
            image_object.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.getLinkUrl()));
                    startActivity(intent);
                }
            });
        }
    }
    public void set_close_txt(String txt){
        TextView close_txt = (TextView) findViewById(R.id.close_txt);
        close_txt.setText(txt);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
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
}