package brenda.com.showoff.Upload;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VideoInterface {
    @Multipart
    @POST("upload_video_file.php")
    Call<ResultObject> uploadVideoToServer(@Part MultipartBody.Part video, @Part("token") RequestBody description,@Part("post_description") RequestBody postdescription);
}
