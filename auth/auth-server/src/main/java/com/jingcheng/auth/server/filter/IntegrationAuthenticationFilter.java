package com.jingcheng.auth.server.filter;

import com.alibaba.fastjson.JSONObject;
import com.jingcheng.auth.server.integration.IntegrationAuthentication;
import com.jingcheng.auth.server.integration.IntegrationAuthenticationContext;
import com.jingcheng.auth.server.integration.IntegrationAuthenticator;
import com.jinghceng.util.utils.ErrorType;
import com.jinghceng.util.utils.SystemErrorType;
import com.jinghceng.util.vo.Result;
import org.apache.http.HttpHeaders;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author yuandiajin
 * @date 2018-3-30
 **/
@Component
public class IntegrationAuthenticationFilter extends GenericFilterBean implements ApplicationContextAware {

    private static final String AUTH_TYPE_PARM_NAME = "auth_type";

    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private Collection<IntegrationAuthenticator> authenticators;

    private ApplicationContext applicationContext;

    private RequestMatcher requestMatcher;

    public IntegrationAuthenticationFilter() {
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException{
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            if (requestMatcher.matches(request)) {

                RequestParameterWrapper requestParameterWrapper = new RequestParameterWrapper(request);
                if (requestParameterWrapper.getParameter("password") == null){
                    requestParameterWrapper.addParameter("password","");
                }
                //客户端信息判断
                if(null == isHasClientDetails(requestParameterWrapper)){
                    throw new OAuth2Exception("客户端信息不能为空");
                }

                //设置集成登录信息
                IntegrationAuthentication integrationAuthentication = new IntegrationAuthentication();
                integrationAuthentication.setAuthType(requestParameterWrapper.getParameter(AUTH_TYPE_PARM_NAME));
                integrationAuthentication.setAuthParameters(requestParameterWrapper.getParameterMap());
                IntegrationAuthenticationContext.set(integrationAuthentication);
                try {
                    //预处理
                    this.prepare(integrationAuthentication);

                    filterChain.doFilter(requestParameterWrapper, servletResponse);

                    //后置处理
                    this.complete(integrationAuthentication);
                } finally {
                    IntegrationAuthenticationContext.clear();
                }
            } else {
                filterChain.doFilter(request, servletResponse);
            }
        }catch(Exception e){
            //解决没有进入filter出现异常无法捕获的问题
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json; charset=utf-8");
            Result s = Result.authFail("100",e.getMessage());
            String re = JSONObject.toJSONString(s);
            PrintWriter out = servletResponse.getWriter();
            out.append(re);
        }


    }

    /**
     * 进行预处理
     *
     * @param integrationAuthentication
     */
    private void prepare(IntegrationAuthentication integrationAuthentication){

        //延迟加载认证器
        if (this.authenticators == null) {
            synchronized (this) {
                Map<String, IntegrationAuthenticator> integrationAuthenticatorMap = applicationContext.getBeansOfType(IntegrationAuthenticator.class);
                if (integrationAuthenticatorMap != null) {
                    this.authenticators = integrationAuthenticatorMap.values();
                }
            }
        }

        if (this.authenticators == null) {
            this.authenticators = new ArrayList<>();
        }

        for (IntegrationAuthenticator authenticator : authenticators) {
            if (authenticator.support(integrationAuthentication)) {
                authenticator.prepare(integrationAuthentication);
            }
        }
    }

    /**
     * 后置处理
     *
     * @param integrationAuthentication
     */
    private void complete(IntegrationAuthentication integrationAuthentication) {
        for (IntegrationAuthenticator authenticator : authenticators) {
            if (authenticator.support(integrationAuthentication)) {
                authenticator.complete(integrationAuthentication);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 用途：在拦截时给Request添加参数
     * Cloud OAuth2 密码模式需要判断Request是否存在password参数，
     * 如果不存在会抛异常结束认证
     * 所以在调用doFilter方法前添加password参数
     */
    class RequestParameterWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> params = new HashMap<String, String[]>();

        public RequestParameterWrapper(HttpServletRequest request) {
            super(request);
            this.params.putAll(request.getParameterMap());
        }

        public RequestParameterWrapper(HttpServletRequest request, Map<String, Object> extraParams) {
            this(request);
            addParameters(extraParams);
        }

        public void addParameters(Map<String, Object> extraParams) {
            for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
                addParameter(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public String getParameter(String name) {
            String[]values = params.get(name);
            if(values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        @Override
        public String[] getParameterValues(String name) {
            return params.get(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return params;
        }

        public void addParameter(String name, Object value) {
            if (value != null) {
                if (value instanceof String[]) {
                    params.put(name, (String[]) value);
                } else if (value instanceof String) {
                    params.put(name, new String[]{(String) value});
                } else {
                    params.put(name, new String[]{String.valueOf(value)});
                }
            }
        }

    }



    // 判断请求头中是否包含client信息，不包含返回false
    private String[] isHasClientDetails(HttpServletRequest request) {

        String[] params = null;

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {

            String basic = header.substring(0, 5);

            if (basic.toLowerCase().contains("basic")) {

                String tmp = header.substring(6);
                String defaultClientDetails = new String(Base64.getDecoder().decode(tmp));

                String[] clientArrays = defaultClientDetails.split(":");

                if (clientArrays.length != 2) {
                    return params;
                } else {
                    params = clientArrays;
                }

            }
        }

        String id = request.getParameter("client_id");
        String secret = request.getParameter("client_secret");

        if (header == null && id != null) {
            params = new String[]{id, secret};
        }


        return params;
    }











}
