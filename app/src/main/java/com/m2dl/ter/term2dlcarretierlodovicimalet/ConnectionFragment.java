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
public class ConnectionFragment extends Fragment {

    private View view;
    private static Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.connection_fragment,container,false);
        Button local = (Button)view.findViewById(R.id.connection);
        bundle = this.getArguments();
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("nombreJoueur",2);
                PlateauJeuFragment p = new PlateauJeuFragment();
                p.setArguments(bundle);
                FragmentManager fragment = getFragmentManager();
                FragmentTransaction transaction = fragment.beginTransaction();
                transaction.replace(R.id.content_frame,p);
                transaction.commit();
            }
        });
        return view;
    }
}
