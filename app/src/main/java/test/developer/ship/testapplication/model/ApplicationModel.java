package test.developer.ship.testapplication.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import test.developer.ship.testapplication.entity.ResponseEntity;
import test.developer.ship.testapplication.retrofit.ConnectionManager;

/**
 * Created by Shiplayer on 12.09.18.
 */
public class ApplicationModel extends ViewModel{
    private MutableLiveData<ResponseEntity> mOfferLiveData;
    private ConnectionManager connectionManager;

    public ApplicationModel(){
        connectionManager = new ConnectionManager();
    }

    public LiveData<ResponseEntity> getOffers(){
        if(mOfferLiveData == null){
            mOfferLiveData = connectionManager.getOffers();
        }
        return mOfferLiveData;
    }

    public void updateOffers(){
        final MutableLiveData<ResponseEntity> response = connectionManager.getOffers();
        response.observeForever(new Observer<ResponseEntity>() {
            @Override
            public void onChanged(@Nullable ResponseEntity responseEntity) {
                mOfferLiveData.postValue(responseEntity);
                response.removeObserver(this);
            }
        });
    }
}
