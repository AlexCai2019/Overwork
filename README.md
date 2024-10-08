# Overwork

## 簡介
`Overwork.jar` 是個計時器軟體，以Java語言寫成，擁有計時、自訂時間、彈出視窗等功能。詳細的需求請參考[西瓜公會](https://discord.com/channels/495159997206888448/1288354364636794941/1288354715838582784)。

## 使用
- 支援Java 17.0.10 或以上版本。
- 在與軟體同個資料夾內放置 `save.json` 。
### `save.json` 格式
可參考[本專案範例](https://github.com/AlexCai2019/Overwork/blob/lightweight/save.json)。
```json
{
	"remainTime": {
		"hour": 0,
		"minute": 0,
		"second": 0,
		"color": "000000",
		"size": 24
	},
	"passTime": {
		"hour": 0,
		"minute": 0,
		"second": 0,
		"color": "000000",
		"size": 24
	}
}
```
- remainTime: 剩餘時間
- passTime: 經過時間
    - hour: 小時(0 ~ 59)
    - minute: 分鐘(0 ~ 59)
    - second: 秒(0 ~ 59)
    - color: 顏色(16進位)(0 ~ FFFFFF)
    - size: 字型大小(0 ~ 99)

## 注意
- 按右上角叉叉會自動儲存時間，但其他方法(例如工作管理員)不會。
- 本專案目前還在開發中，您目前所在的分支是[lightweight](https://github.com/AlexCai2019/Overwork/tree/lightweight)(輕量)分支，其中不含任何對Google API與Twitch的使用與引用。
- 相較之下，本專案的main分支乃是真正的集大成之作，也是lightweight分支完成後的下一步目標。

## 聯繫作者
- GitHub: [Alex Cai](https://github.com/AlexCai2019)
- E-mail: <a href="mailto:alexcai910630@gmail.com">alexcai910630@gmail.com</a>
- Discord: @alexcai

> Overwork專案使用[GPL-3.0 license](https://github.com/AlexCai2019/Overwork/blob/lightweight/LICENCE)開放原始碼。了解[GNU通用公眾授權條款](https://zh.wikipedia.org/zh-tw/GNU%E9%80%9A%E7%94%A8%E5%85%AC%E5%85%B1%E8%AE%B8%E5%8F%AF%E8%AF%81)。