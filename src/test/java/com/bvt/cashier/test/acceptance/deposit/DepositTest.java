package com.bvt.cashier.test.acceptance.deposit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bvt.cashier.test.acceptance.common.TestFixture;



public class DepositTest extends TestFixture {

    @Test(enabled = true)
    public void shouldOpenPage() {
     driver.get("http://www.google.com");
     Assert.assertEquals(driver.getTitle(), "Google");
    }
}
