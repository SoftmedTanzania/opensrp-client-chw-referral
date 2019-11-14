package org.smartregister.chw.referral.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.referral.R;
import org.smartregister.chw.referral.contract.BaseReferralCallDialogContract;
import org.smartregister.chw.referral.listener.BaseReferralCallWidgetDialogListener;

import java.util.Objects;

import static android.view.View.GONE;
import static org.smartregister.util.Utils.getName;

public class BaseReferralCallDialogFragment extends DialogFragment implements BaseReferralCallDialogContract.View {

    public static final String DIALOG_TAG = "BaseReferralCallDialogFragment_DIALOG_TAG";
    private static String referralClientName;
    private static String referralClientPhoneNumber;
    private static String referralFamilyHeadName;
    private static String referralFamilyHeadPhone;
    private View.OnClickListener listener = null;

    public static BaseReferralCallDialogFragment launchDialog(Activity activity, String clientName, String referralClientPhone, String familyHeadName, String familyHeadPhone) {
        BaseReferralCallDialogFragment dialogFragment = BaseReferralCallDialogFragment.newInstance();
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        referralClientPhoneNumber = referralClientPhone;
        referralClientName = clientName;
        referralFamilyHeadName = familyHeadName;
        referralFamilyHeadPhone = familyHeadPhone;
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, DIALOG_TAG);

        return dialogFragment;
    }

    public static BaseReferralCallDialogFragment newInstance() {
        return new BaseReferralCallDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ChwTheme_Dialog_FullWidth);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.referral_member_call_widget_dialog_fragment, container, false);
        setUpPosition();
        if (listener == null) {
            listener = new BaseReferralCallWidgetDialogListener(this);
        }

        initUI(dialogView);
        return dialogView;
    }

    private void initUI(ViewGroup rootView) {

        if (StringUtils.isNotBlank(referralClientPhoneNumber)) {
            TextView malariaClientNameTextView = rootView.findViewById(R.id.call_referral_client_name);
            malariaClientNameTextView.setText(referralClientName);

            TextView callMalariaClientPhone = rootView.findViewById(R.id.call_referral_client_phone);
            callMalariaClientPhone.setTag(referralClientPhoneNumber);
            callMalariaClientPhone.setText(getName(getCurrentContext().getString(R.string.call), referralClientPhoneNumber));
            callMalariaClientPhone.setOnClickListener(listener);
        } else {

            rootView.findViewById(R.id.layout_malaria_client).setVisibility(GONE);
        }

        if (StringUtils.isNotBlank(referralFamilyHeadPhone)) {
            TextView familyHeadName = rootView.findViewById(R.id.malaria_call_head_name);
            familyHeadName.setText(referralFamilyHeadName);

            TextView clientCallHeadPhone = rootView.findViewById(R.id.referral_call_head_phone);
            clientCallHeadPhone.setTag(referralFamilyHeadPhone);
            clientCallHeadPhone.setText(getName(getCurrentContext().getString(R.string.call), referralFamilyHeadPhone));
            clientCallHeadPhone.setOnClickListener(listener);

        } else {

            rootView.findViewById(R.id.malaria_layout_family_head).setVisibility(GONE);
        }

        rootView.findViewById(R.id.malaria_call_close).setOnClickListener(listener);

    }

    private void setUpPosition() {
        Objects.requireNonNull(getDialog().getWindow()).setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.y = 20;
        getDialog().getWindow().setAttributes(p);
    }

    @Override
    public Context getCurrentContext() {
        return getActivity();
    }

    @Override
    public BaseReferralCallDialogContract.Dialer getPendingCallRequest() {
        return null;
    }

    @Override
    public void setPendingCallRequest(BaseReferralCallDialogContract.Dialer dialer) {
//        Implement pending call request
//        BaseAncWomanCallDialogContract.Dialer mDialer = dialer;
    }
}
