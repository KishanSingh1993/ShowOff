package brenda.com.showoff.Upload;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import brenda.com.showoff.R;
import brenda.com.showoff.SignUp.SignUp;

import static android.app.Activity.RESULT_OK;

public class Upload extends Fragment {

    private TextView upload_text;
    private Button upload_camera_button;
    private Button upload_gallery_button;
    private Typeface typeface;

    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_CAMERA = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private String image_url;


    public Upload() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.upload, container, false);

        //Initialize the views.
        upload_text = view.findViewById(R.id.upload_text);
        upload_camera_button = view.findViewById(R.id.upload_camera_button);
        upload_gallery_button = view.findViewById(R.id.upload_gallery_button);

        //Initialize the font.
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/myriadpro.OTF");
        upload_text.setTypeface(typeface);
        upload_camera_button.setTypeface(typeface);
        upload_gallery_button.setTypeface(typeface);

        upload_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Do something here
                        try {
                            boolean result= check_permission(getActivity());
                            if(result) {

                                Fragment record_video = new RecordVideo();
                                FragmentTransaction ft = getFragmentManager().beginTransaction(); //Initialize the fragment manager and begin the transaction.
                                ft.replace(R.id.base_layout, record_video, "record_video"); //Replace the main activity base layout with the fragment.
                                ft.commit(); //Commit the transaction.

                            }
                        } catch (Exception h) {
                            System.out.println("exception in camera button");
                        }
                    }
                }, 500);
            }
        });

        upload_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Do something here
                        try {
                            boolean result= check_permission(getActivity());
                            if(result) {
                                show_file_chooser();
                            }
                        } catch (Exception h) {
                            System.out.println("exception in gallery button");
                        }
                    }
                }, 500);
            }
        });

        return view;
    }


    //Function to check if user has given permissions to access external storage.
    public boolean check_permission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    //If the user has chosen to open camera then call this intent.
    private void camera_intent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //If the user has chosen to open gallery then call this intent.
    private void show_file_chooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Activity result to handle the image selected.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the user chose the image from gallery.
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                //user_image_view.setImageBitmap(bitmap);

                //open upload image fragment.
                if(bitmap != null) {
                    image_url = get_string_image(bitmap);

                    Bundle bundle = new Bundle();
                    bundle.putString("image_url", image_url);

                    //replace_fragment(new ImageUpload(), bundle);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //If the user took the image from camera.
        else if(requestCode == REQUEST_CAMERA)
        {
            onCaptureImageResult(data);
        }
    }

    //Save the image file captured from camera to directory.
    private void onCaptureImageResult(Intent data) {
        if (data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            // bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Set the bitmap to user image view.
            //user_image_view.setImageBitmap(bitmap);

            //open upload image fragment.
            if(bitmap != null) {
                image_url = get_string_image(bitmap);

                Bundle bundle = new Bundle();
                bundle.putString("image_url", image_url);

                //replace_fragment(new ImageUpload(), bundle);
            }

        }
        else
        {
            // Show toast.
            Toast.makeText(getActivity(), "Error capturing picture from camera. Please try again later.",
                    Toast.LENGTH_LONG).show();
        }
    }

    //Function to convert bitmap to string image.
    public String get_string_image(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
