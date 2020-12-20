package com.example.shreddit.Models;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.shreddit.Utils.MyCallbackInterface;
import com.example.shreddit.Views.Initial.RegisterFragment;

import java.util.List;

public class SubRepo {
    private SubDao mSubDao;
    private BoardFireBaseModel fireBaseModel;
    private List<Board> mAllBoards;
    private ConnectivityManager cm;
    public SubRepo(Application application) {
        SubDB db = SubDB.getDatabase(application);
        mSubDao = db.subDao();
        fireBaseModel = BoardFireBaseModel.getInstance(application);
        cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        mAllBoards = mSubDao.getAllBoards();
    }

    public List<Board> getAllBoards() {
        if(Build.VERSION.SDK_INT >= 23){
            Network activeNetwork = cm.getActiveNetwork();
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(activeNetwork);
            boolean validated = networkCapabilities == null
                    || !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            if(validated) {
                mAllBoards = fireBaseModel.getAllBoards();
            }
            else{
                mAllBoards = mSubDao.getAllBoards();
            }
        }
        return mAllBoards;
    }

    public void insert(Board board, MyCallbackInterface success) {
        new SubRepo.insertAsyncTask(mSubDao, fireBaseModel,false,success).execute(board);
    }

    public void updateBoard(Board board) {
        mSubDao.updateBoard(board);
    }

    public void deleteBoards(Board... boards) {
        mSubDao.deleteBoards(boards);
    }

    private static class insertAsyncTask extends AsyncTask<Board, Void, Void> {
        private SubDao mAsyncTaskDao;
        private BoardFireBaseModel mAsyncFireBaseModel;
        private Boolean isLocal;
        private MyCallbackInterface cb;
        public insertAsyncTask(SubDao dao, BoardFireBaseModel fireBaseModel, Boolean local, MyCallbackInterface success) {
            mAsyncTaskDao = dao;
            mAsyncFireBaseModel = fireBaseModel;
            isLocal = local;
            cb = success;
        }

        @Override
        protected Void doInBackground(Board... boards) {
            if(isLocal)
                mAsyncTaskDao.insert(boards[0]);
            else
                mAsyncFireBaseModel.insert(boards[0],cb);
            return null;
        }

    }

}
