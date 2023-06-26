package com.sexed.androidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.foysaltech.wptoandroidapp.R;
import com.sexed.androidapp.app.PostActivity;
import com.sexed.androidapp.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter {


    private Context context;
    private List<Post> posts;


    //Constructor
    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_entry, parent, false);
        return new PostViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);
        final PostViewHolder postHolder = (PostViewHolder) holder;
        postHolder.setCurrentPost(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView cardPt;
        private TextView cardEx;

        private Post currentPost;

        public PostViewHolder(View itemView) {
            super(itemView);

            cardPt = itemView.findViewById(R.id.cardPt);
            cardEx = itemView.findViewById(R.id.cardEx);

            itemView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void setCurrentPost(Post post) {
            currentPost = post;
            String title = post.getTitle().get("rendered").toString().replaceAll("\"", "");
            String excerpt = post.getExcerpt().get("rendered").toString().replaceAll("\"", "");

            cardPt.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));
            cardEx.setText(Html.fromHtml(excerpt, Html.FROM_HTML_MODE_LEGACY));
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {

            String title = currentPost.getTitle().get("rendered").toString().replaceAll("\"", "");
            String content = currentPost.getContent().get("rendered").toString().replaceAll("\"", "");
            String excerpt = currentPost.getExcerpt().get("rendered").toString().replaceAll("\"", "");

            content = contentFilter(content, "<ins", "</ins>");
            content = videoFilter(content, "<iframe", "/iframe>");

            Intent intent = PostActivity.createIntent(v.getContext(), currentPost.getId(),
                    currentPost.getFeatured_media(), Html.fromHtml(title,
                            Html.FROM_HTML_MODE_LEGACY).toString(), excerpt, content);
            v.getContext().startActivity(intent);
        }

        public String contentFilter(String content, String first, String last) {

            String contentOutput;
            String contentResult;


            //set index
            int firstIndex = content.indexOf(first);
            int lastIndex = content.lastIndexOf(last);

            if (firstIndex != -1 || lastIndex != -1) {

                //get substring
                contentOutput = content.substring(firstIndex, lastIndex + last.length());

                //replace
                contentResult = content.replace(contentOutput, "");

            } else {
                contentResult = content;
            }
            return contentResult;
        }

        public String videoFilter(String content, String first, String last) {

            String oldContentSubstring;
            String newContentSubstring;
            String contentResult;


            //set index
            int firstIndex = content.indexOf(first);
            int lastIndex = content.lastIndexOf(last);

            if (firstIndex != -1 || lastIndex != -1) {

                //get substring
                oldContentSubstring = content.substring(firstIndex, lastIndex + last.length());

                newContentSubstring = "<div class=\"videoWrapper\">" + oldContentSubstring + "</div>";

                contentResult = content.replace(oldContentSubstring, newContentSubstring);

            } else {
                contentResult = content;
            }
            return contentResult;

        }
    }
}
