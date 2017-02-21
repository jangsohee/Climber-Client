package dwg.climber.oil_climber;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
            String url = client.imgSearch(send_img);
            System.out.println("Recieved");

            class OneShotTask implements Runnable {
                private String url;
                private OneShotTask(String url) { this.url = url; }
                public void run() {
                    //intent
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.url));
                    startActivity(intent);
                }
            }
            runOnUiThread(new Thread(new OneShotTask(url)));
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public void finish_button_click(View v){
        finish();
    }
}