package com.viktoria.sharent.service;

import com.cloudinary.Cloudinary;
import com.viktoria.sharent.SharentalUtils;
import com.viktoria.sharent.exception.SharentalException;
import com.viktoria.sharent.pojo.Image;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;


@Service
public class ImageUploadService {
    private static final String CLOUDINARY_PUBLIC_ID_CONST = "public_id";

    private static final String IMGUR_IMAGE_UPLOAD_URL = "https://api.imgur.com/3/upload";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Value("${imgur.clientId}")
    private String imgurClientKey;

    @Value("${cloudinary.watermark_image_id}")
    private String watermarkImageTag;

    private HttpClient httpClient;

    @PostConstruct
    void init() {
        logger.info("Initializing cloudinary and http client for imgur");
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",cloudName);
        config.put("api_key",apiKey);
        config.put("api_secret",apiSecret);
        cloudinary = new Cloudinary(config);
        PoolingClientConnectionManager poolingClientConnectionManager = new PoolingClientConnectionManager();
        poolingClientConnectionManager.setDefaultMaxPerRoute(10);
        poolingClientConnectionManager.setMaxTotal(100);
        httpClient = new DefaultHttpClient(poolingClientConnectionManager);
    }

    public Image uploadImage(MultipartFile imageFile) {
        try {
            File file = File.createTempFile(UUID.randomUUID().toString(), "");
            imageFile.transferTo(file);
            return uploadToCloudinary(file);
        } catch (Exception e) {
            logger.error("Error uploading image to cloudinary", e);
            SharentalUtils.raiseException("Some Error occured, please try again");
        }

        return null;
    }

    /**
     *  Start uploading to cloudinary server
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    Image uploadToCloudinary(File file) throws IOException {
        Map map = cloudinary.uploader().upload(file, new HashMap());
        String uploadedImageTag = (String) map.get(CLOUDINARY_PUBLIC_ID_CONST);
        logger.info("Image uploaded to cloudinary with response {} and uploadTag {}", map, uploadedImageTag);
        return createImageVariations(uploadedImageTag);
    }

    /**
     * Creates different variants for the same image
     * @param uploadedImageTag
     * @return
     */
    private Image createImageVariations(String uploadedImageTag) {
        Image image = new Image();
        image.setOriginalImageUrl(cloudinary.url().generate(uploadedImageTag));

        image.setImageUrl(uploadToImgur(cloudinary.url().generate(uploadedImageTag)));

        ImgurImage imgurImage = new ImgurImage(image.getImageUrl());

        return Image.builder()
                .hdpiImageUrl(imgurImage.getLargeThumbnail())
                .mdpiImageUrl(imgurImage.getSmallThumbnail())
                .ldpiImageUrl(imgurImage.getMediumThumbnail())
                .build();
    }

    /**
     * uploads provided url to imgur, to reduce api load on cloudinary
     *
     * @param url
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String uploadToImgur(String url) {
        HttpPost post = new HttpPost(IMGUR_IMAGE_UPLOAD_URL);
        post.setHeader("Authorization", "Client-ID " + imgurClientKey);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("image", url));
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            logger.error("Unable to set entity while uploading image to imgur", e);
            throw new SharentalException("Some Error occured, please try again");
        }

        try {
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("Unable to get OK status from imgur while uploading image using client key {}, response {}",
                        imgurClientKey, EntityUtils.toString(httpResponse.getEntity()));
                throw new SharentalException("Some error occured, we are working on it");
            }
            String response = EntityUtils.toString(httpResponse.getEntity());
            Map<String, String> res = (Map<String, String>) ((Map) SharentalUtils.getMapFromJson(response, String.class, Object.class, HashMap.class)).get("data");
            return res.get("link");
        } catch (IOException e) {
            logger.error("Unable to upload to imgur server", e);
            throw new SharentalException("Some Error occured, please try again");
        }

    }






    /**
     * Handles processing on imgur images
     * @author avinash
     *
     */
    @SuppressWarnings("unused")
    private class ImgurImage {
        String imageUrl;
        String smallSquareImage;
        String bigSquareImage;
        String smallThumbnail;
        String mediumThumbnail;
        String largeThumbnail;
        String hugeThumbnail;

        public ImgurImage(String imageUrl) {
            this.imageUrl = imageUrl;
            init();
        }

        private void init() {
            int index = imageUrl.lastIndexOf(".");
            String imageUrlWithoutExtension = imageUrl.substring(0, index);
            String extension = imageUrl.substring(index+1, imageUrl.length());
            smallSquareImage = imageUrlWithoutExtension + "s." + extension;
            bigSquareImage = imageUrlWithoutExtension + "b." + extension;
            smallThumbnail = imageUrlWithoutExtension + "t." + extension;
            mediumThumbnail = imageUrlWithoutExtension + "m." + extension;
            largeThumbnail = imageUrlWithoutExtension + "l." + extension;
            hugeThumbnail = imageUrlWithoutExtension + "h." + extension;
        }

        public String getSmallSquareImage() {
            return smallSquareImage;
        }

        public String getBigSquareImage() {
            return bigSquareImage;
        }

        public String getMediumThumbnail() {
            return mediumThumbnail;
        }

        public String getLargeThumbnail() {
            return largeThumbnail;
        }

        public String getHugeThumbnail() {
            return hugeThumbnail;
        }

        public String getSmallThumbnail() {
            return smallThumbnail;
        }


    }

}

