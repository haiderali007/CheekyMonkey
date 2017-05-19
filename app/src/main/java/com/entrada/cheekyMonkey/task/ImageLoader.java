package com.entrada.cheekyMonkey.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import com.entrada.cheekyMonkey.Manifest;
import com.entrada.cheekyMonkey.R;
import com.entrada.cheekyMonkey.util.Util;

public class ImageLoader {


    final int stub_id = R.drawable.liquor;
    //final int stub_id = R.drawable.background_card;
    PhotosQueue photosQueue = new PhotosQueue();
    PhotosLoader photoLoaderThread = new PhotosLoader();
    //the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
    private File cacheDir;

    public ImageLoader(Context context) {
        //Make the background thread low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "eCABSPoSFnb/AndSO/Images");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();


       /* if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //RUNTIME PERMISSION Android M
            if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "eCABSPoSFnb/AndSO/Images");
            }else{
               requestPermission(context);
            }

        }*/
    }

    public void DisplayImage(String url, ImageView imageView) {
        if (cache.containsKey(url))
            imageView.setImageBitmap(cache.get(url));
        else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    static int REQUEST_WRITE_EXTERNAL_STORAGE=1;

    private static void requestPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            new AlertDialog.Builder(context)
                    .setMessage("WRITE_EXTERNAL_STORAGE PERMISSION")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }).show();

        }else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }



    private Bitmap getBitmap(String url) {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);

        //from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        //from web
        try {
            Bitmap bitmap = null;
            InputStream is = new URL(url).openStream();
            OutputStream os = new FileOutputStream(f);
            Util.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("imputstream", "excption");
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            fis.close();
            fis = new FileInputStream(f);
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void stopThread() {
        photoLoaderThread.interrupt();
    }

    public void clearCache() {
        //clear memory cache
        cache.clear();

        //clear SD cache
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    //stores List of photos to download
    class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        //removes all instances of this ImageView
        public void Clean(ImageView image) {
            for (int j = 0; j < photosToLoad.size(); ) {
                if (photosToLoad.get(j).imageView == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    //thread waits until there are any images to load in the queue
                    if (photosQueue.photosToLoad.size() == 0)
                        synchronized (photosQueue.photosToLoad) {
                            photosQueue.photosToLoad.wait();
                        }
                    if (photosQueue.photosToLoad.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp = getBitmap(photoToLoad.url);
                        cache.put(photoToLoad.url, bmp);
                        if (photoToLoad.imageView.getTag().equals(photoToLoad.url)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a = (Activity) photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit

                Log.i("InterruptedException", "excption");
            }
        }
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

}
