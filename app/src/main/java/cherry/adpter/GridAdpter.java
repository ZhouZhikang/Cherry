package cherry.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import cherry.action.model.ViewNews;
import cherry.cherry.R;

/**
 * Created by aqi on 15/7/3.
 */
public class GridAdpter extends BaseAdapter {
    private LayoutInflater inflater;

    private List<ViewNews> mlist;
    private Context mContext;

    private ImageView mImage;
    private TextView mTitle;

    public GridAdpter(Context context, List<ViewNews> list) {
        this.mlist = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {

        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.fragment_tab02_item, null);

        if (convertView == null) {
            convertView = new TextView(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(lp);
        }

        mImage = (ImageView) convertView.findViewById(R.id.item_img);
        mTitle = (TextView) convertView.findViewById(R.id.item_title);

        Drawable d = null;
        try {
            d = new GetImageTask().execute(mlist.get(position).getImgUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mImage.setImageDrawable(d);
        mTitle.setText(mlist.get(position).getTitle());

//        view.setBackground(d);

//        view.setGravity(Gravity.BOTTOM);
//        view.setTextColor(Color.WHITE);
//        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) view.getLayoutParams();
//        lp.height = (int) (getPositionRatio(position) * 200);
//        view.setLayoutParams(lp);

//        convertView.setBackgroundResource(mBackG);


        return convertView;
    }


    private final Random mRandom = new Random();
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    private class GetImageTask extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {

            Drawable drawable = loadImageFromNetwork(params[0]);

            return drawable;
        }

        //download image from network using @urladdress
        private Drawable loadImageFromNetwork(String urladdr) {
            // TODO Auto-generated method stub
            Drawable drawable = null;
            try {
                //judge if has picture locate or not according to filename
                drawable = Drawable.createFromStream(new URL(urladdr).openStream(), "image.jpg");
            } catch (IOException e) {
//                Log.d("test", e.getMessage());
            }
            if (drawable == null) {
//                Log.d("test", "null drawable");
            } else {
//                Log.d("test", "not null drawable");
            }
            return drawable;
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (mHeightRatio > 0.0) {
//            // set the image views size
//            int width = View.MeasureSpec.getSize(widthMeasureSpec);
//            int height = (int) (width * mHeightRatio);
//            setMeasuredDimension(width, height);
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
//    }


}
