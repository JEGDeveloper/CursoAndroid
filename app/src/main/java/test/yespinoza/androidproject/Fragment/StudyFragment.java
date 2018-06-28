package test.yespinoza.androidproject.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import test.yespinoza.androidproject.Activity.MainActivity;
import test.yespinoza.androidproject.Adapter.CardViewAdapter;
import test.yespinoza.androidproject.Model.CardView;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.Model.Utils.SwipeListener;
import test.yespinoza.androidproject.Model.Utils.SwipeableRecyclerViewTouchListener;
import test.yespinoza.androidproject.R;

public class StudyFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static View rootView;
    private ArrayAdapter<String> studyTypesAdapter;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    private ImageButton btnNext;
    public static RecyclerView.Adapter adapter;
    private static List<CardView> items;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StudyFragment() {
        // Required empty public constructor
    }

    public static StudyFragment newInstance(String param1, String param2) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            items = new ArrayList<>();
            adapter = null;
            rootView = inflater.inflate(R.layout.fragment_study, container, false);
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinStudyTypes);
        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.StudiesType));
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(LTRadapter);
        btnNext = (ImageButton) rootView.findViewById(R.id.imgBtnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(new JobFragment());
            }
        });
        rootView.findViewById(R.id.imgBtnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClickListener(v);

            }
        });

        recycler = rootView.findViewById(R.id.reciclador);

        return rootView;
    }

    private void next(Fragment pFragment){
        FragmentTransaction oFragmentTransaction = getFragmentManager().beginTransaction();
        oFragmentTransaction.replace(R.id.frmLayoutIndex, pFragment, "fragment_screen");
        oFragmentTransaction.commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onAddClickListener(View pView){
        EditText etStudyName = rootView.findViewById(R.id.etStudyName);
        EditText etDateFrom = rootView.findViewById(R.id.etDateFrom);
        EditText etDateTo = rootView.findViewById(R.id.etDateTo);
        String studyName = Helper.TextFormat(etStudyName.getText().toString());
        String dates = etDateFrom.getText().toString()+
                (etDateTo.getText().toString().equals("") ?
                        "":
                        " - "+
                                etDateTo.getText().toString());
        if(studyName.equals("")){
            Toast.makeText(rootView.getContext(),"",Toast.LENGTH_SHORT).show();
            return;
        }
        items.add(new CardView( studyName,
                        ((Spinner)rootView.findViewById(R.id.spinStudyTypes)).getSelectedItem().toString()+
                                (dates.equals("")? "": " | "+dates)));
        etStudyName.setText("");
        etDateFrom.setText("");
        etDateTo.setText("");

        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(rootView.getContext());
        recycler.setLayoutManager(lManager);
        adapter = new CardViewAdapter(items);
        recycler.setAdapter(adapter);
        btnNext.setEnabled(true);


        /**Swipe to Delete**/
        recycler.addOnItemTouchListener( new SwipeableRecyclerViewTouchListener(recycler, new SwipeListener(adapter, items)));
    }

}
