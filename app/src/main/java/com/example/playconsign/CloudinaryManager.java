package com.example.playconsign;

import android.content.Context;
import android.net.Uri;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryManager {
    private static Cloudinary cloudinary;

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", BuildConfig.CLOUD_NAME);
            config.put("api_key", BuildConfig.API_KEY);
            config.put("api_secret", BuildConfig.API_SECRET);
            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }

    public static void uploadImage(Context context, Uri imageUri, Callback callback) {
        new Thread(() -> {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                Map uploadResult = getCloudinary().uploader().upload(inputStream, ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");
                callback.onSuccess(imageUrl);
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }).start();
    }

    public interface Callback {
        void onSuccess(String imageUrl);
        void onFailure(Exception e);
    }
}

