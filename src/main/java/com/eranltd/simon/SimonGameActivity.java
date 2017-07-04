package com.eranltd.simon;

import java.util.ArrayList;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SimonGameActivity extends Activity implements ColorFragment.PushListener {

    /** MediaPlayer class for User Audio Interaction. */
	MediaPlayer player;
	/** SequenceIndex a running counter for random color selection. */
	private int sequenceIndex = 0;


	/** Holds the "Different" Views. */
	private ArrayList<ColorFragment> sequence;
	private ColorFragment[] colors;

	/** Game Statues grey-square object in the middle of the screen . */
	private TextView indicator;

	private boolean userIsRight;
	private int userIsRightIndex = 0; //running counter...
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        
        indicator = (TextView)findViewById(R.id.indicator);
        
        colors = new ColorFragment[4];
        colors[0] = (ColorFragment)findViewById(R.id.topleft);
        colors[1] = (ColorFragment)findViewById(R.id.topright);
        colors[2] = (ColorFragment)findViewById(R.id.bottomleft);
        colors[3] = (ColorFragment)findViewById(R.id.bottomright);


        colors[0].setPushListener(this);
        colors[1].setPushListener(this);
        colors[2].setPushListener(this);
        colors[3].setPushListener(this);



        initSequence();
        doSequence();
    }

	/** Close MediaPlayer on complete to allow different audio stream one by one . */
	private MediaPlayer.OnCompletionListener onComplete = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			mp.release();
		}
	};
    
    private void initSequence() {
        sequence = new ArrayList<ColorFragment>();
        incSequence();
    }
    
	private void doSequence() {
        indicator.setText("" + (sequenceIndex + 1));


		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {

				ColorFragment color = sequence.get(sequenceIndex);

				int id = color.getId();
				switch (id) {
					case R.id.bottomleft:
						player = MediaPlayer.create(SimonGameActivity.this, R.raw.doo);
						break;
					case R.id.topleft:
						player = MediaPlayer.create(SimonGameActivity.this, R.raw.fa);
						break;
					case R.id.topright:
						player = MediaPlayer.create(SimonGameActivity.this, R.raw.mi);
						break;
					case R.id.bottomright:
						player = MediaPlayer.create(SimonGameActivity.this, R.raw.re);
						break;
				}
				player.setOnCompletionListener(onComplete);
				player.start();

				sequence.get(sequenceIndex).on();
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {

						sequence.get(sequenceIndex).off();
						Log.i("app","sequence " + sequenceIndex);
						sequenceIndex++;
						if(sequenceIndex < sequence.size()) {
							doSequence();
						} else {
							Log.i("app","chanlenge ?");
					        indicator.setText("?");
							sequenceIndex = 0;
							userIsRight = true;
						}
					}
				}, 300);
			}
		}, 1000);
	}
	
	private void incSequence() {
		sequence.add(colors[(int) (Math.random() * colors.length)]);
		doSequence();
	}

	@Override
	public void onPush(View v) {

		int id = v.getId();
		switch (id) {
			case R.id.bottomleft:
				player = MediaPlayer.create(SimonGameActivity.this, R.raw.doo);
				break;
			case R.id.topleft:
				player = MediaPlayer.create(SimonGameActivity.this, R.raw.fa);
				break;
			case R.id.topright:
				player = MediaPlayer.create(SimonGameActivity.this, R.raw.mi);
				break;
			case R.id.bottomright:
				player = MediaPlayer.create(SimonGameActivity.this, R.raw.re);
				break;
		}

		if (userIsRight) {
			if (v == sequence.get(userIsRightIndex)) {
		        indicator.setText("" + (userIsRightIndex + 1));
				Log.i("app", "good " + userIsRightIndex);
				userIsRightIndex++;
				
				if (userIsRightIndex >= sequence.size()) {
					Log.i("app", "The User is right");
					userIsRight = false;
					userIsRightIndex = 0;
			        indicator.setText("\u2714");
			        (new Handler()).postDelayed(new Runnable() {
						@Override
						public void run() {
							incSequence();
						}
					}, 1000);
				}
			} else {
				Log.i("app", "The User is Wrong");

				player = MediaPlayer.create(SimonGameActivity.this, R.raw.fail);

				userIsRight = false;
				userIsRightIndex = 0;
		        indicator.setText("\u2718");

		        (new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						initSequence();
					}
				}, 3000);
			}
		}
		player.setOnCompletionListener(onComplete);
		player.start();
	}
}