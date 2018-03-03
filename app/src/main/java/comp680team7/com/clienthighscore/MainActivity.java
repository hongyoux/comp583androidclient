package comp680team7.com.clienthighscore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.CropHintsParams;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import comp680team7.com.clienthighscore.service.BackendService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_KEY = "CLOUD_API_KEY";

    public static Integer REQUEST_IMAGE_CAPTURE = 1;
    public static Integer REQUEST_IMAGE_SELECTION = 2;

    public static BackendService SERVICE;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SERVICE = retrofit.create(BackendService.class);
    }

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Call<List<Game>> games = SERVICE.getGames();
//        games.enqueue(new Callback<List<Game>>() {
//            @Override
//            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
//                System.out.println("SUCCESS");
//            }
//
//            @Override
//            public void onFailure(Call<List<Game>> call, Throwable t) {
//                System.out.println("FAIL");
//            }
//        });

        findViewById(R.id.capturePicButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                "comp680team7.com.clienthighscore.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        findViewById(R.id.selectPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGalleryChooser();
            }
        });
    }

    private void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"), REQUEST_IMAGE_SELECTION);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            if(file.exists()) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

                Call call = SERVICE.uploadImage(body);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        System.out.println("SUCCESS!!");
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        t.printStackTrace();
                        System.out.println("FAILURE!!");
                    }
                });
            }
        } else if(requestCode == REQUEST_IMAGE_SELECTION && resultCode == RESULT_OK) {
            processImageData(data.getData());
        }
    }

    private void processImageData(Uri uri) {
        try {
            Bitmap bitmap =
                    scaleBitmapDown(
                            MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                            1200);

//            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
            sendToCloudVision(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToCloudVision(final Bitmap bitmap) {
        new AsyncTask<Object, Void, Void>(){
            @Override
            protected Void doInBackground(Object... objects) {

                HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_KEY) {
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                        super.initializeVisionRequest(request);

                        String packageName = getPackageName();

                        request.getRequestHeaders().set("X-Android-Package", packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        request.getRequestHeaders().set("X-Android-Cert", sig);
                    }
                };

                Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                builder.setVisionRequestInitializer(requestInitializer);

                Vision vision = builder.build();

                List<AnnotateImageRequest> listOfImageRequest = new ArrayList<AnnotateImageRequest>();

                AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                Image image = new Image();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

                byte[] imageBytes = baos.toByteArray();

                image.encodeContent(imageBytes);

                annotateImageRequest.setImage(image);
                ImageContext imageContext = new ImageContext();
                CropHintsParams cropHintsParams = new CropHintsParams();
                cropHintsParams.setAspectRatios(Collections.singletonList(1.77f));
                imageContext.setCropHintsParams(cropHintsParams);
                annotateImageRequest.setImageContext(imageContext);
                List<Feature> featureList = new ArrayList<Feature>();

                Feature labelDetection = new Feature();
                labelDetection.setType("LABEL_DETECTION");
                labelDetection.setMaxResults(10);

//                Feature cropHint = new Feature();
//                cropHint.setType("CROP_HINTS");


                featureList.add(labelDetection);
//                featureList.add(cropHint);

                annotateImageRequest.setFeatures(featureList);
                listOfImageRequest.add(annotateImageRequest);

                BatchAnnotateImagesRequest request = new BatchAnnotateImagesRequest();

                request.setRequests(listOfImageRequest);

                try {
                    Vision.Images.Annotate annotate = vision.images().annotate(request);
                    annotate.setDisableGZipContent(true);

                    BatchAnnotateImagesResponse response = annotate.execute();
                    System.out.println(convertResponseToString(response));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "I found these things:\n\n";

//        System.out.println(response.getResponses().get(0).getCropHintsAnnotation().getCropHints().get(0).getBoundingPoly());
//        System.out.println(response.getResponses().get(0).getCropHintsAnnotation().getCropHints().get(0).getConfidence());

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        return message;
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
