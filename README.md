# HttpUrlConnection



<br/>




## Permission


<br/>


Manifest에 인터넷 Permission을 부여한다.


```xml
<uses-permission android:name="android.permission.INTERNET"/>
```



<br/>




## AsyncTesk


<br/>


AsyncTask를 쓰게 되면 리턴타입이 생길수없다. (대부분 기본적으로(void)씀 쓰레드도 마찬가지)



리턴타입을 쓰게 된다면 과정없이 결과가 바로 return이 되버린다.

#### newTask (AsyncTask)

```java
public void newTask(String url) {

        new AsyncTask<String, Void, Void>() {
            // 백그라운드 처리 함수

            @Override
            protected Void doInBackground(String... params) {

                try {
                    // getData 함수로 데이터를 가져온다.
                    String result = getData(params[0]);
                    Log.i("Network", result);
                } catch (Exception e) {

                    e.printStackTrace();

                }

                return null;
            }

        }.execute(url);

    }
```



#### getData



인자로 받은 url로 네트웍을 통해 데이터를 가져오는 함수이다. 데이터의 형태는 무조건 string이다.




getData의 경우 네트워크라서 서브쓰레드에서 강제로 돌려야한다.



쓰레드(핸들러에서 String을 바로받기 애매함) vs AsyncTask (그래서 이걸쓴다)






```
여기서 예외처리를 throw를 해준 이유는 앱마다 UI가 다를텐데 , 예외가 생기면 어떤곳은 토스트로 띄워주는데가있고 어떤데는 팝업으로 띄워주는데가 있을것이다.


따라서 결과값을 받아서 해당 UI단에서 처리를 해준다. 에러를 호출 한 측에서 넘겨 받는데 Maincode에서 결정을 해준다.


한마디로 예외를 싹다 모아서 처리를 해준다 . 보통 try catch문 보다 이런식으로 많이쓴다.
```








```java
public String getData(String url) throws Exception {

}
```


##### 요청처리 Request




```java
  String result = "";
        // 네트워크 처리
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
```




##### 응답처리 Response





```java
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
```





<br/>




## Interface



인터페이스를 활용하면 재사용성을 증가 시킬 수 있다.




```java
public interface TaskInterface {

    public String getUrl();
    public void resultExecute(String result);

}
```



<br/>




기존에 있던 newTask를 url을 받는것이 아니라 인터페이스를 받아서 구현한다.



```java
public static void newTask(final TaskInterface taskInterface) {



          ...---skip---...

          @Override
            protected void onPostExecute(String result) {
                // 값을 화면에 출력한다
                taskInterface.resultExecute(result);
            }

        }.execute(taskInterface.getUrl());

}
```




<br/>




메인에서는 implement로 interface를 받아 Override시킨다.

```java
public class MainActivity extends AppCompatActivity implements TaskInterface{


            ...---skip---...


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
```



## Android Emulator




Thread안에서 HttpUrlConnection을 통해서 서버에 접속해 데이터를 가져오고 가져온 데이터를 로그로 출력



TextView로 값을 받아와 화면에 출력





![console_1.png]()





![console_2.png]()





![emul.png]()
