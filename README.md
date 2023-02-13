# ChatGPT-QQRobot

## 我觉得我应该让你们知晓
openai似乎删除或者隐藏了原先的chatgpt模型（自从Feb 8, 2023），因此本项目现在调用的并**不是**chatgpt，而是openai的一个GPT-3模型：text-davinci-003（chatgpt也是openai的一个GPT-3模型）。具体可前往下文的 *版本-v2.5* 查看。



## 简介

an **unofficial** implement of ChatGPT in **Tencent QQ**.

这是一个**非官方**的基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)实现的**qq机器人版**ChatGPT，初衷是想给因各种原因无法正常使用ChatGPT的人也能体验到ChatGPT。可用于拓展、自定义。

🌹🌹🌹感谢[acheong08/ChatGPT](https://github.com/acheong08/ChatGPT)、[PlexPt/chatgpt-java](https://github.com/PlexPt/chatgpt-java)、[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git) 🌹🌹🌹

## 特性
- 回复为引用回复，且默认情况下，在群聊需@才会回复
- 支持上下文对话。向机器人发送 “重置会话” 可以清除会话历史
- 支持使用多个apiKey。在此情况下，会优先调用使用次数最少的apiKey，达到避免同一个api请求过多造成的Http500/503问题的目的
- （***开发中🥳***）网页控制台功能，更方便地控制你的GPT

## 使用

❤❤❤ 开箱即用!!! ❤❤❤

你只需要

1.  clone本项目

2.  拥有

    -   一个OpenAI账号

    -   一个qq号

        并把它们配置在application.yml里:

```
//这是application.yml文件
#ChatGPT
#有多少apiKey就写多少，不要留空白
apiKey:
  - sk-xxxx
  - sk-xxxx
  - sk-xxxx

#qq
qq : 123456
password : xxxx
```

3.  然后 run！！！😁😁😁

此时你的qq便是ChatGPT了！！！✨✨✨

tips：机器人响应速度与你的网络环境挂钩。

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

### v2.5 (Feb 13, 2023)
- 基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)

- chatgpt似乎删除了或者隐藏了其模型，原有方法已不再适用。因此现在使用的是并**不是**chatgpt模型，而是openai的[GPT-3](https://platform.openai.com/docs/models/gpt-3)模型：text-davinci-003
- openai对其的介绍：

    Most capable GPT-3 model. Can do any task the other models can do, often with higher quality, longer output and better instruction-following. Also supports [inserting](https://platform.openai.com/docs/guides/completion/inserting-text) completions within text.
    功能最强大的GPT-3模型。可以做任何其他模型可以做的任务，通常具有更高的质量，更长的输出和更好的指令遵循。也支持[插入](https://platform.openai.com/docs/guides/completion/inserting-text)补全文本。

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
