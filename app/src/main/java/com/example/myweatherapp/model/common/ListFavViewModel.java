package com.example.myweatherapp.model.common;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myweatherapp.model.dao.DataAccess;
import com.example.myweatherapp.model.dao.MyAppDataBase;

import java.util.List;

public class ListFavViewModel extends AndroidViewModel {

    private DataAccess dataAccess;
    private MyAppDataBase favDB;
    private LiveData<List<CityFav>> mCityFavs;


    public ListFavViewModel(@NonNull Application application) {
        super(application);
        favDB = MyAppDataBase.getDatabase(application);
        dataAccess = favDB.dataAccess();
        mCityFavs = dataAccess.getCity();
    }
    

    public void insert(CityFav cityFav) {
        new InsertAsyncTask(dataAccess).execute(cityFav);
    }

    public void delete(CityFav cityFav) {new DeleteAsyncTask(dataAccess).execute(cityFav);}

    public  LiveData<List<CityFav>> getAllFavs() {
        return mCityFavs;
    }

    private class OperationsAsyncTask extends AsyncTask<CityFav, Void, Void> {
        DataAccess mAsyncTaskDao;

        OperationsAsyncTask(DataAccess dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(CityFav... cityFavs) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(DataAccess mDao) {
            super(mDao);
        }

        @Override
        protected Void doInBackground(CityFav... cityFavs) {
            mAsyncTaskDao.addTown(cityFavs[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask{

        public DeleteAsyncTask(DataAccess dao) {
            super(dao);
        }
        @Override
        protected Void doInBackground(CityFav... cityFavs) {
            mAsyncTaskDao.delete(cityFavs[0]);
            return null;
        }
    }
}
