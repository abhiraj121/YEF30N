package com.YEF.yefApp;

        import android.content.Context;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;



public class GalleryHolder extends RecyclerView.ViewHolder {
    View mview;
    public GalleryHolder(@NonNull View itemView) {
        super(itemView);
        mview = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });

    }
    public void setDetails(Context ctx, String image) {
        ImageView mImage=mview.findViewById(R.id.imageView);
        Glide.with(ctx)
                .load(image).placeholder(R.mipmap.ic_launcher).fitCenter().centerCrop()
                .into(mImage);
    }
    private ViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener=clickListener;
    }

}

