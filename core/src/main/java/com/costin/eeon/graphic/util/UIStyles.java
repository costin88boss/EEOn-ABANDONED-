package com.costin.eeon.graphic.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class UIStyles {
    public static final Label.LabelStyle defLabStyle = new Label.LabelStyle() {{
        font = Font.Nokia22;
        fontColor = Color.GRAY;
    }};

    public static final TextButton.TextButtonStyle defButtStyle = new TextButton.TextButtonStyle() {{
        font = Font.Nokia22;
        this.fontColor = Color.GRAY;
        this.overFontColor = Color.DARK_GRAY;
    }};

    public static final TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle() {
        {
            font = Font.Nokia22;
            fontColor = new Color(0, 1, 1, 1);
            overFontColor = new Color(0, 0.75f, 0.75f, 1);
            downFontColor = new Color(0, 0.5f, 0.5f, 1);
        }
    };

    public static final TextField.TextFieldStyle tFieldStyle = new TextField.TextFieldStyle() {{
        font = Font.Nokia22;
        fontColor = Color.WHITE;
        cursor = Utils.dot;
        selection = Utils.dot.tint(new Color(1, 1, 1, 0.5f));
    }};

    public static final Label.LabelStyle labStyle = new Label.LabelStyle() {
        {
            font = Font.Nokia22;
            fontColor = Color.RED;
        }};
}
