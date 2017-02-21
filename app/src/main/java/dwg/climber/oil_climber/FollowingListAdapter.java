package dwg.climber.oil_climber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class FollowingListAdapter extends ArrayAdapter implements View.OnClickListener {

    public interface ButtonListener {
        void onButtonClick(int position) ;
    }

    int resourceId ;
    private ButtonListener buttonListener ;

    FollowingListAdapter(Context context, int resource, ArrayList<FollowingListItem> list, ButtonListener listener) {
        super(context, resource, list) ;

        this.resourceId = resource ;
        this.buttonListener = listener ;
    }

    // 새롭게 만든 Layout을 위한 View를 생성하는 코드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        final TextView textView = (TextView) convertView.findViewById(R.id.following_celeb_name);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final FollowingListItem listItem = (FollowingListItem) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        textView.setText(listItem.getCelebName());
        textView.setTag(listItem.getCelebID());

        // deleteButton의 TAG에 id값 지정. Adapter를 click listener로 지정
        Button deleteButton = (Button) convertView.findViewById(R.id.delete_following_button);
        deleteButton.setTag(listItem.getCelebID());
        deleteButton.setOnClickListener(this);

        return convertView;
    }

    // deleteButton이 눌러졌을 때 실행되는 onClick함수
    public void onClick(View v) {
        // ButtonListener(MainActivity)의 onButtonClick() 함수 호출
        if (this.buttonListener != null) {
            this.buttonListener.onButtonClick((int)v.getTag()) ;
        }
    }
}
