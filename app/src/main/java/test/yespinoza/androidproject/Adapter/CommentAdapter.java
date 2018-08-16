package test.yespinoza.androidproject.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.yespinoza.androidproject.Model.Entity.UserComment;
import test.yespinoza.androidproject.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<UserComment> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        //public ImageView image;
        public TextView userName;
        public TextView message;
        public TextView date;
        public CommentViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.tv_username);
            message = (TextView) view.findViewById(R.id.tv_message);
            date = (TextView) view.findViewById(R.id.tv_date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }

    }
    public CommentAdapter(List<UserComment> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_view, viewGroup, false);
        return new CommentViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CommentViewHolder viewHolder, int i) {
        viewHolder.userName.setText(items.get(i).getUserName());
        viewHolder.message.setText(items.get(i).getMessage());
        viewHolder.date.setText(items.get(i).getCreateDate().toString());
    }
}
