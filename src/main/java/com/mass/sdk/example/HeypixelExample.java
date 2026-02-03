package com.mass.sdk.example;

import com.mass.sdk.MassHeypixel;

public class HeypixelExample {
    static void main(String[] args) {
        try{
            var address = MassHeypixel.StartProxy(23333, "bUSNBWWVT+G5OC/jKU3IWd2X8xec5e20WBv8RnUjxWzV3030/6EyH3f5YvIL+mHaFo6+CSoo46u5uIMs9d/pJS4cXaks6c1UYSPjanbqSQuIaAkYLnLRp28DhCoHDEVOO9ja0zRlWzkZcHW0HiYG8RCeIJ8VUltzDLR60df0hLzeDsR95u6uouqiga1HJ7lHcBWwvj48R5rclnzrGX8lUGvVQ5hWkWd0KsuGtpE38VYEEKg6DU8axnY2uVxSZ+xWprEorbVSurBawh+Hn8iHi/eqo568O4q0UvZa3SL4tdsBmPv0KRvAVnl90FGhsYY770plJARarZN1/ma8RTrR9w==");
            System.out.println("Address: " + address);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
