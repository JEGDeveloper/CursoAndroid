package test.yespinoza.androidproject.Model.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/*
import tns.coope_ande.R;
import tns.coope_ande.databinding.ViewMessageBinding;
*/

public class FragmentBase extends Fragment {

    //private MaterialDialog mDialog;
    public boolean animationProcess = false;

    public boolean validateNetworkConnetion() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkConnetion = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!networkConnetion) {
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void initToolbar(ActionBar actionBar, String title) {
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle(title);
            actionBar.setElevation(0);
        }
    }

    public void toolbarSetup(ActionBar actionBar, int titleId, int colorId) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getString(titleId));
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorId)));
            actionBar.setElevation(0);
            actionBar.show();
        }
    }

    public void toolbarSetup(ActionBar actionBar, int colorId) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorId)));
            actionBar.setElevation(0);
            actionBar.show();
        }
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        return Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public void showToastMessage(String message) {
        if (message != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "TimedOut..", Toast.LENGTH_LONG).show();
        }
    }

    public String decimalFormat(String amount) {
        if (!amount.isEmpty()) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat dec = new DecimalFormat("#,##0.00", symbols);
            return dec.format(Double.valueOf(amount));
        }
        return "";
    }

    public float getInitialHeight(final View view) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return view.getMeasuredHeight();
    }

    public void expand(final View view, float minHeight, float maxHeight) {
        activeAnimationProcess();
        final ValueAnimator va = ValueAnimator.ofFloat(minHeight, maxHeight);
        va.setDuration(300);
        va.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            view.getLayoutParams().height = (int) value.floatValue();
            view.requestLayout();
        });
        va.start();
    }

    public void collapse(final View view, float minHeight, float maxHeight) {
        activeAnimationProcess();
        final ValueAnimator va = ValueAnimator.ofFloat(maxHeight, minHeight);
        va.setDuration(300);
        va.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            view.getLayoutParams().height = (int) value.floatValue();
            view.requestLayout();
        });
        va.start();
    }

    public void activeAnimationProcess() {
        animationProcess = true;
        new android.os.Handler().postDelayed(() -> animationProcess = false, 300);
    }

    public void fadeIn(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.0f);
            view.animate().alpha(1.0f);
        }
    }

    public void fadeOut(final View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.animate()
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                         @Override
                         public void onAnimationEnd(Animator animation) {
                             super.onAnimationEnd(animation);
                             view.setVisibility(View.GONE);
                             view.animate().setListener(null);
                         }
                     }
                );
        }
    }
}
