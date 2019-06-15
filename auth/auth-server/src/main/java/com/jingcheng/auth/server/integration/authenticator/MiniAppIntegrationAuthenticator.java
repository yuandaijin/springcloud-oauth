package com.jingcheng.auth.server.integration.authenticator;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.jingcheng.auth.server.integration.IntegrationAuthentication;
import com.jingcheng.auth.server.integration.IntegrationAuthenticator;
import com.jingcheng.auth.server.wechat.WechatMiniAppToken;
import com.jingcheng.user.client.UserClient;
import com.jingcheng.user.common.UserVo;
import com.jinghceng.util.vo.Result;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 小程序集成认证
 *
 * @author yuandiajin
 * @date 2018-3-31
 **/
@Service
public class MiniAppIntegrationAuthenticator implements IntegrationAuthenticator {

    public final static String SOCIAL_TYPE_WECHAT_MINIAP = "wechat";

    @Autowired
    private UserClient userClient;

    @Autowired
    private WxMaService wxMaService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 小程序登录包含三个参数 （code    iv   encryptedData）
     *
     * @param integrationAuthentication
     * @return
     */
    @Override
    public UserVo authenticate(IntegrationAuthentication integrationAuthentication) {
        WxMaJscode2SessionResult session = null;
        String code = integrationAuthentication.getAuthParameter("code");
        try {
            session = this.wxMaService.getUserService().getSessionInfo(code);
            WechatMiniAppToken wechatToken = new WechatMiniAppToken(session.getOpenid(), session.getUnionid(), session.getSessionKey());
            // 加密算法的初始向量
            wechatToken.setIv(integrationAuthentication.getAuthParameter("iv"));
            // 用户的加密数据
            wechatToken.setEncryptedData(integrationAuthentication.getAuthParameter("encryptedData"));
        } catch (WxErrorException e) {
            throw new InternalAuthenticationServiceException("获取微信小程序用户信息失败", e);
        }
        String unionId = session.getUnionid();
        Result<UserVo> result = userClient.findByUnionId(unionId);
        if (result.isSuccess()) {
            result.getData().setPassword(passwordEncoder.encode(code));
            return result.getData();
        }
        return null;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SOCIAL_TYPE_WECHAT_MINIAP.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
