package com.autodo.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.HashMap;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class ErweimaUtils {


    //解析二维码图片,返回结果封装在Result对象中
    public static String parseQRcodeBitmap(Bitmap bitmapSource) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmapQrcode.compress(Bitmap.CompressFormat.PNG, 50, outputStream);

        //保存图片


//        //获取到待解析的图片
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        //如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)
//        //并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
//        options.inJustDecodeBounds = true;
//        //此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了
//        Bitmap bitmap;//= BitmapFactory.decodeFile(bitmapPath, options);
//        //我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素
//        /**
//         options.outHeight = 400;
//         options.outWidth = 400;
//         options.inJustDecodeBounds = false;
//         bitmap = BitmapFactory.decodeFile(bitmapPath, options);
//         */
//        //以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
//        options.inSampleSize = options.outHeight / 400;
//        if (options.inSampleSize <= 0) {
//            options.inSampleSize = 1; //防止其值小于或等于0
//        }
//        /**
//         * 辅助节约内存设置
//         *
//         * options.inPreferredConfig = Bitmap.Config.ARGB_4444;  // 默认是Bitmap.Config.ARGB_8888
//         * options.inPurgeable = true;
//         * options.inInputShareable = true;
//         */
//        options.inJustDecodeBounds = false;


//        Bitmap bitmap = BitmapFactory.decodeStream(ConvertUtil.parse(outputStream));
        //新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmapSource);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析对象
        QRCodeReader reader = new QRCodeReader();


        //解析转换类型UTF-8
        HashMap<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (result == null) {
            return null;
        }
        return result.toString();
    }

}
