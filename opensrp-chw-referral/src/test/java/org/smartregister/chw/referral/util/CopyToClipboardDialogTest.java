package org.smartregister.chw.referral.util;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by cozej4 on 2019-10-26.
 *
 * @cozej4 https://github.com/cozej4
 */
public class CopyToClipboardDialogTest {

    @Mock
    private CopyToClipboardDialog copyToClipboardDialog;

    @Mock
    private Context context;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertNotNull() {
        copyToClipboardDialog = new CopyToClipboardDialog(context, 1);
        Assert.assertNotNull(copyToClipboardDialog);
    }

    @Test
    public void setContent() {
        copyToClipboardDialog = new CopyToClipboardDialog(context);
        copyToClipboardDialog.setContent("test content");
        Assert.assertEquals("test content", copyToClipboardDialog.content);
    }
}