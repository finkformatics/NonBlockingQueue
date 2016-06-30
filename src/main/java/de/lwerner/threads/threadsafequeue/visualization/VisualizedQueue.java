package de.lwerner.threads.threadsafequeue.visualization;

import de.lwerner.threads.threadsafequeue.NonBlockingQueue;

import java.awt.*;

class VisualizedQueue extends NonBlockingQueue<Integer> {

    private static final Color LINE_COLOR = Color.DARK_GRAY;
    private static final Color TEXT_COLOR = Color.DARK_GRAY;
    private static final Color READ_INDEX_MARKER_COLOR = new Color(0, 200, 100);
    private static final Color WRITE_INDEX_MARKER_COLOR = new Color(200, 50, 50);

    private QueueCanvas parent;

    /**
     * Initializes this queue
     */
    VisualizedQueue(QueueCanvas parent) {
        super(10);

        this.parent = parent;
    }

   /* private void drawQueue(Graphics g, int parentWidth, int parentHeight) {
        g.setColor(LINE_COLOR);

        g.drawLine(25, parentHeight / 2 - 50, parentWidth - 25, parentHeight / 2 - 50);
        g.drawLine(25, parentHeight / 2 + 50, parentWidth - 25, parentHeight / 2 + 50);
        g.drawLine(25, parentHeight / 2, parentWidth - 25, parentHeight / 2);
        for (int i = 0; i <= 11; i++) {
            g.drawLine(25 + i * (parentWidth - 50) / 11, parentHeight / 2 - 50, 25 + i * (parentWidth - 50) / 11, parentHeight / 2 + 50);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        g.drawString("index", 35, parentHeight / 2 - 18);
        g.drawString("content", 35, parentHeight / 2 + 32);
    }

    private void drawMarkers(Graphics g, int parentWidth, int parentHeight) {
        g.setColor(READ_INDEX_MARKER_COLOR);
        int readMarkerX = 25 + (int)((getReadIndex() + 1.5) * (parentWidth - 50) / 11) - 10;
        int readMarkerY = parentHeight / 2 - 80;
        g.fillOval(readMarkerX, readMarkerY, 20, 20);
        g.setColor(WRITE_INDEX_MARKER_COLOR);
        int writeMarkerX = 25 + (int)((getWriteIndex() + 1.5) * (parentWidth - 50) / 11) - 10;
        int writeMarkerY = parentHeight / 2 + 60;
        g.fillOval(writeMarkerX, writeMarkerY, 20, 20);
    }

    private void drawContents(Graphics g, int parentWidth, int parentHeight) {
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        int upperTextY = parentHeight / 2 - 18;
        int lowerTextY = parentHeight / 2 + 32;
        for (int i = 0; i < 10; i++) {
            int textX = (int)((i + 1.5) * (parentWidth - 50) / 11);
            g.drawString("" + i, textX, upperTextY);
            Integer content = getFieldContent(i);
            g.drawString(content == null ? "[empty]" : "" + content, textX, lowerTextY);
        }
    }

    void render(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(READ_INDEX_MARKER_COLOR);
        g.fillOval(10, 10, 20, 20);
        g.drawString("read index marker", 40, 26);
        g.setColor(WRITE_INDEX_MARKER_COLOR);
        g.fillOval(10, 40, 20, 20);
        g.drawString("write index marker", 40, 56);

        drawQueue(g, parent.getWidth(), parent.getHeight());
        drawMarkers(g, parent.getWidth(), parent.getHeight());
        drawContents(g, parent.getWidth(), parent.getHeight());
    }
*/
}