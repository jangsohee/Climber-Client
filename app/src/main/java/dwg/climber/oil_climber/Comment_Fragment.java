package dwg.climber.oil_climber;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comment_Fragment extends Fragment {

    private HashMap<Integer,List<String>> cmt_list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comment_fragment, container, false);
        prepareData();
        set_cmt_list(v);
        return v;
    }

    public void set_cmt_list(View v){
        LayoutInflater infalInflater = (LayoutInflater) this.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout lay = (LinearLayout) v.findViewById(R.id.comment_lay);
        TextView txt;

        for(int i=0;i<cmt_list.size();i++){
            View convertView=infalInflater.inflate(R.layout.comment_naver, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.comment_img);
            final List<String> list = cmt_list.get(i);
            final String url_i = list.get(0);

            Glide.with(this).load(url_i).into(img);
            txt = (TextView) convertView.findViewById(R.id.comment_txt);
            txt.setText(list.get(1));
            txt = (TextView) convertView.findViewById(R.id.comment_time);
            txt.setText(list.get(2));

            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(3)));
                    startActivity(intent);
                }
            });

            lay.addView(convertView);
        }
    }
    public void prepareData(){
        cmt_list= new HashMap<>();

        List<String> close = new ArrayList<String>();
        close.add("http://image.hankookilbo.com/i.aspx?Guid=5f5962508457411cb1882a0747acb9a0&Month=201603&size=640");
        close.add("송중기는 참 잘생겼지요!");
        close.add("51분전");
        close.add("http://m.naver.com");
        for(int i=0;i<6;i++)
            cmt_list.put(i,close);
    }
}