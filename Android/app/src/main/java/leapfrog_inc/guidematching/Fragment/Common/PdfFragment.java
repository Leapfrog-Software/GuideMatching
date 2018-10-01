package leapfrog_inc.guidematching.Fragment.Common;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import leapfrog_inc.guidematching.Fragment.BaseFragment;
import leapfrog_inc.guidematching.R;

public class PdfFragment extends BaseFragment {

    private String mPdfFileName;
    private String mTitle;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPage;

    public void set(String pdfFilename, String title) {
        mPdfFileName = pdfFilename;
        mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_pdf, null);

        initAction(view);
        initContent(view);

        return view;
    }

    private void initAction(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(AnimationType.horizontal);
            }
        });
    }

    private void initContent(View view) {
        
        ((TextView)view.findViewById(R.id.headerTitleTextView)).setText(mTitle);

        try {
            File file = new File(getActivity().getCacheDir(), mPdfFileName);
            if (!file.exists()) {
                InputStream asset = getActivity().getAssets().open(mPdfFileName);
                FileOutputStream output = new FileOutputStream(file);
                final byte[] buffer = new byte[1024];
                int size;
                while ((size = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, size);
                }
                asset.close();
                output.close();
            }
            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
            mPage = mPdfRenderer.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(mPage.getWidth(), mPage.getHeight(), Bitmap.Config.ARGB_4444);
            mPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            ((ImageView)view.findViewById(R.id.pdfImageView)).setImageBitmap(bitmap);

        } catch (Exception e) {}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPage != null) {
            mPage.close();
        }
        if (mPdfRenderer != null) {
            mPdfRenderer.close();
        }
        if (mFileDescriptor != null) {
            try {
                mFileDescriptor.close();
            } catch (Exception e) {}
        }
    }
}
