package dutta.swarnava.sundaysuspense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder>
{
    List<model> searchList;
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model)
    {
        holder.title.setText(model.gettitle());
        holder.author.setText(model.getauthor());
        Glide.with(holder.icon.getContext()).load(model.getimg()).into(holder.icon);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mylist,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView icon;
        TextView title;
        TextView author;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

            icon=(ImageView) itemView.findViewById(R.id.Audioicon);
            title=(TextView) itemView.findViewById(R.id.Audiotitle);
            author=(TextView) itemView.findViewById(R.id.Audioauthor);


            itemView.setOnClickListener(view -> mclicklistener.onItemClick(view,getAdapterPosition()));
        }
    }
    private myadapter.OnItemClickListener mclicklistener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(myadapter.OnItemClickListener listener)
    {
        mclicklistener=listener;
    }


}
