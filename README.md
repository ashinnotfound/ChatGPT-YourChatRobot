# ChatGPT-YourChatRobot

> ### NEWS: 已接入OPENAI的ai画图功能([DALL·E模型](https://platform.openai.com/docs/models/dall-e))
> - 无需为此功能额外配置apikey，使用原先的即可 
> - 默认指令 “ai画图” 可在配置选项修改，亦可修改返回图片方式(限qq机器人)
> ![](https://cdn.jsdelivr.net/gh/ashinnotfound/ImageHosting/img/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-10%20133117.png)![](https://cdn.jsdelivr.net/gh/ashinnotfound/ImageHosting/img/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-10%20132601.png)
> --- 2023.11.10

## 简介

> 如果觉得不错，请点点右上角的星星，这能让我快乐一整天🥰🥰🥰

> 由社区共同维护，欢迎大家的好idea或者直接提pr🫣🫣🫣

> 我还只是一位大学生，这是我在学业闲暇之余的项目，所以可能有时回复会不是很及时🥹🥹🥹

an **unofficial** implement of ChatGPT in **QQ**/**Wechat**.

这是一个**开箱即用**的**非官方**的聊天机器人，初衷是想给因各种原因无法正常使用ChatGPT的人也能体验到ChatGPT。可用于拓展、自定义。

qq机器人实现基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)；

微信机器人实现基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[wxmbaci/itchat4j-uos](https://github.com/wxmbaci/itchat4j-uos).

🌹🌹🌹感谢[acheong08/ChatGPT](https://github.com/acheong08/ChatGPT)、[PlexPt/chatgpt-java](https://github.com/PlexPt/chatgpt-java)、[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)、[mamoe/mirai](https://github.com/mamoe/mirai.git)和[wxmbaci/itchat4j-uos](https://github.com/wxmbaci/itchat4j-uos) 🌹🌹🌹

## 原理

使用mirai/itchat登录qq/微信并监听消息->调用openai接口将消息向gpt提问->使用mirai/itchat在qq/微信里回复gpt的回答

其中ai画图采用[DALL·E模型](https://platform.openai.com/docs/models/dall-e)的[generation方法](https://platform.openai.com/docs/guides/images/generations)

## 使用

❤❤❤ 开箱即用!!! ❤❤❤

> 除了下面的方法，你也可以下载release的zip包直接使用

你只需要

1.  clone本项目

2.  拥有

    -   一个OpenAI账号

    -   一个qq号/微信号

        并把它们配置在application.yml里:

```
proxy:
  #  代理配置
  #  国内墙了gpt的api，所以得用代理，一般你使用的代理软件会有相关信息，例子：
  #  host: 127.0.0.1
  #  port: 7890
  #  若不需要留空即可
  host:
  port:

gpt:
  # 使用的 chat 模型 如gpt-3.5-turbo/gpt-4 (https://platform.openai.com/docs/models/models)
  model: gpt-3.5-turbo
  # 最大token限制 越多的token意味着越多的花费(gpt-3.5-turbo上限为4096token, gpt-4则为8192)
  maxToken: 2048
  # 信息熵 越高回答越随机(Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.---via OPENAI)
  temperature: 0.5
  # 最大请求时间 超时自动中断请求
  ofSeconds: 10000
  # 基础提问 支持多个提问 可用来设定人格(对应api中的system角色)
  basicPrompt:
    - "用中文回答我的问题"
  #  openai的apikey
  #  支持多个key（虽然有判空，但仍然建议有多少个写多少个，别留空👨‍🔧）
  apiKey:
    -
qq:
  #  是否使用qq true/false
  enable: true
  #  登陆方法：true扫码登录(推荐) false密码登录
  loginByQRCode: true
  #  qq账号密码
  account:
  #  (扫码登录则无需填写密码)
  password:
  #  是否自动同意好友申请
  acceptNewFriend: false
  #  是否自动同意被邀请入群
  acceptNewGroup: false
  #  ai画图时返回方法: true链接(更快) false图片
  returnDrawByURL: true

wechat:
  #  是否使用微信 true/false
  enable: false
  #  生成的登录二维码路径 默认与项目同级
  qrPath: "./"

keyword:
  #  重置会话指令
  reset: "重置会话"
  #  ai画图指令(DALL·E模型 https://platform.openai.com/docs/models/dall-e)
  #  generation 根据关键词生成图片(https://platform.openai.com/docs/guides/images/generations)
  draw: "ai画图"
```

3.  然后 run！！！😁😁😁

此时你的qq/微信便是ChatGPT了！！！✨✨✨

tips：机器人响应速度与你的网络环境挂钩。

### 你可能需要了解:

-   获取apiKey
    https://platform.openai.com/account/api-keys

-   第一次使用qq账号密码登录时可能会遇到滑动验证码问题

    根据终端所给提示进行操作即可，主要步骤为：

    1.  打开最下方的`Captcha link`
    2.  打开浏览器开发者工具,切换到 `Network` 标签页.
    3.  滑动验证码
    4.  验证完成后,在 `Network` 中找到名为 `cap_union_new_verify` 的请求, 复制 ticket 的值
    5.  在终端中输入ticket的值(注意去掉引号"")

## 特性
- qq登录使用[mirai临时修复组件](https://github.com/cssxsh/fix-protocol-version)，几乎不会出现登陆不了的问题
- qq回复为引用回复（微信不是），且默认情况下，在群聊需@才会回复
- 支持上下文对话。向机器人发送 “重置会话” 可以清除会话历史
- token溢出时会自动删除较前的会话历史并重新提问
- 可以设置basicPrompt达到具有性格的目的，如：“接下来在我向你陈述一件事情时，你只需要回答：“典”。”
- 支持使用多个apiKey。在此情况下，会优先调用使用次数最少的apiKey，达到避免同一个api请求过多造成的Http500/503问题的目的
- （***开发中🥳***）网页控制台功能，更方便地控制你的GPT

## 版本历史
<details>

<summary></summary>

### v3.8 (NOV 10, 2023)
- 把之前写的([DALL·E模型](https://platform.openai.com/docs/models/dall-e))接入完善了，现在可以在聊天中直接调用其进行ai画图
- qq机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 微信机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[wxmbaci/itchat4j-uos](https://github.com/wxmbaci/itchat4j-uos)
### v3.7 (Aug 8, 2023)
- 最近有空能闲下来看看这个项目，主要更新了项目依赖、优化了下代码结构、增加了token消耗的计算、优化了bot交互返回信息
- qq机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 微信机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[wxmbaci/itchat4j-uos](https://github.com/wxmbaci/itchat4j-uos)
### v3.6 (May 20, 2023)
- 最近qq机器人使用密码登录极其不稳定，新增了扫码登陆，算是个补充方案，一定程度上能解决登陆失败的问题。
- qq机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 微信机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[wxmbaci/itchat4j-uos](https://github.com/wxmbaci/itchat4j-uos)
  
### v3.5 (Mar 19, 2023)
- 今天在github冲浪的时候发现，2023了竟然还有能用的java微信sdk！！！
- 现在你也可以将微信也变成chatgpt了🥰🥰🥰
- qq机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 微信机器人基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[wxmbaci/itchat4j-uos](https://github.com/wxmbaci/itchat4j-uos)

### v3.0 (Mar 4, 2023)

- 基于[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java)和[mamoe/mirai](https://github.com/mamoe/mirai.git)
- 成功接入openai刚开放的chatgpt的api，但是国内被墙了（包括之前的gpt3.0模型），需要代理才可以正常使用

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
</details>

## 其他

若使用过程中遇到问题或bug，欢迎随时联系我(email: `ashinnotfound@qq.com`)👨‍🔧😎

## 支持我
如果觉得不错，给我买杯喝的吧
![微信赞赏码](https://cdn.jsdelivr.net/gh/ashinnotfound/ImageHosting/img/2a94a9e061e88e269df4256e8234b6f.jpg)

看,星星！✨
## Star History
[![Star History Chart](https://api.star-history.com/svg?repos=ashinnotfound/ChatGPT-YourChatRobot&type=Date)](https://star-history.com/#ashinnotfound/ChatGPT-YourChatRobot&Date)
