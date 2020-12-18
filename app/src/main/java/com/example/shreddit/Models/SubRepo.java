package com.example.shreddit.Models;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.shreddit.Views.Initial.RegisterFragment;

import java.util.List;

public class SubRepo {
    private SubDao mSubDao;
    private BoardFireBaseModel fireBaseModel;
    private LiveData<List<Board>> mAllBoards;
    public SubRepo(Application application) {
        SubDB db = SubDB.getDatabase(application);
        mSubDao = db.subDao();
        mAllBoards = mSubDao.getAllBoards();
        fireBaseModel = BoardFireBaseModel.getInstance(application);
    }

    public LiveData<List<Board>> getAllBoards() {
        return mAllBoards;
    }

    public void insert(Board board, RegisterFragment.MyCallbackInterface success) {
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
        private RegisterFragment.MyCallbackInterface cb;
        public insertAsyncTask(SubDao dao, BoardFireBaseModel fireBaseModel, Boolean local, RegisterFragment.MyCallbackInterface success) {
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
