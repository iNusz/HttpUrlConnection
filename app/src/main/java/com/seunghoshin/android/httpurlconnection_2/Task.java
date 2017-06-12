package com.seunghoshin.android.httpurlconnection_2;

import android.os.AsyncTask;
import android.util.Log;

import static com.seunghoshin.android.httpurlconnection_2.Remote.getData;

/**
 * Created by SeungHoShin on 2017. 6. 13..
 */

public class Task {

    // AsyncTask를 쓰게 되면 리턴타입이 생길수없다 (대부분 기본적으로(void)씀 쓰레드도 마찬가지)
    // 리턴타입을 쓰면 과정없이 결과가 바로 return이 되버린다.
    // 옭기는 과정에서 Task.newTask를 하면 바로 하나의 Task가 생성할수 있겠끔 static을 달아준다

    // 재사용하기 위해서는 String url 처럼 넘겨주는 인자와 처리가 끝나고나서 아래쪽에 있는 textView처럼 호출되는 인자를 분리해야한다
    // 내가 넘겨주는곳에 결과처리함수 , url빼낼수있는 함수 2가지를 만든다
    //todo final 써준 이유 ?
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
                    //  todo 여기인가 ?
                    e.printStackTrace();
                    // todo 여기인가 ?
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

}
