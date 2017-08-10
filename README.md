android爱心动画的实现

 爱心动画的实现思路
 - 1.首先会生成很多的布局，所以应该是一个layout,然后动态的添加view
 - 2.每个view添加的时候是一个完整的动画，两部分：首先在底部由小变大，然后是一个曲线的上升过程. 在曲线上升的过程中，会有一个渐隐的效果。
 -  3.点击的的星星的颜色是随机的,曲线上升的速率也是随机的。

这个工程实现的真正难度在于Bezier曲线的绘制。

示例：
![enter image description here](https://github.com/curtis2/AndroidMakelikeLayout/blob/master/device-2017-08-10-160959.gif)