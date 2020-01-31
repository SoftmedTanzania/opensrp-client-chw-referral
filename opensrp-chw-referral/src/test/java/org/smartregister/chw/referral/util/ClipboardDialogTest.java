package org.smartregister.chw.referral.util;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.referral.custom_views.ClipboardDialog;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class ClipboardDialogTest {

    @Mock
    private ClipboardDialog clipboardDialog;

    @Mock
    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertNotNull() {
        clipboardDialog = new ClipboardDialog(context, 1);
        Assert.assertNotNull(clipboardDialog);
    }

    @Test
    public void setContent() {
        clipboardDialog = new ClipboardDialog(context);
        clipboardDialog.content = "test content";
        Assert.assertEquals("test content", clipboardDialog.content);
    }
}