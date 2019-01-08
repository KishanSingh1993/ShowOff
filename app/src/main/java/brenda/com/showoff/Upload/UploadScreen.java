package brenda.com.showoff.Upload;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brenda.com.showoff.MainActivity;
import brenda.com.showoff.R;
import brenda.com.showoff.Util.CameraUtils;
import brenda.com.showoff.activities.AddBioScreen;
import brenda.com.showoff.apis.ApiUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static brenda.com.showoff.AppController.TAG;


public class UploadScreen extends Fragment implements EasyPermissions.PermissionCallbacks {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static String imageStoragePath,videoPath;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final int PICK_IMAGE = 1;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    private TextView txtDescription;
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo,btnPickImg,btnUpload;
    private OnFragmentInteractionListener mListener;
    private String encodedImage;
    int actionId=0;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private Uri uri;
    private String pathToStoredVideo;
    private String utoken;

    public UploadScreen() {
        // Required empty public constructor
    }

    public static UploadScreen newInstance(String param1, String param2) {
        UploadScreen fragment = new UploadScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_upload_screen, container, false);

        SharedPreferences tokenpref = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        utoken = tokenpref.getString("token","");

        if (!CameraUtils.isDeviceSupportCamera(getContext())) {
            Toast.makeText(getContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        txtDescription = view.findViewById(R.id.txt_desc);
        imgPreview = view.findViewById(R.id.imgPreview);
        videoPreview = view.findViewById(R.id.videoPreview);
        btnCapturePicture = view.findViewById(R.id.btnCapturePicture);
        btnRecordVideo = view.findViewById(R.id.btnRecordVideo);
        btnPickImg = view.findViewById(R.id.btnpickfromgalarry);
        btnUpload = view.findViewById(R.id.upload);

        btnUpload.setVisibility(View.GONE);

        /**
         * Capture image on button click
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                actionId=1;
                if (CameraUtils.checkPermissions(getContext())) {
                    captureImage();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            }
        });


        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                actionId=2;

                Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(videoCaptureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

        btnPickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImg();
                actionId=3;
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (actionId==2){

                    uploadVideoToServer(pathToStoredVideo);
                    Toast.makeText(getContext(),"Upload video",Toast.LENGTH_LONG).show();
                }
                else if (actionId==1){

                    callImageApi();
                }

            }
        });

        restoreFromBundle(savedInstanceState);

        return view;

    }

    /**
     * Restoring store image path from saved instance state
     */
    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    } else if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + VIDEO_EXTENSION)) {
                        //previewVideo();
                    }
                }
            }
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void pickImg(){

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }


    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_VIDEO);
        if (file != null) {
            videoPath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getContext(), file);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getContext(), imageStoragePath);

                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE){
            uri = data.getData();
            if(EasyPermissions.hasPermissions(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                videoPreview.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.GONE);
                imgPreview.setVisibility(View.GONE);
                btnPickImg.setVisibility(View.GONE);
                btnCapturePicture.setVisibility(View.GONE);
                btnRecordVideo.setVisibility(View.GONE);
                videoPreview.setVideoURI(uri);
                videoPreview.start();
                btnUpload.setVisibility(View.VISIBLE);
                pathToStoredVideo = getRealPathFromURIPath(uri, getActivity());


            }else{
                EasyPermissions.requestPermissions(getActivity(), getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }


        else if (requestCode == PICK_IMAGE) {
            //TODO: action
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                txtDescription.setVisibility(View.GONE);
                videoPreview.setVisibility(View.GONE);

                imgPreview.setVisibility(View.VISIBLE);
                imgPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            if(EasyPermissions.hasPermissions(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                videoPreview.setVideoURI(uri);
                videoPreview.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, getActivity());
                Log.d(TAG, "Recorded Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void previewCapturedImage() {
        try {
            // hide video preview
            txtDescription.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
            btnPickImg.setVisibility(View.GONE);
            btnCapturePicture.setVisibility(View.GONE);
            btnRecordVideo.setVisibility(View.GONE);
            btnUpload.setVisibility(View.VISIBLE);
            imgPreview.setVisibility(View.VISIBLE);

            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            imgPreview.setImageBitmap(bitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.d("base64",encodedImage);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(getContext());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void uploadVideoToServer(String pathToVideoFile){
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("token"), utoken);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("showoff", videoFile.getName(), videoBody);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile,tokenBody);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();

                String status = response.body().getSuccess();

                if (status.matches("1")){

                    txtDescription.setVisibility(View.VISIBLE);
                    videoPreview.setVisibility(View.GONE);
                    btnPickImg.setVisibility(View.VISIBLE);
                    btnCapturePicture.setVisibility(View.VISIBLE);
                    btnRecordVideo.setVisibility(View.VISIBLE);
                    btnUpload.setVisibility(View.GONE);
                    imgPreview.setVisibility(View.GONE);

                    Toast.makeText(getContext(), "Result " + status, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Result " + result.getSuccess());
                }



            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
            }
        });
    }


    private void callImageApi(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loging...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.baseUrl + ApiUrl.uploadImage + ".php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("success");

                            if (status.matches("1")){

                                txtDescription.setVisibility(View.VISIBLE);
                                videoPreview.setVisibility(View.GONE);
                                btnPickImg.setVisibility(View.VISIBLE);
                                btnCapturePicture.setVisibility(View.VISIBLE);
                                btnRecordVideo.setVisibility(View.VISIBLE);
                                btnUpload.setVisibility(View.GONE);
                                imgPreview.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", utoken);
                params.put("image_url", encodedImage);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
