package cn.tedu.sp11.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import cn.tedu.web.util.JsonResult;

@Component
public class AccessFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		//对指定的derviceid过滤,如果过滤所有服务,直接返回true
		//判断全面请求,是否要执行过滤
		//判断只会item-service 进行过滤,user,order都不执行过滤
		
		//获取这次请求ID    上下文对象
		RequestContext ctx = RequestContext.getCurrentContext();//
		String  serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
		if(serviceId.equals("item-service")) {
			return true;
		}
		return false;
	}
	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();//
           HttpServletRequest req = ctx.getRequest();
           String token = req.getParameter("token");
           if(token==null) {
        	   ctx.setSendZuulResponse(false);
        	   ctx.setResponseStatusCode(200);
        	   ctx.setResponseBody(JsonResult.err("not info").toString());
           }
         //zuul过滤器返回的数据设计为以后扩展使用，
   		//目前该返回值没有被使用
		return null;
	}
	@Override
	public String filterType() {
		//过滤器类型,前置,路由,后置
		return FilterConstants.PRE_TYPE;//前置
	}
	@Override
	public int filterOrder() {
    //过滤器的循序号
		return FilterConstants.PRE_DECORATION_FILTER_ORDER+1;
	}

}
