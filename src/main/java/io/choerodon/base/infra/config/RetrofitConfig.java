package io.choerodon.base.infra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.choerodon.base.infra.retrofit.AppMarketRetrofitClient;
import io.choerodon.base.infra.retrofit.PublishAppRetrofitClient;
import io.choerodon.base.infra.retrofit.RemoteTokenRetrofitClient;
import io.choerodon.core.exception.CommonException;
import io.choerodon.base.infra.retrofit.AppDownloadRetrofitClient;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zongw.lee@gmail.com
 * @date 2019/8/20
 */
@Configuration
public class RetrofitConfig {

    private String baseUrl = "http://localhost";

    // todo 修改全局的baseUrl时的线程安全 (修改baseUrl，获取RetrofitClient，发出请求，这三步是一个原子操作)
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Bean("RemoteTokenRetrofitClient")
    public RemoteTokenRetrofitClient remoteTokenRetrofitClient() {
        Retrofit retrofit = getCommonRetrofit();
        return retrofit.create(RemoteTokenRetrofitClient.class);
    }
    @Bean("AppDownloadRetrofitClient")
    public AppDownloadRetrofitClient appDownloadRetrofitClient() {
        Retrofit retrofit = getCommonRetrofit();
        return retrofit.create(AppDownloadRetrofitClient.class);
    }

    @Bean("AppMarketRetrofitClient")
    public AppMarketRetrofitClient appMarketRetrofitClient() {
        Retrofit retrofit = getCommonRetrofit();
        return retrofit.create(AppMarketRetrofitClient.class);
    }

    @Bean("PublishAppRetrofitClient")
    public PublishAppRetrofitClient publishAppRetrofitClient() {
        Retrofit retrofit = getCommonRetrofit();
        return retrofit.create(PublishAppRetrofitClient.class);
    }

    private Retrofit getCommonRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    HttpUrl httpUrl = HttpUrl.parse(baseUrl);
                    if (ObjectUtils.isEmpty(httpUrl)) {
                        throw new CommonException("error.retrofit.baseUrl.illegal", baseUrl);
                    }
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    HttpUrl oldHttpUrl = request.url();
                    HttpUrl newFullUrl = oldHttpUrl
                            .newBuilder()
                            .host(httpUrl.host())
                            .port(httpUrl.port())
                            .build();
                    return chain.proceed(builder.url(newFullUrl).build());
                })
                .build();
        Gson gson = new GsonBuilder()
                //配置Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
