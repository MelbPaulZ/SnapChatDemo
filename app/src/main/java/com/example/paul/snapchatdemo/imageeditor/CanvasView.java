package com.example.paul.snapchatdemo.imageeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

	public static boolean enableCanvas = false;

	public int width;
	public int height;
	private Bitmap mBitmap;
	public Canvas mCanvas;
	private List<Path> pathList;
	private Path latestPath;

	Context context;
	private Paint mPaint;
	private float mX, mY;
	private static final float TOLERANCE = 5;

	public CanvasView(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;

		// init list of paths
		pathList = new ArrayList<Path>();
		latestPath = new Path();
		pathList.add(latestPath);

		// and we set a new Paint with the desired attributes
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeWidth(10f);
	}

	// override onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// your Canvas will draw onto the defined Bitmap
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);


	}

	// override onDraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		for (Path path: pathList) {
			canvas.drawPath(path, mPaint);
		}

	}

	public void undo() {
		// erase the drawing
		latestPath.reset();
		invalidate();

		// remove path from the list
		pathList.remove(pathList.size()-1);

		// update latest path with the latest element on the list
		latestPath = pathList.get(pathList.size() - 1);
	}

	// when ACTION_DOWN start touch according to the x,y values
	private void startTouch(float x, float y) {
		// create new path
		latestPath = new Path();

		// add path to the list
		pathList.add(latestPath);

		latestPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	// when ACTION_MOVE move touch according to the x,y values
	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOLERANCE || dy >= TOLERANCE) {
			latestPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	// when ACTION_UP stop touch
	private void upTouch() {
		latestPath.lineTo(mX, mY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startTouch(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			moveTouch(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			upTouch();
			invalidate();
			break;
		}
		return enableCanvas;
	}
}