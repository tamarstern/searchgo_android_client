package com.searchgo.nativeServices;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.searchgo.constants.ServiceConstants;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class PostEventImageService {

	private final String url = ServiceConstants.EVENT_PICTURE_URL_POST;
	private static String POST_FIELD = "filename";
	private Bitmap image;
	private String token;

	private String eventId;
	private String path;

	public PostEventImageService(Bitmap image, String token, String eventId, String path) {
		this.image = image;
		this.token = token;
		this.eventId = eventId;
		this.path = path;

	}


	private void cleanResources(FileOutputStream fos, File f, ByteArrayOutputStream bos) {
		try {
            if(fos != null) {
                fos.close();
            }
            if(f != null && f.exists()) {
                f.delete();
            }
			if(bos != null) {
				bos.close();
			}
        } catch (Exception ex) {
            Log.e("finally failed ", "finally failed", ex);
        }
	}


	public String uploadFile() {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 10 * 1024 * 1024;
		final File sourceFile = new File(path);
		FileOutputStream fos = null;
		ByteArrayOutputStream bos = null;
		String serverResponseMessage = null;
		String responce = null;

			try {

				sourceFile.createNewFile();

				// Convert bitmap to byte array
				bos = new ByteArrayOutputStream();
				image.compress(CompressFormat.JPEG, 80, bos);
				byte[] bitmapMetadata = bos.toByteArray();

				// write the bytes in file
				fos = new FileOutputStream(sourceFile);
				fos.write(bitmapMetadata);
				fos.flush();


				FileInputStream fileInputStream = new FileInputStream(sourceFile.getPath());
				String urlImage = MessageFormat.format(url, eventId);
				URL url = new URL(urlImage);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty(POST_FIELD, sourceFile.getName());
				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + POST_FIELD + "\";filename="
						+ sourceFile.getName() + lineEnd);
				dos.writeBytes(lineEnd);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				int serverResponseCode = conn.getResponseCode();
				serverResponseMessage = conn.getResponseMessage();
				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);
				if (serverResponseCode <= 200) {

					//TODO - write success code
				}
				fileInputStream.close();
				dos.flush();
				dos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			} catch (MalformedURLException ex) {
				Log.e("uploadException", "Upload file to server Exception", ex);
			} catch (IOException e) {
				Log.e("uploadException", "Upload file to server Exception", e);
			} finally {
				cleanResources(fos, sourceFile, bos);
			}

		return responce;
	}
}
