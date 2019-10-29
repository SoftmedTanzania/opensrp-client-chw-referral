package org.smartregister.chw.referral.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.smartregister.chw.referral.contract.BaseReferralRegisterContract;
import org.smartregister.chw.referral.presenter.BaseReferralRegisterPresenter;


@PrepareForTest(BaseReferralRegisterPresenter.class)
public class BaseReferralRegisterPresenterTest {
    @Mock
    protected BaseReferralRegisterPresenter baseReferralRegisterPresenter;

    @Mock
    protected BaseReferralRegisterContract.View view;

    @Mock
    protected BaseReferralRegisterContract.Model model;

    @Mock
    protected BaseReferralRegisterContract.Interactor interactor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getView(){
        BaseReferralRegisterPresenter presenterWithNullView = new BaseReferralRegisterPresenter(null,model,interactor);
        Assert.assertNull(presenterWithNullView.getView());

        BaseReferralRegisterPresenter presenter = new BaseReferralRegisterPresenter(view,model,interactor);
        Assert.assertNotNull(presenter.getView());
    }

}
