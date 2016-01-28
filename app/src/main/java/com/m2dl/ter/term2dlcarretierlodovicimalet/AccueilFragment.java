package com.m2dl.ter.term2dlcarretierlodovicimalet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by CCA3441 on 26/01/2016.
 */
public class AccueilFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.accueil_fragment,container,false);
        Button button = (Button)view.findViewById(R.id.demarer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment = getFragmentManager();
                FragmentTransaction transaction = fragment.beginTransaction();
                transaction.replace(R.id.content_frame,new ParametreFragment());
                transaction.commit();
            }
        });
        Button recherche = (Button) view.findViewById(R.id.rechercher);
        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
