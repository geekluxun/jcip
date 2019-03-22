package com.geekluxun;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright,2018-2019,xinxindai Co.,Ltd.
 *
 * @Author: luxun
 * @Create: 2019-03-22 16:08
 * @Description:
 * @Other:
 */
public class FutureRendererDemo extends FutureRenderer {
    public static void main(String[] argc){
        FutureRendererDemo demo = new FutureRendererDemo();
        demo.renderPage("luxun");
    }

    @Override
    void renderText(CharSequence s) {
        System.out.println("渲染文档");
    }

    @Override
    List<ImageInfo> scanForImageInfo(CharSequence s) {
        List<ImageInfo> list = new ArrayList<>();
        
        return list;
    }

    @Override
    void renderImage(ImageData i) {
        System.out.println("渲染图片");
    }
}
