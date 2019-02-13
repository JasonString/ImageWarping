# ImageWarping
反向影像變形

## 使用的Library
* java 1.8
* opencv 3.1.0(測試過4.0.1也可以運行)
## 主要功能
* 主程式為NewWarp.java
* Onlines.java判斷像素點是否在直線上，新作法已經不把線上的點另外計算，
* WithInLineReg.java判斷像素是否可以垂直投影在直線上
* CalNewPts.java計算像素在直線上的投影像素
  * DistLinePt.java計算投影像素的位移向量


