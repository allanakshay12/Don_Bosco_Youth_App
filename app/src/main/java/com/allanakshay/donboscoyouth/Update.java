package com.allanakshay.donboscoyouth;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

public class Update extends AppCompatActivity {

    private static final String TAG = "Status" ;
    WebView webView;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        final AVLoadingIndicatorView avi = (AVLoadingIndicatorView)findViewById(R.id.avi_update);
        String url = "https://app.box.com/s/rubmlxlgxh9h5fybnure8duiq1n7euon";
        webView = (WebView)findViewById(R.id.updateweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                avi.smoothToShow();

            }


            @Override
            public void onPageFinished(WebView view, String url) {

                avi.smoothToHide();
            }

        });
        webView.loadUrl(url);
        isStoragePermissionGranted();
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                webView.setDownloadListener(new DownloadListener() {

                    @Override
                    public void onDownloadStart(final String url, final String userAgent,
                                                final String contentDisposition, final String mimeType,
                                                long contentLength) {
                        final DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));

                        request.setMimeType(mimeType);


                        String cookies = CookieManager.getInstance().getCookie(url);


                        request.addRequestHeader("cookie", cookies);


                        request.addRequestHeader("User-Agent", userAgent);


                        request.setDescription("Downloading file...");


                        request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                                mimeType));


                        request.allowScanningByMediaScanner();


                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                        url, contentDisposition, mimeType));
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File",
                                Toast.LENGTH_LONG).show();
                    }});
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }

        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            webView.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(final String url, final String userAgent,
                                            final String contentDisposition, final String mimeType,
                                            long contentLength) {
                    final DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));

                    request.setMimeType(mimeType);


                    String cookies = CookieManager.getInstance().getCookie(url);


                    request.addRequestHeader("cookie", cookies);


                    request.addRequestHeader("User-Agent", userAgent);


                    request.setDescription("Downloading file...");


                    request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                            mimeType));


                    request.allowScanningByMediaScanner();


                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                    url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File",
                            Toast.LENGTH_LONG).show();
                }});
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            webView.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(final String url, final String userAgent,
                                            final String contentDisposition, final String mimeType,
                                            long contentLength) {
                    final DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));

                            request.setMimeType(mimeType);


                            String cookies = CookieManager.getInstance().getCookie(url);


                            request.addRequestHeader("cookie", cookies);


                            request.addRequestHeader("User-Agent", userAgent);


                            request.setDescription("Downloading file...");


                            request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                                    mimeType));


                            request.allowScanningByMediaScanner();


                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                            url, contentDisposition, mimeType));
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);
                            Toast.makeText(getApplicationContext(), "Downloading File",
                                    Toast.LENGTH_LONG).show();
                }});
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("id", LoginPage.userid);
        outState.putString("connection", CoverPage.connectedstate);
        outState.putString("fb_id", LoginPage.fb_user_id);
        outState.putString("image", ImageSet.setimage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        LoginPage.userid = savedInstanceState.getString("id");
        CoverPage.connectedstate = savedInstanceState.getString("connection");
        LoginPage.fb_user_id = savedInstanceState.getString("fb_id");
        ImageSet.setimage = savedInstanceState.getString("image");
    }
}
