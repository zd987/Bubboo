package com.zhaodong8701.bubboo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.zhaodong8701.bubboo.R.drawable;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LockScreenAppActivity extends Activity {

	/** Called when the activity is first created. */
	boolean inDragMode;
	int selectedImageViewX;
	int selectedImageViewY;
	int windowwidth;
	int windowheight;
	ImageView droid;
	ImageView circle;
	ImageView left;
	ImageView right;
	ImageView above;
	ImageView below;
	RelativeLayout nestFrame;
	ImageView animal;
	int selected = 0;// new Intent(Intent.ACTION_DIAL, null);
	int animalIndex = -1;
	int home_x, home_y;
	int[] droidpos;

	private LayoutParams layoutParams;

	@SuppressLint("NewApi")
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onAttachedToWindow();
	}

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		List<Integer> animalList = new ArrayList<Integer>();
		animalList.add(drawable.bear);
		animalList.add(drawable.ass);
		animalList.add(drawable.cat);
		animalList.add(drawable.dog);
		animalList.add(drawable.elephant);
		animalList.add(drawable.fox);
		animalList.add(drawable.giraffe);
		animalList.add(drawable.goat);
		animalList.add(drawable.gorilla);
		animalList.add(drawable.hippopotamus);
		animalList.add(drawable.horse);
		animalList.add(drawable.kangaroo);
		animalList.add(drawable.koala);
		animalList.add(drawable.lion);
		animalList.add(drawable.monkey);
		animalList.add(drawable.panda);
		animalList.add(drawable.snake);
		animalList.add(drawable.squirrel);
		animalList.add(drawable.tiger);
		animalList.add(drawable.tortoise);
		animalList.add(drawable.zebra);
		animalIndex = animalList.get(new Random().nextInt(animalList.size()));

		droid = (ImageView) findViewById(R.id.droid);
		circle = (ImageView) findViewById(R.id.circle);
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		above = (ImageView) findViewById(R.id.above);
		below = (ImageView) findViewById(R.id.below);
		nestFrame = (RelativeLayout) findViewById(R.id.nestFrame);
		animal = (ImageView) findViewById(R.id.animal);
		animal.setImageResource(animalIndex);

		System.out.println("measured width" + droid.getMeasuredWidth());
		System.out.println(" width" + droid.getWidth());

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		windowwidth = size.x;
		windowheight = size.y;

		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			finish();
		}

		try {

			startService(new Intent(this, MyService.class));
			StateListener phoneStateListener = new StateListener();
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);

			droid.setOnTouchListener(new View.OnTouchListener() {

				@SuppressLint("NewApi")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int[] img_coordinates = new int[2];
					int x_cord = (int) event.getRawX();
					int y_cord = (int) event.getRawY();

					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:
						// nestFrame.setVisibility(View.VISIBLE);
						break;
					case MotionEvent.ACTION_MOVE:
						// Limit the button to move in the range of the circle.
						circle.getLocationOnScreen(img_coordinates);
						int x_center = img_coordinates[0] + circle.getWidth()
								/ 2;
						int y_center = img_coordinates[1] + circle.getHeight()
								/ 2;
						int r = circle.getWidth() / 2;
						int rx = x_cord - x_center;
						int ry = y_cord - y_center;
						int rr = rx * rx + ry * ry;
						int nx = x_cord;
						int ny = y_cord;
						if (rx * rx + ry * ry >= r * r) {
							nx = (int) (x_center + r * (rx / Math.sqrt(rr)));
							ny = (int) (y_center + r * (ry / Math.sqrt(rr)));
						}
						System.out.println("zhaodong " + nx + " " + ny);
						droid.setX(nx - droid.getWidth() / 2);
						droid.setY(ny - droid.getHeight() / 2);
						// If the button move near the menu, the menu icon will
						// iverse its color to show the selection.
						boolean updated = false;
						left.getLocationOnScreen(img_coordinates);
						rr = droid.getWidth() / 2;
						int r2 = left.getWidth() / 2;
						int r3 = r2 + rr;
						rx = nx - img_coordinates[0] - r2;
						ry = ny - img_coordinates[1] - r2;
						if (rx * rx + ry * ry <= r3 * r3) {
							left.setImageResource(R.drawable.ic_menu_call_selected);
							selected = R.drawable.ic_menu_call_selected;// new
																		// Intent(Intent.ACTION_DIAL,
																		// null);
							updated = true;
						} else {
							left.setImageResource(R.drawable.ic_menu_call);
							if (!updated) {
								selected = 0;
							}
						}

						right.getLocationOnScreen(img_coordinates);
						rr = droid.getWidth() / 2;
						r2 = right.getWidth() / 2;
						r3 = r2 + rr;
						rx = nx - img_coordinates[0] - r2;
						ry = ny - img_coordinates[1] - r2;
						if (rx * rx + ry * ry <= r3 * r3) {
							right.setImageResource(R.drawable.bubbo_menu_selected);
							selected = R.drawable.bubbo_menu_selected;// setImage(selected);
							updated = true;
						} else {
							right.setImageResource(R.drawable.bubbo_menu);
							if (!updated) {
								selected = 0;
							}
						}

						above.getLocationOnScreen(img_coordinates);
						rr = droid.getWidth() / 2;
						r2 = above.getWidth() / 2;
						r3 = r2 + rr;
						rx = nx - img_coordinates[0] - r2;
						ry = ny - img_coordinates[1] - r2;
						if (rx * rx + ry * ry <= r3 * r3) {
							above.setImageResource(R.drawable.ic_menu_start_conversation_selected);
							selected = R.drawable.ic_menu_start_conversation_selected;
							updated = true;
						} else {
							above.setImageResource(R.drawable.ic_menu_start_conversation);
							if (!updated) {
								selected = 0;
							}
						}

						below.getLocationOnScreen(img_coordinates);
						rr = droid.getWidth() / 2;
						r2 = below.getWidth() / 2;
						r3 = r2 + rr;
						rx = nx - img_coordinates[0] - r2;
						ry = ny - img_coordinates[1] - r2;
						if (rx * rx + ry * ry <= r3 * r3) {
							below.setImageResource(R.drawable.ic_menu_home_selected);
							selected = R.drawable.ic_menu_home_selected;
							updated = true;
						} else {
							below.setImageResource(R.drawable.ic_menu_home);
							if (!updated) {
								selected = 0;
							}
						}
						break;
					case MotionEvent.ACTION_UP:
						if (selected != 0) {
							Intent intent = null;
							switch (selected) {
							case R.drawable.ic_menu_call_selected:
								intent = new Intent(Intent.ACTION_DIAL, null);
								break;
							case R.drawable.bubbo_menu_selected:
								zoomImageFromThumb(animal, animalIndex);
								nestFrame.setVisibility(View.INVISIBLE);
								return true;
							case R.drawable.ic_menu_start_conversation_selected:
								intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri
										.parse("content://mms-sms/conversations/"));
								break;
							case R.drawable.ic_menu_home_selected:
								break;
							}
							v.setVisibility(View.GONE);
							if (intent != null) {
								startActivity(intent);
							}
							finish();
						} else {
							circle.getLocationOnScreen(img_coordinates);
							droid.setX(img_coordinates[0] + circle.getWidth()
									/ 2 - droid.getWidth() / 2);
							droid.setY(img_coordinates[1] + circle.getHeight()
									/ 2 - droid.getHeight() / 2);
						}
						break;
					}
					return true;
				}
			});

		} catch (Exception e) {
		}

	}

	class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("call Activity off hook");
				finish();

				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	};

	public void onSlideTouch(View view, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int x_cord = (int) event.getRawX();
			int y_cord = (int) event.getRawY();

			if (x_cord > windowwidth) {
				x_cord = windowwidth;
			}
			if (y_cord > windowheight) {
				y_cord = windowheight;
			}

			layoutParams.leftMargin = x_cord - 25;
			layoutParams.topMargin = y_cord - 75;

			view.setLayoutParams(layoutParams);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// Don't allow back to dismiss.
		return;
	}

	// only used in lockdown mode
	@Override
	protected void onPause() {
		super.onPause();

		// Don't hang around.
		// finish();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// Don't hang around.
		// finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			// this is where I can do my stuff
			return true; // because I handled the event
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		}

		return false;

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

			System.out.println("alokkkkkkkkkkkkkkkkk");
			return true;
		}
		return false;
	}

	public void onDestroy() {
		super.onDestroy();
	}
	
	private int mShortAnimationDuration = 1000;

	@SuppressLint("NewApi")
	private void zoomImageFromThumb(final View thumbView, int imageResId) {
		final ImageView expandedImageView = (ImageView) findViewById(R.id.animal);
		expandedImageView.setImageResource(imageResId);

		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();
		
		thumbView.getGlobalVisibleRect(startBounds);
		int animalWidth = animal.getWidth();
		int animalHeight = animal.getHeight();
		int smallIndex = 2;
		finalBounds.left = windowwidth / 2 - animalWidth / 2;
		finalBounds.top = windowheight / 2 - animalHeight / 2;

		float startScale = (float) 1 / smallIndex;
		AnimatorSet set = new AnimatorSet();
		Animator animator = ObjectAnimator.ofFloat(expandedImageView, View.X,
				startBounds.left, finalBounds.left).setDuration(
				mShortAnimationDuration);
		set.play(animator)
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						startBounds.top, finalBounds.top).setDuration(
						mShortAnimationDuration))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						1f, startScale).setDuration(mShortAnimationDuration))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
						1f, startScale).setDuration(mShortAnimationDuration));
		Animator a2 = ObjectAnimator
				.ofFloat(expandedImageView, View.SCALE_X, startScale,
						2f)
				.setDuration(
						(long) (mShortAnimationDuration
								* (2f - startScale) / (1f - startScale)));
		Animator a3 = ObjectAnimator
				.ofFloat(expandedImageView, View.SCALE_Y, startScale,
						2f)
				.setDuration(
						(long) (mShortAnimationDuration
								* (2f - startScale) / (1f - startScale)));
		set.play(a2
				)
				.with(a3)
				.after(animator);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListener(){
			@Override
			public void onAnimationCancel(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				finish();
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationStart(Animator arg0) {
			}			
		});
		set.start();
	}

}