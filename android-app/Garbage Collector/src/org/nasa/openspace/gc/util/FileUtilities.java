package org.nasa.openspace.gc.util;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

/*
 * This class stores the byte array of imagedata sent to it into the 
 * sdcard/Fireresponder on the phone
 */

public class FileUtilities {
        // Declare a variable to hold byte array derived from picture
        public static byte[] alteredImageData = null;

        public static boolean StoreByteImage(byte[] originalImageData) {

                // Start sampling and compression into a picture format
                int quality = 75;
                int sampleSize = 8;
                Bitmap myImage = null;

                try {

                        // Create a BitmapFactory options for sampling
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = sampleSize;

                        // decode byte array received
                        myImage = BitmapFactory.decodeByteArray(originalImageData, 0,
                                        originalImageData.length, options);

                        // Create a new ByteArrayOutputStream to be used for compression
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        // compress with quality value into a ByteArrayOutputStream with
                        // JPEG format
                        myImage.compress(CompressFormat.JPEG, quality, baos);
                        baos.flush();

                        // Assign alteredImageData to modified picture
                        alteredImageData = baos.toByteArray();

                        baos.close();

                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }

                return true;
        }

        public static void saveImage(String path, byte[] image) {
                String sdcard = Environment.getExternalStorageDirectory().getPath();

                Log.i("FileUtilities", "Sd card path is " + sdcard);

                File dir = new File(sdcard + "/" + path);
                dir.mkdirs();

                String fileName = Calendar.getInstance().getTimeInMillis() + ".jpg";

                Log.i("FileUtilities", "File name is " + fileName);

                File file = new File(dir, fileName);

                try {

                        FileOutputStream fos = new FileOutputStream(file);
                        Bitmap bitMap = BitmapFactory.decodeByteArray(image, 0,
                                        image.length);
                        bitMap.compress(CompressFormat.JPEG, 100, fos);

                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                }
        }
        
        public static String saveJsonData(String path, String jsonData) {
                String sdcard = Environment.getExternalStorageDirectory().getPath();

                File dir = new File(sdcard + "/" + path);
                dir.mkdirs();

                String fileName = "new-mission-plan.json";

                File file = new File(dir, fileName);
                
                try {
                
                        FileWriter writer = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(writer);
                        out.write(jsonData);
                        out.close();

                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                return fileName;
        }

        public static String santitizeToFileName(final String dirty) {
                return dirty.replaceAll("[\\Q.,-+ ()\\E]", "");
        }
}