package com.example.app.doctor;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.List;

public class LineGraphView extends View {
    private Paint linePaint;
    private Paint pointPaint;
    private Paint fillPaint;
    private Paint tooltipPaint;
    private Paint tooltipTextPaint;
    
    private List<Float> scores = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private float animationProgress = 0f;
    private float maxScore = 100f;
    private float padding = 40f;
    
    private int selectedIndex = -1;
    private float touchX = -1;

    public LineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(8f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setPathEffect(new CornerPathEffect(50));

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setShadowLayer(8, 0, 4, Color.parseColor("#44000000"));

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.parseColor("#40FFFFFF"));
        fillPaint.setStyle(Paint.Style.FILL);

        tooltipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tooltipPaint.setColor(Color.WHITE);
        tooltipPaint.setStyle(Paint.Style.FILL);
        tooltipPaint.setShadowLayer(12, 0, 6, Color.parseColor("#66000000"));

        tooltipTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tooltipTextPaint.setColor(Color.parseColor("#4F46E5"));
        tooltipTextPaint.setTextSize(32f);
        tooltipTextPaint.setTextAlign(Paint.Align.CENTER);
        tooltipTextPaint.setFakeBoldText(true);
    }

    public void setData(List<Float> newScores, List<String> newDates) {
        this.scores = newScores;
        this.dates = newDates;
        startAnimation();
    }

    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scores == null || scores.isEmpty()) return false;

        float width = getWidth() - (padding * 2);
        float stepX = width / Math.max(1, scores.size() - 1);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                selectedIndex = Math.round((touchX - padding) / stepX);
                selectedIndex = Math.max(0, Math.min(selectedIndex, scores.size() - 1));
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                selectedIndex = -1;
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (scores == null || scores.isEmpty()) return;

        float width = getWidth() - (padding * 2);
        float height = getHeight() - (padding * 4);
        float startY = padding + 40f;
        
        float stepX = width / Math.max(1, scores.size() - 1);

        Path path = new Path();
        Path fillPath = new Path();

        for (int i = 0; i < scores.size(); i++) {
            float x = padding + (i * stepX);
            float targetY = startY + height - (scores.get(i) / maxScore * height);
            // Apply animation progress to Y coordinate
            float y = startY + height - ((scores.get(i) / maxScore * height) * animationProgress);

            if (i == 0) {
                path.moveTo(x, y);
                fillPath.moveTo(x, startY + height);
                fillPath.lineTo(x, y);
            } else {
                path.lineTo(x, y);
                fillPath.lineTo(x, y);
            }
            
            if (i == scores.size() - 1) {
                fillPath.lineTo(x, startY + height);
                fillPath.close();
            }
        }

        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(path, linePaint);

        // Draw points and Tooltip
        for (int i = 0; i < scores.size(); i++) {
            float x = padding + (i * stepX);
            float y = startY + height - ((scores.get(i) / maxScore * height) * animationProgress);
            
            if (i == selectedIndex) {
                canvas.drawCircle(x, y, 16f, pointPaint);
                drawTooltip(canvas, x, y, i);
            } else {
                canvas.drawCircle(x, y, 10f, pointPaint);
            }
        }
    }

    private void drawTooltip(Canvas canvas, float x, float y, int index) {
        String scoreText = Math.round(scores.get(index)) + "%";
        String dateText = (dates != null && index < dates.size()) ? dates.get(index) : "";
        
        String fullText = scoreText + " (" + dateText + ")";
        float textWidth = tooltipTextPaint.measureText(fullText);
        float rectPadding = 20f;
        
        RectF rect = new RectF(
            x - (textWidth / 2) - rectPadding,
            y - 100f,
            x + (textWidth / 2) + rectPadding,
            y - 40f
        );

        // Keep tooltip within bounds
        if (rect.left < 5) {
            rect.offset(-rect.left + 5, 0);
        } else if (rect.right > getWidth() - 5) {
            rect.offset(getWidth() - 5 - rect.right, 0);
        }

        canvas.drawRoundRect(rect, 15f, 15f, tooltipPaint);
        canvas.drawText(fullText, rect.centerX(), rect.centerY() + 12f, tooltipTextPaint);
        
        // Draw a small arrow pointing down
        Path arrow = new Path();
        arrow.moveTo(x - 15, y - 40);
        arrow.lineTo(x + 15, y - 40);
        arrow.lineTo(x, y - 25);
        arrow.close();
        canvas.drawPath(arrow, tooltipPaint);
    }
}
