package com.m2dl.ter.term2dlcarretierlodovicimalet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by CCA3441 on 26/01/2016.
 */
public class ParametreFragment extends Fragment {

    private View view;
    private Button lancer;
    private CheckBox piege;
    private CheckBox pouvoir;
    private SeekBar seek;
    private TextView textSeek;



    private final int min=10;
    private final int max=20;
    private final int step=2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.parametre_fragment,container,false);
        lancer = (Button)view.findViewById(R.id.lancer);
        piege = (CheckBox)view.findViewById(R.id.piege);
        pouvoir = (CheckBox)view.findViewById(R.id.pouvoir);
        seek = (SeekBar)view.findViewById(R.id.seek);
        textSeek = (TextView)view.findViewById(R.id.textSeek);

        lancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getFragmentManager();
                FragmentTransaction transaction = fragment.beginTransaction();
                transaction.replace(R.id.content_frame,new PlateauJeuFragment());
                transaction.commit();
            }
        });
        seek.setMax((max - min) / step);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = min + (progress * step);
                textSeek.setText(""+((int)value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return view;
    }
}