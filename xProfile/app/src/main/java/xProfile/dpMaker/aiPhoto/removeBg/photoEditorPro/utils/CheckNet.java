package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.dialog.NoNetDialog;
import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.xProfile;

public class CheckNet
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    public static Activity context;
    public boolean returnCheckValue = false;
    public CheckNet() {
        super();
    }

    public CheckNet(Activity mainActivity) {
        context = mainActivity;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
        if (isConnected){
            new RunToCheckInternet().execute("www.google.com");
        } else {
            new RunToCheckInternet().execute("www.google.com");
        }
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

    }

    public void isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) xProfile.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        if (isConnected){
            new RunToCheckInternet().execute("www.google.com");
        } else {
            new RunToCheckInternet().execute("www.google.com");
        }
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    private  class RunToCheckInternet extends AsyncTask<String, Integer, Boolean> {
        protected Boolean doInBackground(String...params ) {
            publishProgress(5);
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                if(urlConnect.getResponseMessage().equals("OK")){
                   return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return  false;
            }
            return false;
        }

            @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            checkNetAccess(result);
            if (result) {
                new NoNetDialog(context).hideDialog();
            } else {
                new NoNetDialog(context).showDialog();
            }
        }
    }


    private boolean checkNetAccess(boolean result){
        returnCheckValue = result;
        return result;
    }

}