package me.sweetll.v2ex.Authentication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sweet on 2/3/16.
 */
public interface V2EXRetrofitService {
    @Headers("User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36")
    @GET("signin")
    Call<ResponseBody> signInGet();

    @Headers("User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36")
    @FormUrlEncoded
    @POST("signin")
    Call<ResponseBody> signInPost(
            @Field("u") String u,
            @Field("p") String p,
            @Field("once") String once,
            @Field("next") String next
    );

    @POST("signup")
    Call<ResponseBody> signUp();
}
