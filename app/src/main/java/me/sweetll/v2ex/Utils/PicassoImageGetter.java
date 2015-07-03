package me.sweetll.v2ex.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by sweet on 15-7-3.
 */

public class PicassoImageGetter implements Html.ImageGetter {

    final Resources resources;

    final Picasso pablo;

    final TextView textView;

    public PicassoImageGetter(final TextView textView, final Resources resources, final Picasso pablo) {
        this.textView = textView;
        this.resources = resources;
        this.pablo = pablo;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final BitmapDrawablePlaceHolder result = new BitmapDrawablePlaceHolder();

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(final Void... meh) {
                try {
                    return pablo.load(source).get();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                try {
                    final BitmapDrawable drawable = new BitmapDrawable(resources, bitmap);

                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    result.setDrawable(drawable);
                    result.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    textView.setText(textView.getText()); // invalidate() doesn't work correctly...
                } catch (Exception e) {
                /* nom nom nom*/
                }
            }

        }.execute((Void) null);

        return result;
    }

    static class BitmapDrawablePlaceHolder extends BitmapDrawable {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

    }
}
