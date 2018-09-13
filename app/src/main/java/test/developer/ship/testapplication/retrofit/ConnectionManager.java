package test.developer.ship.testapplication.retrofit;

import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import test.developer.ship.testapplication.entity.Offer;
import test.developer.ship.testapplication.entity.ResponseEntity;

/**
 * Created by Shiplayer on 12.09.18.
 */
public class ConnectionManager {
    private Retrofit retrofit;
    private ApiCommunication apiCommunication;


    public ConnectionManager(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://sandytrast.info")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        apiCommunication = retrofit.create(ApiCommunication.class);
    }

    public MutableLiveData<ResponseEntity> getOffers(){
        final MutableLiveData<ResponseEntity> mutableLiveData = new MutableLiveData<>();
        apiCommunication.getOffers(1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful() && response.code() == 200) {
                    JsonObject body = response.body();
                    List<Offer> offerList;
                    ResponseEntity ans;
                    if(body != null && body.has("offers")){
                        Gson gson = new GsonBuilder().create();
                        JsonArray array = body.getAsJsonArray("offers");
                        offerList = gson.fromJson(array, new TypeToken<List<Offer>>(){}.getType());
                        if(offerList != null){
                            ans = new ResponseEntity(offerList);
                        } else
                            ans = new ResponseEntity(new Exception());
                        mutableLiveData.postValue(ans);
                    } else{
                        mutableLiveData.postValue(new ResponseEntity(new Exception()));
                    }
                } else
                    mutableLiveData.postValue(new ResponseEntity(new Exception()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mutableLiveData.postValue(new ResponseEntity(t));
            }
        });
        return mutableLiveData;
    }

    interface ApiCommunication {
        @GET("/ins/")
        Call<JsonObject> getOffers(@Query("id") int id);
    }
}
