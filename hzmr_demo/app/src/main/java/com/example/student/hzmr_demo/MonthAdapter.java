package com.example.student.hzmr_demo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * 어댑터 객체 정의
 *
 * @author Mike
 *
 */
public class MonthAdapter extends BaseAdapter {

    public static final String TAG = "MonthAdapter";

    Context mContext;

    public static int oddColor = Color.rgb(225, 225, 225);
    public static int headColor = Color.rgb(12, 32, 158);

    private int selectedPosition = -1;

    private MonthItem[] items;
    private String[] state;
    private String[] protoState;

    private int countColumn = 7;

    int mStartDay;
    int startDay;
    int curYear;
    int curMonth;

    int firstDay;
    int lastDay;
    String ID;

    Calendar mCalendar;
    boolean recreateItems = false;

    public MonthAdapter(Context context) {
        super();

        mContext = context;

        init();
    }

    public MonthAdapter(Context context, String id) {
        super();

        mContext = context;
        ID = id;
        init();
    }

    private void init() {
        items = new MonthItem[7 * 6];
        state = new String[7*6];

        mCalendar = Calendar.getInstance();
        recalculate();
        resetDayNumbers();

    }

    public void recalculate() {

        // set to the first day of the month
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        // get week day
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        firstDay = getFirstDay(dayOfWeek);
        Log.d(TAG, "firstDay : " + firstDay);

        mStartDay = mCalendar.getFirstDayOfWeek();
        curYear = mCalendar.get(Calendar.YEAR);
        curMonth = mCalendar.get(Calendar.MONTH);
        lastDay = getMonthLastDay(curYear, curMonth);

        Log.d(TAG, "curYear : " + curYear + ", curMonth : " + curMonth + ", lastDay : " + lastDay);

        int diff = mStartDay - Calendar.SUNDAY - 1;
        startDay = getFirstDayOfWeek();
        Log.d(TAG, "mStartDay : " + mStartDay + ", startDay : " + startDay);

    }

    public void setPreviousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        recalculate();

