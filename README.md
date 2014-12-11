WeSurface
=========
###写在最前面：
      当前这个仅仅只能算作是一个demo，应为开发时间较短，并且并没有提供丰富的api，所以距离框架还有些距离，
    不过会抽出时间进行维护。
###效果：
![github](https://github.com/splm/WeSurface/blob/master/WeSurface/SCR_201412_clip.gif "github")
###代码：
      先说一下这个demo的开发思路，就是开启一个线程不断的去刷新surfaceview控件，如果对控件不熟的朋友可以Google一下，
    这方面的知识。
######attr.xml文件中的自定义属性
```Java
<declare-styleable name="WeSurface">
        <attr name="state" format="enum">
            <enum name="left_up" value="0"/>
            <enum name="right_up" value="1"/>
            <enum name="left_down" value="2"/>
            <enum name="right_down" value="3"/>
        </attr>
        
        <attr name="speed" format="enum">
            <enum name="slow" value="0" />
            <enum name="middle" value="1"/>
            <enum name="fast" value="2"/>
        </attr>
        
        <attr name="src" format="reference" />
        <attr name="scale" format="float" />
        <attr name="step" format="integer" />
    </declare-styleable>
```
###使用方法
                  第一种：可以将attr.xml文件和me.splm.wesurface.MySurfaceView，复制到项目中；
                  第二种：通过依赖项目的方式（推荐）；
######布局文件中（第一步）
```Java
xmlns:WeSurface="http://schemas.android.com/apk/res-auto" //声明命名空间
```
######布局文件中（第二步）
```Java
<me.splm.wesurface.MySurfaceView
        android:id="@+id/mysurfaceview"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        WeSurface:speed="fast"
        WeSurface:state="left_up" 
        WeSurface:src="@drawable/test2"
        />
```
######Java类中
```Java
//设置到达边缘处的回调函数
mySurfaceView.setOnRuningListener(new OnRuningListener() {
            
            @Override
            public void onToBounds() {
                
                Log.e("---------------->>>>", "到达边缘了");
                
            }
        });
```
###注意：
      该demo只是给各位看官提供开发思路，满足一些简单需求。
      
###联系方式：
    微博：splm
    邮箱：splm_lis@163.com
