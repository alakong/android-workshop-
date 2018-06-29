package com.example.student.p447;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ItemAdapter itemAdapter;
    ArrayList<Item> list;
    LinearLayout container;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container=(LinearLayout) findViewById(R.id.container);
        list= new ArrayList<>();
        list.add(new Item("돼지","010-1111-2222",30,R.drawable.icon1));
        list.add(new Item("강집","010-1111-3333",31,R.drawable.icon2));
        list.add(new Item("시바견","010-1111-4444",32,R.drawable.icon3));
        list.add(new Item("다람이","010-1111-5555",33,R.drawable.icon4));
        list.add(new Item("샐리","010-1111-6666",34,R.drawable.icon5));
        list.add(new Item("여우","010-1111-7777",35,R.drawable.icon6));
        list.add(new Item("코카","010-1111-8888",36,R.drawable.icon7));


        itemAdapter= new ItemAdapter(list);
        listView=findViewById(R.id.listView);
        listView.setAdapter(itemAdapter);
    }

    public void clickBt(View v){
        itemAdapter.addItem( new Item("콜라","010-1111-9999",37,R.drawable.icon8));
        itemAdapter.notifyDataSetChanged();//listview-refresh

    }



    //ItemAdapter

    public class ItemAdapter extends BaseAdapter {

        ArrayList<Item> list;

        public ItemAdapter(){
        }
        public ItemAdapter(ArrayList<Item> list){
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public void addItem(Item item){
            list.add(item);
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vw=null;
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw=inflater.inflate(R.layout.item,container,true);
            //R.layout.item: 어떠한 xml을 이용하여 뷰객체를 만들건지,그 xml 안에 container을 가져오겠다는 구문/거기서 아래 텍스트 뷰를 불러와서 매칭
            TextView name=vw.findViewById(R.id.textView);
            TextView phone=vw.findViewById(R.id.textView2);
            TextView age=vw.findViewById(R.id.textView3);
            ImageView img=vw.findViewById(R.id.imageView);

            name.setText(list.get(i).getName());
            phone.setText(list.get(i).getMobile());
            age.setText(list.get(i).getAge()+"");
            img.setImageResource(list.get(i).getResId());

            return vw;
        }
    }

}
