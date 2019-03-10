![ToggleImageView.png](https://upload-images.jianshu.io/upload_images/9414344-f5e0fd2f8d63af43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 零、前言
>自定义一个简单的小控件来解决切换按钮个小问题，不然写起来挺麻烦  
特别是多图的情况，selector不能用，难道一张一张switch?

```
[1].点击时透明度变化
[2].若干个图片轮回切换
[3].自定义点击时动画
```


多图 | 两图 
---|---
![](https://upload-images.jianshu.io/upload_images/9414344-bca3bca5a846724f.gif?imageMogr2/auto-orient/strip) | ![](https://upload-images.jianshu.io/upload_images/9414344-81e9bdf995153728.gif?imageMogr2/auto-orient/strip)

略缩放 | 自定义 
---|---
![](https://upload-images.jianshu.io/upload_images/9414344-ec54f12699e06d93.gif?imageMogr2/auto-orient/strip) | ![](https://upload-images.jianshu.io/upload_images/9414344-35bcd61ac2515e75.gif?imageMogr2/auto-orient/strip)

---

#### 一、使用


##### 0.引入依赖
>已经发布到github了

```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    
    dependencies {
        implementation 'com.github.toly1994328:ToggleImageView:0.02'
    }
```

---

##### 1.双按钮使用
>为了使用简单一点，直接给个初始化双按钮的方法

```
ctrl.initTwoButton(
        R.drawable.icon_start_3, v -> {
            //TODO 播放
        }, R.drawable.icon_stop_3, v -> {
            //TODO 停止
        });
```

---

##### 2.多按钮的使用
>当然两个按钮也适用

```
 player.setResIds(
         R.drawable.icon_play_single, R.drawable.icon_play_single_loop,
         R.drawable.icon_play_loop, R.drawable.icon_play_random);
         
 player.setOnToggleListener((view, idx) -> {
     switch (idx) {
         case 0:
             //TODO 单曲播放
             break;
         case 1:
             //TODO 单曲循环
             break;
         case 2:
             //TODO 循环播放
             break;
         case 3:
             //TODO 随机播放
             break;
     }
 });
```

---

##### 3.添加自定义动画
>只要传入一个Animator就行了，至于Animator动画，可以详见：[ Animator 家族使用指南](https://juejin.im/post/5c245f086fb9a049c043146a#heading-31)

```
ObjectAnimator rotation = ObjectAnimator.ofFloat(ctrl, "rotation", 0f, 180f).setDuration(300);
ObjectAnimator rotationX = ObjectAnimator.ofFloat(ctrl, "rotationX", 0f, 180f).setDuration(300);
ObjectAnimator rotationY = ObjectAnimator.ofFloat(ctrl, "rotationY", 0f, 180f).setDuration(300);

ctrl.setAnimator(rotation,rotationX,rotationY);
```



---


##### 二、代码实现
>这个很简单，只是通过资源id列表和一个索引来切换图片，并添加一些效果  
点击时执行动画，动画结束后执行对应的回调以及OnToggleListener

```
/**
 * 作者：张风捷特烈<br/>
 * 时间：2019/3/10/010:8:00<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：多图标切换器
 */
public class ToggleImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "ToggleImageView";
    private AnimatorSet mSet;//动画集合
    private List<Integer> mResIds;//图片资源id
    private List<OnClickListener> mListeners;
    private int mCurrentIdx;//当前位置
    private boolean isWithScale = true;//点击时是否略微缩放
    private Animator[] mAnimators;//自定义动画
    public ToggleImageView(Context context) {
        this(context, null);
    }
    public ToggleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResIds = new ArrayList<>();
        mListeners = new ArrayList<>();
        mSet = new AnimatorSet();
        //设置动画结束监听
        mSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setImageResource(mResIds.get(mCurrentIdx));
                if (mListeners.size() > 0) {
                    mListeners.get(mCurrentIdx).onClick(ToggleImageView.this);
                }
                if (mOnToggleListener != null) {
                    mOnToggleListener.toggle(ToggleImageView.this, mCurrentIdx);
                }
                mCurrentIdx++;
                if (mCurrentIdx == mResIds.size()) {
                    mCurrentIdx = 0;
                }
            }
        });
    }
    public void setAnimator(Animator... animators) {
        mAnimators = animators;
    }
    public void setResIds(List<Integer> resIds) {
        mResIds = resIds;
        setImageResource(mResIds.get(mCurrentIdx));
        mCurrentIdx = 1;
    }
    /**
     * 初始化双按钮
     *
     * @param resFrom 第一状态
     * @param callBackFrom 第一状态器
     * @param resTo  第二状态
     * @param callBackTo 第二状态器
     */
    public void initTwoButton(int resFrom, OnClickListener callBackFrom, int resTo, OnClickListener callBackTo) {
        mResIds.add(resFrom);
        mResIds.add(resTo);
        mListeners.add(callBackFrom);
        mListeners.add(callBackTo);
        setImageResource(mResIds.get(mCurrentIdx));
        mCurrentIdx = 1;
    }
    /**
     * 是否有缩放效果
     * @param withScale 默认true
     */
    public void setWithScale(boolean withScale) {
        isWithScale = withScale;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, .1f, 1f).setDuration(300);
                mSet.play(alpha);
                if (isWithScale) {
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, .8f, 1f).setDuration(300);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, .8f, 1f).setDuration(300);
                    mSet.play(scaleX).with(scaleY);
                }
                mSet.playTogether(mAnimators);
                mSet.start();
                break;
        }
        return super.onTouchEvent(event);
    }
    //监听
    public interface OnToggleListener {
        void toggle(View view, int currId);
    }
    private OnToggleListener mOnToggleListener;
    public void setOnToggleListener(OnToggleListener onToggleListener) {
        mOnToggleListener = onToggleListener;
    }
}
```

>比较简单，源码地址：[toly1994328/BiggerView](https://github.com/toly1994328/ToggleImageView)
