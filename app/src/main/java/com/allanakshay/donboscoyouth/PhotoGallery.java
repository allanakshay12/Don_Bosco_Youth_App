package com.allanakshay.donboscoyouth;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

public class PhotoGallery extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "photo_status" ;
    Intent intentphga;
    final String photourl = "https://drive.google.com/open?id=0B-gNtAmk7ZuuQXlveGh2d2w1bWM";
    WebView phwebview;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        final AVLoadingIndicatorView avi = (AVLoadingIndicatorView)findViewById(R.id.avi_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        phwebview = (WebView)findViewById(R.id.photowebview);
        phwebview.getSettings().setJavaScriptEnabled(true);
        phwebview.setWebViewClient(new WebViewClient(){
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
        phwebview.loadUrl(photourl);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout5);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view5);
        navigationView.setNavigationItemSelectedListener(this);
        isStoragePermissionGranted();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                phwebview.setDownloadListener(new DownloadListener() {

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

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            phwebview.setDownloadListener(new DownloadListener() {

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
            phwebview.setDownloadListener(new DownloadListener() {

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout5);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings5) {
            intentphga = new Intent(PhotoGallery.this, AboutUs.class);
            startActivity(intentphga);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.upcomingevents5) {
            intentphga = new Intent(PhotoGallery.this, UpcomingEvents.class);
            startActivity(intentphga);
            finish();
        } else if (id == R.id.knowyourgroup5) {
            intentphga = new Intent(PhotoGallery.this, KnowGroup.class);
            startActivity(intentphga);
            finish();
        } else if (id == R.id.attendance5) {

            intentphga = new Intent(PhotoGallery.this, Attendance.class);
            startActivity(intentphga);
            finish();

        } else if (id == R.id.photogallery5) {

        }  else if (id == R.id.join5) {
            intentphga = new Intent(PhotoGallery.this, JoinUs.class);
            startActivity(intentphga);
            finish();

        }   else if (id == R.id.feedback5) {
            intentphga = new Intent(PhotoGallery.this, Feedback.class);
            startActivity(intentphga);

        }   else if (id == R.id.finance5) {
            intentphga = new Intent(PhotoGallery.this, Finance.class);
            startActivity(intentphga);
            finish();
        }   else if (id == R.id.mom5) {
            intentphga = new Intent(PhotoGallery.this, MOM.class);
            startActivity(intentphga);
            finish();
        }  else if (id == R.id.myprofile_drawer5) {
            if(!LoginPage.userid.equals("Just a Guest")) {
                intentphga = new Intent(PhotoGallery.this, My_Profile.class);
                startActivity(intentphga);
                finish();
            } else
                Toast.makeText(PhotoGallery.this, "Please login to gain access to this content", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout5);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
