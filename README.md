# ChatGPT-QQRobot

## 简介

an **unofficial** implement of ChatGPT in **Tencent QQ**.

这是一个**非官方**的基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mirai](https://github.com/mamoe/mirai.git)实现的**qq机器人版**ChatGPT，初衷是想给因各种原因无法正常使用ChatGPT的人也能体验到ChatGPT。

🌹🌹🌹感谢[acheong08/ChatGPT](https://github.com/acheong08/ChatGPT)、[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mirai](https://github.com/mamoe/mirai.git) 🌹🌹🌹

## 使用

❤❤❤ 开箱即用!!! ❤❤❤

你只需要

1.  clone本项目

2.  拥有

    -   一个OpenApi账号

    -   一个qq号

        并把它们配置在application.properties里:

```
//这是application.properties文件
#注意不用加双引号
#ChatGPT
apiKey = xxx
#qq
qq = 123456 //qq账号
password = xxx //qq密码
```

3.  然后 run！！！😁😁😁

此时你的qq便是ChatGPT了！！！✨✨✨

### 你可能需要了解:

-   获取apiKey
    https://platform.openai.com/account/api-keys

-   第一次使用时可能会遇到滑动验证码问题

    根据终端所给提示进行操作即可，主要步骤为：

    1.  打开最下方的`Captcha link`
    2.  打开浏览器开发者工具,切换到 `Network` 标签页.
    3.  滑动验证码
    4.  验证完成后,在 `Network` 中找到名为 `cap_union_new_verify` 的请求, 复制 ticket 的值
    5.  在终端中输入ticket的值(注意去掉引号"")

## 其他

若使用过程中遇到问题或bug，请告知我，谢谢👨‍🔧😎

看,星星！✨
[![Star History Chart](https://api.star-history.com/svg?repos=ashinnotfound/ChatGPT-QQRobot&type=Date)](https://star-history.com/#ashinnotfound/ChatGPT-QQRobot&Date)
