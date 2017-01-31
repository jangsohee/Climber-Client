package dwg.climber.oil_climber;

import android.content.Context;
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

public class Hot_Fragment extends Fragment {

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hot, container, false);
        prepareListData();
        set_hot_data(v);
        return v;
    }

    public void set_hot_data(View v){
        LinearLayout hot_list = (LinearLayout) v.findViewById(R.id.image_list);
        for(int i=0; i<listDataHeader.size();i++){
            View view = getGroupView(i);
            hot_list.addView(view);
        }
    }

    public View getGroupView(final int groupPosition) {
        LayoutInflater infalInflater = (LayoutInflater) this.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = infalInflater.inflate(R.layout.horizontal_list, null);

        TextView title = (TextView) convertView.findViewById(R.id.submenu);
        String text = listDataHeader.get(groupPosition);
        title.setText(text);

        List<String> image_list = listDataChild.get(text);
        LinearLayout hot_list = (LinearLayout) convertView.findViewById(R.id.horizontal_list);
        for(int i=0;i<image_list.size();i++){
            final String url_i = image_list.get(i);
            ImageView image_object = new ImageView(this.getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
            layoutParams.setMargins(20,20,20,20);
            image_object.setLayoutParams(layoutParams);
            Glide.with(this).load(url_i).into(image_object);
            hot_list.addView(image_object);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sub_content= (View) v.findViewById(R.id.horizontal_scroll);
                if (sub_content.getVisibility()==View.VISIBLE) sub_content.setVisibility(View.GONE);
                else sub_content.setVisibility(View.VISIBLE);
            }
        });
        return convertView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding data header
        listDataHeader.add("1위-송중기 군대");
        listDataHeader.add("2위-송중기 송혜교");
        listDataHeader.add("3위-kbs 연예대상");
        // Adding child data
        List<String> heading1 = new ArrayList<String>();
        List<String> heading2 = new ArrayList<String>();
        List<String> heading3 = new ArrayList<String>();
        heading1.add("http://image.hankookilbo.com/i.aspx?Guid=5f5962508457411cb1882a0747acb9a0&Month=201603&size=640");
        heading1.add("http://pds.joins.com/news/component/htmlphoto_mmdata/201603/22/htm_20160322135135248388.jpg");
        heading1.add("http://scontent.cdninstagram.com/t51.2885-15/e35/12783456_1283039071712777_1483267387_n.jpg?ig_cache_key=MTE5Nzg1MjI1ODM5MDI1OTg2Mw%3D%3D.2");
        heading1.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTJei137g651k6Nu6Cwd2KaBLTj0nnHOKWab39PO8ao_hUCvN8s");
        heading2.add("http://image.hankookilbo.com/i.aspx?Guid=ab17df059d444b22ad0dc9d57ab2ee0a&Month=201602&size=640");
        heading2.add("http://img.etoday.co.kr/pto_db/2016/04/20160405073228_847993_600_743.jpg");
        heading2.add("http://img.tf.co.kr/article/home/2016/03/20/20161640145852493310.jpg");
        heading2.add("http://image.hankookilbo.com/i.aspx?Guid=a18d122408724ac3826cdecfb84d9684&Month=HKSports&size=640");
        heading3.add("http://m1.daumcdn.net/thumb/C480x270/70/?fname=http://i1.daumcdn.net/svc/image/U03/tvpot_thumb/s7de8mBfPm2eB2DBmFP2Bqy/thumb.png?t=1483203146440");
        heading3.add("http://cfile215.uf.daum.net/image/1746D1194B3669FA111D33");
        heading3.add("https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/121206_%EB%AC%B8%ED%99%94%EC%97%B0%EC%98%88%EB%8C%80%EC%83%81_%EC%86%A1%EC%A4%91%EA%B8%B0.jpg/220px-121206_%EB%AC%B8%ED%99%94%EC%97%B0%EC%98%88%EB%8C%80%EC%83%81_%EC%86%A1%EC%A4%91%EA%B8%B0.jpg");
        heading3.add("https://i.ytimg.com/vi/EV3sdmvleFE/maxresdefault.jpg");
        listDataChild.put(listDataHeader.get(0), heading1);
        listDataChild.put(listDataHeader.get(1), heading2);
        listDataChild.put(listDataHeader.get(2), heading3);
    }
}
