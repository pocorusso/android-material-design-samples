package com.pocorusso.mdsamples;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class GalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public GalleryFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handler for UI thread operation in response to the result of bitmap decoding
        Handler responseHandler = new Handler();

        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder, String url, Bitmap bitmap) {
                        photoHolder.bindBitmap(bitmap);
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        initGallery(v);
        return v;
    }

    protected void initGallery(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_gallery_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        //query content resolve to get a cursor on a background thread.
        new AsyncTask<Void,Void,Cursor>(){
            @Override
            protected Cursor doInBackground(Void... voids) {
                return initCursor();
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                mRecyclerView.setAdapter(new PhotoAdapter(getActivity(), cursor));
            }

        }.execute();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.cleanQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
    }

    private Cursor initCursor() {
        final String[] projection = { MediaStore.Images.Media.DATA,
                                        MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                orderBy);
        return cursor;
    }

    private class GalleryItem {
        private int mId;
        private Uri mUri;

        public int getId() {
            return mId;
        }

        public Uri getUri() {
            return mUri;
        }

        public GalleryItem() {}

        public void setId(int id) {
            mId = id;
        }

        public void setUri(Uri uri) {
            mUri = uri;
        }

    }

    private class PhotoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mItemImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView.findViewById(R.id.gallery_item_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindBitmap(Bitmap bm) {
            mItemImageView.setImageBitmap(bm);
        }

        public void bindGalleryItem(int id, String path){
            if(mGalleryItem == null) {
                mGalleryItem = new GalleryItem();
            }
            mGalleryItem.setId(id);
            mGalleryItem.setUri(Uri.parse(path));
        }

        @Override
        public void onClick(View view) {
            PhotoHolder holder = (PhotoHolder) view.getTag();
            if (holder != null && holder.mGalleryItem!=null) {
                int id = holder.mGalleryItem.getId();
                //TODO handle on click action here
                Toast.makeText(getActivity(), "Item clicked: " + id, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * PhotoAdapter wraps a CursorAdapter to handle loading photo images from the gallery cursor
     */
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private CursorAdapter mCursorAdapter;
        private Context mContext;

        public PhotoAdapter(Context context, Cursor cursor) {
            mContext = context;
            mCursorAdapter = new CursorAdapter(context, cursor, 0) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.gallery_item, parent, false);
                    PhotoHolder photoHolder = new PhotoHolder(view);
                    view.setTag(photoHolder);
                    return view;
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    PhotoHolder photoHolder = (PhotoHolder) view.getTag();

                    int id = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
                    String path = (String)mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    photoHolder.bindGalleryItem(id, path);

                    photoHolder.bindBitmap(null);
                    mThumbnailDownloader.queueThumbnail(photoHolder, path);
                }
            };
        }


        /**
         * Hand off the view inflating to cursor adapter
         * @param viewGroup
         * @param viewType
         * @return
         */
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), viewGroup);
            return new PhotoHolder(view);
        }

        /**
         * Hand off the bind to cursor adapter
         * @param photoHolder
         * @param position
         */
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            mCursorAdapter.getCursor().moveToPosition(position);
            mCursorAdapter.bindView(photoHolder.itemView, mContext, mCursorAdapter.getCursor());

        }

        @Override
        public int getItemCount() {
            return mCursorAdapter.getCount();
        }
    }
}
