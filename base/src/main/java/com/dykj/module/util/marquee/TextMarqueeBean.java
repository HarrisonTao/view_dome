package com.dykj.module.util.marquee;

import com.dykj.module.R;

/**
 * TextMarquee ，实体 NewsListBean
 */

public class TextMarqueeBean {
    /**
     * id : 3
     * title : 公告2
     */

    private int id;
    private String title;
    private int iconDraw;
    private String iconUrl;

    public TextMarqueeBean(String title, int iconDraw, String iconUrl) {
        this.title = title;
        this.iconDraw = iconDraw;
        this.iconUrl = iconUrl;
    }

    public TextMarqueeBean(String title, String iconUrl) {
        id = 0;
        iconDraw = R.mipmap.icon_return;
        this.title = title;
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconDraw() {
        return iconDraw;
    }

    public void setIconDraw(int iconDraw) {
        this.iconDraw = iconDraw;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
