package com.seunghoshin.android.httpurlconnection_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.seunghoshin.android.httpurlconnection_2.domain.Data;
import com.seunghoshin.android.httpurlconnection_2.domain.Row;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskInterface {

    /* 기초정보
        url : http://openAPI.seoul.go.kr:8088/44596473463934693939657144686a/json/SearchPublicToiletPOIService/1/5/
        인증키 : 44596473463934693939657144686a

    */

    //고정된 url
    static final String URL_PREFIX = "http://openAPI.seoul.go.kr:8088/";
    static final String URL_CERT = "44596473463934693939657144686a";
    static final String URL_MID = "/json/SearchPublicToiletPOIService/";  // xml을 json으로 쓸꺼니까 바꿔준다

    // 한 페이지에 불러오는 데이터 수
    static final int PAGE_OFFSET = 10;

    int pageBegin = 1;
    int pageEnd = 10;


    ListView listView;
    TextView textView;
    String url = "";


    // 아답터에서 사용할 데이터 공간
    // final을 해주면 new를 통해서 datas에 들어올 방법이 없다. 메모리공간(datas)은 바뀔수없다.
    // 데이터를 빼고 넣고 할때 notify해주면 변경사항이 반영된다 todo ?? 무슨말인지..

    final List<String> datas = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);


        // 데이터 - 위에서 공간 할당
        // 아답터
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        // 셋팅
        listView.setAdapter(adapter);


        // 최초 호출시 첫번째 집합을 불러온다
        setPage(1, 10);
        setUrl(pageBegin, pageEnd);


//        try {
        // 프로토콜 지정을 해줘야한다 . (http:// , https:// )

//            String result = getData(url);
        // newTask를 통해서 try catch 문이 사라진다  todo 그러면 예외가 생기게되면 Toast를 넣어주고싶으면 ->그런데 서버문제를 토스트로 띄워주면 앱설계가 잘못된거다
//        newTask(url);  -> newTask에서 인자값을 activity를 받는다
        Remote.newTask(this);
//        } catch (Exception e) {
//            Log.e("Network", e.toString());
//            Toast.makeText(this, "네트워크 오류 :" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    private void setPage(int page, int offset) {
        pageEnd = page * PAGE_OFFSET; // 마지막 페이지
        pageBegin = pageEnd - PAGE_OFFSET + 1; // 첫 시작 페이지
    }

    // Remote.newTask가 호출되기전에 url완성
    private void setUrl(int begin, int end) {
        // String
        // StringBuffer
        // StringBuilder   -> 가장 빠른 속도 (몇십배차이)

        // String 연산..........
        // String result = "문자열" + "문자열" + "문자열";
        //                  --------------
        //                   메모리 공간 할당
        //                  -------------------------
        //                      메모리 공간 할당    --> 문자열이 연산을 하면 싹다 연산이 됨으로 느려진다

//        StringBuffer sb = new StringBuffer();   // 동기화 지원
//        sb.append("문자열");
//        sb.append("문자열");
//
//
//        StringBuilder sbl = new StringBuilder(); // 동기화 미지원
//        sb.append("문자열");
//        sb.append("문자열");

        url = URL_PREFIX + URL_CERT + URL_MID + begin + "/" + end;
    }


    // 인터페이스 사용
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void resultExecute(String jsonString) {

        Gson gson = new Gson();

        // 1. json String -> Class 로 변환
        Data data = gson.fromJson(jsonString, Data.class);

        // 2. class를 -> json String 으로 변환 , 총개수를 화면에 셋팅
        textView.setText("총 개수:" + data.getSearchPublicToiletPOIService().getList_total_count());

        // 건물의 이름을 listView에 셋팅  (찾아가보면 POI안에 ROW안에 있다)

        Row rows[] = data.getSearchPublicToiletPOIService().getRow();

        //네트윅에서 가져온 데이터를 꺼내서 datas에 담아준다 .
        for (Row row : rows) {
            datas.add(row.getFNAME());
        }
        // 그리고 adapter를 갱신해준다
        adapter.notifyDataSetChanged();
    }

}
