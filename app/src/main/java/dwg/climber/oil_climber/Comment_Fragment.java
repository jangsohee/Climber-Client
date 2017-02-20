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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.thrift.TException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dwg.climber.oil_climber.R.id.image_list;

public class Comment_Fragment extends Fragment {

    private List<CmtData> cmt_list;

    TextView text_view;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.comment_fragment, container, false);
        cmt_list = new ArrayList<CmtData>();
        new Thread(new Comments()).start();

        Button syncBtn = (Button) v.findViewById(R.id.sync_button);
        syncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Comments()).start();
            }
        });

        //prepareData();
        //set_cmt_list(v);
        return v;
    }

    class Comments implements Runnable {

        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://dwg-test.ctqok39grnhr.us-west-2.rds.amazonaws.com:3306/climber";

        static final String USERNAME = "dwg_climber";
        static final String PASSWORD = "rnrmfzhfldk";

        @Override
        public void run() {
            Connection conn = null;
            Statement stmt = null;

            try{
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                System.out.println("\n- MySQL Connection");
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM Comments WHERE c_id=7 GROUP BY comment ORDER BY time DESC";
                //sql = "SELECT * FROM Comments WHERE c_id=7 AND comment in (SELECT comment FROM Comments GROUP BY comment) ORDER BY time DESC";
                //sql = "SELECT DISTINCT comment, time, post_id, url Comments WHERE c_id=7 ORDER BY time DESC";
                //sql = "SELECT DISTINCT comment, time, post_id, url FROM Comments WHERE c_id=7"; //러블리즈, ORDER BY cmt_num DESC
                ResultSet rs = stmt.executeQuery(sql);
                dwg.climber.oil_climber.DailyResult d_result= new dwg.climber.oil_climber.DailyResult();
                cmt_list.clear(); //syncBtn 누르면 clear를 위한

                while(rs.next()){
                    String cmt = rs.getString("comment").toString();
                    String time = rs.getString("time").toString();
                    String post_id = rs.getString("post_id").toString();
                    String url = rs.getString("url").toString();
                    CmtData cd = new CmtData(post_id, time, cmt, url);

                    cmt_list.add(cd);
                }

                performSet();
                stmt.close();
                conn.close();
            }catch(SQLException se1){
                se1.printStackTrace();
            }catch(Exception ex){
                ex.printStackTrace();
            }finally{
                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                }
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
            System.out.println("\n\n- MySQL Connection Close");
        }
        private void performSet() throws TException
        {
            class OneShotTask implements Runnable {
                private OneShotTask() {}
                public void run() {
                    set_cmt_list(v);
                }
            }
            getActivity().runOnUiThread(new Thread(new OneShotTask()));
        }
    }

    public void set_cmt_list(View v){
        LayoutInflater infalInflater = (LayoutInflater) this.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout lay = (LinearLayout) v.findViewById(R.id.comment_lay);

        //syncBtn 누르면 clear를 위한
        if(((LinearLayout) lay).getChildCount() > 0)
        ((LinearLayout) lay).removeAllViews();

        TextView txt;
        for(int i=0;i<cmt_list.size();i++){
            View convertView = infalInflater.inflate(R.layout.comment_naver, null);
            //ImageView img = (ImageView) convertView.findViewById(R.id.comment_img);
            final CmtData list = cmt_list.get(i);

            //Glide.with(this).load(list).into(img);
            txt = (TextView) convertView.findViewById(R.id.comment_txt);
            txt.setText(list.cmt);
            txt = (TextView) convertView.findViewById(R.id.comment_time);
            txt.setText(list.time);
            txt = (TextView) convertView.findViewById(R.id.comment_id);
            txt.setText(list.post_id);

            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.url));
                    startActivity(intent);
                }
            });

            lay.addView(convertView);

        }
    }
    /*
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
    */

}