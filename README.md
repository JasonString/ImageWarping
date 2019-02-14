# ImageWarping
使用java實行反向影像變形演算法,IDE使用Eclipse,並使用WindowBuilder(plugin)輔助GUI設計

## 使用的Library
* java 1.8
* opencv 3.1.0(測試過4.0.1也可以運行)
## 簡介
* 運行需求
 * 原始圖
 * 控制點及其位移，可以手打輸入，也可以滑鼠點擊輸入
 * 原始圖上直線為csv檔，每一列皆為一條直線，每列有4個數字，分別為 起始點x座標 起始點y座標 終點x座標 終點y座標
* 主程式為NewWarp.java
* Onlines.java判斷像素點是否在直線上，新作法已經不把線上的點另外計算，
* WithInLineReg.java判斷像素是否可以垂直投影在直線上
* CalNewPts.java計算像素在直線上的投影像素
  * DistLinePt.java計算投影像素的位移向量


