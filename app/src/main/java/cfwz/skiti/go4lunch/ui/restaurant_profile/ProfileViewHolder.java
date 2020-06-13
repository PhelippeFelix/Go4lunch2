package cfwz.skiti.go4lunch.ui.restaurant_profile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import cfwz.skiti.go4lunch.R;
import cfwz.skiti.go4lunch.model.Workmate;

/**
 * Created by Skiti on 12/06/2020
 */

public class ProfileViewHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_main_picture) ImageView mImageView;
        @BindView(R.id.detail_textview_username) TextView mTextView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void updateWithData(Workmate results){
            RequestManager glide = Glide.with(itemView);
            if (!(results.getUrlPicture() == null)){
                glide.load(results.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mImageView);
            }else{
                glide.load(R.drawable.ic_anon_user_48dp).apply(RequestOptions.circleCropTransform()).into(mImageView);
            }

            this.mTextView.setText(itemView.getResources().getString(R.string.restaurant_detail_recyclerview, results.getName()));
            this.changeTextColor(R.color.colorBlack);
        }

        private void changeTextColor(int color){
            int mColor = itemView.getContext().getResources().getColor(color);
            this.mTextView.setTextColor(mColor);
        }
    }