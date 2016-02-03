package me.sweetll.v2ex.Authentication;

import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sweet on 2/2/16.
 */
public class V2EXServerAuthenticate implements ServerAuthenticate {
    @Override
    public String userSignUp(String user, String pass, String email, String secureCode, String authType) throws Exception {
        return "Sign Up";
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        String authToken = null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v2ex.com")
                .build();
        V2EXRetrofitService mV2EXRetrofitService = retrofit.create(V2EXRetrofitService.class);
        Call<ResponseBody> call = mV2EXRetrofitService.signInGet();
        Response<ResponseBody> response = call.execute();

        Document document = Jsoup.parse(response.body().string());
        String once = document.select("input[name=once]").first().val();

        call = mV2EXRetrofitService.signInPost(user, pass, once, "/");
        response = call.execute();

        document = Jsoup.parse(response.body().string());

        for (int i = 0; i < response.headers().size(); i++) {
            Logger.d(response.headers().value(i));
            if (response.headers().name(i).contains("Set-Cookie") && response.headers().value(i).contains("A2=")) {
                Logger.d(response.headers().value(i));
                return response.headers().value(i);
            }
        }

        return null;
    }
}
