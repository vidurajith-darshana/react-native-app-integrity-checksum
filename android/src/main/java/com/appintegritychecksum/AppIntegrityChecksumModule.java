package com.appintegritychecksum;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import dalvik.system.DexFile;

@ReactModule(name = AppIntegrityChecksumModule.NAME)
public class AppIntegrityChecksumModule extends ReactContextBaseJavaModule {

  private ReactContext reactContext;
  public static final String NAME = "AppIntegrityChecksum";

  public AppIntegrityChecksumModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  @ReactMethod
  public void getChecksum(Callback cb) throws Exception {

      JarFile containerJar = null;
      try {

          // getting hash of js bundle file
          File jsBundleFile = getIndexFile();
          StringBuilder bundleHash = new StringBuilder(getSha512Hash(jsBundleFile, null));

          // getting hash of manifest and native files
          String apkPath = reactContext.getApplicationInfo().sourceDir;
          containerJar = new JarFile(apkPath);

          ZipEntry classesZe = containerJar.getEntry("classes.dex");
          ZipEntry manifestZe = containerJar.getEntry("AndroidManifest.xml");

          if (classesZe != null) {
              InputStream in = containerJar.getInputStream(classesZe);
              String dexHash = getSha512Hash(null, in);
              bundleHash.append(dexHash);
          }

          if (manifestZe != null) {
              InputStream in = containerJar.getInputStream(manifestZe);
              String manifestHash = getSha512Hash(null, in);
              bundleHash.append(manifestHash);
          }

          Log.i("HASH: ", bundleHash.toString());
          cb.invoke(bundleHash.toString(),null);

      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          if (containerJar != null) {
              try {
                  containerJar.close();
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }
  }

  private String getSha512Hash(File file, InputStream inputStream) throws Exception{
      MessageDigest shaDigest = MessageDigest.getInstance("SHA-512");

      //Get file input stream for reading the file content
      FileInputStream fis = null;
      InputStream ins = null;
      if (file != null) {
          fis = new FileInputStream(file);
      } else if (inputStream != null) {
          ins =  inputStream;
      }

      //Create byte array to read data in chunks
      byte[] byteArray = new byte[1024];
      int bytesCount = 0;

      byte[] bytes = null;

      if (fis != null) {

          //Read file data and update in message digest
          while ((bytesCount = fis.read(byteArray)) != -1) {
              shaDigest.update(byteArray, 0, bytesCount);
          };

          //close the stream; We don't need it now.
          fis.close();

          //Get the hash's bytes
          bytes = shaDigest.digest();

      } else if (ins != null) {

          //Read file data and update in message digest
          while ((bytesCount = ins.read(byteArray)) != -1) {
              shaDigest.update(byteArray, 0, bytesCount);
          };

          //close the stream; We don't need it now.
          ins.close();

          //Get the hash's bytes
          bytes = shaDigest.digest();
      }

      if (bytes != null) {
          //This bytes[] has bytes in decimal format;
          //Convert it to hexadecimal format
          StringBuilder sb = new StringBuilder();
          for(int i=0; i< bytes.length ;i++)
          {
              sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
          }
          return sb.toString();
      }
      return "";
  }

  private File getIndexFile() throws IOException {

      AssetManager assetMgr = reactContext.getAssets();
      InputStream inputStream = assetMgr.open("index.android.bundle");

      File file = File.createTempFile(new Date().getTime() + "", null, reactContext.getCacheDir());

      try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
          int read;
          byte[] bytes = new byte[8192];
          while ((read = inputStream.read(bytes)) != -1) {
              outputStream.write(bytes, 0, read);
          }
      } catch (Exception e) {
          return null;
      }
      return file;
  }
}
