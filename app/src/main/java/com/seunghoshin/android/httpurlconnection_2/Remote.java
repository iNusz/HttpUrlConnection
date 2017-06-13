package com.seunghoshin.android.httpurlconnection_2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SeungHoShin on 2017. 6. 13..
 */

public class Remote {


    // AsyncTask를 쓰게 되면 리턴타입이 생길수없다 (대부분 기본적으로(void)씀 쓰레드도 마찬가지)
    // 리턴타입을 쓰면 과정없이 결과가 바로 return이 되버린다.
    // 옭기는 과정에서 Task.newTask를 하면 바로 하나의 Task가 생성할수 있겠끔 static을 달아준다

    // 재사용하기 위해서는 String url 처럼 넘겨주는 인자와 처리가 끝나고나서 아래쪽에 있는 textView처럼 호출되는 인자를 분리해야한다
    // 내가 넘겨주는곳에 결과처리함수 , url빼낼수있는 함수 2가지를 만든다
    //todo final 써준 이유 ? 이너클래스에서 final이 아니면 지역변수를 바로 못쓴다. new 를해줘서 이너 클래스를 만들어줬는데
    //todo 이너클래스에서 인터페이스의 메소드를 호출할 때는 final을 붙여줘야한다 . 보통의경우가 아니라 쓰레드의 경우일때만 ..
    public static void newTask(final TaskInterface taskInterface) {

        new AsyncTask<String, Void, String>() {
            // 백그라운드 처리 함수

            @Override
            protected String doInBackground(String... params) {
                String result = "";
                try {
                    // getData 함수로 데이터를 가져온다.
                    result = getData(params[0]);
                    Log.i("Network", result);
                } catch (Exception e) {
                    //  todo 여기인가 ? -> 구지 토스트를 띄워줄 필요가 없다 e.printStackTrace() 가 콘솔창에 로그를 띄워준다
                    e.printStackTrace();
                    // todo 여기인가 ? -> 구지 토스트를 띄워줄 필요가 없다 e.printStackTrace() 가 콘솔창에 로그를 띄워준다
                }

                return result;
            }


            @Override
            protected void onPostExecute(String result) {
                // 값을 화면에 출력한다
                taskInterface.resultExecute(result);
            }

        }.execute(taskInterface.getUrl());

    }







    // 인자로 받은 url로 네트웍을 통해 데이터를 가져오는 함수 , 데이터의 형태는 무조건 string이다
    // 앱마다 UI가 다를텐데 , 예외가 생기면 어떤곳은 토스트로 띄워주는데가있고 어떤데는 팝업으로 띄워주는데가 있을것이다
    // 그래서 결과값을 받아서 UI단에서 처리를해준다 . 에러를 호출한측에서 넘겨 받는다. Maincode에서 결정을 해준다
    // 예외를 싹다 모아서 처리를 해준다 . 보통 try catch문 보다 이런식으로 많이쓴다.
    // getData의 경우 네트워크니까 서브쓰레드에서 강제로 돌려야한다. 쓰레드(핸들러에서 String을 바로받기 애매함) vs Async (그래서 이걸쓴다)
    // 옭기는 과정에서 Remote.getData 하면 바로 하나의 Remote가 생성할수 있겠끔 static을 달아준다
    public static String getData(String url) throws Exception {

        String result = "";

        // 네트윅 처리
        // 1. 요청처리 Request
        // 1.1 Url 객체 만들기
        URL serverUrl = new URL(url);

        // 1.2 연결객체 생성
        // 로그인 패스워드가 들어가는 앱들은 HttpsURLConnection 으로 만들어줘여한다
        // 또는 URL(네이버라던가 등등)이 HTTPS만 지원하면 그걸 써야한다
        // 우리나라는 http를 많이쓴다 , Https 로 바꿔서 써도 되는데 만약에 안된다면 인증서 작업을 해줘야 한다 .
        // 안된다는 경우는 뭐 누가버전 업데이트라던가 등등 OS업데이트에서 막힐수 있다는것 .
        HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();   //url 객체에서 연결을 꺼낸다

        // 1.3 http method 결정
        con.setRequestMethod("GET");

        // 2. 응답처리 Response
        // 2.1 응답코드 분석
        int responseCode = con.getResponseCode();
        // 2.2 정상적인 응답처리
        if (responseCode == HttpURLConnection.HTTP_OK) { //정상적인 코드처리
            // 텍스트만 받을때는 Reader(장점:줄단위읽기) , 다른것도 받아야할때는 inputString 을 써야한다 . (후자를많이씀)
            // 데이터 읽어올때 빠르게 처리하기 위해 스트림을 열어 Buffer에 담았다
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String temp = null;
            // result 을 바로 넣게 되면 무한 루프가 생기기 때문에 temp에다가 넣어둔다
            while ((temp = br.readLine()) != null) {
                result += temp;
            }

            // 2.3 오류에 대한 응답처리
        } else {
            // todo 각자 호출측으로 Exception을 만들어서 넘겨줄것 ~ / 이부분은 강제로 예외를 발생 , throw로 네트워크~오류가있다고 상위로넘겨줘야함
            // 아래로그로 확인하자, 에러코드를 상단으로 넘기자
            Log.e("Network", "error_code=" + responseCode);
        }

        return result;

    }
}
