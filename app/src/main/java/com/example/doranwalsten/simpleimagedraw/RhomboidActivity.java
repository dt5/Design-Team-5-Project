package com.example.doranwalsten.simpleimagedraw;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by doranwalsten on 2/4/16.
 */
public class RhomboidActivity extends AppCompatActivity {

    //Objects in the view that are important to reference
    ImageView mImageView;
    SeekBar[] parameters = new SeekBar[3];
    String[] names = new String[3];
    TextView[] titles = new TextView[3];
    ArrayList<double[]> bounds = new ArrayList<double[]>();
    RhomboidFlap new_flap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhomboid);//
        mImageView = (ImageView) findViewById(R.id.rhom_Image);
        parameters[0] = (SeekBar) findViewById(R.id.seekBar_1); //RATIO
        parameters[1] = (SeekBar) findViewById(R.id.seekBar_2); //ALPHA
        parameters[2] = (SeekBar) findViewById(R.id.seekBar_3); //BETA

        names[0] = "Ratio";
        names[1] = "Alpha";
        names[2] = "Beta";

        titles[0] = (TextView) findViewById(R.id.textView1);
        titles[0].setText(String.format("%s: 1.73",names[0]));
        titles[1] = (TextView) findViewById(R.id.textView2);
        titles[1].setText(String.format("%s: 0.0",names[1]));
        titles[2] = (TextView) findViewById(R.id.textView3);
        titles[2].setText(String.format("%s: 60.0",names[2]));

        //Here, edit the bounds on each of the variables
        double[] ratio_bound = {1.0,2.0};
        bounds.add(ratio_bound);
        double[] alpha_bound = {0.0,90.0};
        bounds.add(alpha_bound);
        double[] beta_bound = {0.0,90.0};
        bounds.add(beta_bound);

        Bitmap back = (Bitmap) this.getIntent().getExtras().get("Background");
        mImageView.setImageBitmap(back);

        mImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        } else {
                            return false;
                        }

                    case DragEvent.ACTION_DROP:
                        View draggedImage = (View) event.getLocalState();
                        draggedImage.setX(event.getX() - draggedImage.getWidth() / 2 - new_flap.getDisplacement()[0]);
                        draggedImage.setY(event.getY() - draggedImage.getHeight() / 2 - new_flap.getDisplacement()[1]);
                        draggedImage.setVisibility(View.VISIBLE);


                        //Try dropping a new image at the point

                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                }
                return false;
            }
        });

        new_flap = (RhomboidFlap) findViewById(R.id.flap);
        /*
        new_flap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new_flap.setTouchpoint(event.getRawX(), event.getRawY());
                    ClipData clipData = ClipData.newPlainText("", "");

                    //Use Bitmap to create Shadow
                    v.setDrawingCacheEnabled(true);
                    Bitmap viewCapture = v.getDrawingCache();
                    FlapDragShadowBuilder shadowBuilder = new FlapDragShadowBuilder(viewCapture);
                    shadowBuilder.setDisplacement(new_flap.getDisplacement()[0], new_flap.getDisplacement()[1]);
                    v.startDrag(clipData, shadowBuilder, v, 0);
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.GONE);
                }

                return true;
            }
        });
        */

        for (int i = 0; i < 3; i++) {
            final double specific_range = bounds.get(i)[1] - bounds.get(i)[0];
            final double specific_bottom = bounds.get(i)[0];
            final TextView specific_text = titles[i];
            final String name = names[i];
            final int id = i;
            parameters[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //edited = true;
                    int progressChanged = seekBar.getProgress();
                    double display_value = progressChanged / 100. * specific_range + specific_bottom;
                    switch(id) {
                        case 0:
                            new_flap.setHeight(display_value);
                            break;
                        case 1:
                            new_flap.setAlpha(display_value*Math.PI/180.);
                            break;
                        case 2:
                            new_flap.setBeta(display_value*Math.PI/180.);
                            break;
                    }
                    new_flap.invalidate();
                    specific_text.setText(String.format("%s : %.2f",name, display_value));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RhomboidActivity.this, EditImage.class);
        intent.putExtra("Background",((BitmapDrawable) mImageView.getDrawable()).getBitmap());
        //Need to somehow save the drawn flap to the UI, but not as a RhomboidFlap
        //Design returnImage to get the cropping right, identify the location on the screen where
        //flap is

        startActivity(intent);
    }
}
