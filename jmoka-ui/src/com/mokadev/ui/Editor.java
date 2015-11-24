package com.mokadev.ui;

import com.shelodev.oping2.IndexBuffer;
import com.shelodev.oping2.OpingParser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;

public class Editor extends JTextPane
{
    private char[] code;
    private SimpleAttributeSet[] styles = new SimpleAttributeSet[8];
    private Timer timer;
    private boolean editable = true;
    private StyledDocument document;

    public Editor()
    {
        setMargin(new Insets(10, 10, 10, 10));
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        document = getStyledDocument();

        int w = getFontMetrics(getFont()).charWidth(' ');
        TabStop[] stops = new TabStop[8];

        for (int i = 0; i < stops.length; i++)
        {
            stops[i] = new TabStop(w * i * 2);
        }

        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setTabSet(attrs, new TabSet(stops));
        setParagraphAttributes(attrs, false);

        setupStyles();

        updateContent();

        document.addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                if (editable)
                {
                    scheduleUpdate();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
            }
        });

        timer = new Timer(50, e -> updateContent());
        timer.setRepeats(false);
    }

    private void scheduleUpdate()
    {
        if (!timer.isRunning())
        {
            timer.start();
        }
    }

    private void updateContent()
    {
        editable = false;

        if (code == null)
        {
            try
            {
                code = document.getText(0, document.getLength()).toCharArray();
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
            }
        }

        int cursorPosition = getCaretPosition();

        IndexBuffer buffer;
        OpingParser parser = new OpingParser();

        try
        {
            buffer = parser.parseSyntax(code);
            buffer.close();
        }
        catch (Exception e)
        {
            editable = true;
            code = null;
            return;
        }

        try
        {
            removeAll();
            document.remove(0, document.getLength());
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }

        try
        {
            int lastEnd = 0;

            for (int i = 0; i < buffer.getSize(); i++)
            {
                int position = buffer.getPosition(i);
                int length = buffer.getLength(i);

                if (position > lastEnd)
                {
                    for (int j = lastEnd; j < position; j++)
                    {
                        document.insertString(document.getLength(),
                                String.valueOf(code[j]), styles[7]);
                    }
                }

                String code = buffer.getString(this.code, i);
                byte type = buffer.getType(i);

                document.insertString(document.getLength(), code, styles[type]);

                lastEnd = position + length;
            }

            if (lastEnd < code.length)
            {
                for (int j = lastEnd; j < code.length; j++)
                {
                    document.insertString(document.getLength(),
                            String.valueOf(code[j]), styles[7]);
                }
            }
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }

        editable = true;
        code = null;

        setCaretPosition(cursorPosition);
    }

    private void setupStyles()
    {
        styles[0] = new SimpleAttributeSet();
        StyleConstants.setBold(styles[0], true);

        styles[1] = new SimpleAttributeSet();
        StyleConstants.setBold(styles[1], true);

        styles[2] = new SimpleAttributeSet();
        StyleConstants.setBold(styles[2], true);

        // ELEMENT_BRANCH_NAME
        styles[3] = new SimpleAttributeSet();
        StyleConstants.setForeground(styles[3], new Color(180, 164, 88));
        StyleConstants.setBold(styles[3], true);

        styles[4] = new SimpleAttributeSet();

        // ELEMENT_LEAF_KEY
        styles[5] = new SimpleAttributeSet();
        StyleConstants.setForeground(styles[5], new Color(180, 110, 51));

        styles[6] = new SimpleAttributeSet();

        styles[7] = new SimpleAttributeSet();
    }

    public void load(BasePathFile filePath)
    {
        code = filePath.read();
        updateContent();
    }
}
