package com.sbxcloud.android.sbxcloudsdk.net.message;

import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.message.ChannelHelper;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.auth.SbxUser;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.util.SbxDataValidator;
import com.sbxcloud.android.sbxcloudsdk.util.SbxJsonModeler;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lgguzman on 24/03/17.
 */

public class SbxChannel {

    private int id;

    private String name;

    private List<SbxMessage> messages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SbxMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<SbxMessage> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SbxChannel(String name) {
        this.name = name;
    }

    public Single<SbxChannel> saveChannel() throws Exception{
        SbxUrlComposer sbxUrlComposer= ChannelHelper.getUrlCreateChannel(getName());
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<SbxChannel>() {
            @Override
            public void subscribe(final SingleEmitter<SbxChannel> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                setId(jsonObject.getJSONObject("channel").getInt("id"));
                                e.onSuccess(SbxChannel.this);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        });
    }


    public void saveChannelInBackground(final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= ChannelHelper.getUrlCreateChannel(getName());
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        setId(jsonObject.getJSONObject("channel").getInt("id"));
                        simpleResponse.onSuccess(SbxChannel.this);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }


    public Single<SbxChannel> addMember(int [] users) throws Exception{
        SbxUrlComposer sbxUrlComposer= ChannelHelper.getUrlAddMember(getId(),users);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<SbxChannel>() {
            @Override
            public void subscribe(final SingleEmitter<SbxChannel> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                e.onSuccess(SbxChannel.this);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        });
    }


    public void addMemberInBackground(int [] users, final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer=  ChannelHelper.getUrlAddMember(getId(),users);
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        simpleResponse.onSuccess(SbxChannel.this);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }



    public Single<SbxMessage> sendMessage(final SbxMessage sbxMessage) throws Exception{
        SbxUrlComposer sbxUrlComposer= ChannelHelper.getUrlSendMessage(getId(),sbxMessage.getSbxJsonModeler());
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<SbxMessage>() {
            @Override
            public void subscribe(final SingleEmitter<SbxMessage> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                e.onSuccess(sbxMessage);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        });
    }


    public void sendMessageInBackground(final SbxMessage sbxMessage, final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer=  ChannelHelper.getUrlSendMessage(getId(),sbxMessage.getSbxJsonModeler());
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        simpleResponse.onSuccess(sbxMessage);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public  Single <List<SbxMessage>> getMessage(final Class<? extends SbxJsonModeler> clazz) throws Exception{
        SbxUrlComposer sbxUrlComposer= ChannelHelper.getUrlListMessage(getId());
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<List<SbxMessage>>() {
            @Override
            public void subscribe(final SingleEmitter<List<SbxMessage>> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                List<SbxMessage> messages = new ArrayList<SbxMessage>();
                                JSONArray jsonArray = jsonObject.getJSONArray("messages");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject message=jsonArray.getJSONObject(i);
                                    SbxMessage sbxMessage = new SbxMessage();
                                    sbxMessage.setCreatedAt(SbxDataValidator.getDate(message.getString("created")));
                                    sbxMessage.setId(message.getInt("id"));
                                    sbxMessage.setSender(message.getInt("user_id"));
                                    sbxMessage.setSederEmail(message.getString("user_email"));
                                    sbxMessage.setSenderLogin(message.getString("user_login"));
                                    SbxJsonModeler sbxJsonModeler = clazz.newInstance();
                                    sbxJsonModeler.wrapFromJson(message.getJSONObject("body"));
                                    sbxMessage.setSbxJsonModeler(sbxJsonModeler);
                                    messages.add(sbxMessage);

                                }
                                e.onSuccess(messages);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        });
    }

}