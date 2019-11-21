 package com.aura.springboot.map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aura.springboot.entity.Constant;

 //可以定国外  
 // 
  /**
   * 
   * @author Carry
   *
   */  //  
 
public class GetLocationBaiduMap {

    public static void main(String[] args) {
    	
        String lat="22.5435036";                        //25.8106928601 ,113.941458 
        String lng="113.9370558";	
        //22.570554 ,113.9788266 深圳
    	//38.2309862,140.8986277 日本
    	//14.5910801,121.0135374 菲律宾
    	//37.5560598,126.9749752 韩国
        //29.6325268,91.3396506 拉萨
        //阿里云连接  
        String  pushToken="aaaa sdsds";
    	String result [] = pushToken.split(" ");
 	   int index1=result.length;
 	   System.out.println("index1》》》》》"+result[0]);
/*  try { 
            String s=getLocationByBaiduMap(lng, lat);
            System.out.println(s);
        } catch (Exception e) {                         
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  */

    }
    public static String getLocationByBaiduMap(String longitude,String latitude) throws Exception {
        System.out.println("--------------"+longitude+"-----------------");
        System.out.println("--------------"+latitude+"-----------------");
        String locJson=geturl("http://api.map.baidu.com/geoconv/v1/?coords=" + longitude + "," +latitude + "&from=1&to=5&ak=2BPK8gn9TPeX5QV0SOziY2G30BnwRvUY");
        System.out.println(locJson);
        JSONObject jobject =  JSON.parseObject(locJson);
        JSONArray jsonArray = jobject.getJSONArray("result");
        String lat=jsonArray.getJSONObject(0).getString("y");
        String lng=jsonArray.getJSONObject(0).getString("x");
        System.out.println("jobject>>>>>>"+jobject);
        //System.out.println(lat);
        String addrJson = geturl("http://api.map.baidu.com/geocoder/v2/?ak=2BPK8gn9TPeX5QV0SOziY2G30BnwRvUY&location=" + lat + "," + lng + "&output=json&pois=1");
        System.out.println(addrJson);
        JSONObject jobjectaddr =  JSON.parseObject(addrJson);
        //JSONObject rJsonObject=jobjectaddr.getJSONObject("result");
        //System.out.println(rJsonObject);
        String addr=jobjectaddr.getJSONObject("result").getString("cityCode");
        //addr=(String) JSON.parseObject(addr).get("city");
        //addr=new String(addr.getBytes("gbk"),"UTF-8");
        return addr;
    }
    
