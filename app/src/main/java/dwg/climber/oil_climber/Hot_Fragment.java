package dwg.climber.oil_climber;

import android.content.Context;
import android.content.Intent;
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

import org.apache.thrift.TException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static dwg.climber.oil_climber.R.id.image_list;

public class Hot_Fragment extends Fragment {

    int f_id=1;
    View m_view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_hot, container, false);
        new Thread(new ThriftRpcCallThread()).start();
        //prepareListData();
        //set_hot_data(v);
        return m_view;
    }



    class ThriftRpcCallThread implements Runnable {

        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://dwg-test.ctqok39grnhr.us-west-2.rds.amazonaws.com:3306/example_db";

        static final String USERNAME = "dwg_climber";
        static final String PASSWORD = "rnrmfzhfldk";
        List<dwg.climber.oil_climber.DailyResult> hot_result;

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
                sql = "select * from f_id_"+f_id+" LIMIT 5";
                ResultSet rs = stmt.executeQuery(sql);
                String index = null;
                hot_result = new ArrayList<>();
                dwg.climber.oil_climber.DailyResult d_result= new dwg.climber.oil_climber.DailyResult();
                while(rs.next()){
                    String time = rs.getDate("time").toString();
                    if(index == null)
                        index = time;
                    else if(!index.equals(time)) {
                        d_result.setDate(index);
                        hot_result.add(d_result);
                        d_result = new dwg.climber.oil_climber.DailyResult();
                        index=time;
                    }
                    d_result.addToImgUrl(rs.getString("url"));
                }
                d_result.setDate(index);
                hot_result.add(d_result);
                performThriftRPC();
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

        private void performThriftRPC() throws TException
        {
            class OneShotTask implements Runnable {
                private OneShotTask() {}
                public void run() {
                    set_hot_data1(hot_result);
                }
            }
            getActivity().runOnUiThread(new Thread(new OneShotTask()));
        }
    }
    public void set_hot_data1(List<dwg.climber.oil_climber.DailyResult> hot_data){
        LinearLayout hot_list = (LinearLayout) m_view.findViewById(image_list);
        for(int i=0; i<hot_data.size();i++){
            View view = getGroupView1(hot_data.get(i),i);
            hot_list.addView(view);
        }
    }
    public View getGroupView1(final dwg.climber.oil_climber.DailyResult h_result, int groupPosition) {
        LayoutInflater infalInflater = (LayoutInflater) this.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = infalInflater.inflate(R.layout.horizontal_list, null);

        TextView title = (TextView) convertView.findViewById(R.id.submenu);
        String text = h_result.getDate();
        title.setText(text);

        final List<String> image_list = h_result.getImgUrl();
        LinearLayout hot_list = (LinearLayout) convertView.findViewById(R.id.horizontal_list);

        for(int i=0;i<image_list.size();i++){

            final String url_i = image_list.get(i);
            final int index = i;
            ImageView image_object = new ImageView(this.getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
            layoutParams.setMargins(20,20,20,20);
            image_object.setLayoutParams(layoutParams);
            Glide.with(this).load(url_i).into(image_object);
            image_object.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageSlider.class);
                    intent.putStringArrayListExtra("DailyResult", (ArrayList<String>) image_list);
                    intent.putExtra("index",index);
                    startActivity(intent);
                }
            });
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

}
