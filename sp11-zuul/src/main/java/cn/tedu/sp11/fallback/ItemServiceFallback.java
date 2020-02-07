package cn.tedu.sp11.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import cn.tedu.web.util.JsonResult;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemServiceFallback implements FallbackProvider {

	/**
	 * 返回一个服务ID,来确定,那个服务会应用当前类中 降级代码
	 * 如果返回一个null 表示所有服务
	 *     *        所有
	 *     
	 *     return "item-service"  只有商品调用失败时,才应用当前降级代码
	 */
	@Override
	public String getRoute() {
		
		return "item-service";
	}
/**
 * ClientHttpResponse 对象,是用来封装想客户端响应的降级数据
 */
	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return response();
	}
	private ClientHttpResponse response() {
        return new ClientHttpResponse() {
            //下面三个方法都是协议号
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }
            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
            	log.info("fallback body");
            	String s = JsonResult.err().msg("后台服务错误").toString();
                return new ByteArrayInputStream(s.getBytes("UTF-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
