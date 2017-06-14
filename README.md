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
여기서 예외처리를 throw를 해준 이유는 앱마다 UI가 다를텐데 ,


예외가 생기면 어떤곳은 토스트로 띄워주는데가있고 어떤데는 팝업으로 띄워주는데가 있을것이다.


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
            // todo 각자 호출측으로 Exception을 만들어서 넘겨줄것 ~
            // 이부분은 강제로 예외를 발생 , throw로 네트워크~오류가있다고 상위로넘겨줘야함
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



여기서 final이 쓰이는 이유는 이너클래스에서는 final이 아니면 지역변수를 바로 못쓴다.



new 를해줘서 이너 클래스를 만들어줬는데 이너클래스에서 인터페이스의 메소드를 호출할 때는 final을 붙여줘야한다





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



<br/>



## API



서울시 공공화장실 위치를 제공해주는 API를 사용해보자




<br/>




##### setPage


페이지를 셋팅 해준다


```java
private void setPage(int page , int offset){
        pageEnd = page * PAGE_OFFSET; // 마지막 페이지
        pageBegin = pageEnd - PAGE_OFFSET+1; // 첫 시작 페이지
    }
```




##### setUrl




newTask가 호출 되기전에 url을 완성한다




```java
    private void setUrl(int begin, int end){
        url = URL_PREFIX+ URL_CERT + URL_MID + begin + "/" + end ;
    }
```





<br/>




##### Convert Json to java Pojo Classes




```
http://pojo.sodhanalibrary.com/ 에 접속해 Url을 붙여넣은 뒤 Pojo클래스를 만들어 준다.
```




##### gson




자바객체를 Json 표현식으로 변환




```
http://search.maven.org/#artifactdetails%7Ccom.google.code.gson%7Cgson%7C2.8.1%7C

위의 링크에 접속해서 gson 을 사용하자.
```





그 후에 build.gradle(Module:app)에 가서 아래를 추가해준다.



만약 자동으로 그래들을 업데이트 하고 싶으면 * 을 쓰면 된다 .



예를 들어 gson:gson:2.*





```gradle
compile 'com.google.code.gson:gson:2.8.1'
```




##### 쓰는 방법



```java
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
```




## ListView


건물이름을 출력 해보자.



##### adapter



아답터에서 사용할 공간을 만들고 아답터를 만들어 셋팅해준다.


final을 해주면 new를 통해서 datas에 들어올 방법이 없다. 메모리공간(datas)은 바뀔수없다.


데이터를 빼고 넣고 할때 notify해주면 변경사항이 반영된다.


```java
final List<String> datas = new ArrayList<>();
        // 데이터 - 위에서 공간 할당
        // 아답터
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datas);
        // 셋팅
        listView.setAdapter(adapter);
```




## GoogleMap

구글 맵이랑 리스트랑 같이 띄워보자.


##### API key 가져오기



```
https://console.developers.google.com 에 들어가 key를 받아온다.
```



받아온 다음에 Manifest에 다음을 추가 해준다.



```xml
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="(API key값 입력 ! )"/>
```


그 후에 원활한 지도 사용을 위해 xml에 들어가 태그 이름을 바꿔준다.



```xml
<com.google.android.gms.maps.MapView/>

아래처럼 변환

<fragment...    class="com.google.android.gms.maps.SupportMapFragment"/>
```



MainActivity에 OnMapReadyCallback을 implement 해준다 .


onCreate에 코드를 작성한다.



```java
        //맵을 세팅
        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.mapView);
        // 로드되면 onReady 호출하도록
        mapFragment.getMapAsync(this);
```

<br/>



## Marker 찍기


resultExecute 메소드에서 다음의 코드를 작성한다


```java
        //네트윅에서 가져온 데이터를 꺼내서 datas에 담아준다 .
        for (Row row : rows) {
            datas.add(row.getFNAME());

            //row를 돌면서 화장실 하나하나의 좌표의 마커를 생성한다  , 여기서 LatLng은 double타입인데 row은 String이다
            // 해결방법은 row에 들어가서 String을 바꿔준다 todo ? 또다른 방법 ? , Data를 처음에 입력할때 double로 입력 ?

            MarkerOptions marker = new MarkerOptions();
            LatLng tempcord = new LatLng(row.getY_WGS84(), row.getX_WGS84());
            marker.position(tempcord);
            // row에 getFNAME으로 썻다
            marker.title(row.getFNAME());

            // 지도에 마커 등록
            mymap.addMarker(marker);
        }



        // onMapReady가 끝나면 이쪽 함수 (resultExecute)가 실행되므로 여기에 넣어줘야한다
        //지도 컨트롤
        LatLng sinsa = new LatLng(37.516066, 127.019361);
        mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(sinsa, 10));
```





<br/>


## Android Emulator




Thread안에서 HttpUrlConnection을 통해서 서버에 접속해 데이터를 가져오고 가져온 데이터를 로그로 출력



TextView로 값을 받아와 화면에 출력



Json으로 10개의 항목을 출력



listView로 건물명을 10개의 항목을 출력





![console_1.png](https://github.com/iNusz/HttpUrlConnection/blob/master/app/src/main/res/mipmap-xxhdpi/console_1.png)





![console_2.png](https://github.com/iNusz/HttpUrlConnection/blob/master/app/src/main/res/mipmap-xxhdpi/console_2.png)





![emul.png](https://github.com/iNusz/HttpUrlConnection/blob/master/app/src/main/res/mipmap-xxhdpi/emul.png)
