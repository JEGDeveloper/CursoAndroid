package test.yespinoza.androidproject.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import test.yespinoza.androidproject.Activity.ChangePassword;
import test.yespinoza.androidproject.Activity.Index;
import test.yespinoza.androidproject.Adapter.UserAdapter;
import test.yespinoza.androidproject.Model.DAL.UserDAL;
import test.yespinoza.androidproject.Model.User;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.R;

public class SettingsFragment extends Fragment {
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
    private View rootView;
    private int blackColor;
    private int disabledColor;
    private Drawable textViewEnabled;
    private Drawable textViewDisabled;
    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView.findViewById(R.id.btnSend)
                .setOnClickListener(new
                                            View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    onSendButtonListener(v);
                                                }
                                            });
        rootView.findViewById(R.id.btnChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangePasswordButtonListener(v);
            }
        });
        blackColor = rootView.getResources().getColor(R.color.colorBlack);
        disabledColor = rootView.getResources().getColor(R.color.colorDisabled);
        textViewEnabled = rootView.getResources().getDrawable(R.drawable.text_view_shape);
        textViewDisabled = rootView.getResources().getDrawable(R.drawable.disabled_text_view_shape);
        loadUser();
        EnableForm(false);
        progress  = new ProgressDialog(rootView.getContext());
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onChangePasswordButtonListener(View pView){
        startActivity(new Intent(rootView.getContext(), ChangePassword.class));
    }
    public void onSendButtonListener(View pView){
        Button btnSend = (Button) pView.findViewById(R.id.btnSend);
        boolean isSaving = btnSend.getText().toString().toUpperCase().equals(getString(R.string.btnSettingsSave).toUpperCase());
        if(isSaving){
            //Save Method
            User oUser = UserAdapter.getCurrentUser();
            EditText editText = rootView.findViewById(R.id.etName);
            oUser.setName(Helper.TextFormat(editText.getText().toString()));
            editText = rootView.findViewById(R.id.etLastName);
            oUser.setLastName(Helper.TextFormat(editText.getText().toString()));
            editText = rootView.findViewById(R.id.etPhone);
            oUser.setPhone(Helper.TextFormat(editText.getText().toString()));
            editText = rootView.findViewById(R.id.etDateOfBirth);
            oUser.setDateOfBirth(editText.getText().toString().trim());
            editText = rootView.findViewById(R.id.etEmail);
            oUser.setEmail(editText.getText().toString().trim());
            editText = rootView.findViewById(R.id.etAddress);
            oUser.setAddress(Helper.TextFormat(editText.getText().toString()));

            if (oUser.getName().equals("") ||
                    oUser.getLastName().equals("") ||
                    oUser.getEmail().equals("") ||
                    oUser.getPhone().equals("") ||
                    oUser.getDateOfBirth().equals("")||
                    oUser.getAddress().equals("")) {
                Toast.makeText(rootView.getContext(), getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!Helper.emailValidation(oUser.getEmail())){
                Toast.makeText(rootView.getContext(), getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!Helper.dateValidation(oUser.getDateOfBirth())){
                Toast.makeText(rootView.getContext(), getString(R.string.invalidDate), Toast.LENGTH_SHORT).show();
                return;
            }
            boolean userModified = UserDAL.modificarUser(oUser);
            Toast.makeText(rootView.getContext(),
                    getString(userModified ? R.string.SettingsSaveSuccess : R.string.SettingsSaveFailed),
                    Toast.LENGTH_SHORT);
            if(userModified) {
                UserAdapter.setCurrentUser(oUser);
                Index.getInstance().userData();
            }
        }
        EnableForm(!isSaving);
        btnSend.setText(isSaving ? getString(R.string.btnSettingsEdit) : getString(R.string.btnSettingsSave));
    }
    private void EnableForm(boolean enable){
        EditText editText = rootView.findViewById(R.id.etName);
        editText.setEnabled(enable);
        int color = enable ? blackColor : disabledColor;
        Drawable textViewShape = enable ? textViewEnabled : textViewDisabled;
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etLastName);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etPhone);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etDateOfBirth);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etEmail);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etAddress);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

    }

    private void loadUser(){
        User user = UserAdapter.getCurrentUser();
        if(user != null) {
            ((EditText)rootView.findViewById(R.id.etIdNumber)).setText(user.getId());
            ((EditText)rootView.findViewById(R.id.etName)).setText(user.getName());
            ((EditText)rootView.findViewById(R.id.etLastName)).setText(user.getLastName());
            ((EditText)rootView.findViewById(R.id.etDateOfBirth)).setText(user.getDateOfBirth());
            ((EditText)rootView.findViewById(R.id.etPhone)).setText(user.getPhone());
            ((EditText)rootView.findViewById(R.id.etEmail)).setText(user.getEmail());
            ((EditText)rootView.findViewById(R.id.etAddress)).setText(user.getAddress());
        }
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

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
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
}
