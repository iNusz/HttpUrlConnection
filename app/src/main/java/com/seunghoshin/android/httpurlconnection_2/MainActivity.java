package com.seunghoshin.android.httpurlconnection_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TaskInterface{

    TextView textView;
    String url = "http://google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
//        try {
        // 프로토콜 지정을 해줘야한다 . (http:// , https:// )

//            String result = getData(url);
        // newTask를 통해서 try catch 문이 사라진다  todo 그러면 예외가 생기게되면 Toast를 넣어주고싶으면
//        newTask(url);  -> newTask에서 인자값을 activity를 받는다
            Task.newTask(this);
//        } catch (Exception e) {
//            Log.e("Network", e.toString());
//            Toast.makeText(this, "네트워크 오류 :" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }



    // 인터페이스 사용
    @Override
    public String getUrl(){
        return url;
    }

    @Override
    public void resultExecute(String result){
        textView.setText(result);
    }

}
