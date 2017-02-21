package dwg.climber.oil_climber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;

import java.util.Arrays;


public class FollowingActivity extends AppCompatActivity {

    private int addFollowingID = 0;
    String[] celebs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_following);

        celebs = getResources().getStringArray(R.array.celeb_array);
        final FollowingDB fdb = new FollowingDB(getApplicationContext(), "follow.db", null, 1);
        // followingCelebArray is one-based
        final Integer[] followingCelebArray = fdb.getCelebList();

        Spinner spinner = (Spinner)findViewById(R.id.celeb_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.celeb_array, R.layout.spinner_setting);
        adapter.setDropDownViewResource(R.layout.spinner_setting);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // position is zero-based
                addFollowingID = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // button event listeners
        Button addFollowing = (Button) findViewById(R.id.add_following_button);
        addFollowing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Arrays.asList(followingCelebArray).contains(addFollowingID)) {
                    Toast.makeText(FollowingActivity.this, "이미 팔로우하고 있는 사람입니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    fdb.insert("insert into followings values(" + addFollowingID + ");");
                    Toast.makeText(FollowingActivity.this, "성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(getIntent());
                    // 해당 연예인 페이지로 바로 연결되는 것도 좋을 듯
                }
            }
        });
    }

    public void finish_button_click(View v){
        finish();
    }
}
