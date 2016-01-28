package com.m2dl.ter.term2dlcarretierlodovicimalet;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCA3441 on 28/01/2016.
 */
public class PlateauJeuFragment extends Fragment {

    private View view;
    private ImageView drawingImageView;
    private Bitmap bitmap;
    private static int precedentOption;
    private static int getY;
    private static List<ImageView> imageViews;
    private final int padding = 15;
    private static int lock;
    private static int position;
    int[] viewCoords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.plateau_jeu, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutJeu);
        imageViews = new ArrayList<>();
        Button tour = (Button)view.findViewById(R.id.tourSuivant);
        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        for (int i = 0; i < 9; i++) {
            viewCoords = new int[2];
            getY = 0;
            drawingImageView = new ImageView(getActivity());
            drawingImageView.setPadding(padding, 5, padding, 5);
            bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.allumette);
            drawingImageView.setImageBitmap(getRoundedCornerBitmap(bitmap, true));
            imageViews.add(drawingImageView);
            imageViews.get(i).getLocationOnScreen(viewCoords);
            imageViews.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (position < 9) {
                            lock = position;
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (lock != -1) {
                            getY = (int) event.getY();
                            if (precedentOption == 0) {
                                precedentOption = getY;
                            }
                            imageViews.get(lock).setImageBitmap(getRoundedCornerBitmap(bitmap, false));
                            if (getY > 400 && lock == position) {
                                position++;
                            }
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        precedentOption = 0;
                        getY = 0;
                        if (lock == position) {
                            imageViews.get(lock).setImageBitmap(getRoundedCornerBitmap(bitmap, true));
                        } else {
                            bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.allumettebrule);
                            imageViews.get(lock).setImageBitmap(getRoundedCornerBitmap(bitmap, true));
                        }
                    }
                    return true;
                }
            });
            layout.addView(imageViews.get(i));
        }

        return view;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean posX) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        if(posX) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        } else {
            canvas.drawBitmap(bitmap, 0, getY - precedentOption, paint);
        }

        return output;
    }


}
