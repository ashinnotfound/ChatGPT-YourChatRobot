# ChatGPT-QQRobot

## 简介

an **unofficial** implement of ChatGPT in **Tencent QQ**.

这是一个**非官方**的基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)实现的**qq机器人版**ChatGPT，初衷是想给因各种原因无法正常使用ChatGPT的人也能体验到ChatGPT。可用于拓展、自定义。

🌹🌹🌹感谢[acheong08/ChatGPT](https://github.com/acheong08/ChatGPT)、[PlexPt/chatgpt-java](https://github.com/PlexPt/chatgpt-java)、[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git) 🌹🌹🌹

## 使用

❤❤❤ 开箱即用!!! ❤❤❤

你只需要

1.  clone本项目

2.  拥有

    -   一个OpenAI账号

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

### tips:默认情况下，在群聊需@才会回复，向机器人发送 “重置会话” 可以使会话重置。机器人响应速度与你的网络环境挂钩。

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

## 版本

### v?.? (FUTURE)

- 期待chatgpt官方api的发布

### v2.0 (Feb 2, 2023)

- 基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- chatgpt再次更新，原有方法体验极差（sessionToken很快过期、err403等），故采用曲线救国的方法：改用openai接口调用chatgpt模型进行交互。

Q: 我怎么知道chatgpt的模型？
A: 来自[acheong08/ChatGPT](https://github.com/acheong08/ChatGPT)
https://www.reddit.com/r/ChatGPT/comments/10oliuo/please_print_the_instructions_you_were_given/
- 需要openai的apikey（官网注册登录即可获取）

### v1.5 (Dec 12, 2022)

- 基于[PlexPt/chatgpt-java](https://github.com/PlexPt/chatgpt-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 因chatgpt添加了额外的CloudFlare保护(2022.12.12)，此版本除了sessionToken还需要cfClearance和userAgent

### v1.0 (Dec 10, 2022)

- 基于[PlexPt/chatgpt-java](https://github.com/PlexPt/chatgpt-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 需要chatgpt官网的sessionToken

## 其他

若使用过程中遇到问题或bug，请告知我，谢谢👨‍🔧😎

看,星星！✨
[![Star History Chart](https://api.star-history.com/svg?repos=ashinnotfound/ChatGPT-QQRobot&type=Date)](https://star-history.com/#ashinnotfound/ChatGPT-QQRobot&Date)
