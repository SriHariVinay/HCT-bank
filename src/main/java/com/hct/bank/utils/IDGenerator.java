package com.hct.bank.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class IDGenerator {
    public static Long getId(int length){
       String digits = RandomStringUtils.randomNumeric(length);
       long randomId = Long.parseLong(digits);
       return randomId;
    }

}
