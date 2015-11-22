package com.zhuika.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public final class Delimiters {

    public static ByteBuf[] nulDelimiter() {
        return new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[] { 0 }) };
    }

   
    public static ByteBuf[] lineDelimiter() {
        return new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[] { '\r', '\n' }),              
        };
    }

    private Delimiters() {
       
    }
}

