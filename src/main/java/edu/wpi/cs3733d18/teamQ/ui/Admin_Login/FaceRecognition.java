package edu.wpi.cs3733d18.teamQ.ui.Admin_Login;



import com.github.sarxos.webcam.Webcam;
import edu.wpi.cs3733d18.teamQ.ui.Employee;
import edu.wpi.cs3733d18.teamQ.ui.User;
import javafx.print.Collation;
import jdk.nashorn.internal.scripts.JO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import static edu.wpi.cs3733d18.teamQ.ui.Admin_Login.HttpRequest.*;

public class FaceRecognition {
    private static FaceRecognition instance;
    private static String faceSetToken = "8e6a8c399662bd6476e8f7af911e2895";
    Webcam webcam = null;
    BufferedImage capture = null;
    public Thread cameraThread;
    public Thread runningThread;
    public boolean status = false;
    File file = new File("image.jpg");

    User user = User.getUser();
    ArrayList<Employee> emp = user.getEmployees();


    private static String userFaceToken = "";
    private static String apikey = "2-0iadQUGQuV-CScwyaPVHM603VWpFhA";
    private static String apisecret = "mRAPKuHaC0Fz202qhGTUIhoCMfqtNte1";



    public String detectFace(){
//
//        webcam = Webcam.getDefault();
//        webcam.open();
        this.capture = webcam.getImage();
        File file = new File("image.jpg");
        try {
            ImageIO.write(capture, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buff = getBytesFromFile(file);
        String url = "https://api-us.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        byteMap.put("image_file", buff);
        try{
            Response rsp = post(url, map, byteMap);
            String str = new String(rsp.getContent());
            JSONObject result = new JSONObject(str);
            System.out.println(result.toString());
            JSONArray face = result.getJSONArray("faces");
            System.out.println(face.toString());
            System.out.println(face.getJSONObject(0).getString("face_token"));
            userFaceToken = face.getJSONObject(0).getString("face_token");
//            System.out.println(result.getJSONObject());
        }catch (Exception e) {
            e.printStackTrace();
        }
//        webcam.close();
        return userFaceToken;
    }

    public void createFaceSet(){
        String url = "https://api-us.faceplusplus.com/facepp/v3/faceset/create";
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        map.put("display_name", "Quinotaurs");
        try {
            Response rsp = post(url, map, null);
            JSONObject faceset = new JSONObject(new String(rsp.getContent()));
            System.out.println("Face Set");
            System.out.println(faceset.toString());
            System.out.println("Faceset Token");
            System.out.println(faceset.getString("faceset_token"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String addFace(){
        userFaceToken = detectFace();
        String url = "https://api-us.faceplusplus.com/facepp/v3/faceset/addface";
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        map.put("faceset_token", faceSetToken);
        map.put("face_tokens", userFaceToken);
        try {
            if (userFaceToken.equals("")){
                return null;
            }
            Response rsp = post(url,map,null);
            JSONObject addedFace = new JSONObject(new String(rsp.getContent()));
            if (addedFace.getInt("face_added")==0){
                return null;
            }
            System.out.println(addedFace.getInt("face_added"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Employee currentUser = user.getCurrentUser();
        currentUser.setFaceID(userFaceToken);
        user.editEmployeeSingleton(currentUser);
        user.setCurrentUser(currentUser);
        return currentUser.getFaceID();
    }

    public FaceRecognition() {
        cameraThread = new Thread(()->{
            webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();
            while (true){
                this.capture = webcam.getImage();
//                System.out.println("Capturing");
                file = new File("image.jpg");
                try {
                    ImageIO.write(capture, "jpg", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        cameraThread.start();
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }


    public Double faceSearch(){
        Double confidence = null;
        String url = "https://api-us.faceplusplus.com/facepp/v3/search";
        detectFace();
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        map.put("faceset_token", faceSetToken);
        map.put("face_token", userFaceToken);
        byteMap.put("image_file", buff);
        try {
            Response rsp = post(url, map, null);
            JSONObject faceSearched = new JSONObject(new String(rsp.getContent()));
            JSONObject result = faceSearched.getJSONArray("results").getJSONObject(0);
            userFaceToken = faceSearched.getJSONArray("faces").getJSONObject(0).getString("face_token");
            System.out.println(userFaceToken);
            confidence = result.getDouble("confidence");
            System.out.println(confidence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return confidence;
    }


    public Employee compareFaces(){
//        webcam = Webcam.getDefault();
//        webcam.open();
//        this.capture = webcam.getImage();
//        File file = new File("image.jpg");
//        try {
//            ImageIO.write(capture, "jpg", file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.status=true;
        Employee maxEmp = null;
        Double maxConf=0.0;
        Double confidence = null;
        String url = "https://api-us.faceplusplus.com/facepp/v3/compare";
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        byteMap.put("image_file2",buff);
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        System.out.println(emp.size());
        System.out.println("Starting comp");
        for(Employee temp :emp){
            System.out.println(temp.getUsername());
            System.out.println(temp.getFaceID());
            if (!temp.getFaceID().equals("")){
                map.put("face_token1",temp.getFaceID());
                try {
                    Response rsp = post(url, map, byteMap);
                    JSONObject result = new JSONObject(new String(rsp.getContent()));
                    System.out.println(result.toString());
                    if (result.getJSONArray("faces2").length()<1){
                        System.out.println("No face detected. Try again!");
                        break;
                    }
                    Double tempConf = result.getDouble("confidence");
                    System.out.println(confidence);
                    if (tempConf>maxConf){
                        maxConf = tempConf;
                        maxEmp = temp;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        webcam.close();
        if (maxEmp!=null && maxConf>80.0){
            System.out.println(maxEmp.getFaceID());
            System.out.println(maxConf);
            System.out.println(maxEmp.getUsername());
            return maxEmp;
        }else {
            System.out.println("Null emp");
            return null;
        }
    }

    public static FaceRecognition getInstance(){
        if(instance == null)
            instance = new FaceRecognition();

        return instance;
    }

    public static String getUserFaceToken() {
        return userFaceToken;
    }

    public int detectBody(){
        String url = "https://api-us.faceplusplus.com/humanbodypp/beta/detect";
        int numBodies = 0;
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        byteMap.put("image_file",buff);
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        try {
            Response rsp = post(url, map, byteMap);
            JSONObject result = new JSONObject(new String(rsp.getContent()));
            numBodies = result.getJSONArray("humanbodies").length();
            System.out.println("Detected "+numBodies+" bodies");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numBodies;

    }

    public void getGesture(){
        String feedback = "";
        String url = "https://api-us.faceplusplus.com/humanbodypp/beta/gesture";
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        byteMap.put("image_file",buff);
        map.put("api_key", apikey);
        map.put("api_secret", apisecret);
        ArrayList<Double> values = new ArrayList<Double>();
        try {
            Response rsp = post(url, map, byteMap);
            JSONObject result = new JSONObject(new String(rsp.getContent()));
            System.out.println(result.toString());
            System.out.println(result.getJSONArray("hands"));
            Object gestures = result.getJSONArray("hands").get(0);
            JSONObject gest = new JSONObject(gestures.toString());
            JSONObject hand = gest.getJSONObject("gesture");
            values.add(hand.getDouble("thumb_up"));
            values.add(hand.getDouble("thumb_down"));
            values.add(hand.getDouble("rock"));
            values.add(hand.getDouble("heart_b"));
            values.add(hand.getDouble("ok"));
            if (values.indexOf(Collections.max(values))==0){
                feedback="thumb_up";
            }else if (values.indexOf(Collections.max(values))==1){
                feedback="thumb_down";
            }else if (values.indexOf(Collections.max(values))==2){
                feedback="rock";
            }else if (values.indexOf(Collections.max(values))==3){
                feedback="heart";
            }else if (values.indexOf(Collections.max(values))==4){
                feedback="ok";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(feedback);
    }

}