        resetDayNumbers();
        selectedPosition = -1;
    }

    public void setNextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        recalculate();

        resetDayNumbers();
        selectedPosition = -1;
    }

    public void resetDayNumbers() {
        /*
         * 서버에서 이 달의 지각, 조퇴, 결석 받아오기
         */
        GetStateTask task = new GetStateTask("http://70.12.114.136/mv/extractData.do");
        try {
            task.execute(ID,curMonth+1+"").get();
        } catch (Exception e) {
            Log.d("결석상태 요청 task 에러","발생");
        }
    }

    private int getFirstDay(int dayOfWeek) {
        int result = 0;
        if (dayOfWeek == Calendar.SUNDAY) {
            result = 0;
        } else if (dayOfWeek == Calendar.MONDAY) {
            result = 1;
        } else if (dayOfWeek == Calendar.TUESDAY) {
            result = 2;
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            result = 3;
        } else if (dayOfWeek == Calendar.THURSDAY) {
            result = 4;
        } else if (dayOfWeek == Calendar.FRIDAY) {
            result = 5;
        } else if (dayOfWeek == Calendar.SATURDAY) {
            result = 6;
        }

        return result;
    }


    public int getCurYear() {
        return curYear;
    }

    public int getCurMonth() {
        return curMonth;
    }


    public int getNumColumns() {
        return 7;
    }

    public int getCount() {
        return 7 * 6;
    }

    public Object getItem(int position) {
        return items[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView(" + position + ") called.");

        MonthItemView itemView;
        if (convertView == null) {
            itemView = new MonthItemView(mContext);
        } else {
            itemView = (MonthItemView) convertView;
        }

        // create a params
        GridView.LayoutParams params = new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                120);

        // calculate row and column
        int rowIndex = position / countColumn;
        int columnIndex = position % countColumn;

        //Log.d(TAG, "Index : " + rowIndex + ", " + columnIndex);

        if(items[0] == null){
            return itemView;
        }
        // set item data and properties
        itemView.setItem(items[position]);
        itemView.setLayoutParams(params);
        itemView.setPadding(2, 2, 2, 2);

        // set properties
        itemView.setGravity(Gravity.CENTER);

        if (columnIndex == 0) {
            itemView.setTextColor(Color.RED);
        } else {
            itemView.setTextColor(Color.BLACK);
        }

        // set background color
        if (position == getSelectedPosition()) {
            itemView.setBackgroundColor(Color.YELLOW);
        } else {
            itemView.setBackgroundColor(Color.WHITE);
        }

        /*
         * 달력 각 요소에 지각, 결석 여부에 따라 색 칠하기
         *
         */

        String state = items[position].getState();
        if(state!=null) {
            if (state.equals("지각") || items[position].getState().equals("조퇴")) { //지각
                itemView.setTextColor(Color.parseColor("#ffffff"));
                itemView.setBackgroundColor(Color.parseColor("#A6b3b3ff"));
            } else if (items[position].getState().equals("결석")) { //결석
                itemView.setTextColor(Color.parseColor("#ffffff"));
                itemView.setBackgroundColor(Color.parseColor("#A6FF9595"));
            }
        }

        return itemView;
    }


    /**
     * Get first day of week as android.text.format.Time constant.
     * @return the first day of week in android.text.format.Time
     */
    public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }


    /**
     * get day count for each month
     *
     * @param year
     * @param month
     * @return
     */
    private int getMonthLastDay(int year, int month){
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return (31);

            case 3:
            case 5:
            case 8:
            case 10:
                return (30);

            default:
                if(((year%4==0)&&(year%100!=0)) || (year%400==0) ) {
                    return (29);   // 2월 윤년계산
                } else {
                    return (28);
                }
        }
    }








    /**
     * set selected row
     */
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    /**
     * get selected row
     *
     * @return
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    class GetStateTask extends AsyncTask<String,Void,String> {

        String url;

        GetStateTask() {}
        GetStateTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String month = strings[1];
            url += "?id="+id+"&month="+month;    //쿼리문

            //http request
            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection con= null;
            BufferedReader reader = null;

            try{
                url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();

                if(con!=null){
                    con.setConnectTimeout(5000);   //connection 5초이상 길어지면 exepction
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    //con.getContentEncoding();
                    if(con.getResponseCode()!=HttpURLConnection.HTTP_OK)
                        return null;



                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;

                    while(true){
                        line = reader.readLine();
                        if(line == null){
                            break;
                        }
                        sb.append(line);
                    }

                }

            }catch(Exception e){
                return e.getMessage();   //리턴하면 post로

            }finally {

                try {
                    if (reader !=null){
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.disconnect();
            }


            return sb.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("서버에서 받아온 result 스트링값",s);
            protoState = s.split(" ");
            int dayNum;
            state = new String[7*6];
            for(int i=0; i<protoState.length; i++){
                if(protoState[i].length()==3){
                    dayNum = Integer.parseInt(protoState[i].substring(0,1));
                    state[dayNum] = protoState[i].substring(1);
                }
                else if(protoState[i].length()==4){
                    dayNum = Integer.parseInt(protoState[i].substring(0,2));
                    state[dayNum] = protoState[i].substring(2);
                }
            }
            for(int i=0; i<state.length; i++){
                Log.d("일별 상태", i+"일:"+state[i]);
            }
            for (int i = 0; i < 42; i++) {
                // calculate day number
                int dayNumber = (i+1) - firstDay;
                if (dayNumber < 1 || dayNumber > lastDay) {
                    dayNumber = 0;
                }
                // save as a data item
                items[i] = new MonthItem(dayNumber,state[dayNumber]);
                if(items[i].getState()!=null)
                    Log.d(i+"인덱스",items[i].getState());
            }
            notifyDataSetChanged();
        }
    }

}
