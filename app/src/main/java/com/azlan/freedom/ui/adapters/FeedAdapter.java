package com.azlan.freedom.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.azlan.freedom.R;
import com.azlan.freedom.models.Feed;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.internal.TextDrawableHelper;
import com.squareup.picasso.Picasso;
import com.webtoonscorp.android.readmore.ReadMoreTextView;

import java.net.URI;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.getstream.avatarview.AvatarView;
import io.getstream.avatarview.coil.AvatarCoil;

public class FeedAdapter extends FirestoreRecyclerAdapter<Feed,FeedAdapter.FeedViewHolder> {

    private List<Feed> feedList;
    private Context context;
    private boolean isReadMoreClicked = false;
    private boolean isClickable=false;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeedAdapter(@NonNull FirestoreRecyclerOptions<Feed> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull FeedViewHolder holder, int position, @NonNull Feed feed) {
        holder.usernameTextView.setText(feed.getUsername());
         holder.likeCountTextView.setText(feed.getLikescount() + " likes");
         holder.commentCountTextView.setText("View All "+feed.getCemmentscount() + " comments");
//        holder.descriptionTextView.setText(feed.getDescription());
         // Load profile image using an image loading library like Picasso, Glide, or Coil
         // Example with Picasso:

        if(true){
            holder.profileImageView_placeholder.setVisibility(View.GONE);
            Picasso.get()
                    .load("https://lh3.googleusercontent.com/a/AAcHTtfyoAak3ijcR-aRQTrDjlxrv4zOPxEs2ko5Ml2CnV0jWQ=s96-c-rg-br100")
                    .placeholder(R.drawable.avenue)
                    .error(R.drawable.avenue2)
                    .into(holder.profileImageView);
        }
        else{
            holder.profileImageView.setVisibility(View.GONE);
            holder.profileImageView_placeholder.setAvatarInitialsBackgroundColor(Color.GREEN);
            holder.profileImageView_placeholder.setAvatarInitials("S M");
            holder.profileImageView_placeholder.setAvatarInitialsTextColor(Color.WHITE);
        }

         // Load post image using an image loading library
         // Example with Picasso:
         Picasso.get()
                 .load(feed.getMedia_url())
                 .placeholder(R.drawable.avenue)
                 .error(R.drawable.avenue2)
                 .into(holder.postImageView);
        String description= feed.getDescription();
        holder.descriptionTextView.setText(description);
//
//        // Check if the text is truncated (more lines than the specified maxLines)
//        if (holder.descriptionTextView.getText().length() > 25)
//        {
//            // Show the "Read More" link
//            holder.descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
//            holder.descriptionTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    isReadMoreClicked = !isReadMoreClicked;
//                    if (isReadMoreClicked) {
//                        // Expand the TextView to show the full text
//                        holder.descriptionTextView.setEllipsize(null);
//                        holder.descriptionTextView.setMaxLines(Integer.MAX_VALUE);
//                    } else {
//                        // Collapse the TextView to show only the truncated text
//                        holder.descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
//                        holder.descriptionTextView.setMaxLines(2);
//                    }
//                }
//            });
//        } else {
//            // Hide the "Read More" link if the text is not truncated
//            holder.descriptionTextView.setOnClickListener(null);
//        }


    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new FeedAdapter.FeedViewHolder(itemView);
    }

    //
    // ViewHolder class to hold the views of each item
    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImageView;
        AvatarView profileImageView_placeholder;
        ImageView postImageView;
        TextView usernameTextView;
        TextView likeCountTextView;
        TextView commentCountTextView;
        ReadMoreTextView descriptionTextView;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView_placeholder=itemView.findViewById(R.id.image_profile_placeholder);
            profileImageView = itemView.findViewById(R.id.image_profile);
            postImageView = itemView.findViewById(R.id.post_image);
            usernameTextView = itemView.findViewById(R.id.publisher_username);
            likeCountTextView = itemView.findViewById(R.id.likes);
            commentCountTextView = itemView.findViewById(R.id.comments);
            descriptionTextView = itemView.findViewById(R.id.description);
        }
    }

}