    //{"addressComponent":{"adcode":"320612","city":"南通市","city_level":2,"country":"中国","country_code":0,"country_code_iso":"CHN","country_code_iso2":"CN","direction":"","distance":"","district":"通州区","province":"江苏省","street":"长通路","street_number":"","town":""},"business":"","cityCode":161,"formatted_address":"江苏省南通市通州区长通路","location":{"lat":31.93105317856547,"lng":120.96189662538525},"poiRegions":[],"pois":[{"addr":"惠泽路与长通路交叉口东南150米","cp":" ","direction":"西南","distance":"53","name":"南通高新技术创业服务中心C座","parent_poi":{"addr":"惠泽路与长通路交叉口东150米","direction":"西南","distance":"150","name":"南通高新技术创业服务中心","point":{"x":120.96296334228969,"y":31.931761377621539},"tag":"公司企业;园区","uid":"849be02e396bfeaac91b95aa"},"poiType":"房地产","point":{"x":120.9622446978817,"y":31.93134001354758},"tag":"房地产;写字楼","tel":"","uid":"f112225ba25266f1acee7a4d","zip":""},{"addr":"南通市通盛大道","cp":" ","direction":"西南","distance":"132","name":"世界之窗B座","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"房地产","point":{"x":120.96298130839988,"y":31.931470253560478},"tag":"房地产;写字楼","tel":"","uid":"b6a3354be42ff5c3af04575a","zip":""},{"addr":"高新技术创业服务中心B座","cp":" ","direction":"西","distance":"139","name":"南通高新技术创业服务中心B座","parent_poi":{"addr":"惠泽路与长通路交叉口东150米","direction":"西南","distance":"150","name":"南通高新技术创业服务中心","point":{"x":120.96296334228969,"y":31.931761377621539},"tag":"公司企业;园区","uid":"849be02e396bfeaac91b95aa"},"poiType":"房地产","point":{"x":120.96310707117128,"y":31.931324691180877},"tag":"房地产;写字楼","tel":"","uid":"469e15180e64cfbe4d554731","zip":""},{"addr":"惠泽路与长通路交叉口东150米","cp":" ","direction":"西南","distance":"150","name":"南通高新技术创业服务中心","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"公司企业","point":{"x":120.96296334228969,"y":31.931761377621539},"tag":"公司企业;园区","tel":"","uid":"849be02e396bfeaac91b95aa","zip":""},{"addr":"南通市崇川区通盛大道188号","cp":" ","direction":"南","distance":"165","name":"通兑大厦","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"房地产","point":{"x":120.96241537592859,"y":31.932244028619185},"tag":"房地产;写字楼","tel":"","uid":"958b6228161c9efe6140a976","zip":""},{"addr":"经济开发区通盛大道188号","cp":" ","direction":"南","distance":"166","name":"开发区创业外包服务中心","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"政府机构","point":{"x":120.96240639287349,"y":31.9322516897255},"tag":"政府机构;行政单位","tel":"","uid":"a90b125fccbbafe24e5547d3","zip":""},{"addr":"通盛大道188号","cp":" ","direction":"西南","distance":"169","name":"世界之窗科技创意园","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"公司企业","point":{"x":120.96307113895088,"y":31.931883955895075},"tag":"公司企业;园区","tel":"","uid":"3f1eeec7f18bcbeb80a00c69","zip":""},{"addr":"南通市崇川区长川路1号","cp":" ","direction":"东","distance":"175","name":"携程信息技术大楼","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"房地产","point":{"x":120.96033130714544,"y":31.930903325085948},"tag":"房地产;写字楼","tel":"","uid":"81c203b13abeed1ffa0cad44","zip":""},{"addr":"江苏省南通市崇川区长川路携程信息技术大楼1层","cp":" ","direction":"东","distance":"192","name":"帝伟罗邦","parent_poi":{"addr":"南通市崇川区长川路1号","direction":"东","distance":"175","name":"携程信息技术大楼","point":{"x":120.96033130714544,"y":31.930903325085948},"tag":"房地产;写字楼","uid":"81c203b13abeed1ffa0cad44"},"poiType":"购物","point":{"x":120.96020554437405,"y":31.93075010056795},"tag":"购物;家居建材","tel":"","uid":"4adeeaf5713b572fede4a50f","zip":""},{"addr":"南通市开发区新开街道惠泽路能达金融广场(鑫湖国贸中心南)","cp":" ","direction":"西","distance":"263","name":"国家电网充电站(能达金融广场)","parent_poi":{"addr":"","direction":"","distance":"","name":"","point":{"x":0.0,"y":0.0},"tag":"","uid":""},"poiType":"交通设施","point":{"x":120.96426588527915,"y":31.931048888139203},"tag":"交通设施;充电站","tel":"","uid":"d5d1421f67aadfdbadee7a04","zip":""}],"roads":[],"sematic_description":"南通高新技术创业服务中心C座西南53米"}
    private static String geturl(String geturl) throws Exception {
        //请求的webservice的url
        URL url = new URL(geturl);
        //创建http链接
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        //设置请求的方法类型
        httpURLConnection.setRequestMethod("POST");

        //设置请求的内容类型
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

        //设置发送数据
        httpURLConnection.setDoOutput(true); 
        //设置接受数据
        httpURLConnection.setDoInput(true);

        //发送数据,使用输出流
        OutputStream outputStream = httpURLConnection.getOutputStream();
        //发送的soap协议的数据
       //  String requestXmlString = requestXml("北京");
        String content = "user_id="+ URLEncoder.encode("13846", "utf-8");
        //发送数据
        outputStream.write(content.getBytes());

        //接收数据
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        StringBuffer buffer = new StringBuffer();  
        String line = "";  
        while ((line = in.readLine()) != null){  
          buffer.append(line);  
        }  
       String str = buffer.toString();  

        return str;
    }







}




















































































































































































































