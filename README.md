# Overwork

## 簡介
`Overwork.jar` 是個計時器軟體，以Java語言寫成，擁有計時、自訂時間、彈出視窗、自動寫檔等功能。詳細的需求請參考[西瓜公會](https://discord.com/channels/495159997206888448/1288354364636794941/1288354715838582784)。

## 使用
- 支援Java 17.0.10 或以上版本。
- 在與軟體同個資料夾內放置`save.json`。
- `save.json`格式為`{"remainTime": {"h": 0, "m": 0, "s": 0}, "passTime: {"h": 0, "m": 0, "s": 0}}`，其中h, m, s代表時、分、秒。
- 可參考[本專案範例](https://github.com/AlexCai2019/Overwork/blob/lightweight/save.json)。

## 注意
- 本軟體會產生`remainTime.txt`和`passTime.txt`檔案，最好將本軟體置於不雜亂的資料夾內。
- 按右上角叉叉會自動儲存時間，但其他方法(例如工作管理員)不會。
- 本專案目前還在開發中，您目前所在的分支是lightweight(輕量)分支，其中不含任何對Google API與Twitch的使用與引用。
- 相較之下，本專案的main分支乃是真正的集大成之作，也是lightweight分支完成後的下一步目標。

> Overwork專案使用[GPL-3.0 license](https://github.com/AlexCai2019/Overwork/blob/lightweight/LICENCE)開放原始碼。了解[GNU通用公眾授權條款](https://zh.wikipedia.org/zh-tw/GNU%E9%80%9A%E7%94%A8%E5%85%AC%E5%85%B1%E8%AE%B8%E5%8F%AF%E8%AF%81)。
