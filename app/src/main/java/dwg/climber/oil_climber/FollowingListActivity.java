package dwg.climber.oil_climber;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FollowingListActivity extends AppCompatActivity implements FollowingListAdapter.ButtonListener {

    FollowingDB fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        Resources res = getResources();
        String[] celebs = res.getStringArray(R.array.celeb_array);

        fdb = new FollowingDB(getApplicationContext(), "follow.db", null, 1);
        // followingCelebArray is one-based
        Integer[] followingCelebArray = fdb.getCelebList();

        final ListView listview ;
        FollowingListAdapter adapter;
        ArrayList<FollowingListItem> items = new ArrayList<FollowingListItem>();

        Integer followingID;

        for(int i=0; i<followingCelebArray.length; i++) {
            followingID = followingCelebArray[i];
            items.add(new FollowingListItem(followingID, celebs[followingID-1]));
        }

        // Adapter 생성
        adapter = new FollowingListAdapter(this, R.layout.following_listview, items, this) ;

        listview = (ListView) findViewById(R.id.following_listview);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ListView list = (ListView) parent;
                FollowingListItem item = (FollowingListItem)list.getItemAtPosition(position);
                System.out.println(item.getCelebID());

                Intent intent = getIntent();
                intent.putExtra("c_id", item.getCelebID());
                setResult(RESULT_OK,intent);
                finish();
                // 여기에서 해당 연예인 홈으로 연결되도록
                // id를 알아내려면 item.getCelebID()
            }
        });
    }

    @Override
    public void onButtonClick(int id) {
        fdb.delete("delete from followings where id = '" + id + "';");

        finish();
        startActivity(getIntent());
    }

    public void finish_button_click(View v){
        finish();
    }

}
