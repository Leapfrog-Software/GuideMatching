package leapfrog_inc.guidematching.System;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import leapfrog_inc.guidematching.R;

public class PicassoUtility {

    public static void getFaceImage(Context context, String url, ImageView imageView) {
        if (url.length() == 0) {
            imageView.setImageResource(R.drawable.no_face);
            return;
        }
        Picasso.with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .noFade()
                .error(R.drawable.no_face)
                .transform(new CircleTransformation())
                .into(imageView);
    }

    public static void getImage(Context context, String url, ImageView imageView, int errorImage) {
        Picasso.with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .noFade()
                .error(errorImage)
                .into(imageView);
    }

    private static class CircleTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
