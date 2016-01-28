package com.m2dl.ter.term2dlcarretierlodovicimalet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.m2dl.ter.term2dlcarretierlodovicimalet.model.IPouvoir;
import com.m2dl.ter.term2dlcarretierlodovicimalet.model.Joueur;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private static int nombreTotalAlumette;
    private static List<Joueur> joueurs;
    private static boolean tourJoueur;

    private static int nbAllumette;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = this.getArguments();
        if(!bundle.containsKey("nombreJoueur") || !bundle.containsKey("nombreAlummette")){
            FragmentManager fragment = getFragmentManager();
            FragmentTransaction transaction = fragment.beginTransaction();
            transaction.replace(R.id.content_frame, new AccueilFragment());
            transaction.commit();
        }
        joueurs = new ArrayList<>();
        for(int i = 0;i < 2;i++){
            joueurs.add(new Joueur(""+i));
        }

        List<IPouvoir> pouvoirs = new ArrayList<>(); // creer les pouvoirs
        Random rm = new Random();
        Map<Boolean, IPouvoir> pouvoirMap = new HashMap<>();

        int p = rm.nextInt(pouvoirs.size());
        pouvoirMap.put(true, pouvoirs.get(p));

        p = rm.nextInt(pouvoirs.size());
        pouvoirMap.put(false, pouvoirs.get(p));

        tourJoueur = true;
        nombreTotalAlumette = bundle.getInt("nombreAlummette");
        view = inflater.inflate(R.layout.plateau_jeu, container, false);
        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutJeu);
        imageViews = new ArrayList<>();
        final Button tour = (Button)view.findViewById(R.id.tourSuivant);
        nbAllumette = 3;
        lock = -1;
        position = 0;
        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tourJoueur = ! tourJoueur;
                nbAllumette = 3;
                tour.setEnabled(false);
            }
        });
        for (int i = 0; i < nombreTotalAlumette; i++) {
            getY = 0;
            drawingImageView = new ImageView(getActivity());
            drawingImageView.setPadding(padding, 5, padding, 5);
            bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.allumette);
            drawingImageView.setImageBitmap(getRoundedCornerBitmap(bitmap, true));
            imageViews.add(drawingImageView);
            imageViews.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (nbAllumette == 0) {
                            Toast.makeText(getActivity().getApplicationContext(), "Joueur Suivant", Toast.LENGTH_SHORT).show();
                            lock = -1;
                        } else {
                            if (position < nombreTotalAlumette) {
                                lock = position;
                            }
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
                        } else if (lock != -1) {
                            tour.setEnabled(true);
                            bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.allumettebrule);
                            imageViews.get(lock).setImageBitmap(getRoundedCornerBitmap(bitmap, true));
                            bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.allumette);
                            nbAllumette--;
                            if (position == nombreTotalAlumette) {
                                layout.removeAllViews();
                                TextView t = new TextView(getActivity());
                                String str;
                                if (tourJoueur) {
                                    str = "Joueur 2";
                                } else {
                                    str = "Joueur 1";
                                }
                                t.setText("Victoire du " + str);
                                layout.addView(t);
                                tour.setVisibility(View.INVISIBLE);
                                Button b = new Button(getActivity());
                                b.setText("Retour a l'acceuil");
                                layout.addView(b);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (position == 9) {
                                            FragmentManager fragment = getFragmentManager();
                                            FragmentTransaction transaction = fragment.beginTransaction();
                                            transaction.replace(R.id.content_frame, new AccueilFragment());
                                            transaction.commit();
                                        }
                                    }
                                });
                            }
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
