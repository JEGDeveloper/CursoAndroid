package test.yespinoza.androidproject.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.yespinoza.androidproject.Activity.MainActivity;
import test.yespinoza.androidproject.Adapter.CardViewAdapter;
import test.yespinoza.androidproject.Adapter.UserAdapter;
import test.yespinoza.androidproject.BuildConfig;
import test.yespinoza.androidproject.Model.CardView;
import test.yespinoza.androidproject.Model.User;
import test.yespinoza.androidproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResumeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResumeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static View rootView;
    private RecyclerView.LayoutManager lManager;
    private ImageButton btnNext;
    private User oUser;
    private FloatingActionButton fabmenu;
    private String documentName;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID+".provider";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResumeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResumeFragment newInstance(String param1, String param2) {
        ResumeFragment fragment = new ResumeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ResumeFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_resume, container, false);
        oUser = UserAdapter.getCurrentUser();
        TextView tvDateGenerate = rootView.findViewById(R.id.tvDateGenerate);
        tvDateGenerate.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        TextView tvName = rootView.findViewById(R.id.tvUserName);
        tvName.setText(oUser.getFullName());
        TextView tvIdNumber = rootView.findViewById(R.id.tvIdNumber);
        tvIdNumber.setText(oUser.getId());
        TextView tvContact = rootView.findViewById(R.id.tvContactMe);
        tvContact.setText(oUser.getPhone()+" | "+oUser.getEmail());
        TextView tvAddress = rootView.findViewById(R.id.tvAddress);
        tvAddress.setText(oUser.getAddress());
        fabmenu = rootView.findViewById(R.id.fabShare);
        fabmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTicket();
            }
        });
        android.support.v7.widget.RecyclerView.Adapter adapter;
        adapter = StudyFragment.adapter;
        if(adapter != null){
            RecyclerView recycler = (android.support.v7.widget.RecyclerView) rootView.findViewById(R.id.recyclerStudies);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(rootView.getContext());
            recycler.setLayoutManager(lManager);
            recycler.setAdapter(adapter);
        }else{

            LinearLayout layout = rootView.findViewById(R.id.layoutStudies);
            layout.setVisibility(View.GONE);
        }

        adapter = JobFragment.adapter;
        if(adapter != null){
            RecyclerView recycler = (android.support.v7.widget.RecyclerView) rootView.findViewById(R.id.recyclerJobExperience);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(rootView.getContext());
            recycler.setLayoutManager(lManager);
            recycler.setAdapter(adapter);
        }else{

            LinearLayout layout = rootView.findViewById(R.id.layoutJobExperience);
            layout.setVisibility(View.GONE);
        }

        adapter = ReferenceFragment.adapter;
        if(adapter != null){
            RecyclerView recycler = (android.support.v7.widget.RecyclerView) rootView.findViewById(R.id.recyclerReferences);
            recycler.setHasFixedSize(true);
            lManager = new LinearLayoutManager(rootView.getContext());
            recycler.setLayoutManager(lManager);
            recycler.setAdapter(adapter);
        }else{

            LinearLayout layout = rootView.findViewById(R.id.layoutReferences);
            layout.setVisibility(View.GONE);
        }

        return rootView;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private boolean share(File fileSharing) {
        try {
            Uri contentUri = FileProvider.getUriForFile(rootView.getContext(), AUTHORITY, fileSharing);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Compartir comprobante..."));
            return true;
        }
        catch (Exception ex){
            Toast.makeText(rootView.getContext(), "No se puede realizar el compartir", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void createTicket() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            documentName = "/RESUME_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
            fabmenu.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    rootView.draw(canvas);

                    try {
                        FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + documentName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            File root = Environment.getExternalStorageDirectory();
            File mDocument = new File(root, documentName);
            if(share(mDocument))
                new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabmenu.setVisibility(View.VISIBLE);
                }
            }, 10);
        }
    }
}
