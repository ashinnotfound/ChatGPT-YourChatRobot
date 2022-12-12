# ChatGPT-QQRobot

## 简介

an **unofficial** implement of ChatGPT in **Tencent QQ**.

这是一个**非官方**的基于[chatgpt-java](https://github.com/PlexPt/chatgpt-java.git)和[mirai](https://github.com/mamoe/mirai.git)实现的**qq机器人版**ChatGPT，初衷是想给因各种原因无法正常使用ChatGPT的人也能体验到ChatGPT。

🌹🌹🌹再次感谢[chatgpt-java](https://github.com/PlexPt/chatgpt-java.git)和[mirai](https://github.com/mamoe/mirai.git) 🌹🌹🌹

## 使用

❤❤❤ 开箱即用!!! ❤❤❤

你只需要

1.  clone本项目

2.  拥有

    -   一个ChatGPT账号

    -   一个qq号

        并把它们配置在application.properties里:

```
//这是application.properties文件
#ChatGPT
sessionToken = ""
cfClearance = ""
userAgent = ""
#qq
qq = 123456 //qq账号
password = 123456 //qq密码
```

3.  然后 run！！！😁😁😁

此时你的qq便是ChatGPT了！！！✨✨✨

### 你可能需要了解:

-   sessionToken、cfClearance、userAgent获取方法

    1.  登录ChatGPT
    2.  打开浏览器开发者工具，切换到 `Application` 标签页。
    3.  在左侧的 `Storage - Cookies` 中找到 `__Secure-next-auth.session-token` 一行并复制其值,此为sessionToken的值
    4.  同时找到`cf_clearance`一行并复制其值，此为cfClearance的值
    5.  切换到`NetWork`标签页，在随意一个活动里的`Headers`即可找到`User-Agent`，此为userAgent的值

    参考:https://github.com/acheong08/ChatGPT/wiki/Setup#token-authentication
    
    tips：亲测，在获取上述值的时候不要开代理，否则在使用时会报403错误，即拒绝访问🤔

-   第一次使用时可能会遇到滑动验证码问题

    根据终端所给提示进行操作即可，主要步骤为：

    1.  打开最下方的`Captcha link`
    2.  打开浏览器开发者工具,切换到 `Network` 标签页.
    3.  滑动验证码
    4.  验证完成后,在 `Network` 中找到名为 `cap_union_new_verify` 的请求, 复制 ticket 的值
    5.  在终端中输入ticket的值(注意去掉引号"")

## 其他

若使用过程中遇到问题或bug，请告知我，谢谢👨‍🔧😎
